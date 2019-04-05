/**
 * Created by Alex on 2/12/19
 */
public class PIDController
{
    private double optimal_angle;
    private final int KP = 3;
    private final double KI = .003;
    private final int KD = 5; 
    private double integral = 0;
    private double prevErr = 0.0;
    private double turnSpeed;

    public PIDController(double optimal_angle)
    {
        this.optimal_angle = optimal_angle;
    }

    public void setTurnSpeed(double samp)
    {
        samp = (samp + 180) % 360;

        double error = samp - optimal_angle;
        integral = integral + (error + prevErr)/2;
        double P_term = KP * error;
        double I_term = KI * integral;
        double D_term = KD * (error - prevErr);
        double u = (P_term + I_term + D_term)%360;
        prevErr = error;

        turnSpeed=u;
    }
    
    public int getTurnSpeed() {
    	return (int) turnSpeed;
    }
    
    public double getPrevErr() {
    	return prevErr;
    }
}