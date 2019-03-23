import lejos.hardware.sensor.HiTechnicCompass;
import lejos.robotics.SampleProvider;

/**
 * Created by Alex on 2019-03-23
 */
public class CompassController implements Runnable
{
    private HiTechnicCompass compass;

    private SampleProvider compassReading;
    private float[] sample;

    public CompassController(HiTechnicCompass compass)
    {
        this.compass = compass;

        compassReading = compass.getAngleMode();
        sample = new float[compassReading.sampleSize()];
    }

    public void run()
    {
        while (true)
        {
            readCompass();
            Tank.setSample(sample);
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
