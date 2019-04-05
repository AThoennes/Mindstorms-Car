public class BlockageFlag {
	private boolean flag; //For synchronization with the TouchSampler class
	private boolean movingAround; //For synchronization between the movement threads
	public BlockageFlag() {
		flag=false;
		movingAround=false;
	}
	public void setFlag(boolean b) {
		flag=b;
	}
	public boolean isFound() {
		return flag;
	}
	public boolean isMovingAround() {
		return movingAround;
	}
	public void toggle(){
		movingAround=!movingAround;
	}
}
