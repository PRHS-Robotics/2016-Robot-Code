package org.usfirst.frc.team4068.robot.lib;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team4068.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class Controller extends Joystick{

    List<ButtonListener> listeners = new ArrayList<ButtonListener>();
    
    public Controller(int port) {
        super(port);
        new Thread(){
            public void run(){
                listenForButtonPress();
            }
        }.start();
    }
    
    public void addButtonListener(ButtonListener listener){
        listeners.add(listener);
    }
    
    public void removeButtonListener(ButtonListener listener){
        listeners.remove(listener);
    }
    
    private void notifyListeners(int number){
        for(ButtonListener listener:listeners){
            listener.buttonPressed(number);
        }
    }
    
    private void listenForButtonPress(){
        while(true){
            for(XBoxButton button:XBoxButton.values()){
                if(this.getRawButton(button.buttonNumber)){
                    notifyListeners(button.buttonNumber);
                }
            }
            DriverStation.getInstance().waitForData();
        }
    }
    
    public enum XBoxButton{
        A(1),
        B(2),
        X(3),
        Y(4),
        LEFT_BUMPER(5),
        RIGHT_BUMPER(6),
        BACK(7),
        START(8),
        LEFT_JOYSTICK(9),
        RIGHT_JOYSTICK(10);
        
        int buttonNumber;
        XBoxButton(int number){
            buttonNumber = number;
        }
    }
    
    public enum XBoxAxis{
        LEFT_X(1),
        LEFT_Y(2), 
        TRIGGERS(3), //left pos, right neg
        RIGHT_X(4),
        RIGHT_Y(5);
        
        int axisNumber;
        XBoxAxis(int number){
            axisNumber = number;
        }
    }
    
    public interface ButtonListener{
        public void buttonPressed(int number);
    }
}
