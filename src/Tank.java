import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.HiTechnicCompass;

/**
 * Created by Alex Thoennes and Dan Tartaglione on 2/11/19
 *
 * This robot is designed to follow magnetic north. It uses a PID Controller
 * to travel in a straight line.
 */
public class Tank
{
    public static void main (String [] args)
    {
    	final HiTechnicCompass compassSensor = new HiTechnicCompass(SensorPort.S1);
        final EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2);
        final EV3LargeRegulatedMotor leftWheel = new EV3LargeRegulatedMotor(MotorPort.A);
        final EV3LargeRegulatedMotor rightWheel = new EV3LargeRegulatedMotor(MotorPort.B);

        LCD.drawString("Press a button to start", 0, 0);
        Button.waitForAnyPress();
        LCD.clear();
        
        final double OPTIMAL_ANGLE = 180;
        final int BASE_SPEED = 300;

        BlockageFlag flag=new BlockageFlag();
        PIDController pid = new PIDController(OPTIMAL_ANGLE);
        CompassSampler compassSampler = new CompassSampler(compassSensor, pid);
        TouchSampler touchSampler = new TouchSampler(touchSensor, flag);
        MovementController movementController = new MovementController(pid, leftWheel, rightWheel, BASE_SPEED, flag);
        Console console=new Console(flag);
        AvoidanceController avoid=new AvoidanceController(leftWheel, rightWheel, flag);

        Thread move = new Thread(movementController);
        Thread compass = new Thread(compassSampler);
        Thread touch = new Thread(touchSampler);
        Thread display=new Thread(console);
        Thread avoidance=new Thread(avoid);
        
        //Set thread priorities
        
        /*
         * The display thread has the lowest priority because the console is dependent upon the other components and because
         * it is the least essential to the system's survival. The touch sensor has the highest priority because it needs to
         * sample rapidly so that the system does not remain in an erroneous state. This is similar for the avoidance thread,
         * but we ensure that the two movement threads will not run at the same time. The compass thread has a higher priority
         * than the movement thread because the movement thread depends upon its reading to make adjustments; this is important
         * if a large adjustment is necessary.
         */
        touch.setPriority(5);
        avoidance.setPriority(4);
        compass.setPriority(3);
        move.setPriority(2);
        display.setPriority(1);

        /*We start the threads for the console and movement controllers last because it demands information from the controller classes to function.
         *The display thread depends on the movement thread, so it is initiated last. The avoidance thread begins second in case the vehicle begins
         *in an error state.
         */
        touch.start();
        avoidance.start();
        compass.start();
        move.start();
        display.start();
    }
}