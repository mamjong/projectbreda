package nl.gemeente.breda.bredaapp.eastereggs.spaceinvaders;

import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class GameObject {
	
	protected Rect spaceRect = new Rect();
	private int speed;
	private float floatSpeed;
	private boolean visible;
	private Rect aabb;
	
	public float getFloatSpeed() {
		return floatSpeed;
	}
	
	public void setFloatSpeed(float floatSpeed) {
		this.floatSpeed = floatSpeed;
	}
	
	public abstract void draw(Canvas area);
	
	public abstract void update(long timeDelta);
	
	;
	
	public int getSpeed() {
		return speed;
	}
	
	;
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void setSpaceArea(int left, int top, int right, int bottom) {
		spaceRect.left = left;
		spaceRect.top = top;
		spaceRect.right = right;
		spaceRect.bottom = bottom;
	}
	
	
	public Rect getBoundRect() {
		
		return aabb;
	}
	
	public void setBoundRect(Rect aabb) {
		if (this.aabb == null) {
			this.aabb = new Rect(aabb);
		} else {
			this.aabb.left = aabb.left;
			this.aabb.right = aabb.right;
			this.aabb.top = aabb.top;
			this.aabb.bottom = aabb.bottom;
		}
	}
	
	public void setBoundRect(int left, int top, int right, int bottom) {
		
		if (aabb == null) {
			aabb = new Rect(left, top, right, bottom);
		} else {
			aabb.left = left;
			aabb.right = right;
			aabb.top = top;
			aabb.bottom = bottom;
		}
	}
	
	public void setPos(int x, int y) {
		Rect aabb = getBoundRect();
		int width = aabb.width();
		aabb.left = x - width / 2;
		aabb.right = x + width / 2;
		
		setBoundRect(aabb);
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
