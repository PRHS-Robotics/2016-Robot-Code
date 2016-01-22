package org.usfirst.frc.team4068.robot.lib;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Log {
    
    private static String logFilePath = References.LOG_FILE;
    private static Level logLevelConsole = References.LOGLEVEL_CONSOLE;
    private static Level logLevelFile = References.LOGLEVEL_FILE;
    
    public static void setLogFile(String file){
        logFilePath = file;
    }
    
    public static void setLogLevelConsole(Level level){
        logLevelConsole = level;
    }
    
    private static boolean log(String log, Level l){
        int var = (logLevelConsole.isHigherOrEqual(l)?2:0)+
                (logLevelFile.isHigherOrEqual(l)?1:0);
        return logMsg(log, l, var);
    }
    
    public static boolean logFatal(String log){
        return log(log, Level.FATAL);
    }
    
    public static boolean logError(String log){
        return log(log, Level.ERROR);
    }
    
    public static boolean logWarn(String log){
        return log(log, Level.WARN);
    }
    
    public static boolean logInfo(String log){
        return log(log, Level.INFO);
    }
    
    public static boolean logDebug(String log){
        return log(log, Level.DEBUG);
    }
    
    public static boolean logTrace(String log){
        return log(log, Level.TRACE);
    }
    
    //type: 0 - none, 1 - file, 2 - console, 3 - both
    private static boolean logMsg(String msg, Level level, int type){
        String out = String.format("[%s] %s", level.getString(), msg);
        switch(type){
        case 2:
            return writeConsole(out);
        case 1:
            return writeFile(out);
        case 3:
            writeConsole(out);
            return writeFile(out);
        }
        return false; //if code got to here, type int was used incorrectly, or nothing was written
    }
    
    private static boolean writeConsole(String msg){
        System.out.println(msg);
        return true;
    }
    
    private static boolean writeFile(String msg){
        try(PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))){
            logFile.println(msg);
            logFile.close();
            return true;
        }catch(IOException e){
            System.out.println("[ERROR] Error logging message to file");
            e.printStackTrace();
            return false;
        }
    }
    
    enum Level{
        OFF(0, "OFF"), FATAL(1, "FATAL ERROR"), ERROR(2, "ERROR"), 
        WARN(3, "WARNING"), INFO(4, "INFO"), DEBUG(5, "DEBUG"), 
        TRACE(6, "TRACE"), ALL(7, "ALL");
        
        int level;
        String string;
        private Level(int level, String string){
            this.level=level;this.string=string;
        }
        
        public int getLevel(){
            return level;
        }
        
        public String getString(){
            return string;
        }
        
        public boolean isLower(Level l){
            return (this.level < l.level);
        }
        
        public boolean isHigher(Level l){
            return (this.level > l.level);
        }
        
        public boolean isEqual(Level l){
            return (this.level == l.level);
        }
        
        public boolean isHigherOrEqual(Level l){
            return this.isEqual(l) || this.isHigher(l);
        }
    }
}
