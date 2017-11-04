package anagram.gui

import java.awt._
import java.awt.event.ActionEvent
import java.util.concurrent._
import javax.swing._
import javax.swing.text._

import anagram.common.Cancelable
import anagram.solve._
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object GuiMain extends App {

  val listModel = new DefaultListModel[String]
  val textDoc = new PlainDocument()
  val stateDoc = new PlainDocument()
  val ctrl = new Controller(listModel, textDoc, stateDoc)

  new Frame(listModel, textDoc, stateDoc, ctrl.getStartAction, ctrl.getStopAction).setVisible(true)
}

class Controller(val listModel: DefaultListModel[String], val textDoc: PlainDocument, val stateDoc: PlainDocument) {

  private val log = LoggerFactory.getLogger("Controller")

  var service = Option.empty[ExecutorService]

  var cnt = 0

  var _cancelable = Seq.empty[Cancelable]

  def getStartAction: Action = new AbstractAction() {

    override def actionPerformed(e: ActionEvent): Unit = {
      if (service.isDefined) {
        log.info("[actionPerformed] already started")
      } else {
        _cancelable = Seq.empty[Cancelable]
        val es = createDefaultExecutorService
        implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutorService(es)
        service = Some(es)
        val future = Future {
          cnt = 0
          setStateDoc(s"solving $getText")
          fillListModel(Seq.empty[String])
          val siter = solve(getText)
          for (anas <- siter.toStream) {
            SwingUtilities.invokeAndWait { () =>
              val sentences = anas.map(ana => ana.sentence.mkString(" "))
              fillListModel(sentences)
              cnt = siter.solvedAnagrams
              setStateDoc(s"solving. found $cnt anagrams  ")
            }
            Thread.sleep(500)
          }
        }
        future.onComplete {
          case Success(_) =>
            shutdown()
            setStateDoc(s"solved. $cnt anagrams")
          case Failure(ex) =>
            shutdown()
            val msg = ex.getMessage
            setStateDoc(msg)
            log.error(s"Error: $msg", e)
        }
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

  def shutdown(): Unit = {
    _cancelable.foreach(_.cancel())
    service.foreach(s => while (!s.isShutdown) {
      s.shutdownNow()
    })
    service = Option.empty[ExecutorService]
  }

  def solve(srcText: String)(implicit ec: ExecutionContextExecutor): SolverIter = {
    val cfg = CfgSolverAis.cfgGrm
    val solver = new SolverAi(cfg)
    _cancelable :+= solver
    val anas: Iterator[Ana] = solver.solve(srcText, WordLists.wordListIgnoring)
    log.info(s"[solve] after solver.solve")
    val  inst = SolverIter.instance(anas, 500)
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

class Content(listModel: ListModel[String], txtDoc: Document, stateDoc: Document, startAction: Action, stopAction: Action) extends JPanel {

  //setBackground(Color.GREEN)
  setLayout(new BorderLayout())

  add(createCommandColumn, BorderLayout.EAST)
  add(createScrollableList, BorderLayout.CENTER)


  def createCommandColumn: JComponent = {
    val cont = new JPanel()
    cont.setLayout(new BoxLayout(cont, BoxLayout.PAGE_AXIS))
    cont.add(createButtonsPanel)
    cont.add(createTextField(txtDoc))
    cont.add(createCountTextField(stateDoc))
    cont.add(createFillPanel())
    cont.setPreferredSize(new Dimension(300, Int.MaxValue))
    cont
  }

  def createCountTextField(stateDoc: Document): Component = {
    val re = new JTextField()
    re.setEditable(false)
    re.setDocument(stateDoc)
    re
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
//    re.setAction(startAction)
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


  def createTextField(doc: Document): Component = {
    val re = new JTextField()
    re.setDocument(doc)
    re
  }

  def createFillPanel(): Component = {
    val re = new JPanel()
    re.setPreferredSize(new Dimension(1, Int.MaxValue))
    re
  }

  def createScrollableList: JComponent = {
    val list = new JList[String]()
    list.setModel(listModel)
    val re = new JScrollPane(list)
    re
  }
}


class Frame(listModel: ListModel[String], textDoc: Document, stateDoc: Document, startAction: Action, stopAction: Action) extends JFrame {
  setSize(500, 600)
  setTitle("anagram creater")
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setContentPane(new Content(listModel, textDoc, stateDoc, startAction, stopAction))
}

