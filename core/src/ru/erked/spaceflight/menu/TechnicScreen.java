package ru.erked.spaceflight.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import ru.erked.spaceflight.random.INF;

public class TechnicScreen implements Screen{

	private final ru.erked.spaceflight.StartSFlight game;
	private float timer;
	private Screen screen;
	private float time;
	
	public TechnicScreen(final ru.erked.spaceflight.StartSFlight game, Screen screen, float time){
		this.game = game;
		this.screen = screen;
		this.time = time;
	}
	
	@Override
	public void show() {
		timer = 0.0F;
	}

	@Override
	public void render(float delta) {
		INF.elapsedTime++;
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		timer+=delta;
		
		if(timer > time){
			this.dispose();
			game.setScreen(screen);
		}
	}

	@Override
	public void resize(int width, int height) {
	
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
		show();
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		game.dispose();
	}

}
