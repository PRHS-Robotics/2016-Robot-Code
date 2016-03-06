package org.usfirst.frc.team4068.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class Launcher extends PIDSubsystem{
    Talon leftLauncher;
    Talon rightLauncher;
    Talon leadScrewMotor;
    AnalogInput hallEffectSensor;
    Servo launchServo;
    
    public Launcher(Talon left, Talon right, Talon seatMotor, AnalogInput hallSensor, Servo launcher){
        super("Launcher", 1.0, 0.0, 0.0);
        leftLauncher = left;
        rightLauncher = right;
        leadScrewMotor = seatMotor;
        hallEffectSensor = hallSensor;
        launchServo = launcher;
    }

    @Override
    protected double returnPIDInput() {
        hallEffectSensor.getVoltage();
        return 0;
    }

    @Override
    protected void usePIDOutput(double output) {
        leadScrewMotor.set(output);
    }

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub
    }
}
