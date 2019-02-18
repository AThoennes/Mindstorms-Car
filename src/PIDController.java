/**
 * Created by Alex on 2/12/19
 */
public class PIDController
{
    private double optimal_angle;
    private final int KP = 1;
    private final double KI = .001;
    private final int KD = 2;
    private double integral = 0;
    private double prevErr = 0.0;

    public PIDController(double optimal_angle)
    {
        this.optimal_angle = optimal_angle;
    }

    public int getTurnSpeed(double samp)
    {
        samp = (samp + 180) % 360;
        double error = samp - optimal_angle;
        //integral = 0.5f * (integral + (error + prevErr)/2);
        double P_term = KP * error;
        //double I_term = KI * integral;
        //double D_term = KD * (error - prevErr);
        double u = P_term;// + I_term;
        prevErr = error;

        return (int) u;
    }

    public double getIntegral()
    {
        return integral;
    }
}