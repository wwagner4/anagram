package anagram.gui

import java.awt._
import java.awt.event.ActionEvent
import java.nio.file.{Files, Path}
import java.util.concurrent._
import javax.swing._
import javax.swing.border.Border
import javax.swing.event.ListSelectionEvent
import javax.swing.text._

import anagram.common.{Cancelable, IoUtil}
import anagram.morph.{AnagramMorph, Justify}
import anagram.solve._
import net.miginfocom.swing.MigLayout
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object GuiMain extends App {

  val outListModel = new DefaultListModel[String]
  val outListSelectionModel = {
    val lsm = new DefaultListSelectionModel
    lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
    lsm
  }
  val solverListModel = new DefaultListModel[SolverFactory]
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
                       solverListModel: DefaultListModel[SolverFactory],
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

  Seq(
    SolverFactoryPlain(),
    {
      val rater = new RaterRandom
      SolverFactoryRated(SolverFactoryPlain(), rater)
    },
    {
      val rater = new RaterAi(RaterAiCfgs.cfgPlain)
      SolverFactoryRated(SolverFactoryPlain(), rater)
    },
    {
      val rater = new RaterAi(RaterAiCfgs.cfgGrm)
      SolverFactoryRated(SolverFactoryPlain(), rater)
    },
  ).foreach(solverListModel.addElement)
  solverListSelectionModel.setSelectionInterval(2, 2)

  setInfoDoc(selectedSolverFactory.solverDescription)

  var service = Option.empty[Services]

  var cnt = 0

  var _cancelable = Seq.empty[Cancelable]

  solverListSelectionModel.addListSelectionListener(
    (_: ListSelectionEvent) => {
      val idx = solverListSelectionModel.getAnchorSelectionIndex
      val desc = solverListModel.getElementAt(idx).solverDescription
      setInfoDoc(desc)
    })

  def getStartAction: Action = new AbstractAction() {

    override def actionPerformed(e: ActionEvent): Unit = {
      if (service.isDefined) {
        log.info("[actionPerformed] already started")
      } else {
        background((ec: ExecutionContextExecutor) => {
          cnt = 0
          setStateDoc(s"solving $getText")
          fillListModel(Seq.empty[String])
          val siter = solve(getText)(ec)
          for (anas <- siter.toStream) {
            SwingUtilities.invokeAndWait { () =>
              val sentences = anas.map(ana => ana.sentence.mkString(" "))
              fillListModel(sentences)
              cnt = siter.solvedAnagrams
              setStateDoc(s"solving. found $cnt anagrams  ")
            }
            Thread.sleep(500)
          }
        }, () => {
          setStateDoc(s"solved. $cnt anagrams")
        })
      }
    }
  }

  def getStopAction: Action = new AbstractAction() {

    override def actionPerformed(e: ActionEvent): Unit = {
      if (service.isDefined) {
        shutdown()
        setStateDoc(s"canceled. $cnt anagrams")
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
          val _outFile = outFile(src, ana)
          background((_: ExecutionContextExecutor) => {
            val morpher = AnagramMorph.anagramMorphLinear
            val justifier = Justify.justifyDefault
            val lines = morpher.morph(src, ana, calcLines(src))
            justifier.writePng(lines, _outFile.toFile, 400)
            setStateDoc(s"morphed $ana")
            log.info(s"writing morph image to ${_outFile}")
          }, () => {
            setStateDoc(s"wrote morph image to ${_outFile}")
          })
        } else {
          setStateDoc(s"no anagram selected")
        }
      }
    }

    def calcLines(txt: String): Int = (txt.length * 0.7).toInt

    def outFile(src: String, ana: String): Path = {
      val workDir = IoUtil.getCreateWorkDir
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
      main(ec)
    }
    future.onComplete {
      case Success(_) =>
        shutdown()
        onSuccess()
      case Failure(ex) =>
        shutdown()
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

  def solve(srcText: String)(implicit ec: ExecutionContextExecutor): SolverIter = {
    val solver = selectedSolverFactory.createSolver(ec)
    _cancelable :+= solver
    val anas: Iterator[Ana] = solver.solve(srcText, WordLists.wordListIgnoring)
    log.info(s"[solve] after solver.solve")
    val inst = SolverIter.instance(anas, 500)
    _cancelable :+= inst
    inst
  }

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
    new ForkJoinPool(4)
  }

  def selectedSolverFactory: SolverFactory = {
    require(!solverListSelectionModel.isSelectionEmpty)
    val idx = solverListSelectionModel.getAnchorSelectionIndex
    solverListModel.elementAt(idx)
  }

}


class Frame(
             outListModel: ListModel[String],
             outListSelectionModel: ListSelectionModel,
             solverListModel: ListModel[SolverFactory],
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
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setContentPane(new Content)


  class Content extends JPanel {

    private val fontSize = 25f
    private val fontSizeSmall = fontSize * 0.6f
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
      cont.add(createSolverFactoryList, "height 130!, span 3, grow, wrap")
      cont.add(createInfoText, "height 150!, span 3, grow, wrap")
      cont
    }

    def createTextField: Component = {
      val txt = new JTextField()
      txt.setBorder(createTxtBorder)
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

    def createTxtBorder: Border = {
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
      new JScrollPane(list)
    }
    def createSolverFactoryList: Component = {
      val list: JList[SolverFactory] = new JList[SolverFactory]()
      list.setModel(solverListModel)
      list.setSelectionModel(solverListSelectionModel)
      list.setFont(list.getFont.deriveFont(fontSize))
      list.setFixedCellHeight(listCellSize)
      list
    }
  }

}

trait SolverFactory {

  def createSolver(implicit ec: ExecutionContextExecutor): Solver

  def solverDescription: String

  def solverShortDescription: String

  override def toString: String = solverShortDescription

}


case class SolverFactoryPlain(maxDepth: Int = 4, parallel: Int = 4) extends SolverFactory {

  def createSolver(implicit ec: ExecutionContextExecutor): Solver = SolverPlain(maxDepth, parallel)

  def solverDescription: String = s"Solver plain maxDepth:$maxDepth parallel:$parallel"

  def solverShortDescription: String = s"PLAIN"

}

case class SolverFactoryRated(solverFactory: SolverFactory, rater: Rater) extends SolverFactory {

  def createSolver(implicit ec: ExecutionContextExecutor): Solver = {
    SolverRated(solverFactory.createSolver, rater)
  }

  def solverDescription: String = s"Solver rated with: $rater. Base solver: ${solverFactory.solverDescription}"

  def solverShortDescription: String = s"RATED ${rater.shortDescription}"
}