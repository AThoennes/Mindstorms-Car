import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;


/**
 * Created by Alex Thoennes and Dan Tartaglione on 2/2/19
 */
public class main
{
    public static void main (String [] args) throws InterruptedException
    {
        NXTUltrasonicSensor front = new NXTUltrasonicSensor(SensorPort.S1);
        NXTUltrasonicSensor right = new NXTUltrasonicSensor(SensorPort.S2);

        EV3LargeRegulatedMotor backWheel = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor leftWheel = new EV3LargeRegulatedMotor(MotorPort.B);
        EV3LargeRegulatedMotor rightWheel = new EV3LargeRegulatedMotor(MotorPort.C);

        SampleProvider frontReading = front.getDistanceMode();
        SampleProvider rightReading = right.getDistanceMode();

        float[] sample = new float[frontReading.sampleSize()+rightReading.sampleSize()];

        LCD.drawString("Press a button to start",0,0);
        Button.waitForAnyPress();
        LCD.clear();

        backWheel.setSpeed(300);
        backWheel.forward();

        while (true)
        {
            frontReading.fetchSample(sample, 0);
            rightReading.fetchSample(sample, 1);

            if (sample[0] <= 0.5)
            {
                // wall in front
                // check right and left
                // decide where to turn
//                LCD.drawString("WALL IN FRONT", 0, 0);
                leftWheel.synchronizeWith(new RegulatedMotor[] {rightWheel});
                leftWheel.startSynchronization();
                leftWheel.rotate(-35);
                rightWheel.rotate(35);
                leftWheel.endSynchronization();
                leftWheel.waitComplete();
                rightWheel.waitComplete();

                while (sample[0] <= 0.5)
                {
                    frontReading.fetchSample(sample, 0);
                }
//                Thread.sleep(2000);

                leftWheel.startSynchronization();
                leftWheel.rotate(35);
                rightWheel.rotate(-35);
                leftWheel.endSynchronization();
                leftWheel.waitComplete();
                rightWheel.waitComplete();
            }
        }



//        backWheel.rotate(1800);
//        backWheel.rotate(-1800);
//
//        leftWheel.rotate(30);
//        rightWheel.rotate(-30);
//
//        backWheel.rotate(1800);
//        backWheel.rotate(-1800);
//
//        // turn left
//        leftWheel.rotate(-30);
//        rightWheel.rotate(30);
//
//        backWheel.rotate(1800);
//        backWheel.rotate(-1800);
    }
}
