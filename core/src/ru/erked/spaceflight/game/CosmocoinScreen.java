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

public class CosmocoinScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private ru.erked.spaceflight.controllers.SFIn controller;
	
	private Sprite backgroundSprite;
	private int backIter = 0;
	
	private SFButtonS back;
	
	private Sprite elevatorUp;
	private Sprite elevatorDown;
	private Sprite coinIcon;
	private Sound cash;
	private Sound anvil;
	
	private SFFont text;
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;

	private int codeIter = 0;
	
	private Data data;
	
	public CosmocoinScreen(final StartSFlight game){
		this.game = game;
		data = game.data;
	}
	
	@Override
	public void show() {

		controller = new ru.erked.spaceflight.controllers.SFIn();
		
		MainMenu.music.play();
		cash = Gdx.audio.newSound(Gdx.files.internal("sounds/misc/cash.wav"));
		anvil = Gdx.audio.newSound(Gdx.files.internal("sounds/misc/anvil.wav"));

		backgroundSprite = RES.atlas.createSprite("coinFac");
		backgroundSprite.setBounds(0.0F, 0.0F, width, height);
		
		coinIcon = RES.atlas.createSprite("cosmocoin");
		coinIcon.setBounds(0.025F*width, 0.05F*height, 0.1F*height, 0.1F*height);
		
		elevatorUp = RES.atlas.createSprite("coinElevator", 1);
		elevatorUp.setBounds(0.3F*width, -0.4F*width, 0.4F*width, 0.8F*width);
		elevatorDown = RES.atlas.createSprite("coinElevator", 2);
		elevatorDown.setBounds(0.3F*width, 0.0F*width, 0.4F*width, 0.8F*width);
		
		SFButtonsInit();
		
		text = new SFFont(30, Color.WHITE, 1, 2, Color.BLACK);
		
		isTrans = false;
		blackAlpha.setBounds(0.0F, 0.0F, width, height);
		blackAlpha.setAlpha(1.0F);
	}

	@Override
	public void render(float delta) {
		INF.elapsedTime++;
		resourceCheck();
		
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
		
		backgroundSprite.draw(game.batch);

		coinIcon.draw(game.batch);
		text.draw(game.batch, ": " + INF.money + "/" + INF.moneyFull, coinIcon.getX() + 1.1F*coinIcon.getWidth(), coinIcon.getY() + 0.65F*coinIcon.getHeight());
		
		drawBackButton();
		
		if(!INF.lngRussian){
			text.draw(game.batch, "Coin factory", 0.01F*width, 0.975F*height);
		}else{
			text.draw(game.batch, "Фабрика монет", 0.01F*width, 0.975F*height);
		}
		
		if(codeIter >= 19){
			anvil.play();
			codeIter = 0;
		}
		if(codeIter == 9){
			INF.money++;
			INF.moneyAll++;
			cash.play();
			codeIter = 10;
		}
		
		elevatorUp.draw(game.batch);
		elevatorDown.draw(game.batch);
		
		elevatorUp.setY(-1.8F*width + 0.1F*codeIter*width);
		elevatorDown.setY(0.0F*width - 0.1F*codeIter*width);
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		buttonListener();
		
	}
	
	private void SFButtonsInit(){
		if(!INF.lngRussian){
			back = new SFButtonS("backI", "backA", 0.225F*width, width - 0.23F*width, 0.015F*height, 2.98913F, 1.0F, -1);
		}else{
			back = new SFButtonS("backRI", "backRA", 0.2F*width, width - 0.215F*width, 0.01F*height, 2.98913F, 1.0F, -1);
		}
	}

	private void drawBackground(){
		if(INF.elapsedTime % 15 == 0){
			if(backIter<12) backIter++;
			else backIter=0;
		}
	}
	private void drawBackButton(){
		if(controller.isOn(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true)){
			back.setActiveMode(true);
		}else{
			back.setActiveMode(false);
		}
		back.getSprite().draw(game.batch);
	}
	
	private void buttonListener(){
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, false)){
			game.setScreen(new GameScreen(game));
			this.dispose();
		}
		if(controller.isClicked(0.0F, 0.0F, width, height, false, false)){
			codeIter++;
		}
	}
	
	private void resourceCheck(){
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