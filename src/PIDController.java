/**
 * Created by Alex on 2/12/19
 */
public class PIDController
{
    private double optimal_distance;
    private int base_speed;
    private final int KP = 100;
    private final int KI = 0;
    private final int KD = 0;

    public PIDController(double optimal_distance, int base_speed)
    {
        this.optimal_distance = optimal_distance;
        this.base_speed = base_speed;
    }

    public int getTurnSpeed(float samp)
    {
        double error = (optimal_distance - samp);// * 2;
        double turnSpeed = KP * error;

        return (int) turnSpeed;
    }
}
