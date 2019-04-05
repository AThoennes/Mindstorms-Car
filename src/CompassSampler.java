import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.HiTechnicCompass;
import lejos.robotics.SampleProvider;

/**
 * Created by Alex on 2019-03-23
 */
public class CompassSampler implements Runnable
{
    private HiTechnicCompass compassSensor;
    private float[] sample; //The array that stores the sampling
    private PIDController pid;

    public CompassSampler(HiTechnicCompass compass, PIDController pid)
    {
        this.compassSensor = compass;
        this.pid=pid;
    }

    public void run()
    {
    	init();
    	while (true)
        {
        	readCompass();
        	updatePID();
            try{
            	Thread.sleep(2);
            }
            catch(InterruptedException e) {
            	LCD.drawString("CompassSampler Error", 0, 0);
            }
        }
    }
    
    private void init()
    {
    	SampleProvider compassReading = compassSensor.getAngleMode();
    	sample = new float[compassReading.sampleSize()]; //Size 1
    }

    private void readCompass()
    {
        compassSensor.fetchSample(sample, 0);
    }
    
    private void updatePID() {
    	pid.setTurnSpeed(sample[0]);
    }
}