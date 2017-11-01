package anagram.gui

import java.awt._
import java.awt.event.ActionEvent
import javax.swing._
import javax.swing.text._

import anagram.solve._

object GuiMain extends App {

  val listModel = new DefaultListModel[String]
  val textDoc = new PlainDocument()
  val cntDoc = new PlainDocument()
  val ctrl = new Controller(listModel, textDoc, cntDoc)

  new Frame(listModel, textDoc, cntDoc, ctrl.getStartAction, ctrl.getStopAction).setVisible(true)
}

class Controller(val listModel: DefaultListModel[String], val textDoc: PlainDocument, val cntDoc: PlainDocument) {

  fillListModel(Seq("a", "b", "wolfi"))

  var solverIter = Option.empty[SolverIter]


  def getStartAction: Action = new AbstractAction() {

    override def actionPerformed(e: ActionEvent): Unit = {
      println(s"STARTED '$getText'")
      if (solverIter.isDefined) {
        println("Already started")
      } else {
        solverIter = Some(solve(getText))
      }
      setCntDoc("counting not yet implemented")
    }
  }

  def getStopAction: Action = new AbstractAction() {
    override def actionPerformed(e: ActionEvent): Unit = {
      println(s"STOPPED")
      if(solverIter.isDefined) {
        solverIter.get.shutdownNow()
      } else {
        println("not started")
      }
    }
  }

  def solve(srcText: String): SolverIter = {
    val cfg = CfgSolverAis.cfgGrm
    val anas: Stream[Ana] = new SolverAi(cfg).solve(srcText, WordLists.wordListIgnoring)
    SolverIter.instance(anas, 10)
  }

  def getText: String = textDoc.getText(0, textDoc.getLength)

  def setCntDoc(text: String): Unit = {
    cntDoc.remove(0, cntDoc.getLength)
    cntDoc.insertString(0, text, null)
  }

  def fillListModel(values: Iterable[String]): Unit = {
    listModel.removeAllElements()
    for ((s, i) <- values.zipWithIndex) {
      listModel.add(i, s)
    }
  }

}

class Content(listModel: ListModel[String], txtDoc: Document, cntDoc: Document, startAction: Action, stopAction: Action) extends JPanel {

  //setBackground(Color.GREEN)
  setLayout(new BorderLayout())

  add(createCommandColumn, BorderLayout.EAST)
  add(createScrollableList, BorderLayout.CENTER)


  def createCommandColumn: JComponent = {
    val cont = new JPanel()
    //cont.setBackground(Color.YELLOW)
    cont.setLayout(new BoxLayout(cont, BoxLayout.PAGE_AXIS))
    cont.add(createButtonsPanel)
    cont.add(createTextField(txtDoc))
    cont.add(createCountTextField(cntDoc))
    cont.add(createFillPanel())
    cont.setPreferredSize(new Dimension(300, Int.MaxValue))
    cont.setMinimumSize(new Dimension(200, 0))
    cont
  }

  def createCountTextField(cntDoc: Document): Component = {
    val re = new JTextField()
    re.setEditable(false)
    re.setDocument(cntDoc)
    re
  }

  def createButtonsPanel: Component = {
    val re = new JPanel()
    re.setLayout(new FlowLayout())
    re.add(createStartButton)
    re.add(createStopButton)
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
    list.setPreferredSize(new Dimension(300, 0))
    val re = new JScrollPane(list)
    re
  }
}


class Frame(listModel: ListModel[String], textDoc: Document, cntDoc: Document, startAction: Action, stopAction: Action) extends JFrame {
  setSize(800, 400)
  setTitle("anagram creater")
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setContentPane(new Content(listModel, textDoc, cntDoc, startAction, stopAction))
}

