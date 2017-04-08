package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.RandomXS128;

import ru.erked.spaceflight.AdMob;
import ru.erked.spaceflight.Data;
import ru.erked.spaceflight.GPGS;
import ru.erked.spaceflight.StartSFlight;
import ru.erked.spaceflight.controllers.SFIn;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;
import ru.erked.spaceflight.tech.CurPR;
import ru.erked.spaceflight.tech.SFButtonS;
import ru.erked.spaceflight.tech.SFFont;

public class ResultsScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private SFIn controller;
	RandomXS128 rand = new RandomXS128();
	
	private Sprite[] backgroundSprite;
	private int backIter = 0;
	
	private SFButtonS back;
	
	private static SFFont text;
	
	private boolean isVictory;
	private boolean isEndlessMode;
	private int score;
	private int bonusTime;
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private Data data;
	private AdMob adMob;
	private GPGS gpgs;

	public ResultsScreen(final StartSFlight game, int gemsToWin, int score, int bonusTime, boolean isVictory, boolean isEndlessMode){
		this.game = game;
		this.isVictory = isVictory;
		this.isEndlessMode = isEndlessMode;
		this.score = score;
		this.bonusTime = bonusTime;
		data = game.data;
		adMob = game.adMob;
		gpgs = game.gpgs;
	}
	
	@Override
	public void show() {

		controller = new SFIn();
		
		backgroundSprite = new Sprite[13];
		for(int i=0;i<13;i++){
			backgroundSprite[i] = RES.atlas.createSprite("resource", i+1);
			backgroundSprite[i].setBounds(0.0F, 0.0F, width, height);
		}
		
		if(!INF.lngRussian){
			back = new SFButtonS("continueI", "continueA", 0.365F*width, width - 0.39F*width, 0.015F*height, 5.97826F, 1.0F, -1);
		}else{
			back = new SFButtonS("continueRI", "continueRA", 0.365F*width, width - 0.39F*width, 0.015F*height, 5.97826F, 1.0F, -1);
		}
		
		text = new SFFont(20, Color.WHITE, 1, 2, Color.BLACK);
		
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
		
		if(isVictory){
			if(!INF.lngRussian){
				text.draw(game.batch, "Level complete!", 
						0.5F*(width-text.getWidth("Level complete!")), 
						0.965F*height);
				///
				text.draw(game.batch, "Score: ", 
						0.05F*width,
						0.965F*height-3.0F*text.getHeight("A"));
				text.setColor(Color.SKY);
				text.draw(game.batch, "" + (int)(score), 
						0.05F*width + text.getWidth("Score: "),
						0.965F*height-3.0F*text.getHeight("A"));
				text.setColor(Color.WHITE);
				///
				text.draw(game.batch, "Reward: ", 
						0.05F*width,
						0.965F*height-4.5F*text.getHeight("A"));
				text.setColor(Color.SKY);
				text.draw(game.batch, "+"+((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F) - ((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F))%5), 
						0.05F*width + text.getWidth("Reward: "),
						0.965F*height-4.5F*text.getHeight("A"));
				text.setColor(Color.WHITE);
				text.draw(game.batch, " to resources", 
						0.05F*width+text.getWidth("Reward: +" + ((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F) - ((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F))%5)),
						0.965F*height-4.5F*text.getHeight("A"));
				///
				text.draw(game.batch, "Time bonus: ", 
						0.05F*width,
						0.965F*height-6.0F*text.getHeight("A"));
				text.setColor(Color.SKY);
				text.draw(game.batch, "+" + (int)(bonusTime*1.25F - (bonusTime*1.25F)%5) + "", 
						0.05F*width + text.getWidth("Time bonus: "),
						0.965F*height-6.0F*text.getHeight("A"));
				text.setColor(Color.WHITE);
				text.draw(game.batch, " to resources", 
						0.05F*width + text.getWidth("Time bonus: +" + (int)(bonusTime*1.25F - (bonusTime*1.25F)%5)),
						0.965F*height-6.0F*text.getHeight("A"));
				///
				if((INF.planetLevel == 9 || INF.planetLevel == 19 || INF.planetLevel == 29 || INF.planetLevel == 39 || INF.planetLevel == 49) && !isEndlessMode){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "New data packets are available!", 
							0.5F*(width-text.getWidth("New data packets are available!")),
							0.965F*height-8.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}
				if(INF.planetLevel == 4 || INF.planetLevel == 14 || INF.planetLevel == 24 || INF.planetLevel == 34){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "New bonus is available!",
							0.5F*(width-text.getWidth("New bonus is available!")),
							0.965F*height-8.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}
			}else{
				text.draw(game.batch, "Уровень пройден!", 
						0.5F*(width-text.getWidth("Уровень пройден!")), 
						0.965F*height);
				///
				text.draw(game.batch, "Счёт: ", 
						0.05F*width,
						0.965F*height-3.0F*text.getHeight("A"));
				text.setColor(Color.SKY);
				text.draw(game.batch, "" + (int)(score), 
						0.05F*width + text.getWidth("Счёт: "),
						0.965F*height-3.0F*text.getHeight("A"));
				text.setColor(Color.WHITE);
				///
				text.draw(game.batch, "Награда: ", 
						0.05F*width,
						0.965F*height-4.5F*text.getHeight("A"));
				text.setColor(Color.SKY);
				text.draw(game.batch, "+"+((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F) - ((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F))%5)+"", 
						0.05F*width + text.getWidth("Награда: "),
						0.965F*height-4.5F*text.getHeight("A"));
				text.setColor(Color.WHITE);
				text.draw(game.batch, " к ресурсам", 
						0.05F*width+text.getWidth("Награда: +" + ((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F) - ((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F))%5)),
						0.965F*height-4.5F*text.getHeight("A"));
				///
				text.draw(game.batch, "Бонус времени: ", 
						0.05F*width,
						0.965F*height-6.0F*text.getHeight("A"));
				text.setColor(Color.SKY);
				text.draw(game.batch, "+" + (int)(bonusTime*1.25F - (bonusTime*1.25F)%5) + "", 
						0.05F*width + text.getWidth("Бонус времени: "),
						0.965F*height-6.0F*text.getHeight("A"));
				text.setColor(Color.WHITE);
				text.draw(game.batch, " к ресурсам", 
						0.05F*width + text.getWidth("Бонус времени: +" + (int)(bonusTime*1.25F - (bonusTime*1.25F)%5)),
						0.965F*height-6.0F*text.getHeight("A"));
				///
				if((INF.planetLevel == 9 || INF.planetLevel == 19 || INF.planetLevel == 29 || INF.planetLevel == 39 || INF.planetLevel == 49) && !isEndlessMode){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "Доступны новые пакеты данных!", 
							0.5F*(width-text.getWidth("Доступны новые пакеты данных!")),
							0.965F*height-8.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}
				if(INF.planetLevel == 4 || INF.planetLevel == 14 || INF.planetLevel == 24 || INF.planetLevel == 34){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "Доступен новый бонус!",
							0.5F*(width-text.getWidth("Доступен новый бонус!")),
							0.965F*height-8.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}
			}
		}else if(isEndlessMode){
			if(!INF.lngRussian){
				text.draw(game.batch, "Game over!", 
						0.5F*(width-text.getWidth("Game over!")), 
						0.965F*height);
				///
				text.draw(game.batch, "Score: ", 
						0.05F*width, 
						0.965F*height-3.0F*text.getHeight("A"));
				text.setColor(Color.SKY);
				text.draw(game.batch, (int)(score)+"", 
						0.05F*width + text.getWidth("Score: "), 
						0.965F*height-3.0F*text.getHeight("A"));
				text.setColor(Color.WHITE);
				///
				text.draw(game.batch, "Gem bonus: ", 
						0.05F*width, 
						0.965F*height-4.5F*text.getHeight("A"));
				text.setColor(Color.SKY);
				text.draw(game.batch, "+" + ((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo()))/10-((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo())/10)%5)), 
						0.05F*width + text.getWidth("Gem bonus: "), 
						0.965F*height-4.5F*text.getHeight("A"));
				text.setColor(Color.WHITE);
				text.draw(game.batch, " to resources", 
						0.05F*width + text.getWidth("Gem bonus: +" + ((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo()))/10-((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo())/10)%5))), 
						0.965F*height-4.5F*text.getHeight("A"));
				///
				if(score > INF.loonRecord && CurPR.getCurPlanet().equals(INF.planetLoon)){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "New record - " + score + " !", 
							0.5F*(width-text.getWidth("New record - " + score + " !")), 
							0.965F*height-7.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}else if(score > INF.emionRecord && CurPR.getCurPlanet().equals(INF.planetEmion)){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "New record - " + score + " !", 
							0.5F*(width-text.getWidth("New record - " + score + " !")), 
							0.965F*height-7.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}else if(score > INF.dertenRecord && CurPR.getCurPlanet().equals(INF.planetDerten)){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "New record - " + score + " !", 
							0.5F*(width-text.getWidth("New record - " + score + " !")), 
							0.965F*height-7.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}else if(score > INF.unarRecord && CurPR.getCurPlanet().equals(INF.planetUnar)){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "New record - " + score + " !", 
							0.5F*(width-text.getWidth("New record - " + score + " !")), 
							0.965F*height-7.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}else if(score > INF.ingmarRecord && CurPR.getCurPlanet().equals(INF.planetIngmar)){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "New record - " + score + " !", 
							0.5F*(width-text.getWidth("New record - " + score + " !")), 
							0.965F*height-7.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}
			}else{
				text.draw(game.batch, "Игра закончена!", 
						0.5F*(width-text.getWidth("Игра закончена!")), 
						0.965F*height);
				///
				text.draw(game.batch, "Счёт: ", 
						0.05F*width, 
						0.965F*height-3.0F*text.getHeight("A"));
				text.setColor(Color.SKY);
				text.draw(game.batch, (int)(score)+"", 
						0.05F*width + text.getWidth("Счёт: "), 
						0.965F*height-3.0F*text.getHeight("A"));
				text.setColor(Color.WHITE);
				///
				text.draw(game.batch, "Бонус: ", 
						0.05F*width, 
						0.965F*height-4.5F*text.getHeight("A"));
				text.setColor(Color.SKY);
				text.draw(game.batch, "+" + ((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo()))/10-((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo())/10)%5)), 
						0.05F*width + text.getWidth("Бонус: "), 
						0.965F*height-4.5F*text.getHeight("A"));
				text.setColor(Color.WHITE);
				text.draw(game.batch, " к ресурсам", 
						0.05F*width + text.getWidth("Бонус: +" + ((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo()))/10-((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo())/10)%5))), 
						0.965F*height-4.5F*text.getHeight("A"));
				///
				if(score > INF.loonRecord && CurPR.getCurPlanet().equals(INF.planetLoon)){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "Новый рекорд - " + score + " !", 
							0.5F*(width-text.getWidth("Новый рекорд - " + score + " !")), 
							0.965F*height-7.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}else if(score > INF.emionRecord && CurPR.getCurPlanet().equals(INF.planetEmion)){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "Новый рекорд - " + score + " !", 
							0.5F*(width-text.getWidth("Новый рекорд - " + score + " !")), 
							0.965F*height-7.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}else if(score > INF.dertenRecord && CurPR.getCurPlanet().equals(INF.planetDerten)){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "Новый рекорд - " + score + " !", 
							0.5F*(width-text.getWidth("Новый рекорд - " + score + " !")), 
							0.965F*height-7.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}else if(score > INF.unarRecord && CurPR.getCurPlanet().equals(INF.planetUnar)){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "Новый рекорд - " + score + " !", 
							0.5F*(width-text.getWidth("Новый рекорд - " + score + " !")), 
							0.965F*height-7.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}else if(score > INF.ingmarRecord && CurPR.getCurPlanet().equals(INF.planetIngmar)){
					text.setColor(Color.YELLOW);
					text.draw(game.batch, "Новый рекорд - " + score + " !", 
							0.5F*(width-text.getWidth("Новый рекорд - " + score + " !")), 
							0.965F*height-7.5F*text.getHeight("A"));
					text.setColor(Color.WHITE);
				}
			}
		}else{
			if(!INF.lngRussian){
				text.draw(game.batch, "Level is not completed.", 
						0.5F*(width-text.getWidth("Level is not completed.")), 
						0.965F*height);
				///
				text.draw(game.batch, "Score: ", 
						0.05F*width, 
						0.965F*height-3.0F*text.getHeight("A"));
				text.setColor(Color.SKY);
				text.draw(game.batch, "" + (int)(score), 
						0.05F*width + text.getWidth("Score: "), 
						0.965F*height-3.0F*text.getHeight("A"));
				text.setColor(Color.WHITE);
				///
			}else{
				text.draw(game.batch, "Уровень не завершен.", 
						0.5F*(width-text.getWidth("Уровень не завершен.")), 
						0.965F*height);
				///
				text.draw(game.batch, "Счёт: ", 
						0.05F*width, 
						0.965F*height-3.0F*text.getHeight("A"));
				text.setColor(Color.SKY);
				text.draw(game.batch, "" + (int)(score), 
						0.05F*width + text.getWidth("Счёт: "), 
						0.965F*height-3.0F*text.getHeight("A"));
				text.setColor(Color.WHITE);
				///
			}
		}
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		buttonListener();
		
	}
	
	private void buttonListener(){
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, true)){
			/***/
			if(isVictory){
				if((INF.planetLevel == 9 || INF.planetLevel == 19 || INF.planetLevel == 29 || INF.planetLevel == 39 || INF.planetLevel == 49) && !isEndlessMode)
					INF.facts++;
				INF.planetLevel++;
				INF.money += (bonusTime*1.25F - (bonusTime*1.25F)%5);
				INF.fuel += (bonusTime*1.25F - (bonusTime*1.25F)%5);
				INF.metal += (bonusTime*1.25F - (bonusTime*1.25F)%5);
				INF.money += ((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F) - ((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F))%5);
				INF.fuel += ((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F) - ((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F))%5);
				INF.metal += ((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F) - ((int)(CurPR.getCurPlanet().getFuelTo()/1.5F + INF.planetLevel*1.25F))%5);
			    if(!isEndlessMode) {
                    if (INF.planetLevel == 1) {
                        gpgs.unlockAchievement(0);
                    } else if (INF.planetLevel == 10) {
                        gpgs.unlockAchievement(1);
                    } else if (INF.planetLevel == 20) {
                        gpgs.unlockAchievement(2);
                    } else if (INF.planetLevel == 30) {
                        gpgs.unlockAchievement(3);
                    } else if (INF.planetLevel == 40) {
                        gpgs.unlockAchievement(4);
                    }
                }
			}else if(isEndlessMode){
				INF.money += ((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo()))/10-((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo())/10)%5));
				INF.fuel += ((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo()))/10-((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo())/10)%5));
				INF.metal += ((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo()))/10-((int)((score) + 5*(CurPR.getCurPlanet().getFuelTo())/10)%5));
				if(score > INF.loonRecord && CurPR.getCurPlanet().equals(INF.planetLoon)){
					gpgs.submitScore(score, 0);
					INF.loonRecord = score;
				}else if(score > INF.emionRecord && CurPR.getCurPlanet().equals(INF.planetEmion)){
					gpgs.submitScore(score, 1);
					INF.emionRecord = score;
				}else if(score > INF.dertenRecord && CurPR.getCurPlanet().equals(INF.planetDerten)){
					gpgs.submitScore(score, 2);
					INF.dertenRecord = score;
				}else if(score > INF.unarRecord && CurPR.getCurPlanet().equals(INF.planetUnar)){
					gpgs.submitScore(score, 3);
					INF.unarRecord = score;
				}else if(score > INF.ingmarRecord && CurPR.getCurPlanet().equals(INF.planetIngmar)){
					gpgs.submitScore(score, 4);
					INF.ingmarRecord = score;
				}
			}
			INF.fuel -= CurPR.getCurPlanet().getFuelTo();
			INF.launch++;
			INF.currentPlanet = "null";
			INF.currentRocket = "null";
			INF.isBombActive = false;
			INF.isColoumnActive = false;
			INF.isLineActive = false;
			INF.isTimeActive = false;
			/***/
			adMob.hide();
			/***/
			game.setScreen(new GameScreen(game));
			this.dispose();
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
		show();
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