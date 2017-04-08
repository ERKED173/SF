package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.Data;
import ru.erked.spaceflight.StartSFlight;
import ru.erked.spaceflight.controllers.SFIn;
import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;
import ru.erked.spaceflight.tech.SFButtonS;
import ru.erked.spaceflight.tech.SFFont;

public class FuelScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private SFIn controller;
	
	private Sprite[] backgroundSprite;
	private int backIter = 0;
	
	private SFButtonS back;
	
	private Sprite fuelIcon;
	private Sprite fuelLine;
	private int fuelIter = 0;
	private Sound bubble;
	
	private static SFFont text;
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private Data data;
	
	public FuelScreen(final StartSFlight game){
		this.game = game;
		data = game.data;
	}
	
	@Override
	public void show() {

		controller = new SFIn();
		
		fuelIcon = RES.atlas.createSprite("fuel");
		fuelIcon.setBounds(0.03F*width, 0.125F*height, 0.1F*height, 0.1F*height);
		fuelLine = RES.atlas.createSprite("fuelLine");
		fuelLine.setBounds(0.0F,0.0F,0.25F*width,0.0F);
		
		MainMenu.music.play();
		bubble = Gdx.audio.newSound(Gdx.files.internal("sounds/misc/bubble.wav"));
		
		backgroundSprite = new Sprite[13];
		for(int i=0;i<13;i++){
			backgroundSprite[i] = RES.atlas.createSprite("fuelScreen", i+1);
			backgroundSprite[i].setBounds(0.0F, 0.0F, width, height);
		}
		
		text = new SFFont(30, Color.WHITE, 1, 2, Color.BLACK);
		
		if(!INF.lngRussian){
			back = new SFButtonS("backI", "backA", 0.225F*width, width - 0.23F*width, 0.015F*height, 2.98913F, 1.0F, -1);
		}else{
			back = new SFButtonS("backRI", "backRA", 0.2F*width, width - 0.215F*width, 0.01F*height, 2.98913F, 1.0F, -1);
		}
		
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
		
		drawBackground();
		
		game.batch.begin();
		
		backgroundSprite[backIter].draw(game.batch);
		
		if(controller.isOn(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true)){
			back.setActiveMode(true);
		}else{
			back.setActiveMode(false);
		}
		back.getSprite().draw(game.batch);

		if(!INF.lngRussian){
			text.draw(game.batch, "Fuel factory", 0.5F*(width-text.getWidth("Fuel factory")), 0.965F*height);
		}else{
			text.draw(game.batch, "Фабрика топлива", 0.5F*(width-text.getWidth("Фабрика топлива")), 0.965F*height);
		}
		
		fuelIcon.draw(game.batch);
		text.draw(game.batch, ": " + INF.fuel + "/" + INF.fuelFull, fuelIcon.getX() + 1.1F*fuelIcon.getWidth(), fuelIcon.getY() + 0.65F*fuelIcon.getHeight());
		
		for(int i=0;i<5;i++){
			fuelLine.setBounds(0.17F*width + i*0.14F*width,0.275F*height,0.1F*width,fuelIter*0.04F*height);
			fuelLine.draw(game.batch);
		}
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		btnListener();
		
	}
	
	private void drawBackground(){
		if(INF.elapsedTime % 15 == 0){
			if(INF.elapsedTime % 15 == 0){
				if(backIter<12) backIter++;
				else backIter=0;
			}
		}
	}
	
	private void btnListener(){
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, false)){
			game.setScreen(new GameScreen(game));
			this.dispose();
		}
		if(controller.isClicked(0.0F, 0.0F, width, height, false, false)){
			fuelIter++;
			if(fuelIter > 12){ 
				fuelIter = 0;
				if(INF.fuel < INF.fuelFull) bubble.play(1.0F);
				INF.fuel++;
				INF.fuelAll++;
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
		show();
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