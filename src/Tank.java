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
 */
public class Tank
{
    final static HiTechnicCompass c1 = new HiTechnicCompass(SensorPort.S1);
    final static HiTechnicCompass c2 = new HiTechnicCompass(SensorPort.S4);

    final static EV3LargeRegulatedMotor leftWheel = new EV3LargeRegulatedMotor(MotorPort.A);
    final static EV3LargeRegulatedMotor rightWheel = new EV3LargeRegulatedMotor(MotorPort.B);

    private final static double OPTIMAL_ANGLE = 180;
    private final static int BASE_SPEED = 100;

    public static void main (String [] args)
    {
        leftWheel.synchronizeWith(new RegulatedMotor[]{rightWheel});

        final SampleProvider c1Reading = c1.getAngleMode();
        final SampleProvider c2Reading = c2.getAngleMode();

        float[] sample = new float[c1Reading.sampleSize() + c2Reading.sampleSize()];

        LCD.drawString("Press a button to start", 0, 0);
        Button.waitForAnyPress();
        LCD.clear();

        // set the turning speed
        leftWheel.setSpeed(0);
        rightWheel.setSpeed(75);
        leftWheel.forward();
        rightWheel.forward();

        PIDController pid = new PIDController(OPTIMAL_ANGLE);

        c1.fetchSample(sample, 0);
        c2.fetchSample(sample, 1);

        // spin left until you find North (angle = 0.0)
        while(averageReadings(sample[0], sample[1]) != 0)
        {
            c1.fetchSample(sample, 0);
            c2.fetchSample(sample, 1);
            double samp = averageReadings(sample[0], sample[1]);
            LCD.clear();
            LCD.drawString(""+samp, 0, 0);
        }

        // Staring speed
        leftWheel.setSpeed(BASE_SPEED);
        rightWheel.setSpeed(BASE_SPEED);
        leftWheel.forward();
        rightWheel.forward();

        // CONTROL LOOP
        while (true)
        {
            // get the samples and average them
            // then display the current reading
            c1.fetchSample(sample, 0);
            c2.fetchSample(sample, 1);
            double samp = averageReadings(sample[0], sample[1]);
            LCD.drawString(""+samp, 0, 0);

            // get new speeds based on the current angle
            int leftSpeed = BASE_SPEED - pid.getTurnSpeed(sample[0]);
            int rightSpeed = BASE_SPEED + pid.getTurnSpeed(sample[0]);

            leftWheel.setSpeed(Math.abs(leftSpeed));
            rightWheel.setSpeed(Math.abs(rightSpeed));

            // adjust wheel direction
            changeDirection(leftSpeed, leftWheel);
            changeDirection(rightSpeed, rightWheel);
        }
    }

    /**
     * takes the two reading from both compasses
     * and returns the average
     *
     * @param s1
     * @param s2
     * @return
     */
    private static double averageReadings(float s1, float s2)
    {
//        s1 -= 180;
        s2 += 180;
//        return ((s1 + s2) / 2) - 180;
        return s1;
    }

    /**
     * changes the direction of the wheels
     * if the speed if < 0 then the wheel goes backwards
     * otherwise it goes forwards
     * @param speed
     * @param wheel
     */
    private static void changeDirection(float speed, EV3LargeRegulatedMotor wheel)
    {
        if (speed >= 0)
        {
            wheel.forward();
        }
        else
        {
            wheel.backward();
        }
    }
}