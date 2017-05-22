package nl.gemeente.breda.bredaapp.eastereggs.spaceinvaders;

public abstract class AirCraft extends nl.gemeente.breda.bredaapp.eastereggs.spaceinvaders.GameObject {
	
	protected boolean killed = false;
	protected int lifePower = 100;
	
	public AirCraft() {
	}
	
	public AirCraft(int left, int top, int right, int bottom, int speed) {
		setBoundRect(left, top, right, bottom);
		setSpeed(speed);
	}
	
	public boolean isKilled() {
		return killed;
	}
	
	public void setKilled(boolean value) {
		killed = value;
	}
	
	public void addDamage(int damage) {
		lifePower -= damage;
		
		if (lifePower <= 0) {
			killed = true;
		}
	}
}
