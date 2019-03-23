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

    private double optimal_angle;

    private int base_speed;
    private final int BACKUP_DEGREES = -360;
    private final int FORWARD_DEGREES = 720;

    public MovementController(PIDController pid, EV3LargeRegulatedMotor leftWheel, EV3LargeRegulatedMotor rightWheel,
                              double optimal_angle, int base_speed)
    {
        this.pid = pid;
        this.leftWheel = leftWheel;
        this.rightWheel = rightWheel;
        this.optimal_angle = optimal_angle;
        this.base_speed = base_speed;

        leftWheel.synchronizeWith(new RegulatedMotor[]{rightWheel});
    }

    public void run()
    {
        findNorth();
        setInitialSpeed(base_speed);

        // CONTROL LOOP
        while (true)
        {
            // touched something
            if (Tank.getSample()[1] == 1)
            {
                moveAroundObject();
            }

            adjustSpeed();

            // adjust wheel direction
            changeWheelDirection(leftWheel.getSpeed(), leftWheel);
            changeWheelDirection(rightWheel.getSpeed(), rightWheel);
        }
    }

    private void findNorth()
    {
        while (!Tank.isFoundNorth())
        {
            // set the turning speed to find magnetic north
            leftWheel.setSpeed(0);
            rightWheel.setSpeed(75);
            leftWheel.forward();
            rightWheel.forward();
        }
    }

    private void setInitialSpeed(int base_speed)
    {
        // Starting speed
        leftWheel.setSpeed(base_speed);
        rightWheel.setSpeed(base_speed);
        leftWheel.forward();
        rightWheel.forward();
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
        // get new speeds based on the current angle
        int leftSpeed = base_speed - pid.getTurnSpeed(Tank.getSample()[0]);
        int rightSpeed = base_speed + pid.getTurnSpeed(Tank.getSample()[0]);

        leftWheel.setSpeed(Math.abs(leftSpeed));
        rightWheel.setSpeed(Math.abs(rightSpeed));
    }

    private void moveAroundObject()
    {
        backup(BACKUP_DEGREES);
        turnLeft();
        forward(FORWARD_DEGREES);
        turnRight();
        forward(FORWARD_DEGREES);
        turnRight();
        forward(FORWARD_DEGREES);
        turnLeft();
        setInitialSpeed(base_speed);
    }

    private void turnLeft()
    {
        while (Tank.getSample()[0] != 90)
        {
            leftWheel.startSynchronization();
            changeWheelDirection(-200, leftWheel);
            changeWheelDirection(200, rightWheel);
            leftWheel.endSynchronization();
            leftWheel.waitComplete();
            rightWheel.waitComplete();
        }
    }

    private void turnRight()
    {
        while (Tank.getSample()[0] != 0)
        {
            leftWheel.startSynchronization();
            changeWheelDirection(200, leftWheel);
            changeWheelDirection(-200, rightWheel);
            leftWheel.endSynchronization();
            leftWheel.waitComplete();
            rightWheel.waitComplete();
        }
    }

    private void backup(int degrees)
    {
        leftWheel.startSynchronization();
        moveBackwards(leftWheel, degrees);
        moveBackwards(rightWheel, degrees);
        leftWheel.endSynchronization();
        leftWheel.waitComplete();
        rightWheel.waitComplete();
    }

    private void forward(int degrees)
    {
        leftWheel.startSynchronization();
        moveForwards(leftWheel, degrees);
        moveForwards(rightWheel, degrees);
        leftWheel.endSynchronization();
        leftWheel.waitComplete();
        rightWheel.waitComplete();
    }

    private void moveForwards(EV3LargeRegulatedMotor wheel, int degrees)
    {
        wheel.rotate(degrees);
    }

    private void moveBackwards(EV3LargeRegulatedMotor wheel, int degrees)
    {
        wheel.rotate(degrees);
    }
}
