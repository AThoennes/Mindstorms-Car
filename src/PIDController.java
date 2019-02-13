/**
 * Created by Alex on 2/12/19
 */
public class PIDController
{
    private double optimal_distance;
    private int base_speed;
    private final int KP = 400;
    private final int KI = 20;
    private final int KD = 0;
    private double integral = 0;
    private double prevErr = 0.0;

    public PIDController(double optimal_distance, int base_speed)
    {
        this.optimal_distance = optimal_distance;
        this.base_speed = base_speed;
    }

    public int getTurnSpeed(float samp)
    {
        double error = (optimal_distance - samp);// * 2;
        if (integral == Double.POSITIVE_INFINITY || integral == Double.NEGATIVE_INFINITY)
        {
            integral = 0;
        }
        else if (error == 0 || error < 0 && prevErr > 0 || error > 0 && prevErr < 0)
        {
            integral = 0;
        }
        else
        {
            integral = integral + error;
        }
        prevErr = error;
        double turnSpeed = (KP * error) + (KI * integral);

        return (int) turnSpeed;
    }

    public double getIntegral()
    {
        return integral;
    }
}
