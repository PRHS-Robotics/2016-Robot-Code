/*
 * Robot.java
 * 
 * v1.0 (Dec 21, 2015)
 * 
 * Author(s): Sean Bowers
 * 
 * The base of the robot code, all code is run from here.
 * */
package org.usfirst.frc.team4068.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team4068.robot.lib.References;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
@SuppressWarnings("unchecked")
public class Robot extends IterativeRobot {
    private static List<RunThread> runningThreads = new ArrayList<RunThread>();
    SendableChooser autonomousPrograms = new SendableChooser();
    private static Object teleopClass;
    private static Object autoClass;
    private static Object testClass;
    static{
        try {
            teleopClass = References.CLASS_TELEOP.newInstance();
            autoClass = References.CLASS_AUTONOMOUS.newInstance();
            testClass = References.CLASS_TEST.newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	/**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        Field[] autoFields = References.CLASS_AUTONOMOUS.getFields();
        for (Field field:autoFields){
            if (field.getType().equals(String.class) && field.isAnnotationPresent(autoProgram.class)){
                if (field.getAnnotation(autoProgram.class).defaultProgram()){
                    try {
                        autonomousPrograms.addDefault((String) field.get(null), field.get(null));
                    } catch (IllegalArgumentException
                            | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        autonomousPrograms.addObject((String) field.get(null), field.get(null));
                    } catch (IllegalArgumentException
                            | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        SmartDashboard.putData("Autonomous Chooser", autonomousPrograms);
    }

    /**
     * This function is called at the start of autonomous
     */
	public void autonomousInit(){
    	Method[] autoMethods = References.CLASS_AUTONOMOUS.getMethods();
    	for (Method method:autoMethods){
    	    if (method.isAnnotationPresent(RunCode.class) && method.getAnnotation(RunCode.class).loop()){
    	        if (method.isAnnotationPresent(runWithProgram.class)){
    	            if (method.getAnnotation(runWithProgram.class).program().equals(autonomousPrograms.getSelected())){
        	        RunLoop t = new RunLoop(method, References.CLASS_AUTONOMOUS);
                        t.start();
                        runningThreads.add(t);
    	            }
    	        }else{
        	    RunLoop t = new RunLoop(method, References.CLASS_AUTONOMOUS);
        	    t.start();
        	    runningThreads.add(t);
    	        }
    	    }else if(method.isAnnotationPresent(RunCode.class)){
        	if (method.isAnnotationPresent(RunCode.class) && method.getAnnotation(RunCode.class).loop()){
        	    if (method.isAnnotationPresent(runWithProgram.class)){
                        if (method.getAnnotation(runWithProgram.class).program().equals(autonomousPrograms.getSelected())){
                            RunOnce t = new RunOnce(method, References.CLASS_AUTONOMOUS);
                            t.start();
                            runningThreads.add(t);
                        }
                    }else{
                        RunOnce t = new RunOnce(method, References.CLASS_AUTONOMOUS);
                        t.start();
                        runningThreads.add(t);
                    }
    	        }
    	    }
    	}
    	while (this.m_ds.isEnabled() && isAutonomous()){}
    	for (RunThread thread:runningThreads){
    	    thread.terminate();
    	}
    	runningThreads.clear();
    }

    /**
     * This function is called at the start of operator control
     */
    public void teleopInit(){
    	Method[] autoMethods = References.CLASS_TELEOP.getMethods();
    	for (Method method:autoMethods){
    	    if (method.isAnnotationPresent(RunCode.class) && method.getAnnotation(RunCode.class).loop()){
		RunLoop t = new RunLoop(method, References.CLASS_TELEOP);
		t.start();
		runningThreads.add(t);
    	    }else if(method.isAnnotationPresent(RunCode.class)){
    	        RunOnce t = new RunOnce(method, References.CLASS_TELEOP);
    	        t.start();
    	        runningThreads.add(t);
    	    }
    	}
    	while (this.m_ds.isEnabled() && isOperatorControl()){}
    	for (RunThread thread:runningThreads){
    	    thread.terminate();
    	}
    	runningThreads.clear();
    }
    
    /**
     * This function is called at the start of test mode
     */
    public void testInit(){
    	Method[] autoMethods = References.CLASS_TEST.getMethods();
    	for (Method method:autoMethods){
    		if (method.isAnnotationPresent(RunCode.class) && method.getAnnotation(RunCode.class).loop()){
				RunLoop t = new RunLoop(method, References.CLASS_TEST);
				t.start();
				runningThreads.add(t);
			}else if(method.isAnnotationPresent(RunCode.class)){
				RunOnce t = new RunOnce(method, References.CLASS_TEST);
				t.start();
				runningThreads.add(t);
			}
    	}
    	while (this.m_ds.isEnabled() && isOperatorControl()){}
    	for (RunThread thread:runningThreads){
    		thread.terminate();
    	}
    	runningThreads.clear();
    }
    
    public void disabledInit(){
        
    }
    
    public void disabledPeriodic(){
        
    }
    
    public void teleopPeriodic(){
        
    }
    
    public void autonomousPeriodic(){
        
    }
    
    public void testPeriodic(){
        
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface RunCode {
	public boolean loop() default false;
    }
    
    /**
     * 
     * @author Sean
     * Annotation used to modify a string (WILL BE IGNORED IF NOT MODIFYING A STRING) that contains the name of
     * a new autonomous program that should be presented to the driver to select on the dashboard.
     * The default option allows the user to choose a default program 
     * (WARNING, IF MORE THAN ONE DEFAULT PROGRAM IS SPECIFIED ONLY ONE WILL ACTUALLY BE DEFAULT, WHICH ONE MAY BE RANDOM)
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface autoProgram {
        public boolean defaultProgram() default false;
    }
    
    /**
     * 
     * @author Sean
     * Annotation to modify a method that should be run with other methods in an autonomous program
     * (SPECIFIED PROGRAM MUST BE DEFINED BY A STRING WITH THE @autoProgram ANNOTATION)
     * Should be used with the @RunCode annotation
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface runWithProgram {
        public String program();
    }
    
    private static class RunLoop extends RunThread{
		Method m;
		Class<Object> c;
		boolean run = true;
		
		public RunLoop(Method m, Class<Object> c){
			super();
			this.m = m;
			this.c = c;
		}
		
		public void run(){
			m.setAccessible(true);
			try {
				while(run && !Thread.interrupted()){
				    if (c.isInstance(teleopClass)){
				        m.invoke(teleopClass, (Object[])null);
				    }else if(c.isInstance(autoClass)){
				        m.invoke(autoClass, (Object[])null);
				    }else if(c.isInstance(testClass)){
				        m.invoke(testClass, (Object[])null);
                                    }
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		public void terminate(){
			super.terminate();
			run = false;
		}
	}
	
	private static class RunOnce extends RunThread{
		Method m;
		Class<Object> c;
		boolean run = true;
		
		public RunOnce(Method m, Class<Object> c){
			super();
			this.m = m;
			this.c = c;
		}
		
		public void run(){
			if (run){
				m.setAccessible(true);
				try {
					m.invoke(c.newInstance(), (Object[])null);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void terminate(){
			super.terminate();
			run = false;
		}
	}
	
	private static abstract class RunThread extends Thread{
		public void terminate(){
			this.interrupt();
		}
		
		public RunThread(){
			Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable e) {
					System.out.println("Uncaught exception: " + e);
					e.printStackTrace(System.out);
				}
			};
			
			this.setUncaughtExceptionHandler(h);
		}
	}
}
