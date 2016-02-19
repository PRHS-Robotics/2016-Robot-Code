package org.usfirst.frc.team4068.robot.teamCode;

import org.usfirst.frc.team4068.robot.Robot.RunCode;
import org.usfirst.frc.team4068.robot.lib.Log;
import org.usfirst.frc.team4068.robot.lib.References;
import org.usfirst.frc.team4068.robot.lib.TargetingAssistant;
import org.usfirst.frc.team4068.robot.lib.TargetingAssistant.LaunchVector;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Teleop {
    
    //static Talon lights = new Talon(0);
    
    //static Talon motor3 = new Talon(3);
    //static Talon motor4 = new Talon(2);
    static Joystick xboxController = new Joystick(0);
    static Joystick joystick = new Joystick(1);
    static Joystick coDriver = new Joystick(2);
    //static RobotDrive drive = new RobotDrive(References.motor1, References.motor2);
    static Servo xServo = new Servo(6);
    static Servo yServo = new Servo(7);
    static NetworkTable vision;
    static double[] yValues;
    static double[] xValues;
    static int yRes = 640;
    static int xRes = 480;
    //static AxisCamera camera = new AxisCamera("10.40.68.11");
    
    @RunCode(loop=true)
    public static void runLauncher(){
        References.Motors.launcherMotor1.set(coDriver.getY());
        References.Motors.launcherMotor2.set(coDriver.getY());
        
        References.Motors.seatMotor.set(coDriver.getRawAxis(5));
        
        if(coDriver.getRawButton(1)){
            References.Motors.launcherServo.set(0);
            //References.launcherServo.set(0);
        }else{
            References.Motors.launcherServo.set(100);
        }
    }
    
    //120 - 160
    @RunCode(loop=false)
    public static void init(){
        //CameraServer.getInstance().startAutomaticCapture("cam1");
        //lights.set(-.4);
        vision = NetworkTable.getTable("GRIP/vision");
        //motor2.setInverted(true);
        //The roboRIO can only act as a master, so when initializing the I2C object, the address
        //of the slave that it will be communicating with is supplied. A new I2C object is needed
        //for each slave the roboRIO needs to talk to.
        byte[] in = new byte[]{'a'};
        byte[] out = new byte[]{0, 0, 0, 0};
        I2C test = new I2C(Port.kMXP, 6);
        test.transaction(out, 4, in, 1);
        boolean direction = true;
        boolean xbox = false;
        boolean ram = false;
        while (true){
            if (xbox){
                if (xboxController.getRawButton(1)){
                    direction = !direction;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                
                if (xboxController.getRawButton(4)){
                    ram = !ram;
                    SmartDashboard.putBoolean("Charge mode", ram);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                
                if (!direction){
                    if (!ram){
                        References.Motors.leftDrive.set(-xboxController.getY());
                        References.Motors.rightDrive.set(-xboxController.getRawAxis(5));
                    }else{
                        References.Motors.leftDrive.set(-xboxController.getY());
                        References.Motors.rightDrive.set(-xboxController.getY());
                    }
                }else{
                    if (!ram){
                        References.Motors.leftDrive.set(xboxController.getY());
                        References.Motors.rightDrive.set(xboxController.getRawAxis(5));
                    }else{
                        References.Motors.leftDrive.set(xboxController.getY());
                        References.Motors.rightDrive.set(xboxController.getY());
                    }
                }
            }else{
                //References.motor1.set(left.getRawAxis(1));
                //References.motor2.set(right.getRawAxis(1));
                References.driveTrain.arcadeDrive(-joystick.getRawAxis(2), -joystick.getRawAxis(1));
            }
        }
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
    
    //static Talon seatMotor = new Talon(0);
    static AnalogInput hallSensor = new AnalogInput(0);
    static double hallVoltage = 0;
    static double prevVoltage = 0;
    static int rotCount = 0;
    static double hallMotorSpeed = .5;
    static double triggerZone = .8;
    @RunCode(loop=false)
    public static void motorTestInit(){
        hallVoltage = 0;
        prevVoltage = 0;
        rotCount = 0;
        hallMotorSpeed = .5;
        triggerZone = .8;
        hallVoltage = hallSensor.getVoltage();
        prevVoltage = hallVoltage;
        edgetype = 0;
    }
    static int edgetype = 0;
    //@RunCode(loop=true)
    public static void motorTest(){
        hallVoltage = hallSensor.getVoltage();
        if ((hallVoltage - prevVoltage) > triggerZone){ //leading edge
            //rotCount++;
            if (edgetype == 1){
                rotCount++;
            }else if (edgetype == 0){
                edgetype = 1;
            }
        }else if ((prevVoltage - hallVoltage) > triggerZone){ //falling edge
            //code for if something should be done on a falling edge
            //rotCount++;
            if (edgetype == 2){
                rotCount++;
            }else if (edgetype == 0){
                edgetype = 2;
            }
        }
        prevVoltage = hallVoltage;
        
        if (rotCount > 21){ //10 rotations
            hallMotorSpeed = 0;
        }
        
        References.Motors.seatMotor.set(hallMotorSpeed);
        SmartDashboard.putNumber("Hall Effect Sensor", hallSensor.getVoltage());
        SmartDashboard.putNumber("rotations", rotCount);
        SmartDashboard.putNumber("Motor Speed", hallMotorSpeed);
        /*
        try {
            Thread.sleep(25);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
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
