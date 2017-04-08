package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.AdMob;
import ru.erked.spaceflight.Data;
import ru.erked.spaceflight.StartSFlight;
import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;
import ru.erked.spaceflight.tech.SFButtonS;
import ru.erked.spaceflight.tech.SFFont;

public class StatisticScreen implements Screen{
	
	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private ru.erked.spaceflight.controllers.SFIn controller;
	
	private Sprite backgroundSprite;

	private SFButtonS back;

	private static SFFont header;
	private static SFFont text;
	public static String elapsedTime;
	private static long hours;
	private static long minutes;
	private static long seconds;
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private Data data;
	private AdMob adMob;
	
	public StatisticScreen(final StartSFlight game){
		this.game = game;
		data = game.data;
		adMob = game.adMob;
	}
	
	@Override
	public void show() {

		controller = new ru.erked.spaceflight.controllers.SFIn();
		
		MainMenu.music.play();
		
		backgroundSprite = RES.atlas.createSprite("scoreboard");
		backgroundSprite.setBounds(0.0F, 0.0F, width, height);
		
		if(!INF.lngRussian){
			back = new SFButtonS("backI", "backA", 0.225F*width, width - 0.23F*width, 0.015F*height, 2.98913F, 1.0F, -1);
		}else{
			back = new SFButtonS("backRI", "backRA", 0.2F*width, width - 0.215F*width, 0.01F*height, 2.98913F, 1.0F, -1);
		}
		
		text = new SFFont(30, Color.WHITE, 1, 2, Color.BLACK);
		header = new SFFont(25, Color.SKY, 2.0F, Color.ROYAL);
		header.setScale(2.0F);
		
		isTrans = false;
		blackAlpha.setBounds(0.0F, 0.0F, width, height);
		blackAlpha.setAlpha(1.0F);

		adMob.show();
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
		
		backgroundSprite.draw(game.batch);
		
		if(controller.isOn(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true)){
			back.setActiveMode(true);
		}else{
			back.setActiveMode(false);
		}
		back.getSprite().draw(game.batch);
		
		seconds = INF.elapsedTime/60;
		minutes = (int)seconds/60;
		hours = (int)minutes/60;
		if(!INF.lngRussian){
			elapsedTime = "Time in the game: " + Integer.toString((int) hours) + "h " + Integer.toString((int) minutes%60) + "m " + Integer.toString((int) seconds%60) + "s";
			header.draw(game.batch, "STATISTICS", 0.5F*width - 0.5F*header.getWidth("STATISTICS"), 0.9F*height);
			text.draw(game.batch, elapsedTime, 0.075F*width, 0.7F*height);
			text.draw(game.batch, "Rocket launches: " + Long.toString((int)(INF.launch)), 0.075F*width, 0.7F*height - 1.5F*text.getHeight("A"));
			text.draw(game.batch, "Data packets: " + Long.toString((int)(INF.facts)) + "/5", 0.075F*width, 0.7F*height - 3.0F*text.getHeight("A"));
			text.draw(game.batch, "Resources extracted: ", 0.075F*width, 0.7F*height - 4.5F*text.getHeight("A"));
			text.draw(game.batch, Integer.toString(INF.moneyAll) + " coins", 0.075F*width, 0.7F*height - 6.0F*text.getHeight("A"));
			text.draw(game.batch, Integer.toString(INF.fuelAll) + " fuel", 0.075F*width, 0.7F*height - 7.5F*text.getHeight("A"));
			text.draw(game.batch, Integer.toString(INF.metalAll) + " metal", 0.075F*width, 0.7F*height - 9.0F*text.getHeight("A"));
			if(INF.planetLevel == 50)
				text.draw(game.batch, "The game is over", 0.075F*width, 0.7F*height - 10.5F*text.getHeight("A"));
			else
				text.draw(game.batch, "Max reached level: " + (INF.planetLevel+1), 0.075F*width, 0.7F*height - 10.5F*text.getHeight("A"));
		}else{
			elapsedTime = "Время в игре: " + Integer.toString((int) hours) + "ч " + Integer.toString((int) minutes%60) + "м " + Integer.toString((int) seconds%60) + "с";
			header.draw(game.batch, "СТАТИСТИКА", 0.5F*width - 0.5F*header.getWidth("СТАТИСТИКА"), 0.9F*height);
			text.draw(game.batch, elapsedTime, 0.075F*width, 0.7F*height);
			text.draw(game.batch, "Запусков ракет: " + Long.toString((int)(INF.launch)), 0.075F*width, 0.7F*height - 1.5F*text.getHeight("A"));
			text.draw(game.batch, "Пакеты данных: " + Long.toString((int)(INF.facts)) + "/5", 0.075F*width, 0.7F*height - 3.0F*text.getHeight("A"));
			text.draw(game.batch, "Ресурсов добыто: ", 0.075F*width, 0.7F*height - 4.5F*text.getHeight("A"));
			text.draw(game.batch, Integer.toString(INF.moneyAll) + " монет", 0.075F*width, 0.7F*height - 6.0F*text.getHeight("A"));
			text.draw(game.batch, Integer.toString(INF.fuelAll) + " топлива", 0.075F*width, 0.7F*height - 7.5F*text.getHeight("A"));
			text.draw(game.batch, Integer.toString(INF.metalAll) + " металла", 0.075F*width, 0.7F*height - 9.0F*text.getHeight("A"));
			if(INF.planetLevel == 50)
				text.draw(game.batch, "Игра пройдена", 0.075F*width, 0.7F*height - 10.5F*text.getHeight("A"));
			else
				text.draw(game.batch, "Максимальный уровень: " + (INF.planetLevel+1), 0.075F*width, 0.7F*height - 10.5F*text.getHeight("A"));
		}
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, true)){
			adMob.hide();
			game.setScreen(new GameScreen(game));
			this.dispose();
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
		header.dispose();
		text.dispose();
	}

}