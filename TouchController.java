import lejos.hardware.sensor.EV3TouchSensor;

/**
 * Created by Alex on 2019-03-23
 */
public class TouchController implements Runnable
{
    private EV3TouchSensor touchSensor;

    private float [] sample;

    public TouchController(EV3TouchSensor touchSensor)
    {
        this.touchSensor = touchSensor;
        this.sample = Tank.getSample();
    }

    public void run()
    {
        while (true)
        {
            readTouchSensor();
            Tank.setSample(sample, 1);
        }
    }

    private void readTouchSensor()
    {
        touchSensor.fetchSample(sample, 1);
    }
}