package org.usfirst.frc.team4068.robot.teamCode;

import org.usfirst.frc.team4068.robot.Robot.RunCode;
import org.usfirst.frc.team4068.robot.lib.References;
import org.usfirst.frc.team4068.robot.lib.References.AutoProgram;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
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
        
        References.cameraSelector.addDefault("Front - Launcher", References.Cameras.visionCam);
        References.cameraSelector.addObject("Front - side", References.Cameras.driverCam);
        References.cameraSelector.addObject("Back", References.Cameras.backCam);
    }
    
    @RunCode(loop=false)
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