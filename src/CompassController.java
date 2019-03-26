import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.HiTechnicCompass;

/**
 * Created by Alex on 2019-03-23
 */
public class CompassController implements Runnable
{
    private HiTechnicCompass compass;

    private float[] sample;

    public CompassController(HiTechnicCompass compass)
    {
        this.compass = compass;
        sample = Tank.getSample();
    }

    public void run()
    {
        while (true)
        {
            LCD.drawString("SAMP 0:"+sample[0],0,0);
            LCD.drawString("SAMP 1:"+Tank.getSample()[1], 0,1);
            readCompass();
            Tank.setSample(sample, 0);
            if (sample[0] == 0)
            {
                Tank.setFoundNorth(true);
            }
        }
    }

    private void readCompass ()
    {
        compass.fetchSample(sample, 0);
    }
}