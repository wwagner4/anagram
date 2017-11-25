package anagram.gui

import java.awt.event.ActionEvent
import java.awt.{BorderLayout, Color, Component, Desktop, Image}
import java.nio.file.{Files, Path}
import java.util.concurrent._
import javax.imageio.ImageIO
import javax.swing._
import javax.swing.border.Border
import javax.swing.event.ListSelectionEvent
import javax.swing.text._

import anagram.Scheduler
import anagram.common.{Cancelable, IoUtil, SortedList}
import anagram.ml.rate.{Rater, RaterAi, RaterRandom}
import anagram.model.{CfgRaterAiFactory, Configurations}
import anagram.morph.{AnagramMorph, Justify}
import anagram.solve._
import anagram.solve.concurrent.AnaExecutionContextImpl
import anagram.words.{WordListFactory, Wordlists}
import net.miginfocom.swing.MigLayout
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object GuiMain extends App {

  val outListModel = new DefaultListModel[String]
  val outListSelectionModel = {
    val lsm = new DefaultListSelectionModel
    lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
    lsm
  }
  val solverListModel = new DefaultListModel[SolverRatedFactory]
  val solverListSelectionModel = {
    val lsm = new DefaultListSelectionModel
    lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
    lsm
  }
  val textDoc = new PlainDocument()
  val infoDoc = new PlainDocument()
  val stateDoc = new PlainDocument()

  val ctrl = Controller(
    outListModel, outListSelectionModel,
    solverListModel, solverListSelectionModel,
    textDoc, stateDoc, infoDoc
  )

  new Frame(
    outListModel, outListSelectionModel,
    solverListModel, solverListSelectionModel,
    textDoc, stateDoc, infoDoc,
    ctrl.getStartAction, ctrl.getStopAction, ctrl.getMorphAction
  ).setVisible(true)

}

case class Controller(
                       outListModel: DefaultListModel[String],
                       outListSelectionModel: DefaultListSelectionModel,
                       solverListModel: DefaultListModel[SolverRatedFactory],
                       solverListSelectionModel: DefaultListSelectionModel,
                       textDoc: PlainDocument,
                       stateDoc: PlainDocument,
                       infoDoc: PlainDocument,
                     ) {

  case class Services(
                       executorService: ExecutorService,
                       executionContextExecutor: ExecutionContextExecutor,
                     )

  private val log = LoggerFactory.getLogger("Controller")

  private val maxDepth = 5
  private val parallel = 3
  private val wordListFactory = Wordlists.plainFreq5k


  val solverFactoryBase = SolverFactoryPlain(maxDepth = maxDepth, parallel = parallel, wordListFactory)


  val solverFactories = Seq(
    {
      val rater = RaterFactoryNone()
      SolverFactoryRated(solverFactoryBase, rater)
    },
    {
      val rater = RaterFactoryRandom()
      SolverFactoryRated(solverFactoryBase, rater)
    },
  )

  private val aiSolverFactories = Configurations.all.map {cfg =>
    val rater = RaterFactoryAi(cfg.cfgRaterAi)
    SolverFactoryRated(solverFactoryBase, rater)
  }

  // Fill the solverListModel
  (solverFactories ++ aiSolverFactories).foreach(solverListModel.addElement)

  solverListSelectionModel.setSelectionInterval(2, 2)

  setInfoDoc(selectedSolverFactory.description)

  var service = Option.empty[Services]

  var _cancelable = Seq.empty[Cancelable]

  solverListSelectionModel.addListSelectionListener(
    (_: ListSelectionEvent) => {
      val idx = solverListSelectionModel.getAnchorSelectionIndex
      val desc = solverListModel.getElementAt(idx).description
      setInfoDoc(desc)
    })

  var cnt = 0

  def getStartAction: Action = new AbstractAction() {
    override def actionPerformed(e: ActionEvent): Unit = {
      if (service.isDefined) {
        log.info("[actionPerformed] already started")
      } else {
        cnt = 0
        background((ec: ExecutionContextExecutor) => {
          setStateDoc(s"solving $getText")
          fillListModel(Seq.empty[String])
          solve(getText)(ec)
        }, () => {
        })
      }
    }
  }

  def solve(srcText: String)(implicit ec: ExecutionContextExecutor): Unit = {

    def updateStatus(sl: SortedList[Ana]): Unit = {
      SwingUtilities.invokeAndWait { () =>
        if (running) {
          val n = sl.size
          setStateDoc(s"solved $n anagrams")
        }
        val line = sl.take(500)
          .map(_.sentence)
          .map(_.mkString(" "))
        fillListModel(line)
      }
    }

    val solver = selectedSolverFactory.createSolver(ec)
    _cancelable :+= solver
    val anas: Iterator[Ana] = solver.solve(srcText)
    log.info(s"[solve] after solver.solve")
    val sl = SortedList.instance[Ana]
    val sched = Scheduler.schedule(1){ () =>
      if (running) {
        var cnt1 = 0
        while (cnt1 < 100) {
          if (!anas.hasNext && running) {
            updateStatus(sl)
            shutdown()
          } else {
            sl.add(anas.next())
            cnt1 += 1
            cnt += 1
          }
        }
        updateStatus(sl)
      }
    }
    _cancelable :+= sched
  }



  def getStopAction: Action = new AbstractAction() {

    override def actionPerformed(e: ActionEvent): Unit = {
      if (service.isDefined) {
        shutdown()
        log.info("canceled")
      } else {
        log.info("not started")
      }
    }
  }

  def getMorphAction: Action = new AbstractAction() {

    override def actionPerformed(e: ActionEvent): Unit = {
      if (service.isDefined) {
        log.info("cannot create a morph image while solving.")
      } else {
        if (selectedAnagram.isDefined) {
          val src = getText
          val ana = selectedAnagram.get
          setStateDoc(s"morphing $ana")
          val _outFile: Path = outFile(src, ana)
          background((_: ExecutionContextExecutor) => {
            val morpher = AnagramMorph.anagramMorphLinear
            val justifier = Justify.justifyDefault
            val lines = morpher.morph(src, ana, calcLines(src))
            justifier.writePng(lines, _outFile.toFile, 400)
            setStateDoc(s"morphed $ana")
            log.info(s"writing morph image to ${_outFile}")
            Desktop.getDesktop.open(_outFile.toFile)
          }, () => {
            shutdown()
            setStateDoc(s"wrote morph image to ${_outFile}")
          })
        } else {
          setStateDoc(s"no anagram selected")
        }
      }
    }

    def calcLines(txt: String): Int = (txt.length * 0.7).toInt

    def outFile(src: String, ana: String): Path = {
      val workDir = IoUtil.dirWork
      val imageDir = workDir.resolve("morph")
      if (!Files.exists(imageDir)) Files.createDirectories(imageDir)
      val src1 = src.replaceAll("\\s", "_")
      val ana1 = ana.replaceAll("\\s", "_")
      val filename = s"anamorph_${src1}_$ana1.png"
      imageDir.resolve(filename)
    }

  }

  def createServices: Some[Services] = {
    val es: ExecutorService = createDefaultExecutorService
    val ec: ExecutionContextExecutor = ExecutionContext.fromExecutorService(es)
    Some(Services(es, ec))
  }

  def selectedAnagram: Option[String] = {
    if (outListSelectionModel.isSelectionEmpty) None
    else {
      val idx = outListSelectionModel.getAnchorSelectionIndex
      val ana = outListModel.get(idx)
      Some(ana)
    }
  }

  def background(main: (ExecutionContextExecutor) => Unit, onSuccess: () => Unit): Unit = {
    _cancelable = Seq.empty[Cancelable]
    service = createServices
    implicit val ec: ExecutionContextExecutor = service.get.executionContextExecutor
    val future = Future {
      try {
        main(ec)
      } catch {
        case _: InterruptedException => // Ignore
      }
    }
    future.onComplete {
      case Success(_) =>
        onSuccess()
      case Failure(ex) =>
        val msg = ex.getMessage
        setStateDoc(msg)
        log.error(s"Error: $msg", ex)
    }
  }

  def shutdown(): Unit = {
    service.foreach(s => while (!s.executorService.isShutdown) {
      s.executorService.shutdownNow()
    })
    _cancelable.foreach(_.cancel())
    service = Option.empty[Services]
  }

  def running: Boolean = service.isDefined

  implicit val ordering: OrderingAnaRatingDesc = new OrderingAnaRatingDesc

  def getText: String = textDoc.getText(0, textDoc.getLength)

  def setStateDoc(text: String): Unit = {
    stateDoc.remove(0, stateDoc.getLength)
    stateDoc.insertString(0, text, null)
  }

  def setInfoDoc(text: String): Unit = {
    infoDoc.remove(0, infoDoc.getLength)
    infoDoc.insertString(0, text, null)
  }

  def fillListModel(values: Iterable[String]): Unit = {
    outListModel.removeAllElements()
    for ((s, i) <- values.zipWithIndex) {
      outListModel.add(i, s)
    }
  }

  def createDefaultExecutorService: ExecutorService = {
    AnaExecutionContextImpl.createDefaultExecutorService((exe: Throwable) => log.error(exe.getMessage, exe))
  }

  def selectedSolverFactory: SolverRatedFactory = {
    require(!solverListSelectionModel.isSelectionEmpty)
    val idx = solverListSelectionModel.getAnchorSelectionIndex
    solverListModel.elementAt(idx)
  }

}


class Frame(
             outListModel: ListModel[String],
             outListSelectionModel: ListSelectionModel,
             solverListModel: ListModel[SolverRatedFactory],
             solverListSelectionModel: ListSelectionModel,
             textDoc: Document,
             stateDoc: Document,
             infoDoc: Document,
             startAction: Action,
             stopAction: Action,
             morphAction: Action,
           ) extends JFrame {

  setSize(800, 800)
  setTitle("anagram creator")
  setIconImages(images.asJava)
  // remove the following line if you are not on mac OS
  com.apple.eawt.Application.getApplication.setDockIconImage(toImage("images/scala-logo-square-orig.png"))
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setContentPane(new Content)


  class Content extends JPanel {

    private val fontSize = 25f
    private val fontSizeSmall = fontSize * 0.8f
    private val listCellSize = (fontSize * 1.2).toInt

    setLayout(new BorderLayout())

    add(createCommandColumn, BorderLayout.EAST)
    add(createOutList, BorderLayout.CENTER)


    def createCommandColumn: JComponent = {
      val cont = new JPanel(new MigLayout("width 400!", "5[grow]5[grow]5[grow]5"))
      cont.add(createStartButton, "grow")
      cont.add(createStopButton, "grow")
      cont.add(createMorphButton, "grow, wrap")
      cont.add(createTextField, "span 3, grow, wrap")
      cont.add(createStatText, "height 150!, span 3, grow, wrap")
      cont.add(createSolverFactoryList, "height 180!, span 3, grow, wrap")
      cont.add(createInfoText, "height 150!, span 3, grow, wrap")
      cont
    }

    def createTextField: Component = {
      val txt = new JTextField()
      txt.setBorder(createBorder)
      txt.setDocument(textDoc)
      txt.setFont(txt.getFont.deriveFont(fontSize))
      txt
    }

    def createStatText: Component = {
      val txt = new JTextArea()
      txt.setDocument(stateDoc)
      txt.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3))
      txt.setEditable(false)
      txt.setLineWrap(true)
      txt.setBackground(new Color(240, 240, 240))
      txt.setFont(txt.getFont.deriveFont(fontSizeSmall))
      txt
    }

    def createInfoText: Component = {
      val txt = new JTextArea()
      txt.setDocument(infoDoc)
      txt.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3))
      txt.setEditable(false)
      txt.setLineWrap(true)
      txt.setBackground(new Color(240, 240, 240))
      txt.setFont(txt.getFont.deriveFont(fontSizeSmall))
      txt
    }

    def createBorder: Border = {
      val out = BorderFactory.createEtchedBorder()
      val inner = BorderFactory.createEmptyBorder(3, 3, 3, 3)
      BorderFactory.createCompoundBorder(out, inner)
    }

    def createMorphButton: Component = {
      val re = new JButton()
      re.setAction(morphAction)
      re.setText("morph")
      re.setFont(re.getFont.deriveFont(fontSize))
      re
    }

    def createStartButton: Component = {
      val re = new JButton()
      re.setAction(startAction)
      re.setText("start")
      re.setFont(re.getFont.deriveFont(fontSize))
      re
    }

    def createStopButton: Component = {
      val re = new JButton()
      re.setAction(stopAction)
      re.setText("stop")
      re.setFont(re.getFont.deriveFont(fontSize))
      re
    }

    def createOutList: Component = {
      val list = new JList[String]()
      list.setModel(outListModel)
      list.setSelectionModel(outListSelectionModel)
      list.setFont(list.getFont.deriveFont(fontSize))
      list.setFixedCellHeight(listCellSize)
      list.setBorder(createBorder)
      new JScrollPane(list)
    }

    def createSolverFactoryList: Component = {
      val list: JList[SolverRatedFactory] = new JList[SolverRatedFactory]()
      list.setModel(solverListModel)
      list.setSelectionModel(solverListSelectionModel)
      list.setFont(list.getFont.deriveFont(fontSize))
      list.setFixedCellHeight(listCellSize)
      list.setBorder(createBorder)
      list
    }
  }

  def images: List[Image] = {
    List("128", "64", "32", "16")
      .map(nr => s"images/scala-logo-square-$nr.jpg")
      .map(res => toImage(res))
  }

  def toImage(res: String): Image = {
    val url = getClass.getClassLoader.getResource(res)
    if (url == null) throw new IllegalArgumentException(s"not a resource '$res'")
    ImageIO.read(url)
  }

}

trait SolverFactory {

  def createSolver(implicit ec: ExecutionContextExecutor): Solver

  def description: String

  def shortDescription: String

  override def toString: String = shortDescription

}

trait SolverRatedFactory {

  def createSolver(implicit ec: ExecutionContextExecutor): SolverRated

  def description: String

  def shortDescription: String

  override def toString: String = shortDescription

}

trait RaterFactory {

  def createRater: Rater

  def description: String

  def shortDescription: String

  override def toString: String = shortDescription

}

case class RaterFactoryAi(raterAiCfg: CfgRaterAiFactory) extends RaterFactory {

  private lazy val cfg = raterAiCfg.cfgRaterAi()

  override def createRater: Rater = new RaterAi(cfg, None)

  override def description: String = {
    raterAiCfg.description
  }

  override def shortDescription: String = {
    raterAiCfg.shortDescription
  }
}

case class RaterFactoryRandom() extends RaterFactory {

  override def createRater = new RaterRandom

  override def description = "Random rating"

  override def shortDescription = "RANDOM"

}

case class RaterFactoryNone() extends RaterFactory {

  override def createRater: Rater = _ => 1.0

  override def description = "no rating"

  override def shortDescription = "NONE"

}


case class SolverFactoryPlain(maxDepth: Int, parallel: Int, wordListFactory: WordListFactory) extends SolverFactory {

  def createSolver(implicit ec: ExecutionContextExecutor): Solver = SolverPlain(maxDepth, parallel, wordListFactory.wordList())

  def description: String = s"Solver plain maxDepth:$maxDepth parallel:$parallel ${wordListFactory.description}"

  def shortDescription: String = s"PLAIN_${wordListFactory.shortSescription}"

}

case class SolverFactoryRated(solverFactoryBase: SolverFactory, raterFactory: RaterFactory) extends SolverRatedFactory {

  def createSolver(implicit ec: ExecutionContextExecutor): SolverRated = {
    SolverRatedImpl(solverFactoryBase.createSolver, raterFactory.createRater)
  }

  def description: String = s"Solver with Rating: ${raterFactory.description}. Base: ${solverFactoryBase.description}"

  def shortDescription: String = s"RATED ${raterFactory.shortDescription}"
}