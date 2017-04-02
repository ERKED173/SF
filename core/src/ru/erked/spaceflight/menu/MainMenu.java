package ru.erked.spaceflight.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.AdMob;
import ru.erked.spaceflight.AndroidOnlyInterface;
import ru.erked.spaceflight.game.TutorialScreen;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.tech.SFButtonS;

public class MainMenu implements Screen {
	
	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	public static final float backgroundTentionIndex = width/256;
	
	private final ru.erked.spaceflight.StartSFlight game;
	public static Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/Deliberate Thought.mp3"));
	private static ru.erked.spaceflight.tech.SFFont text;
	private static ru.erked.spaceflight.tech.SFFont textBlack;
	
	public static boolean isFirstScreen = true;
	
	public static Sprite backgroundSprite;
	private float backgroundX;
	private float backgroundY;
	
	public static Sprite planet1Sprite;
	public static float planet1X;
	public static float planet1Y;
	
	public static Sprite planet2Sprite;
	public static float planet2X;
	public static float planet2Y;
	
	public static Sprite cometSprite;
	public static float cometX;
	public static float cometY;
	
	private SFButtonS btnStart;
	private SFButtonS btnAbout;
	private SFButtonS btnExit;
	private SFButtonS btnEnglish;
	private SFButtonS btnRussian;
	private SFButtonS btnFaq;
	private Screen options;

	private Sprite blackAlpha = ru.erked.spaceflight.random.RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTransGame;
	private boolean isTransAbout;
	private boolean isTransOptions;
	private boolean isTransExit;
	
	private ru.erked.spaceflight.controllers.SFIn controller;
	private AndroidOnlyInterface aoi;
	private ru.erked.spaceflight.Data data;

	private AdMob admob;

	public MainMenu(final ru.erked.spaceflight.StartSFlight game){
		this.game = game;
		aoi = game.aoi;
		data = game.data;
		admob = game.adMob;
	}

	@Override
	public void show() {
		
		resourcesCheck();
		
		options = new MainMenu(game);
		controller = new ru.erked.spaceflight.controllers.SFIn();
		Gdx.input.setInputProcessor(controller);
		
		text = new ru.erked.spaceflight.tech.SFFont(10, Color.WHITE);
		textBlack = new ru.erked.spaceflight.tech.SFFont(10, Color.BLACK);
		
		music.setLooping(true);
		music.setVolume(1.0f);
		music.play();
		
		isTransGame = false;
		isTransAbout = false;
		isTransOptions = false;
		isTransExit = false;

		backgroundSprite = ru.erked.spaceflight.random.RES.atlas.createSprite("menuBackground");
		backgroundX = 0.0F;
		backgroundY = (-1)*(256*backgroundTentionIndex)/2 + height/2;
		backgroundSprite.setBounds(backgroundX, backgroundY, width, 256*backgroundTentionIndex);
		
		planet1Sprite = ru.erked.spaceflight.random.RES.atlas.createSprite("menuPlanet1");
		if(isFirstScreen){
			planet1X = 0.0F - 0.2F*width;
			planet1Y = height - 0.35F*height;
			planet1Sprite.setBounds(planet1X, planet1Y, 2*width, 2*width);
			planet1Sprite.setRotation(45.0F);
		}else{
			planet1Sprite.setBounds(planet1X, planet1Y, 2*width, 2*width);
		}

		planet2Sprite = ru.erked.spaceflight.random.RES.atlas.createSprite("menuPlanet2");
		if(isFirstScreen){
			planet2X = 1.05F*width + planet2Sprite.getWidth();
			planet2Y = 0.05F*height;
			planet2Sprite.setBounds(planet2X, planet2Y, 0.15F*width, 0.15F*width);
		}else{
			planet2Sprite.setBounds(planet2X, planet2Y, 0.15F*width, 0.15F*width);
		}

		cometSprite = ru.erked.spaceflight.random.RES.atlas.createSprite("menuComet");
		if(isFirstScreen){
			cometX = 0.0F;
			cometY = height;
			cometSprite.setBounds(cometX, cometY, 0.15F*width, 0.15F*width);
		}else{
			cometSprite.setBounds(cometX, cometY, 0.15F*width, 0.15F*width);
		}
		
		if(!INF.lngRussian){
			btnStart = new SFButtonS("startGameI", "startGameA", 0.4F*width, 0.3F*width, 0.5F*height, 5.56521F, 1.0F, -1);
			btnAbout = new SFButtonS("aboutI", "aboutA", 0.26367F*width, 0.368165F*width, 0.5F*height - 1.1F*btnStart.getHeight(), 3.66847F, 1.0F, -1);
			btnExit = new SFButtonS("exitI", "exitA", 0.23437F*width, 0.38281F*width, 0.5F*height - 2.2F*btnStart.getHeight(), 3.26086F, 1.0F, -1);
		}else{
			btnStart = new SFButtonS("startGameRI", "startGameRA", 0.4F*width, 0.3F*width, 0.5F*height, 5.56521F, 1.0F, -1);
			btnAbout = new SFButtonS("aboutRI", "aboutRA", 0.26367F*width, 0.368165F*width, 0.5F*height - 1.1F*btnStart.getHeight(), 3.66847F, 1.0F, -1);
			btnExit = new SFButtonS("exitRI", "exitRA", 0.23437F*width, 0.38281F*width, 0.5F*height - 2.2F*btnStart.getHeight(), 3.26086F, 1.0F, -1);
		}
		btnEnglish = new SFButtonS("englishI", "englishA", 0.05F*width, 0.025F*width, 0.1F*height, 1.0F, 1.0F, -1);
		btnRussian = new SFButtonS("russianI", "russianA", 0.05F*width, 0.1F*width, 0.1F*height, 1.0F, 1.0F, -1);
		btnFaq = new SFButtonS("faqI", "faqA", 0.05F*width, 0.175F*width, 0.1F*height, 1.0F, 1.0F, -1);
		
		blackAlpha.setBounds(0.0F, 0.0F, width, height);
		blackAlpha.setAlpha(1.0F);
		
	}

	@Override
	public void render(float delta) {
		INF.elapsedTime++;

		if(INF.elapsedTime % 120 == 0) admob.hide();

		if(alp>0.0F && (!isTransGame && !isTransAbout && !isTransOptions && !isTransExit)){
			blackAlpha.setAlpha(alp);
			alp-=0.05F;
		}else if(!isTransGame && !isTransAbout && !isTransOptions && !isTransExit){
			blackAlpha.setAlpha(0.0F);
			alp = 0.0F;
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		planet1Sprite.setOriginCenter();
		planet1Sprite.rotate(0.0175F);
		
		planet2Sprite.setOriginCenter();
		planet2Sprite.rotate(-0.01F);
		planet2Sprite.setY((float)(planet2Sprite.getY() + 0.03F));
		planet2Sprite.setX(planet2Sprite.getX() - 0.3F);
		if(planet2Sprite.getX() < (0-1)*width){
			planet2Sprite.setX(1.05F*width + planet2Sprite.getWidth());
			planet2Sprite.setY(0.05F*height);
		}
		
		cometSprite.setOrigin(2*width, 2*height);
		cometSprite.rotate(0.25F);
		
		game.batch.begin();
		
		backgroundSprite.draw(game.batch);
		planet1Sprite.draw(game.batch);
		planet2Sprite.draw(game.batch);
		cometSprite.draw(game.batch);
		
		if(controller.isOn(btnStart.getX(), btnStart.getY(), btnStart.getWidth(), btnStart.getHeight(), true)){
			btnStart.setActiveMode(true);
		}else{
			btnStart.setActiveMode(false);
		}
		btnStart.getSprite().draw(game.batch);
		
		if(controller.isOn(btnAbout.getX(), btnAbout.getY(), btnAbout.getWidth(), btnAbout.getHeight(), true)){
			btnAbout.setActiveMode(true);
		}else{
			btnAbout.setActiveMode(false);
		}
		btnAbout.getSprite().draw(game.batch);
		
		if(controller.isOn(btnExit.getX(), btnExit.getY(), btnExit.getWidth(), btnExit.getHeight(), true)){
			btnExit.setActiveMode(true);
		}else{
			btnExit.setActiveMode(false);
		}
		btnExit.getSprite().draw(game.batch);
		if(controller.isOn(btnFaq.getX(), btnFaq.getY(), btnFaq.getWidth(), btnFaq.getHeight(), true)){
			btnFaq.setActiveMode(true);
		}else{
			btnFaq.setActiveMode(false);
		}
		btnFaq.getSprite().draw(game.batch);
		if(controller.isOn(btnRussian.getX(), btnRussian.getY(), btnRussian.getWidth(), btnRussian.getHeight(), true) || INF.lngRussian){
			btnRussian.setActiveMode(true);
		}else{
			btnRussian.setActiveMode(false);
		}
		btnRussian.getSprite().draw(game.batch);
		
		if(controller.isOn(btnEnglish.getX(), btnEnglish.getY(), btnEnglish.getWidth(), btnEnglish.getHeight(), true) || !INF.lngRussian){
			btnEnglish.setActiveMode(true);
		}else{
			btnEnglish.setActiveMode(false);
		}
		btnEnglish.getSprite().draw(game.batch);
		
		textBlack.draw(game.batch, "Space Flight", 0.505F*width - 0.5F*text.getWidth("Space Flight"), 0.96F*height);
		text.draw(game.batch, "Space Flight", 0.5F*width - 0.5F*text.getWidth("Space Flight"), 0.965F*height);
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		if(controller.isClicked(btnExit.getX(), btnExit.getY(), btnExit.getWidth(), btnExit.getHeight(), true, true) || isTransExit){
			isTransExit = true;
			isTransAbout = false;
			isTransGame = false;
			isTransOptions = false;
			if(alp>=1.0F){
				game.dispose();
				music.pause();
				Gdx.app.exit();
			}else{
				blackAlpha.setAlpha(alp);
				alp+=0.05F;
			}
		}
		if(controller.isClicked(btnAbout.getX(), btnAbout.getY(), btnAbout.getWidth(), btnAbout.getHeight(), true, true) || isTransAbout){
			isTransExit = false;
			isTransAbout = true;
			isTransGame = false;
			isTransOptions = false;
			if(alp>=1.0F){
				this.dispose();
				game.setScreen(new AboutScreen(game));
				alp = 1.0F;
			}else{
				blackAlpha.setAlpha(alp);
				alp+=0.05F;
			}
		}
		if(controller.isClicked(btnStart.getX(), btnStart.getY(), btnStart.getWidth(), btnStart.getHeight(), true, true) || isTransGame){
			isTransExit = false;
			isTransAbout = false;
			isTransGame = true;
			isTransOptions = false;
			if(alp>=1.0F){
				alp = 1.0F;
				if(INF.isFirstLogin){
					game.setScreen(new ru.erked.spaceflight.game.GameScreen(game));
					this.dispose();	
				}else{
					game.setScreen(new TutorialScreen(game));
					this.dispose();
				}
			}else{
				blackAlpha.setAlpha(alp);
				alp+=0.05F;
			}
		}
		if(controller.isClicked(btnRussian.getX(), btnRussian.getY(), btnRussian.getWidth(), btnRussian.getHeight(), true, true)){
			INF.lngRussian = true;
			this.dispose();
			game.setScreen(new ru.erked.spaceflight.menu.TechnicScreen(game, options, 0.1F));
		}

		if(controller.isClicked(btnEnglish.getX(), btnEnglish.getY(), btnEnglish.getWidth(), btnEnglish.getHeight(), true, true)){
			INF.lngRussian = false;
			this.dispose();
			game.setScreen(new ru.erked.spaceflight.menu.TechnicScreen(game, options, 0.1F));
		}
		if(controller.isClicked(btnFaq.getX(), btnFaq.getY(), btnFaq.getWidth(), btnFaq.getHeight(), true, true)){
			if(INF.isFirstLogin){
				this.dispose();
				game.setScreen(new TutorialScreen(game));
			}else{
				if(!INF.lngRussian)
					aoi.makeToast("Will be available after you start the game");
				else
					aoi.makeToast("Будет доступно после начала игры");
			}
		}
	}
	
	/**
	private boolean logPassCheck(){
		boolean check = true;
		if(!(INF.login.length() >= 5 && INF.login.length() <= 10)){
			check = false;
		}else{
			for(int i=0;i<INF.login.length();i++){
				if(!(
						(int)INF.login.charAt(i) >= 97 || 
						(int)INF.login.charAt(i) <= 122 ||
						(int)INF.login.charAt(i) >= 48 ||
						(int)INF.login.charAt(i) <= 57 ||
						(int)INF.login.charAt(i) >= 65 ||
						(int)INF.login.charAt(i) <= 90
						))
				{
					check = false;
				}
			}
		}
		if(!(INF.password.length() >= 5 && INF.password.length() <= 10)){
			check = false;
		}else{
			for(int i=0;i<INF.password.length();i++){
				if(!(
						(int)INF.password.charAt(i) >= 97 || 
						(int)INF.password.charAt(i) <= 122 ||
						(int)INF.password.charAt(i) >= 48 ||
						(int)INF.password.charAt(i) <= 57 ||
						(int)INF.password.charAt(i) >= 65 ||
						(int)INF.password.charAt(i) <= 90
						))
				{
					check = false;
				}
			}
		}
		return check;
	}
	*/
	
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
		data.loadSF();
	}

	@Override
	public void hide() {
		data.saveSF();
	}
	
	@Override
	public void dispose() {
		text.dispose();
		data.saveSF();
	}

}