package org.usfirst.frc.team4068.robot.lib;

public class TargetingAssistant {
    //constants
    static final double Cd = .40826; // unitless, .40826 calculated, .47 estimated on internet
    static final double RHO = .59; // kg/m3, air density about 1.2(ish) at sea level
    static final double A = .0506707; // m2
    static final double M = .2948; // kg
    static final double K = .006086; //coefficient to velocity in drag equation, takes all variables into account
    static final double G = 9.81; // m/s2, acceleration due to gravity
    static double maxVelocity = 10; //m/s
    static double velocityStep = .1; //m/s
    static double minTheta = Math.toRadians(10); //degrees
    static double maxTheta = Math.toRadians(90); //degrees
    static double thetaStep = Math.toRadians(.5); //degrees
    static double maxHeight = 3; //m
    static double minHeight = 2.3; //m
    
    //Simulation variables
    static double simTick = .001; // how many seconds does one tick simulate
    static double maxTime = 10; //maximum amount of time in air a projectile may have
   
    //Program variables/objects
    static ProjectileData data = new ProjectileData();
    
    public static LaunchVector calculate(double distance){
        //double maxVelocity = 15; //m/s
        //double velocityStep = .5; //m/s
        //double minTheta = Math.toRadians(10); //degrees
        //double maxTheta = Math.toRadians(60); //degrees
        //double thetaStep = Math.toRadians(.5); //degrees
        //double distance = distance; //m - distance to goal
        //double maxHeight = 2.47;
        //double minHeight = 2.45;
        double v0 = 0;
        double theta = 0;
        boolean run = true;
       
        for (v0 = 0; v0 <= maxVelocity && run; v0 += velocityStep){
            for (theta = minTheta; theta <= maxTheta && run; theta += thetaStep){
                double estTime = (M*(Math.log((M*G)/((v0*Math.sin(theta)*K)+(M*G)))))/(-K);
                double estDist = ((M/K)*v0*Math.sin(theta))*(1-Math.exp(-K*estTime/M));
               
                if ((estDist > distance)){ //if the projectile would have reached max height after the goal, just skip simulation
                    simulate(v0, theta, distance);
                    if (data.y >= minHeight && data.y <= maxHeight){
                        run = false;
                        break;
                    }
                }
            }
        }
       
        if (!run){
            Log.logTrace("Found trajectory for " + distance + " meters from goal:");
            Log.logTrace("Y height: " + data.y);
            Log.logTrace("X distance: " + data.x);
            Log.logTrace("Initial velocity: " + data.v0);
            Log.logTrace("Launch angle: " + Math.toDegrees(theta));
            Log.logTrace("Trajectory time: " + data.t);
            return new LaunchVector(data.v0, Math.toDegrees(theta));
        }else{
            Log.logError("No suitable solutions found for " + distance + "m");
            return null;
        }
    }
    
    public static void simulateOld(double V0, double theta, double x){
       
        double Vx = V0 * Math.cos(theta);
        double Vy = V0 * Math.sin(theta);
        double Xx = 0;
        double Xy = 0;
        double Ax = (.02069 * Math.pow(Vx, 2));
        double Ay = (.02069 * Math.pow(Vy, 2)) - G;
        double t = 0;
       
        while (true){
            //Xx += (Vx * simTick) + .5*Ax*Math.pow(simTick, 2);
            //Xy += (Vy * simTick) + .5*Ay*Math.pow(simTick, 2);
            Xx += Vx*simTick;
            Xy += Vy*simTick;
           
            Vx += (Ax * simTick);
            Vy += (Ay * simTick);
            //Vx = Math.sqrt(Math.pow(Vx, 2) + (2*Ax*((Vx * simTick) + .5*Ax*Math.pow(simTick, 2))));
            //Vy = Math.sqrt(Math.pow(Vy, 2) + (2*Ax*((Vx * simTick) + .5*Ax*Math.pow(simTick, 2))));
           
            if (Vx > 0){
                Ax = -.02069 * Math.pow(Vx, 2);
            }else if(Vx < 0){
                Ax = .02069 * Math.pow(Vx, 2);
            }else{
                Ax = 0;
            }
           
            if (Vy > 0){
                Ay = -(.02069 * Math.pow(Vy, 2)) - G;
            }else if(Vy < 0){
                Ay = (.02069 * Math.pow(Vy, 2)) - G;
            }else{
                Ay = 0;
            }
           
            t += simTick;
           
            if (t > maxTime || Xx >= x || (Vy <= 0 && (Xx - x/2) < 0)){
                break;
            }
        }
        data.x = Xx;
        data.y = Xy;
        data.t = t;
        data.vx = Vx;
        data.vy = Vy;
        data.v0 = V0;
    }
    
    public static void simulate(double v0, double theta, double x){
        
        double t = 0;
        double[] force = {0, -(M*G)};
        double[] velocity = {v0 * Math.cos(theta), v0 * Math.sin(theta)};
        double[] acceleration = {(.02069 * Math.pow(velocity[0], 2)), 
                (.02069 * Math.pow(velocity[1], 2)) - G};
        double[] distance = {0, 0};
        
        while (true){
            if (velocity[0] >= 0){
                force[0] = -(Cd*.5*RHO*Math.pow(velocity[0], 2));
            }else if (velocity[0] < 0){
                force[0] = (Cd*.5*RHO*Math.pow(velocity[0], 2));
            }
            
            if (velocity[1] >= 0){
                force[1] = -(Cd*.5*RHO*Math.pow(velocity[1], 2)) - (G*M);
            }else if (velocity[1] < 0){
                force[1] = (Cd*.5*RHO*Math.pow(velocity[1], 2)) - (G*M);
            }
            
            acceleration[0] = force[0]/M;
            acceleration[1] = force[1]/M;
            
            velocity[0] += acceleration[0]*simTick;
            velocity[1] += acceleration[1]*simTick;
            
            distance[0] += velocity[0]*simTick;
            distance[1] += velocity[1]*simTick;
            
            t += simTick;
            
            if (t > maxTime || distance[0] >= x || (velocity[0] <= 0 && (distance[0] - x/2) < 0)){
                break;
            }
            
        }
        
        data.t = t;
        data.x = distance[0];
        data.y = distance[1];
        data.vx = velocity[0];
        data.vy = velocity[1];
        data.v0 = v0;
        
    }
    
    public static class LaunchVector{
        
        double velocity;
        double angle;
        
        public LaunchVector(double v0, double theta){
            velocity = v0;
            angle = theta;
        }
        
        public double getVelocity(){
            return velocity;
        }
        
        public double getAngle(){
            return angle;
        }
        
    }
    
    public static class ProjectileData{
        
        double x;
        double y;
        double vx;
        double vy;
        double t;
        double v0;
       
        public ProjectileData(){
            x = 0;
            y = 0;
            vx = 0;
            vy = 0;
            t = 0;
        }
       
        public ProjectileData(double x, double y, double vx, double vy, double t){
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.t = t;
        }
    }
}