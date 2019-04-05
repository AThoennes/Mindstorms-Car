import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;

/**
 * Created by Alex on 2019-03-23
 */
public class AvoidanceController implements Runnable
{
    private EV3LargeRegulatedMotor leftWheel;
    private EV3LargeRegulatedMotor rightWheel;
    private BlockageFlag blockageFlag;

    private final int BACKUP_DEGREES = 120;
    private final int FORWARD_DEGREES =700;

    public AvoidanceController(EV3LargeRegulatedMotor leftWheel, EV3LargeRegulatedMotor rightWheel, BlockageFlag flag)
    {
        this.leftWheel = leftWheel;
        this.rightWheel = rightWheel;
        blockageFlag=flag;

        leftWheel.synchronizeWith(new RegulatedMotor[]{rightWheel});
    }

    public void run()
    {
        while (true)
        {
        	try {
            	Thread.sleep(1);
            }
            catch(InterruptedException e) {
            	LCD.drawString("AvoidanceController Error", 0, 0);
            }
        	LCD.drawString(blockageFlag.isFound()+"", 0, 5);
        	if(blockageFlag.isFound()) {
        		this.moveAroundObject();
        		blockageFlag.toggle(); //This signals the other movement thread to continue
        	}
        }
    }

    private void moveAroundObject()
    {
    	leftWheel.setSpeed(150);
        rightWheel.setSpeed(150);
    	backup(BACKUP_DEGREES);
    	halt();
        turnLeft();
        halt();
        forward(FORWARD_DEGREES+100);
        halt();
        turnRight();
        halt();
        forward(FORWARD_DEGREES*2);
        halt();
        turnRight();
        halt();
        forward(FORWARD_DEGREES);
        halt();
        turnLeft();
        halt();
    }

    private void turnLeft()
    {
        leftWheel.startSynchronization();
        leftWheel.rotate(-360);
        rightWheel.rotate(360);
        leftWheel.endSynchronization();
        leftWheel.waitComplete();
        rightWheel.waitComplete();
    }

    private void turnRight()
    {
    	leftWheel.startSynchronization();
        leftWheel.rotate(360);
        rightWheel.rotate(-360);
        leftWheel.endSynchronization();
        leftWheel.waitComplete();
        rightWheel.waitComplete();
    }

    private void backup(int degrees)
    {
        leftWheel.startSynchronization();
        leftWheel.rotate(-degrees);
        rightWheel.rotate(-degrees);
        leftWheel.endSynchronization();
        leftWheel.waitComplete();
        rightWheel.waitComplete();
    }

    private void forward(int degrees)
    {
        leftWheel.startSynchronization();
        leftWheel.rotate(degrees);
        rightWheel.rotate(degrees);
        leftWheel.endSynchronization();
        leftWheel.waitComplete();
        rightWheel.waitComplete();
    }
    
    private void halt()
    {
    	leftWheel.startSynchronization();
        leftWheel.stop();
        rightWheel.stop();
        leftWheel.endSynchronization();
        leftWheel.waitComplete();
        rightWheel.waitComplete();
        try {
        	Thread.sleep(1000);
        }
        catch(InterruptedException e) {
        	LCD.drawString("MovementController Error", 0, 0);
        }
    }
}
