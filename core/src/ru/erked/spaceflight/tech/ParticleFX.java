package ru.erked.spaceflight.tech;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.random.RES;

public class ParticleFX {

	private boolean isDeath;
	private float timer;
	private float x;
	private float y;
	private float width;
	private Sprite sprite;
	
	public ParticleFX(float x, float y, float width, String texture, int index, float timer){
		sprite = RES.atlas.createSprite(texture, index);
		this.setTimer(timer);
		this.setX(x);
		this.setY(y);
		sprite.setBounds(x, y, width, width);
	}

	public boolean isDeath() {
		return isDeath;
	}

	public void setDeath(boolean isDeath) {
		this.isDeath = isDeath;
	}

	public float getTimer() {
		return timer;
	}

	public void setTimer(float timer) {
		this.timer = timer;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		sprite.setX(x);
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		sprite.setY(y);
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void draw(Batch batch) {
		timer -= 0.015F;
		sprite.draw(batch);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
		sprite.setBounds(x, y, width, width);
	}
	
	public void rotate(float angle){
		sprite.rotate(angle);
	}
	
}
