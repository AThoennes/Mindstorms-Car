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
    final static HiTechnicCompass front = new HiTechnicCompass(SensorPort.S1);

    final static EV3LargeRegulatedMotor leftWheel = new EV3LargeRegulatedMotor(MotorPort.A);
    final static EV3LargeRegulatedMotor rightWheel = new EV3LargeRegulatedMotor(MotorPort.B);

    private final static double OPTIMAL_ANGLE = 0;
    private final static int BASE_SPEED = 100;

    public static void main (String [] args)
    {
        leftWheel.synchronizeWith(new RegulatedMotor[]{rightWheel});

        // maybe getCompassMode() ??
        final SampleProvider frontReading = front.getAngleMode();

        float[] sample = new float[frontReading.sampleSize()];

        LCD.drawString("Press a button to start", 0, 0);
        Button.waitForAnyPress();
        LCD.clear();

        leftWheel.setSpeed(0);
        rightWheel.setSpeed(75);
        leftWheel.forward();
        rightWheel.forward();

        PIDController pid = new PIDController(OPTIMAL_ANGLE, BASE_SPEED);

        frontReading.fetchSample(sample, 0); //MUST HAVE INITIAL READING
        while(sample[0]!=0)
        {
            frontReading.fetchSample(sample, 0);
            double samp=sample[0];
            LCD.drawString(""+samp, 0, 0);
        }

        leftWheel.setSpeed(BASE_SPEED);
        rightWheel.setSpeed(BASE_SPEED);
        leftWheel.forward();
        rightWheel.forward();

        while (true)
        {
            frontReading.fetchSample(sample, 0);
            double samp=sample[0];
            LCD.drawString(""+samp, 0, 0);
            int ls=BASE_SPEED-pid.getTurnSpeed(sample[0]);
            int rs=BASE_SPEED+pid.getTurnSpeed(sample[0]);
            leftWheel.setSpeed(Math.abs(ls));
            rightWheel.setSpeed(Math.abs(rs));
            if(ls>=0)
            {
                leftWheel.forward();
            }
            else
            {
                leftWheel.backward();
            }
            if(rs>=0)
            {
                rightWheel.forward();
            }
            else
            {
                rightWheel.backward();
            }
        }
    }
}