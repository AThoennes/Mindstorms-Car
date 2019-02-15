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
        double error = optimal_distance - samp;
        integral = integral + (error + prevErr)/2;
        double P_term = KP * error;
        double I_term = KI * integral;
//        double D_term = KD * (error - prevErr)/dt;
        double u = P_term + I_term;
        prevErr = error;

        return (int) u;
    }

    public double getIntegral()
    {
        return integral;
    }
}
