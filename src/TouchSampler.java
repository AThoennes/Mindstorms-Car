import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

/**
 * Created by Alex on 2019-03-23
 */
public class TouchSampler implements Runnable
{
    private EV3TouchSensor touchSensor;
    private float[] sample; //The array that stores the sampling
    private BlockageFlag blockageFlag;

    public TouchSampler(EV3TouchSensor touchSensor, BlockageFlag b)
    {
        this.touchSensor = touchSensor;
        this.blockageFlag=b;
    }

    public void run()
    {
        init();
    	while (true)
        {
            this.readTouchSensor();
            this.setFlag();
            try{
            	Thread.sleep(1);
            }
            catch(InterruptedException e) {
            	LCD.drawString("TouchSampler Error", 0, 0);
            }
        }
    }
    
    private void init()
    {
    	SampleProvider touchReading = touchSensor.getTouchMode();
    	sample = new float[touchReading.sampleSize()]; //Size 1
    }

    private void readTouchSensor()
    {
        touchSensor.fetchSample(sample, 0);
    }
    
    private void setFlag() {
    	if(sample[0]==1) {
        	blockageFlag.setFlag(true);
        }
        else blockageFlag.setFlag(false);
    }
}