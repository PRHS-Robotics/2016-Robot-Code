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
import org.usfirst.frc.team4068.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.vision.USBCamera;

@SuppressWarnings("rawtypes")
public class References {
    public static DriveTrain driveTrain = new DriveTrain(Motors.leftDrive, Motors.rightDrive);
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
	    public static final USBCamera driverCam = new USBCamera("cam1");
	    public static final USBCamera visionCam = new USBCamera("cam2");
	}
	
	public static class Motors{
	    public static Talon leftDrive = new Talon(4);//left
	    public static Talon rightDrive = new Talon(5);//right
	    public static Talon seatMotor = new Talon(1);
	    public static Talon launcherMotor1 = new Talon(2);
	    public static Talon launcherMotor2 = new Talon(3);
	    public static Servo launcherServo = new Servo(0);
	}
}
