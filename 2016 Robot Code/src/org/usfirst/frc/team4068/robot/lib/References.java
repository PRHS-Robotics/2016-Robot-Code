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

@SuppressWarnings("rawtypes")
public class References {
	
	public static Class CLASS_AUTONOMOUS;
	public static Class CLASS_TELEOP;
	public static Class CLASS_TEST;
	
	static{
		//Creates constants with the classes and class names for teleop, autonomous, and test
		final String CLASSNAME_TELEOP = "org.usfirst.frc.team4068.robot.teamCode.Teleop";
		final String CLASSNAME_AUTONOMOUS = "org.usfirst.frc.team4068.robot.teamCode.Autonomous";
		final String CLASSNAME_TEST = "org.usfirst.frc.team4068.robot.teamCode.Test";
		try {
			CLASS_TELEOP = Class.forName(CLASSNAME_TELEOP);
			CLASS_AUTONOMOUS = Class.forName(CLASSNAME_AUTONOMOUS);
			CLASS_TEST = Class.forName(CLASSNAME_TEST);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
