/**
 * Created by Alex on 2/12/19
 */
public class PIDController
{
    private double optimal_distance;
    private int base_speed;

    public PIDController(double optimal_distance, int base_speed)
    {
        this.optimal_distance = optimal_distance;
        this.base_speed = base_speed;
    }

    public int getRightTurnSpeed(float samp)
    {
        double error = (optimal_distance - samp);// * 2;
        double turnSpeed = base_speed * (1 - error);
        return (int) turnSpeed;
    }

    public int getLeftTurnSpeed(float samp)
    {
        double error = (optimal_distance - samp);// * 2;
        double turnSpeed = base_speed * (1 + error);
        return (int) turnSpeed;
    }
}
