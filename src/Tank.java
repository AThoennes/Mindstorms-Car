import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.HiTechnicCompass;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

/**
 * Created by Alex Thoennes and Dan Tartaglione on 2/11/19
 *
 * This robot is designed to follow magnetic north. It uses a PID Controller
 * to travel in a straight line.
 */
public class Tank
{
    final static HiTechnicCompass compassSensor = new HiTechnicCompass(SensorPort.S1);

    final static EV3LargeRegulatedMotor leftWheel = new EV3LargeRegulatedMotor(MotorPort.A);
    final static EV3LargeRegulatedMotor rightWheel = new EV3LargeRegulatedMotor(MotorPort.B);

    private final static double OPTIMAL_ANGLE = 180;
    private final static int BASE_SPEED = 400;

    public static boolean foundNorth = false;

    private static float [] sample;

    public static void main (String [] args)
    {
//        leftWheel.synchronizeWith(new RegulatedMotor[]{rightWheel});

//        final SampleProvider c1Reading = c1.getAngleMode();
//
//        float[] sample = new float[c1Reading.sampleSize()];

        LCD.drawString("Press a button to start", 0, 0);
        Button.waitForAnyPress();
        LCD.clear();

//        // set the turning speed
//        leftWheel.setSpeed(0);
//        rightWheel.setSpeed(75);
//        leftWheel.forward();
//        rightWheel.forward();

//        c1.fetchSample(sample, 0);

        PIDController pid = new PIDController(OPTIMAL_ANGLE);
        CompassController compassController = new CompassController(compassSensor);
        MovementController movementController = new MovementController(pid, leftWheel, rightWheel, OPTIMAL_ANGLE, BASE_SPEED);
        TouchController touchController = new TouchController();

        Thread move = new Thread(movementController);
        Thread compass = new Thread(compassController);
        Thread touch = new Thread(touchController);

        touch.start();
        compass.start();
        move.start();

        // spin left until you find North (angle = 0.0)
//        while (compassController.getSample()[0] != 0)
//        {
//            c1.fetchSample(sample, 0);
//            double samp = sample[0];
//            LCD.clear();
//            LCD.drawString(""+samp, 0, 0);
//        }
//        LCD.clear();

//        // Starting speed
//        leftWheel.setSpeed(BASE_SPEED);
//        rightWheel.setSpeed(BASE_SPEED);
//        leftWheel.forward();
//        rightWheel.forward();

//        // CONTROL LOOP
//        while (true)
//        {
//            // get the samples and average them
//            // then display the current reading
//            c1.fetchSample(sample, 0);
//            double samp = sample[0];
//
//            // get new speeds based on the current angle
//            int leftSpeed = BASE_SPEED - pid.getTurnSpeed(sample[0]);
//            int rightSpeed = BASE_SPEED + pid.getTurnSpeed(sample[0]);
//
//            leftWheel.setSpeed(Math.abs(leftSpeed));
//            rightWheel.setSpeed(Math.abs(rightSpeed));
//
//            // adjust wheel direction
//            changeDirection(leftSpeed, leftWheel);
//            changeDirection(rightSpeed, rightWheel);
//        }
    }

    public static void setFoundNorth(boolean bool)
    {
        foundNorth = bool;
    }

    public static void setSample(float[] samp)
    {
        sample = samp;
    }

    public static float[] getSample()
    {
        return sample;
    }

    /**
     * changes the direction of the wheels
     * if the speed if < 0 then the wheel goes backwards
     * otherwise it goes forwards
     * @param speed
     * @param wheel
     */
//    private static void changeDirection(float speed, EV3LargeRegulatedMotor wheel)
//    {
//        if (speed >= 0)
//        {
//            wheel.forward();
//        }
//        else
//        {
//            wheel.backward();
//        }
//    }
}