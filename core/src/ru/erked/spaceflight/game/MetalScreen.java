package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.Data;
import ru.erked.spaceflight.StartSFlight;
import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;
import ru.erked.spaceflight.tech.SFButtonS;
import ru.erked.spaceflight.tech.SFFont;

public class MetalScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private ru.erked.spaceflight.controllers.SFIn controller;
	
	private Sprite[] backgroundSprite;
	private int backIter=0;
	
	private Sprite metalIngot; 
	private int metalIter = 0;
	private Sound anvil;
	private Sprite metalIcon;

	private SFButtonS back;

	private static SFFont text;
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private Data data; 
	
	public MetalScreen(final StartSFlight game){
		this.game = game;
		data = game.data;
	}
	
	@Override
	public void show() {

		controller = new ru.erked.spaceflight.controllers.SFIn();
		
		MainMenu.music.play();
		anvil = Gdx.audio.newSound(Gdx.files.internal("sounds/misc/anvil.wav"));
		
		backgroundSprite = new Sprite[2];
		for(int i=0;i<2;i++){
			backgroundSprite[i] = RES.atlas.createSprite("metal", i+1);
			backgroundSprite[i].setBounds(0.0F, 0.0F, width, height);
		}
		
		if(!INF.lngRussian){
			back = new SFButtonS("backI", "backA", 0.225F*width, width - 0.23F*width, 0.015F*height, 2.98913F, 1.0F, -1);
		}else{
			back = new SFButtonS("backRI", "backRA", 0.2F*width, width - 0.215F*width, 0.01F*height, 2.98913F, 1.0F, -1);
		}
		
		text = new SFFont(30, Color.WHITE, 1, 2, Color.BLACK);
		
		metalIcon = RES.atlas.createSprite("metal", -1);
		metalIcon.setBounds(0.025F*width, 0.015F*height, 0.1F*height, 0.1F*height);
		metalIngot = RES.atlas.createSprite("metalIngot");
		metalIngot.setBounds(0.055F*width, 0.75F*height, 0.2F*height, 0.2F*height);
		
		isTrans = false;
		blackAlpha.setBounds(0.0F, 0.0F, width, height);
		blackAlpha.setAlpha(1.0F);
	}

	@Override
	public void render(float delta) {
		INF.elapsedTime++;
		resourcesCheck();
		
		if(alp>0.0F && (!isTrans)){
			blackAlpha.setAlpha(alp);
			alp-=0.05F;
		}else if(!isTrans){
			blackAlpha.setAlpha(0.0F);
			alp = 0.0F;
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		backgroundSprite[backIter].draw(game.batch);
		
		if(controller.isOn(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true)){
			back.setActiveMode(true);
		}else{
			back.setActiveMode(false);
		}
		back.getSprite().draw(game.batch);

		if(!INF.lngRussian){
			text.draw(game.batch, "Metal factory", 0.01F*width, 0.975F*height);
		}else{
			text.draw(game.batch, "Фабрика металла", 0.01F*width, 0.975F*height);
		}
		
		metalIcon.draw(game.batch);
		text.draw(game.batch, ": " + INF.metal + "/" + INF.metalFull, metalIcon.getX() + 1.1F*metalIcon.getWidth(), metalIcon.getY() + 0.65F*metalIcon.getHeight());
		
		if(metalIter!=0){
			if(metalIter<=11){
				metalIngot.setX(-0.1F*width+(metalIter-1)*0.055F*width);
				metalIngot.setY(backgroundSprite[backIter].getY() + 0.65F*backgroundSprite[backIter].getHeight()-(metalIter-1)*0.042F*backgroundSprite[backIter].getHeight());
			}else{
				metalIngot.setX(-0.1F*width+(metalIter-2)*0.055F*width);
				metalIngot.setY(backgroundSprite[backIter].getY() + 0.65F*backgroundSprite[backIter].getHeight()-(21-(metalIter-1))*0.042F*backgroundSprite[backIter].getHeight());
			}
			metalIngot.draw(game.batch);
		}
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, false)){
			game.setScreen(new GameScreen(game));
			this.dispose();
		}
		
		if(controller.isClicked(0.0F, 0.0F, width, height, false, false)){
			if(metalIter!=11) metalIter++;
			else metalIter+=2;
			//
			if(backIter == 0) backIter = 1;
			else backIter = 0;
			//
			if(metalIter > 22){
				INF.metal+=2;
				INF.metalAll+=2;
				anvil.play(1.0F);
				metalIter=0;
			}
		}
	}

	private void resourcesCheck(){
		if(INF.money>INF.moneyFull) INF.money = INF.moneyFull;
		if(INF.fuel>INF.fuelFull) INF.fuel = INF.fuelFull;
		if(INF.metal>INF.metalFull) INF.metal = INF.metalFull;
	}
	
	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
		data.saveSF();
	}

	@Override
	public void resume() {
		MainMenu.music.play();
		data.loadSF();
	}

	@Override
	public void hide() {
		data.saveSF();
	}

	@Override
	public void dispose() {
		data.saveSF();
		game.dispose();
		text.dispose();
	}

}