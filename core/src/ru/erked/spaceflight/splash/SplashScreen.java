package ru.erked.spaceflight.splash;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;

public class SplashScreen implements Screen {

	private final ru.erked.spaceflight.StartSFlight game;
	
	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	public static final float splashTentionIndex = width/256;
	
	private Sprite splash;
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp;
	
	private float splashTimer;

	private float splashX;
	private float splashY;
	
	public SplashScreen(final ru.erked.spaceflight.StartSFlight game){
		this.game = game;
	}
	
	@Override
	public void show() {

		splashTimer = 0.0F;
		alp = 0.0F;
		
		splashX = 0.0F;
		splashY = (-1)*(256*splashTentionIndex)/2 + height/2;
		
		splash = RES.atlas.createSprite("splashScreen");
		
		splash.setBounds(splashX, splashY, width, 256*splashTentionIndex);
	
		blackAlpha.setBounds(0.0F, 0.0F, width, height);
		blackAlpha.setAlpha(0.0F);
		
		if(Gdx.app.getType().equals(ApplicationType.Desktop)){
			ru.erked.spaceflight.random.ResetTheGame.reset();
		}
		
	}

	@Override
	public void render(float delta) {
		INF.elapsedTime++;
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		splashTimer+=delta;
		
		if(splashTimer < 3.0F){
			game.batch.begin();
			splash.draw(game.batch);
			game.batch.end();
		}else{
			alp+=0.05F;
			game.batch.begin();
			splash.draw(game.batch);
			blackAlpha.draw(game.batch);
			game.batch.end();
			if(alp<1.0F){
				blackAlpha.setAlpha(alp);
			}else{
				this.dispose();
			}
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
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		game.setScreen(new MainMenu(game));
	}

}
