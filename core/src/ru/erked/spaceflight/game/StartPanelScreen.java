package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.AndroidOnlyInterface;
import ru.erked.spaceflight.Data;
import ru.erked.spaceflight.StartSFlight;
import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;
import ru.erked.spaceflight.tech.CurPR;
import ru.erked.spaceflight.tech.SFButtonS;
import ru.erked.spaceflight.tech.SFFont;

public class StartPanelScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private ru.erked.spaceflight.controllers.SFIn controller;
	
	private Sprite[] backgroundSprite;
	private int backIter = 0;
	
	private SFButtonS back;
	
	private SFButtonS start;
	private SFButtonS monogram;
	private boolean bonus = false;
	
	private static SFFont text;
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private AndroidOnlyInterface aoi;
	private Data data;
	private ru.erked.spaceflight.AdMob adMob;
	
	public StartPanelScreen(final StartSFlight game){
		this.game = game;
		aoi = game.aoi;
		data = game.data;
		adMob = game.adMob;
	}
	
	@Override
	public void show() {
		controller = new ru.erked.spaceflight.controllers.SFIn();
		
		MainMenu.music.play();
		
		if(INF.isBombActive || INF.isColoumnActive || INF.isLineActive || INF.isTimeActive) bonus = true;
		
		backgroundSprite = new Sprite[13];
		for(int i=0;i<13;i++){
			backgroundSprite[i] = RES.atlas.createSprite("resource", i+1);
			backgroundSprite[i].setBounds(0.0F, 0.0F, width, height);
		}
		
		if(!INF.lngRussian){
			back = new SFButtonS("backI", "backA", 0.225F*width, width - 0.23F*width, 0.015F*height, 2.98913F, 1.0F, -1);
			start = new SFButtonS("startENI", "startENA", 0.35F*width, 0.05F*width, 0.15F*height, 2.0F, 1.0F, -1);
		}else{
			back = new SFButtonS("backRI", "backRA", 0.2F*width, width - 0.215F*width, 0.01F*height, 2.98913F, 1.0F, -1);
			start = new SFButtonS("startRUI", "startRUA", 0.35F*width, 0.05F*width, 0.15F*height, 2.0F, 1.0F, -1);
		}
		
		monogram = new SFButtonS("monogrammI", "monogrammA", 0.35F*width, 0.05F*width, 0.75F*height, 2.0F, 1.0F, -1);
		monogram.setY(0.75F*height - 0.5F*monogram.getHeight());
		
		text = new SFFont(30, Color.WHITE, 1, 2, Color.BLACK);
		
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
		
		drawBackground();
		
		game.batch.begin();
		
		backgroundSprite[backIter].draw(game.batch);
		
		if(controller.isOn(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true)){
			back.setActiveMode(true);
		}else{
			back.setActiveMode(false);
		}
		back.getSprite().draw(game.batch);

		if(CurPR.getCurRocket() != null){
			if(CurPR.getCurPlanet() != null){
				start.getSprite().setColor(Color.WHITE);
				if(controller.isOn(start.getX(), start.getY(), start.getWidth(), start.getHeight(), true)){
					start.setActiveMode(true);
					start.setY(0.15F*height - 0.01F*start.getHeight());
				}else{
					start.setActiveMode(false);
					start.setY(0.15F*height);
				}
			}else{
				start.getSprite().setColor(Color.GRAY);
			}
		}else{
			start.getSprite().setColor(Color.GRAY);
		}
		start.getSprite().draw(game.batch);
		
		if(controller.isOn(monogram.getX(), monogram.getY(), monogram.getWidth(), monogram.getHeight(), true)){
			monogram.setActiveMode(true);
			monogram.setY(0.7F*height - 0.51F*monogram.getHeight());
		}else{
			monogram.setActiveMode(false);
			monogram.setY(0.7F*height - 0.5F*monogram.getHeight());
		}
		monogram.getSprite().draw(game.batch);
		
		if(!INF.lngRussian){
			text.draw(game.batch, "Control panel", 0.5F*(width-text.getWidth("Control panel")), 0.965F*height);
			if(!INF.currentPlanet.equals("null")){
				float h;
				if(bonus) h = 0.75F*height + 0.75F*text.getHeight("A");
				else h = 0.75F*height;
				text.setColor(Color.LIME);
				text.draw(game.batch, "Current planet: " + CurPR.getCurPlanet().getNameUS(), 0.45F*width, h);
				text.setColor(Color.WHITE);
			}else{
				float h;
				if(bonus) h = 0.75F*height + 0.75F*text.getHeight("A");
				else h = 0.75F*height;
				text.setColor(Color.FIREBRICK);
				text.draw(game.batch, "The planet is not selected.", 0.45F*width, h);
				text.setColor(Color.WHITE);
			}
			if(!INF.currentRocket.equals("null")){
				float h;
				if(bonus) h = 0.75F*height - 0.75F*text.getHeight("A");
				else h = 0.75F*height - 1.5F*text.getHeight("A");
				text.setColor(Color.LIME);
				text.draw(game.batch, "Current rocket: " + CurPR.getCurRocket().getNameUS(), 0.45F*width, h);
				text.setColor(Color.WHITE);
			}else{
				float h;
				if(bonus) h = 0.75F*height - 0.75F*text.getHeight("A");
				else h = 0.75F*height - 1.5F*text.getHeight("A");
				text.setColor(Color.FIREBRICK);
				text.draw(game.batch, "The rocket is not selected.", 0.45F*width, h);
				text.setColor(Color.WHITE);
			}
			if(INF.isBombActive){
				text.setColor(Color.LIME);
				text.draw(game.batch, "Current bonus: Bomb", 0.45F*width, 0.75F*height - 2.25F*text.getHeight("A"));
				text.setColor(Color.WHITE);
			}
			if(INF.isColoumnActive){
				text.setColor(Color.LIME);
				text.draw(game.batch, "Current bonus: Coloumn", 0.45F*width, 0.75F*height - 2.25F*text.getHeight("A"));
				text.setColor(Color.WHITE);
			}
			if(INF.isLineActive){
				text.setColor(Color.LIME);
				text.draw(game.batch, "Current bonus: Line", 0.45F*width, 0.75F*height - 2.25F*text.getHeight("A"));
				text.setColor(Color.WHITE);
			}
			if(INF.isTimeActive){
				text.setColor(Color.LIME);
				text.draw(game.batch, "Current bonus: Time", 0.45F*width, 0.75F*height - 2.25F*text.getHeight("A"));
				text.setColor(Color.WHITE);
			}
			text.draw(game.batch, "Next level: " + (INF.planetLevel + 1), 0.45F*width, 0.75F*height - 9.5F*text.getHeight("A"));
		}else{
			text.draw(game.batch, "Панель управления", 0.5F*(width-text.getWidth("Панель управления")), 0.965F*height);
			if(!INF.currentPlanet.equals("null")){
				float h;
				if(bonus) h = 0.75F*height + 0.75F*text.getHeight("A");
				else h = 0.75F*height;
				text.setColor(Color.LIME);
				text.draw(game.batch, "Текущая планета: " + CurPR.getCurPlanet().getNameRU(), 0.45F*width, h);
				text.setColor(Color.WHITE);
			}else{
				float h;
				if(bonus) h = 0.75F*height + 0.75F*text.getHeight("A");
				else h = 0.75F*height;
				text.setColor(Color.FIREBRICK);
				text.draw(game.batch, "Планета не выбрана.", 0.45F*width, h);
				text.setColor(Color.WHITE);
			}
			if(!INF.currentRocket.equals("null")){
				float h;
				if(bonus) h = 0.75F*height - 0.75F*text.getHeight("A");
				else h = 0.75F*height - 1.5F*text.getHeight("A");
				text.setColor(Color.LIME);
				text.draw(game.batch, "Текущая ракета: " + CurPR.getCurRocket().getNameRU(), 0.45F*width, h);
				text.setColor(Color.WHITE);
			}else{
				float h;
				if(bonus) h = 0.75F*height - 0.75F*text.getHeight("A");
				else h = 0.75F*height - 1.5F*text.getHeight("A");
				text.setColor(Color.FIREBRICK);
				text.draw(game.batch, "Ракета не выбрана.", 0.45F*width, h);
				text.setColor(Color.WHITE);
			}
			if(INF.isBombActive){
				text.setColor(Color.LIME);
				text.draw(game.batch, "Текущий бонус: Бомба", 0.45F*width, 0.75F*height - 2.25F*text.getHeight("A"));
				text.setColor(Color.WHITE);
			}
			if(INF.isColoumnActive){
				text.setColor(Color.LIME);
				text.draw(game.batch, "Текущий бонус: Столбец", 0.45F*width, 0.75F*height - 2.25F*text.getHeight("A"));
				text.setColor(Color.WHITE);
			}
			if(INF.isLineActive){
				text.setColor(Color.LIME);
				text.draw(game.batch, "Текущий бонус: Линия", 0.45F*width, 0.75F*height - 2.25F*text.getHeight("A"));
				text.setColor(Color.WHITE);
			}
			if(INF.isTimeActive){
				text.setColor(Color.LIME);
				text.draw(game.batch, "Текущий бонус: Время", 0.45F*width, 0.75F*height - 2.25F*text.getHeight("A"));
				text.setColor(Color.WHITE);
			}
			text.draw(game.batch, "Следующий уровень: " + (INF.planetLevel + 1), 0.45F*width, 0.75F*height - 9.5F*text.getHeight("A"));
		}
		if(!INF.currentPlanet.equals("null") && !INF.currentRocket.equals("null")){
			if(!INF.lngRussian){
				text.setColor(Color.LIME);
				text.draw(game.batch, "Rocket is ready to start.", 0.45F*width, 0.75F*height - 11.0F*text.getHeight("A"));
				text.setColor(Color.WHITE);
			}else{
				text.setColor(Color.LIME);
				text.draw(game.batch, "Ракета готова к запуску.", 0.45F*width, 0.75F*height - 11.0F*text.getHeight("A"));
				text.setColor(Color.WHITE);
			}
		}
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		buttonListener();
		
	}
	
	private void buttonListener(){
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, true)){
			adMob.hide();
			game.setScreen(new GameScreen(game));
			this.dispose();
		}
		
		if(controller.isClicked(monogram.getX(), monogram.getY(), monogram.getWidth(), monogram.getHeight(), true, true)){
			adMob.hide();
			game.setScreen(new PlanetScreen(game));
			this.dispose();
		}
		
		if(controller.isClicked(start.getX(), start.getY(), start.getWidth(), start.getHeight(), true, true)){
			if(CurPR.getCurRocket() != null){
				if(CurPR.getCurPlanet() != null){
					MainMenu.music.pause();
					adMob.hide();
					game.setScreen(new FlightScreen(game));
					this.dispose();
				}else{
					if(!INF.lngRussian){
						aoi.makeToast("You are not prepared to flight");
					}else{
						aoi.makeToast("Вы не готовы к полёту");
					}
				}
			}else{
				if(!INF.lngRussian){
					aoi.makeToast("You are not prepared to flight");
				}else{
					aoi.makeToast("Вы не готовы к полёту");
				}
			}
		}
	}
	
	private void drawBackground(){
		if(INF.elapsedTime % 15 == 0){
			if(INF.elapsedTime % 15 == 0){
				if(backIter<12) backIter++;
				else backIter=0;
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