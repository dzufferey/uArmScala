package uArm

import javax.swing._
import java.awt._
import java.awt.event._

class UArmGUI extends JFrame {

  private var arm: Option[UArm] = None

  setTitle("uArm mouse control")

  val portLabel = new JLabel("port:")
  val portTextField = new JTextField("/dev/ttyUSB0", 20)
  val connect = new JButton("connect")

  val xTF = new JFormattedTextField(0)
  val yTF = new JFormattedTextField(0)
  val zTF = new JFormattedTextField(0)
  val move = new JButton("move")
  val toggle = new JButton("catch/release")

  val status = new JLabel("")

  private def initGUI {
    setLayout(new BorderLayout())

    val topRow = new JPanel()
    topRow.setLayout(new FlowLayout())
    topRow.add(portLabel)
    topRow.add(portTextField)
    topRow.add(connect)
  
    val centerPane = new JPanel

    val moveRow = new JPanel()
    moveRow.setLayout(new FlowLayout())
    moveRow.add(new JLabel("x:"))
    xTF.setColumns(4)
    moveRow.add(xTF)
    moveRow.add(new JLabel("y:"))
    yTF.setColumns(4)
    moveRow.add(yTF)
    moveRow.add(new JLabel("z:"))
    zTF.setColumns(4)
    moveRow.add(zTF)
    moveRow.add(move)
    moveRow.add(toggle)
    centerPane.add(moveRow)
    
    centerPane.add(Box.createVerticalGlue())
    centerPane.add(new JLabel("Hold down the left mouse button to move the arm in the horizontal plane."))
    centerPane.add(new JLabel("Use the right mouse button grip/release objects."))
    centerPane.add(new JLabel("Use the mouse wheel to raise/lower the arm."))
    centerPane.add(Box.createVerticalGlue())


    add(topRow, BorderLayout.NORTH)
    add(centerPane, BorderLayout.CENTER)
    add(status, BorderLayout.SOUTH)

    setSize(600, 400);
    //pack();
  }

  addWindowListener(new WindowAdapter() {
    override def windowClosing(evt: WindowEvent) {
      arm.map(_.close)
      System.exit(0)
    }
  })

  connect.addActionListener(new ActionListener() {
    def actionPerformed(e: ActionEvent) {
      try {
        if (arm.isDefined) {
          arm.map(_.close)
          arm = None
          connect.setText("connect")
        } else {
          val a= new UArm(portTextField.getText().trim())
          a.initialize
          arm = Some(a)
          connect.setText("disconnect")
        }
      } catch {
        case err: Exception =>
          status.setText(err.getMessage())
      }
    }
  })

  val mouse = new MouseAdapter() {

    val speed = 5
    val minDelay = 50
    var x = 0
    var y = 0
    var time = System.currentTimeMillis();

    override def mousePressed(e: MouseEvent) {
      if (e.getButton() == MouseEvent.BUTTON1) {
        x = e.getX()
        y = e.getY()
      }
    }

    override def mouseClicked(e: MouseEvent) {
      if (e.getButton() == MouseEvent.BUTTON3) {
        try {
          arm.map(_.toggleGrip)
        } catch {
          case err: Exception =>
            status.setText(err.getMessage())
        }
      }
    }

    override def mouseDragged(e: MouseEvent) {
        val mask = InputEvent.BUTTON1_DOWN_MASK;
        val t = System.currentTimeMillis();
        if ((e.getModifiersEx() & mask) == mask && t - time >= minDelay) {
          val x2 = e.getX()
          val y2 = e.getY()
          val dx = (x2 - x) / speed
          val dy = (y2 - y) / speed
          if(dx != 0) x = x2
          if(dy != 0) y = y2
          time = t
          try {
            arm.map(_.move(dx, dy, 0, 0))
            status.setText("move: x = " + dx + ", y = " + dy)
          } catch {
            case err: Exception =>
              status.setText(err.getMessage())
          }
        }
    }

    override def mouseWheelMoved(e: MouseWheelEvent) {
      val notches = -1 * e.getWheelRotation()
      try {
        arm.map(_.move(0,0,notches,0))
        status.setText("move: z = " + notches)
      } catch {
        case err: Exception =>
          status.setText(err.getMessage())
      }
    }
  }

  addMouseListener(mouse)
  addMouseMotionListener(mouse)
  addMouseWheelListener(mouse)
  
  move.addActionListener(new ActionListener() {
    def actionPerformed(e: ActionEvent) {
      try {
        val x = xTF.getText().toInt
        val y = yTF.getText().toInt
        val z = zTF.getText().toInt
        arm.map(_.moveTo(x, y, z, 0))
        status.setText("move to: x ="+x+", y ="+y+", z = "+z)
      } catch {
        case err: Exception =>
          status.setText(err.getMessage())
      }
    }
  })
  
  toggle.addActionListener(new ActionListener() {
    def actionPerformed(e: ActionEvent) {
      try {
        arm.map(_.toggleGrip)
      } catch {
        case err: Exception =>
          status.setText(err.getMessage())
      }
    }
  })

  initGUI
  setVisible(true)

}

object UArmGUI {

  def main(args: Array[String]) {
    new UArmGUI
  }
}
