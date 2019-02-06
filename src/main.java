import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;



/**
 * Created by Alex on 2/2/19
 */
public class main
{
    static EV3LargeRegulatedMotor backWheel;
    static EV3LargeRegulatedMotor leftWheel;
    static EV3LargeRegulatedMotor rightWheel;
    public static void main (String [] args)
    {
        backWheel = new EV3LargeRegulatedMotor(MotorPort.A);
        leftWheel = new EV3LargeRegulatedMotor(MotorPort.B);
        rightWheel = new EV3LargeRegulatedMotor(MotorPort.C);

        RegulatedMotor [] syncList = {rightWheel};
        leftWheel.synchronizeWith(syncList);

        // test motors
//        testMotor(backWheel);

        // turn turns
//        testTurns();
        backWheel.rotate(1800);
        backWheel.rotate(-1800);

        leftWheel.rotate(30);
        rightWheel.rotate(-30);

        backWheel.rotate(1800);
        backWheel.rotate(-1800);

        // turn left
        leftWheel.rotate(-30);
        rightWheel.rotate(30);

        backWheel.rotate(1800);
        backWheel.rotate(-1800);
    }

    private static void testTurns()
    {
        try
        {
            // turn right
            leftWheel.rotate(30);
            rightWheel.rotate(-30);
            Thread.sleep(1000);
            // turn left
            leftWheel.rotate(-30);
            rightWheel.rotate(30);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private static void testMotor(EV3LargeRegulatedMotor m)
    {


        while (1==1)
        {
            m.forward();
        }

//        try
//        {
//            m.forward();
//            m.forward();
//            Thread.sleep(1000);
//            m.backward();
//            m.backward();
//            Thread.sleep(1000);
//            m.stop();
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
    }
}
