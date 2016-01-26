package org.usfirst.frc.team4068.robot.teamCode;

import org.usfirst.frc.team4068.robot.Robot.RunCode;
import org.usfirst.frc.team4068.robot.lib.Log;
import org.usfirst.frc.team4068.robot.lib.TargetingAssistant;
import org.usfirst.frc.team4068.robot.lib.TargetingAssistant.LaunchVector;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables2.type.NumberArray;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class Teleop {
    
    static Talon lights = new Talon(0);
    static Servo xServo = new Servo(6);
    static Servo yServo = new Servo(7);
    static NetworkTable vision;
    static double[] yValues;
    static double[] xValues;
    static int yRes = 640;
    static int xRes = 480;
    //static AxisCamera camera = new AxisCamera("10.40.68.11");
    
    
    //120 - 160
    @RunCode(loop=false)
    public static void init(){
        lights.set(-.4);
        vision = NetworkTable.getTable("GRIP/vision");
        
        //xServo.setAngle(0);
        //yServo.setAngle(200);
    }
    
    static Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
    
    @RunCode(loop=true)
    public static void setServo(){
        //camera.getImage(frame);
        //CameraServer.getInstance().setImage(frame);
        //xServo.setAngle(0);
        //yServo.setAngle(160);
    }
    
    static double goalWidth = .52;
    static double viewAngle = 49.4;
    static double heightGoal = 2.1;
    static double camAngle = 45;
    static double camHeight = .9;
    
    @RunCode(loop=false)
    public static void runMotor(){
        //new Talon(1).set(-1);
        //System.out.println("test");
        Log.logTrace("test");
        
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //double[] targetnumY = vision.getNumberArray("centerY", new double[]{0});
        //double[] targetnumX = vision.getNumberArray("centerX", new double[]{0});
        double[] heightArray = vision.getNumberArray("height", new double[]{0});
        double[] centerYArray = vision.getNumberArray("centerX", new double[]{0});
        while (heightArray[0] == 0 || centerYArray[0] == 0){
            heightArray = vision.getNumberArray("height", new double[]{0});
            centerYArray = vision.getNumberArray("centerX", new double[]{0});
        }
        
        //process goals
        double height = heightArray[0];
        double h1 = centerYArray[0] + (.5*height);
        double h2 = 480 - centerYArray[0] - (.5*height);
        
        double theta = (viewAngle*height*h2)/((h1*height)+(height*h2)+1);
        double distance = heightGoal/(Math.tan(Math.toRadians(theta+(camAngle-24.7))));
        Log.logInfo("Distance: "+distance);
        
        LaunchVector test = TargetingAssistant.calculate(distance);
        if (!(test == null)){
            Log.logInfo("Launch info for 2m");
            Log.logInfo("Velocity: " + test.getVelocity());
            Log.logInfo("Angle: " + test.getAngle());
        }else{
            Log.logInfo("Nothing found");
        }
        //206 cm distance
        //52 cm width
    }
    
    /*
    @RunCode(loop=true)
    public static void visionTest(){
        final NumberArray targetNumY = new NumberArray();
        final NumberArray targetNumX = new NumberArray();
        vision.retrieveValue("centerY", targetNumY);
        vision.retrieveValue("centerX", targetNumX);
        
        try{
            SmartDashboard.putNumber("centerY", targetNumY.get(1));
            SmartDashboard.putNumber("centerX", targetNumX.get(1));
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        
        double errorX = (xRes/2) - targetNumX.get(1);
        double errorY = (yRes/2) - targetNumY.get(1);
        SmartDashboard.putNumber("error y", errorY);
        yServo.setAngle(160-((errorY*40)/240));
        if (errorX>0){
            //yServo.setAngle(((errorY*40)/240)+120);
        }else if (errorX<0){
            //yServo.setAngle(160-((errorY*40)/240));
        }
        
    }
    */
    
}
