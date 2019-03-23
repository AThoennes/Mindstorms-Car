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
