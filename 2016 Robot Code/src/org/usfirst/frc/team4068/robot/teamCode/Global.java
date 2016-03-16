package org.usfirst.frc.team4068.robot.teamCode;

import org.usfirst.frc.team4068.robot.Robot.RunCode;
import org.usfirst.frc.team4068.robot.lib.References;
import org.usfirst.frc.team4068.robot.lib.References.AutoProgram;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Global{
    
    @RunCode(loop=false)
    public static void initRobot(){ //initializes robot - use this not robotInit in Robot.java
        References.autoPrograms.addDefault("Do nothing", null);
        for(AutoProgram p : References.AutoProgram.values()){
            References.autoPrograms.addObject(p.name(), p);
        }
        SmartDashboard.putData("Auto Program", References.autoPrograms);
        
        References.cameraSelector.addDefault("Front - Launcher", "one");
        References.cameraSelector.addObject("Front - side", "zero");
        References.cameraSelector.addObject("Back", "two");
        SmartDashboard.putData("Camera Selector", References.cameraSelector);
    }
    
    @RunCode(loop=false)
    public static void oldCameraCode(){
        Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        References.Cameras.driverCam.openCamera();
        References.Cameras.driverCam.startCapture();
        References.Cameras.visionCam.openCamera();
        //Button switchCam = new JoystickButton(References.Controllers.joystick, 2);
        //switchCam.whenReleased(new SwitchCamera());
        //button 2
        References.Cameras.driverCam.setSize(1280, 720);
        References.Cameras.visionCam.setSize(1280, 720);
        SmartDashboard.putBoolean("switch Camera", (References.Controllers.joystick.getRawButton(2)));
        SmartDashboard.putBoolean("switch Camera 2", (References.Controllers.joystick.getRawButton(3)));
        SmartDashboard.putNumber("Camera", 0);
        
        String selection = "zero";
        boolean changed = false;
        while(true){
            SmartDashboard.putBoolean("switch Camera", (References.Controllers.joystick.getRawButton(2)));
            if(References.Controllers.joystick.getRawButton(2)){
            	selection =  "zero";
            	
            	changed = true;
            }
            if(References.Controllers.coDriver.getRawButton(2)){
            	selection = "one";
            	changed = true;
            }
            if(References.Controllers.coDriver.getRawButton(1)){
            	selection = "two";
            	changed = true;
            }
            if((!selection.equals((String)References.cameraSelector.getSelected()))||changed){
                if(!selection.equals((String)References.cameraSelector.getSelected())){
                	selection = (String)References.cameraSelector.getSelected();
                }
                System.out.println(selection);
                if(selection.equals("zero")){
                    References.Cameras.driverCam.stopCapture();
                    References.Cameras.visionCam.startCapture();
                }else if (selection.equals("one")){
                    References.Cameras.visionCam.stopCapture();
                    References.Cameras.driverCam.startCapture();
                }
            }
            
            if(selection.equals("zero")){
                References.Cameras.visionCam.getImage(frame);
            }else if (selection.equals("one")){
                References.Cameras.driverCam.getImage(frame);
            }else if (selection.equals("two")){
                References.Cameras.backCam.getImage(frame);
            }
            CameraServer.getInstance().setImage(frame);
        }
    }
    
    public static void runCameras(){
        Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        Object camera;
        References.Cameras.driverCam.openCamera();
        References.Cameras.visionCam.closeCamera();
        while(true){ 
            camera = References.cameraSelector.getSelected();
            if(USBCamera.class.isInstance(camera)){
                if(camera.equals(References.Cameras.driverCam)){
                    References.Cameras.visionCam.closeCamera();
                    References.Cameras.driverCam.openCamera();
                }else{
                    References.Cameras.driverCam.closeCamera();
                    References.Cameras.visionCam.openCamera();
                }
                ((USBCamera)camera).getImage(frame);
            }else{
                ((AxisCamera)camera).getImage(frame);
            }
            CameraServer.getInstance().setImage(frame);
        }
    }
    
}