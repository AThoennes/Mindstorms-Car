import lejos.hardware.lcd.LCD;

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

    public PIDController(double optimal_angle)
    {
        this.optimal_angle = optimal_angle;
    }

    public int getTurnSpeed(double samp)
    {
        samp = (samp + 180) % 360;

        double error = samp - optimal_angle;
        integral = integral + (error + prevErr)/2;
        double P_term = KP * error;
        double I_term = KI * integral;
        double D_term = KD * (error - prevErr);
        if (I_term < -20 || I_term > 20)
        {
            integral = 0;
        }
        double u = P_term + I_term + D_term;
        LCD.clear();
        LCD.drawString("P:"+P_term,0,0);
        LCD.drawString("I:"+I_term,0,1);
        LCD.drawString("D:"+D_term,0,2);
        LCD.drawString("U:"+u,0,3);

        prevErr = error;

        return (int) u;
    }

    public double getIntegral()
    {
        return integral;
    }

}