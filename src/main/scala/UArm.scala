package uArm

import jssc.SerialPort
import java.lang.AutoCloseable


class UArm( port: String,
            baudRate: Int = SerialPort.BAUDRATE_9600,
            dataBits: Int = SerialPort.DATABITS_8, 
            stopBits: Int = SerialPort.STOPBITS_1,
            parity: Int = SerialPort.PARITY_NONE) extends AutoCloseable {

  //port
  private var opened = false
  private val serialPort = new SerialPort(port)

  //state of the arm
  private var x = 0
  private var y = 0
  private var z = 0
  private var r = 0 //hand rotation
  private var g = false

  //boundaries (taken from the mouse control application by UFactory)
  final private val rotationMax =   90
  final private val rotationMin =  -90
  final private val  stretchMax =  210
  final private val  stretchMin =    0
  final private val   heightMax =  150
  final private val   heightMin = -180
  final private val     offsetX =   55

  def initialize() {
    if (!opened) {
      opened = true
      serialPort.openPort()
      serialPort.setParams(baudRate, dataBits, stopBits, parity) 
    }
  }

  def close() {
    if (opened) {
      serialPort.closePort();
      opened = false
    }
  }

  def reset {
    x = 0
    y = 0
    z = 0
    r = 0
    g = false
    mv(Some(false))
  }


  /** make the sequence of bytes that need to be send to the arm */
  private def makeByteSeq(stretch: Int, height: Int,
                          rotation: Int, handRot: Int,
                          grip: Option[Boolean]): Array[Byte] = {

    def clamp(min: Int, v: Int, max: Int) =
      math.max(min, math.min(v, max))
    def lower(v: Int): Byte = (v & 0xff).toByte
    def upper(v: Int): Byte = ((v >> 8) & 0xff).toByte

    val b01 = clamp(rotationMin, rotation, rotationMax)
    val b23 = clamp(stretchMin, stretch, stretchMax)
    val b45 = clamp(heightMin, height, heightMax)
    val b67 = clamp(rotationMin, handRot, rotationMax)
    val b8 = grip match {
      case Some(true) =>  1: Byte //catch
      case Some(false) => 2: Byte //release
      case None =>        0: Byte //nothing
    }

    //TODO this could by simplified using a ByteBuffer with LITTLE_ENDIAN
    Array[Byte](
        -1: Byte, -86: Byte, // 0xff: Byte, 0xaa: Byte,
        upper(b01), lower(b01),
        upper(b23), lower(b23),
        upper(b45), lower(b45),
        upper(b67), lower(b67),
        b8
      )
  }

  /** The uArm command.
   *  It is not recommended to use setPosition and move. (moveTo is OK.)
   */
  def setPosition(stretch: Int, height: Int, rotation: Int, handRot: Int, grip: Option[Boolean]) {
    if (opened) {
      val bytes = makeByteSeq(stretch, height, rotation, handRot, grip)
      //println("stretch = " + stretch + ", height = " + height + ", rotation = " + rotation)
      //println("command = " + org.apache.commons.codec.binary.Hex.encodeHexString(bytes))
      serialPort.writeBytes(bytes)
    }
  }

  private def mv(grip: Option[Boolean] = None) {
    import math._
    //(0,0,0) is actually (offsetX, 0, 0) away from the center of rotation.
    val x2 = x + offsetX
    val rotation = toDegrees(atan2(y, x2)).toInt
    val stretch = round(sqrt(x2*x2 + y*y)).toInt - offsetX //strech does nor include height
    setPosition(stretch, z, rotation, r, grip)
  }
  
  /** absolute motion */
  def moveTo(x: Int, y: Int, z: Int, handRot: Int) {
    this.x = x
    this.y = y
    this.z = z
    r = handRot
    mv()
  }
  
  /** relative motion */
  def move(dx: Int, dy: Int, dz: Int, dhandRot: Int) {
    x += dx
    y += dy
    z += dz
    r += dhandRot
    mv()
  }

  def grip = {
    g = true
    mv(Some(true))
  }
  
  def release = {
    g = false
    mv(Some(false))
  }

  def toggleGrip = {
    if (g) release
    else grip
  }

}

object UArm {

  def apply(port: String = "/dev/ttyUSB0") = new UArm(port) //for Linux
  //for Windows: COM1
  //for Mac: /dev/tty.usbserial-A9007UX1 ?

}
