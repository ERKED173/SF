package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.AdMob;
import ru.erked.spaceflight.Data;
import ru.erked.spaceflight.StartSFlight;
import ru.erked.spaceflight.controllers.SFIn;
import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;
import ru.erked.spaceflight.tech.SFButtonS;
import ru.erked.spaceflight.tech.SFFont;

public class RecordsTableScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private SFIn controller;
	
	private Sprite backgroundSprite[];
	private int backIter = 0;
	
	private SFButtonS back;
	private SFFont text;
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private Data data;
	private AdMob adMob;
	
	public RecordsTableScreen(final StartSFlight game){
		this.game = game;
		data = game.data;
		adMob = game.adMob;
	}
	
	@Override
	public void show() {

		controller = new SFIn();
		
		MainMenu.music.play();
		
		backgroundSprite = new Sprite[13];
		for(int i=0;i<13;i++){
			backgroundSprite[i] = RES.atlas.createSprite("hangar", i+1);
			backgroundSprite[i].setBounds(0.0F, 0.0F, width, height);
		}

		SFButtonsInit();
		
		text = new SFFont(30, Color.WHITE, 1, 2, Color.BLACK);
		
		isTrans = false;
		blackAlpha.setBounds(0.0F, 0.0F, width, height);
		blackAlpha.setAlpha(1.0F);

		adMob.show();
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
		
		backgroundSprite[backIter].draw(game.batch);

		drawBackButton();
		
		if(!INF.lngRussian){
			text.draw(game.batch, "Records of Endless mode", 0.5F*(width-text.getWidth("Records of Endless mode")), 0.965F*height);
			text.draw(game.batch, "Loon - ", 0.1F*width, 0.965F*height-3.0F*text.getHeight("A"));
			text.setColor(Color.SKY);
			text.draw(game.batch, "" + INF.loonRecord, 0.1F*width+text.getWidth("Loon - "), 0.965F*height-3.0F*text.getHeight("A"));
			text.setColor(Color.WHITE);
			text.draw(game.batch, "Emion - ", 0.1F*width, 0.965F*height-6.0F*text.getHeight("A"));
			text.setColor(Color.SKY);
			text.draw(game.batch, "" + INF.emionRecord, 0.1F*width+text.getWidth("Emion - "), 0.965F*height-6.0F*text.getHeight("A"));
			text.setColor(Color.WHITE);
			text.draw(game.batch, "Derten - ", 0.1F*width, 0.965F*height-9.0F*text.getHeight("A"));
			text.setColor(Color.SKY);
			text.draw(game.batch, "" + INF.dertenRecord, 0.1F*width+text.getWidth("Derten - "), 0.965F*height-9.0F*text.getHeight("A"));
			text.setColor(Color.WHITE);
			text.draw(game.batch, "Unar - ", 0.1F*width, 0.965F*height-12.0F*text.getHeight("A"));
			text.setColor(Color.SKY);
			text.draw(game.batch, "" + INF.unarRecord, 0.1F*width+text.getWidth("Unar - "), 0.965F*height-12.0F*text.getHeight("A"));
			text.setColor(Color.WHITE);
			text.draw(game.batch, "Ingmar - ", 0.1F*width, 0.965F*height-15.0F*text.getHeight("A"));
			text.setColor(Color.SKY);
			text.draw(game.batch, "" + INF.ingmarRecord, 0.1F*width+text.getWidth("Ingmar - "), 0.965F*height-15.0F*text.getHeight("A"));
			text.setColor(Color.WHITE);
		}else{
			text.draw(game.batch, "Рекорды бесконечного режима", 0.5F*(width-text.getWidth("Рекорды бесконечного режима")), 0.965F*height);
			text.draw(game.batch, "Мун - ", 0.1F*width, 0.965F*height-3.0F*text.getHeight("A"));
			text.setColor(Color.SKY);
			text.draw(game.batch, "" + INF.loonRecord, 0.1F*width+text.getWidth("Мун - "), 0.965F*height-3.0F*text.getHeight("A"));
			text.setColor(Color.WHITE);
			text.draw(game.batch, "Эмион - ", 0.1F*width, 0.965F*height-6.0F*text.getHeight("A"));
			text.setColor(Color.SKY);
			text.draw(game.batch, "" + INF.emionRecord, 0.1F*width+text.getWidth("Эмион - "), 0.965F*height-6.0F*text.getHeight("A"));
			text.setColor(Color.WHITE);
			text.draw(game.batch, "Дертен - ", 0.1F*width, 0.965F*height-9.0F*text.getHeight("A"));
			text.setColor(Color.SKY);
			text.draw(game.batch, "" + INF.dertenRecord, 0.1F*width+text.getWidth("Дертен - "), 0.965F*height-9.0F*text.getHeight("A"));
			text.setColor(Color.WHITE);
			text.draw(game.batch, "Унар - ", 0.1F*width, 0.965F*height-12.0F*text.getHeight("A"));
			text.setColor(Color.SKY);
			text.draw(game.batch, "" + INF.unarRecord, 0.1F*width+text.getWidth("Унар - "), 0.965F*height-12.0F*text.getHeight("A"));
			text.setColor(Color.WHITE);
			text.draw(game.batch, "Ингмар - ", 0.1F*width, 0.965F*height-15.0F*text.getHeight("A"));
			text.setColor(Color.SKY);
			text.draw(game.batch, "" + INF.ingmarRecord, 0.1F*width+text.getWidth("Ингмар - "), 0.965F*height-15.0F*text.getHeight("A"));
			text.setColor(Color.WHITE);
		}
		
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
			adMob.hide();
			game.setScreen(new GameScreen(game));
			this.dispose();
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