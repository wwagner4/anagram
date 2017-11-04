package anagram.gui

import java.awt._
import java.awt.event.ActionEvent
import java.nio.file.{Files, Path}
import java.util.concurrent._
import javax.swing._
import javax.swing.border.Border
import javax.swing.plaf.FontUIResource
import javax.swing.text._

import anagram.common.{Cancelable, IoUtil}
import anagram.morph.{AnagramMorph, Justify}
import anagram.solve._
import net.miginfocom.swing.MigLayout
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object GuiMain extends App {

  val listModel = new DefaultListModel[String]
  val listSelectionModel = {
    val lsm = new DefaultListSelectionModel
    lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
    lsm
  }
  val textDoc = new PlainDocument()
  val stateDoc = {
    val doc = new PlainDocument()
    doc
  }

  val ctrl = new Controller(listModel, listSelectionModel, textDoc, stateDoc)

  //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel")
  //setUiFont(new javax.swing.plaf.FontUIResource("SansSerif", Font.PLAIN, 10))
  new Frame(listModel, listSelectionModel, textDoc, stateDoc, ctrl.getStartAction, ctrl.getStopAction, ctrl.getMorphAction).setVisible(true)

  import collection.JavaConverters._

  def setUiFont(fontResource: FontUIResource): Unit = {
    println("setUoFont")
    val d: Iterator[AnyRef] = UIManager.getDefaults.keys().asScala
    println(s"setUoFont $d")
    for (key <- d.toStream) {
      println(s"has $key")
      val value = UIManager.get(key)
      if (value.isInstanceOf[javax.swing.plaf.FontUIResource])
        println(s"changed $key")
        UIManager.put(key, fontResource)
    }
  }
}

class Controller(
                  val listModel: DefaultListModel[String],
                  val listSelectionModel: DefaultListSelectionModel,
                  val textDoc: PlainDocument,
                  val stateDoc: PlainDocument) {

  case class Services(
                       executorService: ExecutorService,
                       executionContextExecutor: ExecutionContextExecutor,
                     )

  private val log = LoggerFactory.getLogger("Controller")

  var service = Option.empty[Services]

  var cnt = 0

  var _cancelable = Seq.empty[Cancelable]

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
          background((ec: ExecutionContextExecutor) => {
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
    if (listSelectionModel.isSelectionEmpty) None
    else {
      val idx = listSelectionModel.getAnchorSelectionIndex
      val ana = listModel.get(idx)
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
    _cancelable.foreach(_.cancel())
    service.foreach(s => while (!s.executorService.isShutdown) {
      s.executorService.shutdownNow()
    })
    service = Option.empty[Services]
  }

  def solve(srcText: String)(implicit ec: ExecutionContextExecutor): SolverIter = {
    val cfg = CfgSolverAis.cfgGrm
    val solver = new SolverAi(cfg)
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

  def fillListModel(values: Iterable[String]): Unit = {
    listModel.removeAllElements()
    for ((s, i) <- values.zipWithIndex) {
      listModel.add(i, s)
    }
  }

  def createDefaultExecutorService: ExecutorService = {
    new ForkJoinPool(4)
  }


}


class Frame(
             listModel: ListModel[String],
             listSelectionModel: ListSelectionModel,
             textDoc: Document,
             stateDoc: Document,
             startAction: Action,
             stopAction: Action,
             morphAction: Action,
           ) extends JFrame {
  setSize(500, 600)
  setTitle("anagram creator")
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setContentPane(new Content)


  class Content extends JPanel {

    setLayout(new BorderLayout())

    add(createCommandColumn, BorderLayout.EAST)
    add(createScrollableList, BorderLayout.CENTER)


    def createCommandColumn: JComponent = {
      val cont = new JPanel(new MigLayout("width 300!", "5[grow]5[grow]5[grow]5"))
      cont.add(createStartButton, "grow")
      cont.add(createStopButton, "grow")
      cont.add(createMorphButton, "grow, wrap")
      cont.add(createTextField(textDoc), "span 3, grow, wrap")
      cont.add(createStatText(stateDoc), "height 150!, span 3, grow, wrap")
      cont
    }

    def createTextField(doc: Document): Component = {
      val txt = new JTextField()
      txt.setBorder(createTxtBorder)
      txt.setDocument(doc)
      txt
    }

    def createStatText(stateDoc: Document): Component = {
      val txt = new JTextArea()
      txt.setDocument(stateDoc)
      txt.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3))
      txt.setEditable(false)
      txt.setLineWrap(true)
      txt.setBackground(new Color(240, 240, 240))
      txt
    }

    def createTxtBorder: Border = {
      val out = BorderFactory.createEtchedBorder()
      val inner = BorderFactory.createEmptyBorder(3, 3, 3, 3)
      BorderFactory.createCompoundBorder(out, inner)
    }

    def createButtonsPanel: Component = {
      val re = new JPanel()
      re.setLayout(new FlowLayout())
      re.add(createStartButton)
      re.add(createStopButton)
      re.add(createMorphButton)
      re
    }

    def createMorphButton: Component = {
      val re = new JButton()
      re.setAction(morphAction)
      re.setText("morph")
      re
    }

    def createStartButton: Component = {
      val re = new JButton()
      re.setAction(startAction)
      re.setText("start")
      re
    }

    def createStopButton: Component = {
      val re = new JButton()
      re.setAction(stopAction)
      re.setText("stop")
      re
    }

    def createScrollableList: JComponent = {
      val list = new JList[String]()
      list.setModel(listModel)
      list.setSelectionModel(listSelectionModel)
      val re = new JScrollPane(list)
      re
    }
  }

}

