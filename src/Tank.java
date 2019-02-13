import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Wheel;

/**
 * Created by Alex Thoennes and Dan Tartaglione on 2/11/19
 *
 * min dist: 0.03 m
 * max dist: 2.5m
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

        int ts=0;

        PIDController pid = new PIDController(OPTIMAL_DISTANCE, BASE_SPEED);

        while (true)
        {
            frontReading.fetchSample(sample, 0);
            rightReading.fetchSample(sample, 1);
            ts=pid.getTurnSpeed(sample[1]);
            LCD.drawString("TS:" + ts,0,0);
            LCD.drawString("I:" + pid.getIntegral(), 0,1);
//            LCD.clear();

            if (ts < -1000)
            {
                //turn in place
                leftWheel.setSpeed(100);
                rightWheel.setSpeed(0);
                //rightWheel.backward();
            }
            else
            {
                rightWheel.forward();
                leftWheel.setSpeed(BASE_SPEED - ts);
                rightWheel.setSpeed(BASE_SPEED + ts);
            }
        }
    }
}