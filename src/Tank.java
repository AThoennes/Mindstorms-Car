import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.HiTechnicCompass;
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
    final static HiTechnicCompass front = new HiTechnicCompass(SensorPort.S1);

    final static EV3LargeRegulatedMotor leftWheel = new EV3LargeRegulatedMotor(MotorPort.A);
    final static EV3LargeRegulatedMotor rightWheel = new EV3LargeRegulatedMotor(MotorPort.B);

    private final static double OPTIMAL_DISTANCE = 0.1;
    private final static int BASE_SPEED = 100;

    public static void main (String [] args)
    {
        leftWheel.synchronizeWith(new RegulatedMotor[]{rightWheel});

        // maybe getCompassMode() ??
        SampleProvider frontReading = front.getAngleMode();

        float[] sample = new float[frontReading.sampleSize()];

        LCD.drawString("Press a button to start", 0, 0);
        Button.waitForAnyPress();
        LCD.clear();

        leftWheel.setSpeed(BASE_SPEED);
        rightWheel.setSpeed(BASE_SPEED);
        leftWheel.forward();
        rightWheel.forward();

        PIDController pid = new PIDController(OPTIMAL_DISTANCE, BASE_SPEED);

        while (true)
        {
            frontReading.fetchSample(sample, 0);
        }
    }
}