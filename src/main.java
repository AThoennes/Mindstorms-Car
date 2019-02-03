import lejos.hardware.ev3.EV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;



/**
 * Created by Alex on 2/2/19
 */
public class main
{
    public static void main (String [] args)
    {
        EV3LargeRegulatedMotor backWheel = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor leftWheel = new EV3LargeRegulatedMotor(MotorPort.B);
        EV3LargeRegulatedMotor rightWheel = new EV3LargeRegulatedMotor(MotorPort.C);

        RegulatedMotor [] syncList = {leftWheel, rightWheel};
        leftWheel.synchronizeWith(syncList);

//        // if top of the motor faces to the middle
//        leftWheel.forward(); // turns left
//        leftWheel.backward(); // turns right
//
//        // if motors face to the outside
//        leftWheel.backward(); // turns left
//        leftWheel.forward(); // turns right

        // test motors
        testMotor(backWheel);
        testMotor(leftWheel);
//        testMotor(rightWheel);
    }

    private static void testMotor(EV3LargeRegulatedMotor m)
    {
        try
        {
            m.forward();
            Thread.sleep(1000);
            m.backward();
            Thread.sleep(1000);
            m.stop();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
