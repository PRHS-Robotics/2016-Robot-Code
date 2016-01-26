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

@SuppressWarnings("rawtypes")
public class References {
	
	public static Class CLASS_AUTONOMOUS;
	public static Class CLASS_TELEOP;
	public static Class CLASS_TEST;
	public static final String CLASSNAME_TELEOP;
	public static final String CLASSNAME_AUTONOMOUS;
	public static final String CLASSNAME_TEST;
	public static final String LOG_FILE = "/home/lvuser/log.txt";
	public static final Level LOGLEVEL_CONSOLE = Level.ALL;
	public static final Level LOGLEVEL_FILE = Level.INFO;
	
	static{
		//Creates constants with the classes and class names for teleop, autonomous, and test
		CLASSNAME_TELEOP = "org.usfirst.frc.team4068.robot.teamCode.Teleop";
		CLASSNAME_AUTONOMOUS = "org.usfirst.frc.team4068.robot.teamCode.Autonomous";
		CLASSNAME_TEST = "org.usfirst.frc.team4068.robot.teamCode.Test";
		try {
			CLASS_TELEOP = Class.forName(CLASSNAME_TELEOP);
			CLASS_AUTONOMOUS = Class.forName(CLASSNAME_AUTONOMOUS);
			CLASS_TEST = Class.forName(CLASSNAME_TEST);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
