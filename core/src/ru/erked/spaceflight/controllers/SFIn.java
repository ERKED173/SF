package ru.erked.spaceflight.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
 
public class SFIn implements InputProcessor {
	
	public static final float width = Gdx.graphics.getWidth();
	public static final float height = Gdx.graphics.getHeight();
	public static Sound clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/misc/click.wav"));
	
	public static String key = "";
	
	/**Current cursor coordinates*/
	public static int cursorX;
	public static int cursorY;
	
	/**Current cursor coordinates from start drag*/
	public static int tdX;
	public static int tdY;
	public static int tdNRX;
	public static int tdNRY;
	public static int tdSRX;
	public static int tdSRY;
	
	/**Current cursor coordinates in drag*/
	public static int tdrX;
	public static int tdrY;
	public static int tdrRX;
	public static int tdrRY;
	public static int tdrFRX;
	public static int tdrFRY;
	public static int tdrSRX;
	public static int tdrSRY;
	public static int tdrOX;
	public static int tdrOY;
	
	/**Current cursor coordinates where finished drag*/
	public static int tuX;
	public static int tuY;
	
	/**Number of touch (only on Android)*/
	public static int numTouch; 
	
	public boolean isOn(float x, float y, float width, float height, boolean numTouch){
		if(!numTouch) SFIn.numTouch = 0;
		if(SFIn.numTouch == 0){
			if(tdX != 10000 && tdY != 10000){
				if(tdrX >= x && tdrX <= x + width){
					if(tdrY >= y && tdrY <= y + height){
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public boolean isClicked(float x, float y, float width, float height, boolean sound, boolean numTouch){
		if(!numTouch) SFIn.numTouch = 0;
		if(SFIn.numTouch == 0){
			if(tdrX >= x && tdrX <= x + width){
				if(tdrY >= y && tdrY <= y + height){
					if(tuX >= x && tuX <= x + width){
						if(tuY >= y && tuY <= y + height){
							if(sound) clickSound.play(1.0F);
							tuX = 10000;
							tuY = 10000;
							return true;
						}else{
							return false;
						}
					}else{
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public boolean isOnCam(float x, float y, float width, float height, Camera camera, float ratio){
		if(tdX*ratio != 10000 && tdY*ratio != 10000){
			if((tdrOX*ratio + (camera.position.x - 0.5F*camera.viewportWidth)) >= x && (tdrOX*ratio + (camera.position.x - 0.5F*camera.viewportWidth)) <= x + width){
				if((tdrOY*ratio + (camera.position.y - 0.5F*camera.viewportHeight)) >= y && (tdrOY*ratio + (camera.position.y - 0.5F*camera.viewportHeight))  <= y + height){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public boolean isClickedCam(float x, float y, float width, float height, boolean sound, Camera camera, float ratio){
		if((tdrFRX == tdNRX) && (tdrFRY == tdNRY)){
			if((tuX*ratio + (camera.position.x - 0.5F*camera.viewportWidth)) >= x && (tuX*ratio + (camera.position.x - 0.5F*camera.viewportWidth)) <= x + width){
				if((tuY*ratio + (camera.position.y - 0.5F*camera.viewportHeight)) >= y && (tuY*ratio + (camera.position.y - 0.5F*camera.viewportHeight))  <= y + height){
					if(sound) clickSound.play(1.0F);
					tuX = 10000;
					tuY = 10000;
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			tuX = 10000;
			tuY = 10000;
			return false;
		}
	}
	
	public void reset(){
		tdX = 10000;
		tdY = 10000;
		tdrX = 0;
		tdrY = 0;
		tuX = 10000;
		tuY = 10000;
		tdNRX = 10000;
		tdNRY = 10000;
		tdrRX = 0;
		tdrRY = 0;
		tdrFRX = 0;
		tdrFRY = 0;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		key = Character.toString(character);
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		tdX = screenX;
		tdY = (int)(height - screenY);
		tdNRX = screenX;
		tdNRY = (int)(height - screenY);
		tdSRX = screenX;
		tdSRY = (int)(height - screenY);
		tdrX = screenX;
		tdrY = (int)(height - screenY);
		tdrFRX = screenX;
		tdrFRY = (int)(height - screenY);
		tdrSRX = screenX;
		tdrSRY = (int)(height - screenY);
		tdrOX = screenX;
		tdrOY = (int)(height - screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		tuX = screenX;
		tuY = (int)(height - screenY);
		tdX = 10000;
		tdY = 10000;
		tdrRX = 0;
		tdrRY = 0;
		tdrOX = 10000;
		tdrOY = 10000;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		tdrX = screenX;
		tdrY = (int)(height - screenY);
		tdrRX = screenX;
		tdrRY = (int)(height - screenY);
		tdrFRX = screenX;
		tdrFRY = (int)(height - screenY);
		tdrSRX = screenX;
		tdrSRY = (int)(height - screenY);
		tdrOX = screenX;
		tdrOY = (int)(height - screenY);
		numTouch = pointer;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		cursorX = screenX;
		cursorY = screenY;
		return false;
	}

	@Override
	public boolean scrolled(int amount) {

		return false;
	}

}
