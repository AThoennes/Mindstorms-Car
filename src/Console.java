import lejos.hardware.lcd.LCD;

public class Console implements Runnable
{
	private BlockageFlag flag;
	
	public Console(BlockageFlag flag)
	{
		this.flag=flag;
	}
	
	public void run()
	{
		//We reserve the first line (y=0) for error messages printed to the LCD
		while(true)
		{
			LCD.clear();
			if(flag.isFound()) {
				LCD.drawString("STATUS: Blockage detected", 0, 1);
			}
			else {
				LCD.drawString("STATUS: Normal", 0, 1);
			}
			try {
				Thread.sleep(500);
			}
			catch(InterruptedException e) {
				LCD.drawString("Console Error", 0, 0);
			}
		}
	}
}
