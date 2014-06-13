# Interface to control the uArm robotic arm by UFactory written in Scala

The code assumes that the [RemoteControl application](https://github.com/UFactory/UF_uArm) (tested with beta 3) is running on the Arduino.

Instead of exposing the remote control interface in a Scala API, the goal is to
expose a "reverse kinematics" API.  The user just set the position (x,y,z) and
the arm moves to the position.  Part of this is already done in the uArm driver
which does not expose the individual servo, but things like rotation, stretch
and height.  Here, we go all the way to Cartesian coordinates.  (0,0,0) is the
position of the arm after initialization by the driver.  The unit should
(hopefully) be millimeters.

# ToDo

* does not yet work quite as expected, need to look into the uArm controller to really understand how they convert to angle
* beside jSSC, maybe we can use Netty (might be easier to have a full-duplex communication with the Arduino)


## Compiling and running

This project is build using [sbt](http://www.scala-sbt.org/).
To install sbt follow the instructions at [http://www.scala-sbt.org/release/tutorial/Setup.html](http://www.scala-sbt.org/release/tutorial/Setup.html).

Then, in a console, execute:
```
$ sbt
> compile
> run
```
run will launch a small GUI application to control the uArm with the mouse.
Maintain the left mouse button pressed to move the arm in the horizontal plan.
Use the mouse wheel to raise/lower the arm.
Use the right mouse button grip/release objects.


## Acknowledgements

source of inspiration:
* [Arduino Java tutorial](http://playground.arduino.cc/Interfacing/Java)
* [jSSC tutorial](https://code.google.com/p/java-simple-serial-connector/wiki/jSSC_Start_Working)
* [the mouse control application by UFactory](https://github.com/UFactory/uArm_Mouse_Control_for_Windows)
* [the mouse control application in Java by gschaden](https://github.com/gschaden/uArm)
