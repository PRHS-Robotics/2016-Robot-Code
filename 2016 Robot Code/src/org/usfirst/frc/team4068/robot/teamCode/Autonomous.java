package org.usfirst.frc.team4068.robot.teamCode;

import org.usfirst.frc.team4068.robot.Robot.RunCode;
import org.usfirst.frc.team4068.robot.lib.References;

import edu.wpi.first.wpilibj.Timer;

public class Autonomous {
    
    @RunCode(loop=false)
    public static void autoProgramLowBar(){
        if(References.autoPrograms.getSelected().equals(References.AutoProgram.LOW_BAR_AND_SHOOT)){
            while(true){
                References.driveTrain.arcadeDrive(1, 0);
            }
        }
    }
    
    
}
