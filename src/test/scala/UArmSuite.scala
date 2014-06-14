package uArm

import org.scalatest._

class UArmSuite extends FunSuite {

  val arm = UArm() 

  test("initialize and reset") {
    arm.initialize
    arm.reset
  }

  test("grip for 0.5 sec, then release") {
    arm.grip
    Thread.sleep(500)
    arm.release
    Thread.sleep(500)
    arm.reset
    Thread.sleep(500)
  }


  test("move around: setPosition, strech") {
    arm.setPosition(0,0,0,0,None)
    Thread.sleep(1000)
    arm.setPosition(100,0,0,0,None)
    Thread.sleep(1000)
    arm.setPosition(200,0,0,0,None)
    Thread.sleep(1000)
    arm.reset
    Thread.sleep(1000)
  }

  test("move around: setPosition, rotation") {
    arm.setPosition(0,0,0,0,None)
    Thread.sleep(1000)
    arm.setPosition(0,45,0,0,None)
    Thread.sleep(1000)
    arm.setPosition(0,90,0,0,None)
    Thread.sleep(1000)
    arm.setPosition(0,0,0,0,None)
    Thread.sleep(1000)
    arm.setPosition(0,-45,0,0,None)
    Thread.sleep(1000)
    arm.setPosition(0,-90,0,0,None)
    Thread.sleep(1000)
    arm.setPosition(100,0,0,0,None)
    Thread.sleep(1000)
  }

  test("move around: setPosition, height") {
    arm.setPosition(100,0,0,0,None)
    Thread.sleep(1000)
    arm.setPosition(100,0,50,0,None)
    Thread.sleep(1000)
    arm.setPosition(100,0,100,0,None)
    Thread.sleep(1000)
    arm.setPosition(100,0,0,0,None)
    Thread.sleep(1000)
    arm.setPosition(100,0,-50,0,None)
    Thread.sleep(1000)
    arm.setPosition(100,0,-100,0,None)
    Thread.sleep(1000)
    arm.setPosition(0,0,0,0,None)
    Thread.sleep(1000)
  }

  test("move around: setPosition, handRot") {
    arm.setPosition(0,0,0,60,None)
    Thread.sleep(1000)
    arm.setPosition(0,0,0,0,None)
    Thread.sleep(1000)
    arm.setPosition(0,0,0,-60,None)
    Thread.sleep(1000)
    arm.reset
    Thread.sleep(500)
  }

  test("move around: moveTo X") {
    arm.moveTo(0,0,0,0)
    Thread.sleep(1000)
    arm.moveTo(100,0,0,0)
    Thread.sleep(1000)
    arm.moveTo(200,0,0,0)
    Thread.sleep(1000)
    arm.reset
    Thread.sleep(1000)
  }

  test("move around: moveTo Y") {
    arm.moveTo(0,0,0,0)
    Thread.sleep(1000)
    arm.moveTo(0,100,0,0)
    Thread.sleep(1000)
    arm.moveTo(0,200,0,0)
    Thread.sleep(1000)
    arm.moveTo(0,0,0,0)
    Thread.sleep(1000)
    arm.moveTo(0,-100,0,0)
    Thread.sleep(1000)
    arm.moveTo(0,-200,0,0)
    Thread.sleep(1000)
    arm.reset
    Thread.sleep(1000)
  }

  test("move around: moveTo Z") {
    arm.moveTo(50,0,0,0)
    Thread.sleep(1000)
    arm.moveTo(50,0,50,0)
    Thread.sleep(1000)
    arm.moveTo(50,0,100,0)
    Thread.sleep(1000)
    arm.moveTo(50,0,0,0)
    Thread.sleep(1000)
    arm.moveTo(50,0,-50,0)
    Thread.sleep(1000)
    arm.moveTo(50,0,-100,0)
    Thread.sleep(1000)
    arm.reset
    Thread.sleep(1000)
  }

  test("move around: move, draw a cube") {
    arm.moveTo(50,-50,-50,0)
    Thread.sleep(1000)
    arm.move(0,100,0,0)
    Thread.sleep(1000)
    arm.move(0,0,100,0)
    Thread.sleep(1000)
    arm.move(0,-100,0,0)
    Thread.sleep(1000)
    arm.move(100,0,0,0)
    Thread.sleep(1000)
    arm.move(0,100,0,0)
    Thread.sleep(1000)
    arm.move(0,0,-100,0)
    Thread.sleep(1000)
    arm.move(0,-100,0,0)
    Thread.sleep(1000)
    arm.move(-100,0,0,0)
    Thread.sleep(1000)
    arm.reset
    Thread.sleep(500)
  }

}
