/*
 * References.java
 * 
 * v1.0 (Dec 21, 2015)
 * 
 * Authors: Sean Bowers
 * 
 * A class to store variables and constants that will be used throughout the program.
 */
package org.usfirst.frc.team4068.robot.lib;

import org.usfirst.frc.team4068.robot.lib.Log.Level;
import org.usfirst.frc.team4068.robot.subsystems.Arduino;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.vision.USBCamera;

@SuppressWarnings("rawtypes")
public class References {
    //public static DriveTrain driveTrain = new DriveTrain(Motors.leftDrive, Motors.rightDrive);
    public static SendableChooser autoPrograms = new SendableChooser();
    public static SendableChooser cameraSelector = new SendableChooser();
    public static Arduino arduino = new Arduino(I2C.Port.kMXP, 8);
    public static DriverStation ds = null;
    public static boolean robotInit = false;
    public static RobotDrive driveTrain = new RobotDrive(References.Motors.leftDrive, References.Motors.rightDrive);
	public static Class CLASS_AUTONOMOUS;
	public static Class CLASS_TELEOP;
	public static Class CLASS_TEST;
	public static Class CLASS_GLOBAL;
	public static final String CLASSNAME_TELEOP;
	public static final String CLASSNAME_AUTONOMOUS;
	public static final String CLASSNAME_TEST;
	public static final String CLASSNAME_GLOBAL;
	public static final String LOG_FILE = "/home/lvuser/log.txt";
	public static final Level LOGLEVEL_CONSOLE = Level.ALL;
	public static final Level LOGLEVEL_FILE = Level.INFO;
	
	static{
		//Creates constants with the classes and class names for teleop, autonomous, and test
		CLASSNAME_TELEOP = "org.usfirst.frc.team4068.robot.teamCode.Teleop";
		CLASSNAME_AUTONOMOUS = "org.usfirst.frc.team4068.robot.teamCode.Autonomous";
		CLASSNAME_TEST = "org.usfirst.frc.team4068.robot.teamCode.Test";
		CLASSNAME_GLOBAL = "org.usfirst.frc.team4068.robot.teamCode.Global";
		try {
			CLASS_TELEOP = Class.forName(CLASSNAME_TELEOP);
			CLASS_AUTONOMOUS = Class.forName(CLASSNAME_AUTONOMOUS);
			CLASS_TEST = Class.forName(CLASSNAME_TEST);
			CLASS_GLOBAL = Class.forName(CLASSNAME_GLOBAL);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static class Cameras{
	    public static final USBCamera driverCam = new USBCamera("cam0");
	    public static final USBCamera visionCam = new USBCamera("cam1");
	    public static final AxisCamera backCam = new AxisCamera("10.40.68.11");
	}
	
	public static class Motors{
	    public static Talon leftDrive = new Talon(1);//left
	    public static Talon rightDrive = new Talon(0);//right (7 practice, 0 real)
	    public static Talon seatMotor = new Talon(2);
	    public static Talon launcherMotor1 = new Talon(3);
	    public static Talon launcherMotor2 = new Talon(4);
	    public static Servo launcherServo = new Servo(6);
	}
	
	public static class Controllers{
	    public static Controller xboxController = new Controller(0);
	    public static Joystick joystick = new Joystick(1);
	    public static Controller coDriver = new Controller(2);
	}
	
	public static enum AutoProgram{
	        NO_AUTO("No Auto Program"),
	        LOW_BAR_AND_SHOOT("Low bar & shoot"),
	        LOW_BAR("Low Bar");
	    
	        String name;
	        AutoProgram(String name){
	            this.name = name;
	        }
	        public void addToChooser(SendableChooser programs){
	            programs.addObject(name, this);
	        }
	        public String getName(){
	            return name;
	        }
	    }
}
