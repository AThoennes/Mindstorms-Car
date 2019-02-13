import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

/**
 * Created by Alex Thoennes and Dan Tartaglione on 2/11/19
 */
public class Tank
{
    final static NXTUltrasonicSensor front = new NXTUltrasonicSensor(SensorPort.S1);
    final static NXTUltrasonicSensor right = new NXTUltrasonicSensor(SensorPort.S2);

    final static EV3LargeRegulatedMotor leftWheel = new EV3LargeRegulatedMotor(MotorPort.A);
    final static EV3LargeRegulatedMotor rightWheel = new EV3LargeRegulatedMotor(MotorPort.B);

    private final static double OPTIMAL_DISTANCE = 0.1;
    private final static int BASE_SPEED = 200;

    public static void main (String [] args)
    {
        leftWheel.synchronizeWith(new RegulatedMotor[]{rightWheel});

        SampleProvider frontReading = front.getDistanceMode();
        SampleProvider rightReading = right.getDistanceMode();

        float[] sample = new float[frontReading.sampleSize() + rightReading.sampleSize()];

        LCD.drawString("Press a button to start", 0, 0);
        Button.waitForAnyPress();
        LCD.clear();

        leftWheel.setSpeed(BASE_SPEED);
        rightWheel.setSpeed(BASE_SPEED);
        leftWheel.forward();
        rightWheel.forward();

        while (true)
        {
            frontReading.fetchSample(sample, 0);
            rightReading.fetchSample(sample, 1);

            if (sample[1] > OPTIMAL_DISTANCE)
            {
                // turn right
                leftWheel.setSpeed(getRightTurnSpeed(sample[1]));
                rightWheel.setSpeed((int) (1.85*BASE_SPEED) - getLeftTurnSpeed(sample[1]));

                while (sample[1] > OPTIMAL_DISTANCE)
                {
                    rightReading.fetchSample(sample, 1);
                    leftWheel.setSpeed(getRightTurnSpeed(sample[1]));
                    LCD.drawString("TURNING RIGHT",0,0);
                }
                LCD.clear();
                leftWheel.setSpeed(BASE_SPEED);
            }
            else if (sample[1] < OPTIMAL_DISTANCE)
            {
                // turn left
                rightWheel.setSpeed(getLeftTurnSpeed(sample[1]));
                leftWheel.setSpeed(getRightTurnSpeed(sample[1]));

                while (sample[1] < OPTIMAL_DISTANCE)
                {
                    rightReading.fetchSample(sample, 1);
                    rightWheel.setSpeed(getLeftTurnSpeed(sample[1]));
                    LCD.drawString("TURNING LEFT", 0, 0);
                }
                LCD.clear();
                rightWheel.setSpeed(BASE_SPEED);
            }
        }
    }

    private static int getRightTurnSpeed(float samp)
    {
        double error = (OPTIMAL_DISTANCE - samp) * 2;
        double turnSpeed = BASE_SPEED * (1 - error);
        return (int) turnSpeed;
    }

    private static int getLeftTurnSpeed(float samp)
    {
        double error = (OPTIMAL_DISTANCE - samp) * 2;
        double turnSpeed = BASE_SPEED * (1 + error);
        return (int) turnSpeed;
    }
}
