package org.usfirst.frc.team4068.robot.subsystems;

import org.usfirst.frc.team4068.robot.Robot.State;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class Arduino {
    
    I2C arduino;
    public Arduino(Port port,int address){
        arduino = new I2C(port, address);
        sendData("Comm");
    }
    
    public void setState(State state){
        sendData(state.getArduinoCode());
    }
    
    public int getTick(){
        byte[] data = readData("rt");
        int tick = (int) data[0];
        return tick;
    }
    
    public void sendData(String data){
        char[] charArray = data.toCharArray();
        
        //creates a packet with enough room for the data and the checksum
        byte[] packet = new byte[charArray.length + 1]; 
        
        for (int i = 0; i < charArray.length; i++) {
            packet[i] = (byte) charArray[i];
        }
        
        byte[] writeData = packet;
        arduino.transaction(writeData, writeData.length, null, 0);
    }
    
    public byte[] readData(String request){
        byte[] dataReceived = new byte[7];
        char[] charArray = request.toCharArray();
        byte[] writeData = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
                writeData[i] = (byte) charArray[i];
        }
        arduino.transaction(writeData, writeData.length, dataReceived, 7);
        return dataReceived;
    }
    public byte read(){
        byte[] in = {0};
        arduino.readOnly(in, 1);
        return in[0];
    }
    
}
