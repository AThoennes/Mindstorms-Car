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
    public static double dl=0.0;
    public static double dr=0.0;

    final static NXTUltrasonicSensor front = new NXTUltrasonicSensor(SensorPort.S1);
    final static NXTUltrasonicSensor right = new NXTUltrasonicSensor(SensorPort.S2);

    final static EV3LargeRegulatedMotor backWheel = new EV3LargeRegulatedMotor(MotorPort.A);
    final static EV3LargeRegulatedMotor leftWheel = new EV3LargeRegulatedMotor(MotorPort.B);
    final static EV3LargeRegulatedMotor rightWheel = new EV3LargeRegulatedMotor(MotorPort.C);

    public static void main (String [] args)
    {
        leftWheel.synchronizeWith(new RegulatedMotor[] {rightWheel});

        SampleProvider frontReading = front.getDistanceMode();
        SampleProvider rightReading = right.getDistanceMode();

        float[] sample = new float[frontReading.sampleSize()+rightReading.sampleSize()];

        LCD.drawString("Press a button to start",0,0);
        Button.waitForAnyPress();
        LCD.clear();

        backWheel.setSpeed(300);
        backWheel.backward();
        leftWheel.setSpeed(200);
        rightWheel.setSpeed(200);

        while (true)
        {
            frontReading.fetchSample(sample, 0);
            rightReading.fetchSample(sample, 1);


            //Left
            if (sample[1] < 0.16)
            {
                //turnLeft(10);
                leftWheel.startSynchronization();
                leftWheel.rotateTo((int)(-30*Math.atan(sample[1]*20.94)));
                rightWheel.rotateTo((int)(22*Math.atan(sample[1]*20.94)));
                leftWheel.endSynchronization();
                while(sample[1]<0.16)
                {
                    rightReading.fetchSample(sample,  1);
                }
            }
            else if (sample[1] > 0.16)
            {
                //turnLeft(10);
                leftWheel.startSynchronization();
                leftWheel.rotateTo((int)(22*Math.atan(sample[1]*14.96)));
                rightWheel.rotateTo((int)(-30*Math.atan(sample[1]*14.96)));
                leftWheel.endSynchronization();
                while(sample[1]>0.16)
                {
                    rightReading.fetchSample(sample,  1);
                }
            }
            else
            {
                leftWheel.startSynchronization();
                leftWheel.rotateTo(0);
                rightWheel.rotateTo(0);
                leftWheel.endSynchronization();
            }
//            int m=(int)wcm(sample[1], sample[0]);
//            leftWheel.rotateTo(m);
//            rightWheel.rotateTo(-m);
        }
    }

//    public static void turnLeft(double deg)
//    {
//    	double r=0.15/Math.tan(deg)+0.035;
//    	double hyp=Math.sqrt(Math.pow(r+0.035, 2)+0.0225);
//    	double degRight=Math.acos(0.15/hyp);
//    	dl-=deg;
//    	dr+=degRight;
//    	leftWheel.synchronizeWith(new RegulatedMotor[] {rightWheel});
//        leftWheel.startSynchronization();
//        leftWheel.rotate(-(int)deg);
//        rightWheel.rotate(-(int)degRight);
//        leftWheel.endSynchronization();
//        leftWheel.waitComplete();
//        rightWheel.waitComplete();
//    }
//
//    public static void turnRight(double deg)
//    {
//    	double r=0.15/Math.tan(deg)+0.035;
//    	double hyp=Math.sqrt(Math.pow(r-0.035, 2)+0.0225);
//    	double degLeft=Math.acos(0.15/hyp);
//    	dr-=deg;
//    	dl+=degLeft;
//    	leftWheel.synchronizeWith(new RegulatedMotor[] {rightWheel});
//        leftWheel.startSynchronization();
//        leftWheel.rotate(-(int)degLeft);
//        rightWheel.rotate(-(int)deg);
//        leftWheel.endSynchronization();
//        leftWheel.waitComplete();
//        rightWheel.waitComplete();
//    }

//    public static double wcm(double rd, double fd)
//    {
//    	double f=0;
//    	if(fd<0.35) {f=(0.35-fd)/0.35;}
//    	double g=1-Math.pow(Math.E, 0.155-rd);
//    	double dist=Math.toDegrees((5/6.0*Math.atan(g*(Math.pow(Math.E, f)))));
//    	if(f>0) return -1*dist;
//    	else return dist;
//    }
}
