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

    public MovementController (PIDController pid, EV3LargeRegulatedMotor leftWheel, EV3LargeRegulatedMotor rightWheel,
                               double optimal_angle, int base_speed)
    {
        this.pid = pid;
        this.leftWheel = leftWheel;
        this.rightWheel = rightWheel;
        this.optimal_angle = optimal_angle;
        this.base_speed = base_speed;

        leftWheel.synchronizeWith(new RegulatedMotor[]{rightWheel});
    }

    public void run ()
    {
        findNorth();
        setInitialSpeed(base_speed);

        // CONTROL LOOP
        while (true)
        {
            // get new speeds based on the current angle
            int leftSpeed = base_speed - pid.getTurnSpeed(Tank.getSample()[0]);
            int rightSpeed = base_speed + pid.getTurnSpeed(Tank.getSample()[0]);

            leftWheel.setSpeed(Math.abs(leftSpeed));
            rightWheel.setSpeed(Math.abs(rightSpeed));

            // adjust wheel direction
            changeWheelDirection(leftSpeed, leftWheel);
            changeWheelDirection(rightSpeed, rightWheel);
        }
    }

    private void findNorth()
    {
        while (!Tank.foundNorth)
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
}
