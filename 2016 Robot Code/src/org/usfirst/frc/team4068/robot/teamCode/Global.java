package org.usfirst.frc.team4068.robot.teamCode;

import org.usfirst.frc.team4068.robot.Robot.RunCode;
import org.usfirst.frc.team4068.robot.lib.References;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Global {
    static CameraServer dashboard = CameraServer.getInstance();
    static SendableChooser cameraSelection = new SendableChooser();
    static Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
    
    static int currSession;
    static int sessionfront;
    static int sessionback;
    
    @RunCode(loop=false)
    public static void testCode(){
        References.Cameras.driverCam.startCapture();
        CameraServer.getInstance().startAutomaticCapture(References.Cameras.driverCam);
    }
    
    //@RunCode(loop=false)
    public static void initRobot(){ //init code should go here, not in robotInit in Robot.java
        SmartDashboard.putBoolean("Driver Camera", true);
        
        References.Motors.leftDrive.setInverted(true);
        //References.Cameras.driverCam.setFPS(30);
        //References.Cameras.driverCam.ex
        //References.Cameras.driverCam.updateSettings();
        //References.Cameras.driverCam.closeCamera();
        //
        sessionfront = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        
        sessionback = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        currSession = sessionfront;
        NIVision.IMAQdxConfigureGrab(currSession);
        //dashboard.startAutomaticCapture("cam1");
        
        
        
        while (true){
            if (SmartDashboard.getBoolean("Driver Camera")){
                currSession = sessionfront;
            }else{
                currSession = sessionback;
            }
            if(true){
                if(currSession == sessionfront){
                          
                          NIVision.IMAQdxStopAcquisition(sessionback);
                          NIVision.IMAQdxStartAcquisition(currSession);
                          NIVision.IMAQdxConfigureGrab(currSession);
                } else if(currSession == sessionback){
                          
                          NIVision.IMAQdxStopAcquisition(sessionfront);
                          NIVision.IMAQdxStartAcquisition(currSession);
                          NIVision.IMAQdxConfigureGrab(currSession);
                }
            }
            NIVision.IMAQdxGrab(currSession, frame, 0);
            dashboard.setImage(frame);
            
        }
        
    }
    
    //@RunCode(loop=true)
    public static void cameras(){
        
    }
}
