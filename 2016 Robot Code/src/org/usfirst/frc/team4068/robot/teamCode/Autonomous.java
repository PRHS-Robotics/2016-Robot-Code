package org.usfirst.frc.team4068.robot.teamCode;

import org.usfirst.frc.team4068.robot.Robot.RunCode;
import org.usfirst.frc.team4068.robot.lib.References;

import edu.wpi.first.wpilibj.Timer;

public class Autonomous {
    
    @RunCode(loop=false)
    public static void autoProgramLowBar(){
        while(!References.robotInit){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        References.AutoProgram autoProgram = (References.AutoProgram)References.autoPrograms.getSelected();
        System.out.println(autoProgram.getName());
        if(autoProgram.equals(References.AutoProgram.LOW_BAR_AND_SHOOT)){
            Timer timer = new Timer();
            timer.start();
            while(timer.get() <= 5){
                for(int i=0;i<100;i++){
                    References.driveTrain.arcadeDrive(1, 0);
                }
            }
            
        }else if(autoProgram.equals(References.AutoProgram.LOW_BAR)){
            Timer timer = new Timer();
            timer.start();
            while(timer.get() <= 5){
                for(int i=0;i<100;i++){
                    References.driveTrain.arcadeDrive(1, 0);
                }
            }
        }
        
    }
    
    
}
