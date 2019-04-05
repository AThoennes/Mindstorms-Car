import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;

/**
 * Created by Alex on 2019-03-23
 */
public class MovementController implements Runnable
{
    private PIDController pid;

    private EV3LargeRegulatedMotor leftWheel;
    private EV3LargeRegulatedMotor rightWheel;
    private BlockageFlag blockageFlag;

    private int base_speed;

    public MovementController(PIDController pid, EV3LargeRegulatedMotor leftWheel, EV3LargeRegulatedMotor rightWheel, int base_speed, BlockageFlag flag)
    {
        this.pid = pid;
        this.leftWheel = leftWheel;
        this.rightWheel = rightWheel;
        this.base_speed = base_speed;
        this.blockageFlag=flag;

        leftWheel.synchronizeWith(new RegulatedMotor[]{rightWheel});
    }

    public void run()
    {
        try {
        	Thread.sleep(1000);
        }
        catch(InterruptedException e) {
        	LCD.drawString("MovementController Error", 0, 0);
        }
    	findNorth();
        setInitialSpeed(base_speed);

        // CONTROL LOOP
        while (true)
        {
            // touched something
            if(!blockageFlag.isFound())
            {
            	//PID controller
            	adjustSpeed();
            	
            	//Adjust wheel directions
            	changeWheelDirection(leftWheel.getSpeed(), leftWheel);
            	changeWheelDirection(rightWheel.getSpeed(), rightWheel);
            }
            else {
            	blockageFlag.toggle();
            	//This thread stops for a set amount of time, allowing the avoidance movement to resolve
            	//The toggle signals the other thread to begin
            	//Another approach would be to repeatedly loop a .sleep() until the movingAround flag in the BlockageFlag object is toggled again
            	try {
            		Thread.sleep(45000);
            	}
            	catch(Exception e) {
            		e.printStackTrace();
            	}
            }
        }
    }

    private void findNorth()
    {
        while(pid.getPrevErr()!=0)
        {
            // set the turning speed to find magnetic north
            leftWheel.startSynchronization();
            leftWheel.setSpeed(0);
            rightWheel.setSpeed(75);
            leftWheel.forward();
            rightWheel.forward();
            leftWheel.endSynchronization();
        }
    }

    private void setInitialSpeed(int base_speed)
    {
        // Starting speed
        leftWheel.startSynchronization();
        leftWheel.setSpeed(base_speed);
        rightWheel.setSpeed(base_speed);
        leftWheel.forward();
        rightWheel.forward();
        leftWheel.endSynchronization();
//        leftWheel.waitComplete();
//        rightWheel.waitComplete();
    }

    /**
     * changes the direction of the wheels
     * if the speed if < 0 then the wheel goes backwards
     * otherwise it goes forwards
     *
     * @param speed
     * @param wheel
     */
    private void changeWheelDirection(float speed, EV3LargeRegulatedMotor wheel)
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

    private void adjustSpeed()
    {
    	int leftSpeed = base_speed - pid.getTurnSpeed();
        int rightSpeed = base_speed + pid.getTurnSpeed();

        leftWheel.setSpeed(Math.abs(leftSpeed));
        rightWheel.setSpeed(Math.abs(rightSpeed));
    }
}
