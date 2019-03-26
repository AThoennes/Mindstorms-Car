import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.HiTechnicCompass;
import lejos.robotics.SampleProvider;

/**
 * Created by Alex Thoennes and Dan Tartaglione on 2/11/19
 *
 * This robot is designed to follow magnetic north. It uses a PID Controller
 * to travel in a straight line.
 */
public class Tank
{
    private final static HiTechnicCompass compassSensor = new HiTechnicCompass(SensorPort.S3);

    private final static EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S4);

    private final static EV3LargeRegulatedMotor leftWheel = new EV3LargeRegulatedMotor(MotorPort.C);
    private final static EV3LargeRegulatedMotor rightWheel = new EV3LargeRegulatedMotor(MotorPort.D);

    private final static double OPTIMAL_ANGLE = 180;
    private final static int BASE_SPEED = 300;

    private static boolean foundNorth = false;

    private static float [] sample;

    public static void main (String [] args)
    {
        SampleProvider compassReading = compassSensor.getAngleMode();
        SampleProvider touchReading = touchSensor.getTouchMode();
        sample = new float[compassReading.sampleSize() + touchReading.sampleSize()];


        LCD.drawString("Press a button to start", 0, 0);
        Button.waitForAnyPress();
        LCD.clear();

        PIDController pid = new PIDController(OPTIMAL_ANGLE);
        CompassController compassController = new CompassController(compassSensor);
        MovementController movementController = new MovementController(pid, leftWheel, rightWheel, OPTIMAL_ANGLE, BASE_SPEED);
        TouchController touchController = new TouchController(touchSensor);

        Thread move = new Thread(movementController);
        Thread compass = new Thread(compassController);
        Thread touch = new Thread(touchController);

        touch.start();
        compass.start();
        move.start();

        touch.run();
        compass.run();
        move.run();
    }

    public static void setFoundNorth(boolean bool)
    {
        foundNorth = bool;
    }

    public static boolean isFoundNorth()
    {
        return foundNorth;
    }

    public static void setSample(float[] samp, int idx)
    {
        sample[idx] = samp[idx];
    }

    public static float[] getSample()
    {
        return sample;
    }
}