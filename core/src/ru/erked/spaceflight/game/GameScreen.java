package ru.erked.spaceflight.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector3;

import ru.erked.spaceflight.Data;
import ru.erked.spaceflight.StartSFlight;
import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;
import ru.erked.spaceflight.tech.CurPR;
import ru.erked.spaceflight.tech.ParticleFX;
import ru.erked.spaceflight.tech.Rocket;
import ru.erked.spaceflight.tech.SFButtonA;
import ru.erked.spaceflight.tech.SFButtonS;
import ru.erked.spaceflight.tech.SFFont;

public class GameScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private SpriteBatch batch;
	public static OrthographicCamera camera;
	private ru.erked.spaceflight.controllers.SFIn controller;
	private RandomXS128 rand = new RandomXS128();
	
	public static boolean isFromMenu = true;
	
//Background
	private Sprite backgroundSprite;
	private float backgroundX;
	private float backgroundY;
	private final float backgroundTentionIndex = (float)2*width/2560.0F;
	
//Buildings
	private SFButtonS hangar;
	private SFButtonS analytic;	
	private SFButtonS control;	
	private SFButtonS gunStore;	
	private SFButtonS library;	
	
//FuelFactory
	private Sprite[] fuelFactorySpriteI;
	private Sprite[] fuelFactorySpriteA;
	private float fuelFactoryWidthI;
	private float fuelFactoryWidthA;
	private float fuelFactoryHeightI;
	private float fuelFactoryHeightA;
	private float fuelFactoryXI;
	private float fuelFactoryXA;
	private float fuelFactoryYI;
	private float fuelFactoryYA;
	private float fuelFactoryTentionIndex;
//CoinFactory
	private Sprite[] coinFactorySprite;
	private Sprite coinFactorySpriteActive;
	private float coinFactoryWidth;
	private float coinFactoryHeight;
	private float coinFactoryX;
	private float coinFactoryY;
	private float coinFactoryTentionIndex;
//MetalFactory
	private Sprite metalFactoryActive;
	private Sprite[] metalFactorySprite;
	private float metalFactoryWidth;
	private float metalFactoryHeight;
	private float metalFactoryX;
	private float metalFactoryY;
	private float metalFactoryTentionIndex;
	
//Scroll & Scale
	private static float camX;
	private static float camY;
	private static float prevDragX;
	private static float prevDragY;
	public static float curViewWidth;
	public static float curViewHeight;
	private SFButtonA bPlus;
	private SFButtonA bMinus;
	
//Buttons
	private SFButtonA btnMN;
	private SFButtonA btnPlus;
	private SFButtonA btnHome;
	private SFButtonA btnAchi;

//Resources
	private Sprite moneySprite;
	private Sprite fuelSprite;
	private Sprite metalSprite;
	private float moneyX;
	private float moneyY;
	private float moneyWidth;
	private float moneyHeight;
	private static SFFont text;
	private static SFFont textBtn;
	private Sprite line;
	private Sprite cosmocoinLine;
	private Sprite fuelLine;
	private Sprite metalLine;
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTransGame;
	
	private static int schFuel=0;
	private static int schCoin=0;
	private static int schMetal=0;
	
	//Rockets
	private Rocket rocket;
	private SFButtonS rocketS;
	private SFButtonS rocketBall;
	private SFButtonS rocketCircle;
	private SFButtonS rocketBasic;
	private SFButtonS rocketKinetic;
	private SFButtonS rocketDelta;
	private SFButtonS rocketInfinity;
	
	private ArrayList<ParticleFX> smoke;
	static ArrayList<ParticleFX> clouds;
	
	private Data data;
	private ru.erked.spaceflight.GPGS gpgs;
	private ru.erked.spaceflight.AdMob admob;
	
	public GameScreen(final StartSFlight game){
		this.game = game;
		data = game.data;
		gpgs = game.gpgs;
		admob = game.adMob;
	}
	
	@Override
	public void show() {
		
		batch = new SpriteBatch();
		
		controller = new ru.erked.spaceflight.controllers.SFIn();
		MainMenu.music.play();
		
		rocketsPreInit();
		rocket = CurPR.getCurRocket();
		if(rocket != null){
			if(rocket.equals(INF.rocketBall)){
				rocketS = rocketBall;
			}else if(rocket.equals(INF.rocketCircle)){
				rocketS = rocketCircle;
			}else if(rocket.equals(INF.rocketBasic)){
				rocketS = rocketBasic;
			}else if(rocket.equals(INF.rocketKinetic)){
				rocketS = rocketKinetic;
			}else if(rocket.equals(INF.rocketDelta)){
				rocketS = rocketDelta;
			}else if(rocket.equals(INF.rocketInfinity)){
				rocketS = rocketInfinity;
			}
		}
		
		backgroundSprite = RES.atlas.createSprite("spaceport");
		backgroundX = 0.0F;
		backgroundY = 0.0F;
		backgroundSprite.setBounds(backgroundX, backgroundY, width*2, backgroundTentionIndex*2560.0F);
		
		if(curViewWidth >= 0.75F*width && curViewWidth <= 0.85F*backgroundSprite.getWidth())
			camera = new OrthographicCamera(curViewWidth, curViewHeight);
		else{
			camera = new OrthographicCamera(width, height);
			curViewWidth = width;
			curViewHeight = height;
		}
		if(camX == 0 && camY == 0){
			camX = width;
			camY = height;
		}
		camera.position.set(new Vector3(camX, camY, 0));
		
		text = new SFFont(45, Color.WHITE, 1, 2, Color.BLACK);
		textBtn = new SFFont(38, Color.WHITE, 1, 1, Color.BLACK);
		text.setScale(curViewWidth/width);
		textBtn.setScale(curViewWidth/width);
			
		moneyInit();
		fuelFactoryInit();
		coinFactoryInit();
		metalFactoryInit();
		rocketsInit();
		
		btnMN = new SFButtonA("buttonI", "buttonA", 0.132F*width, 0.86F*width, 0.01F*height, 1.5F, 2.0F, -1);
		btnMN.getSprite().setColor(Color.CYAN);
		btnMN.setActiveMode(true);
		btnMN.getSprite().setColor(Color.CYAN);
		btnMN.setActiveMode(false);
		
		btnPlus = new SFButtonA("buttonI", "buttonA", 0.132F*width, 0.86F*width - 1.1F*btnMN.getWidth(), 0.01F*height, 1.5F, 2.0F, -1);
		btnPlus.getSprite().setColor(Color.RED);
		btnPlus.setActiveMode(true);
		btnPlus.getSprite().setColor(Color.RED);
		btnPlus.setActiveMode(false);
	
		bPlus = new SFButtonA("scrollPlusI", "scrollPlusA", 0.075F*width, 0.8885F*width, 0.32F*height, 1.0F, 1.0F, -1);
		bMinus = new SFButtonA("scrollMinusI", "scrollMinusA", 0.075F*width, 0.8885F*width, 0.175F*height, 1.0F, 1.0F, -1);
		
		btnHome = new SFButtonA("homeI", "homeA", 0.075F*width, 0.015F*width, 0.01F*height, 1.0F, 1.0F, -1);
		btnAchi = new SFButtonA("achievementsI", "achievementsA", 0.075F*width, 0.015F*width, 0.01F*height + 0.08F*width, 1.0F, 1.0F, -1);

		hangar = new SFButtonS("angarI", "angarA", 0.5F*width, 0.150F*backgroundSprite.getWidth(), 0.625F*backgroundSprite.getHeight(), 1.33333F, 1.6129F, -1);		
		analytic = new SFButtonS("analyticI", "analyticA", 0.2F*width, 0.6F*backgroundSprite.getWidth(), 0.8F*backgroundSprite.getHeight(), 0.82089F, 1.81818F, -1);		
		control = new SFButtonS("controlI", "controlA", 0.125F*width, 0.469F*backgroundSprite.getWidth(), 0.665F*backgroundSprite.getHeight(), 0.23846F, 2.58064F, -1);		
		gunStore = new SFButtonS("gunStoreI", "gunStoreA", 0.35F*width, 0.461F*backgroundSprite.getWidth(), 0.45F*backgroundSprite.getHeight(), 0.92523F, 1.68686F, -1);		
		library = new SFButtonS("libraryI", "libraryA", 0.325F*width, 0.146F*backgroundSprite.getWidth(), 0.45F*backgroundSprite.getHeight(), 1.27027F, 1.63829F, -1);	
		
		smoke = new ArrayList<ParticleFX>();
		if(clouds == null)
			clouds = new ArrayList<ParticleFX>();
		
		isTransGame = false;
		blackAlpha.setBounds(0.0F, 0.0F, backgroundSprite.getWidth(), backgroundSprite.getHeight());
		blackAlpha.setAlpha(1.0F);
	}

	@Override
	public void render(float delta) {
		INF.elapsedTime++;
		resourcesCheck();
		
		if(alp>0.0F && (!isTransGame)){
			blackAlpha.setAlpha(alp);
			alp-=0.05F;
		}else if(!isTransGame){
			blackAlpha.setAlpha(0.0F);
			alp = 0.0F;
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		touchUpdate();
		
		moneyCoords();
		
		camX = camera.position.x;
		camY = camera.position.y;
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		
		backgroundSprite.draw(batch);
		
		drawBuildings();
		drawRockets();
		drawButtons(delta);
		drawMoney();
		
		blackAlpha.draw(batch);
		
		batch.end();
		
		buttonListener();
		
	}
	
	private void moneyCoords(){
		moneyWidth = 0.05F*curViewWidth;
		moneyHeight = moneyWidth;
		moneyX = camera.position.x - moneyWidth - (curViewWidth/2 - 0.065F*curViewWidth);
		moneyY = camera.position.y - moneyHeight + (curViewHeight/2 - 0.015F*curViewHeight);
		moneySprite.setBounds(moneyX, moneyY, moneyWidth, moneyHeight);
		fuelSprite.setBounds(moneyX + 10.0F*moneyWidth, moneyY, moneyWidth, moneyHeight);
		metalSprite.setBounds(moneyX + 5.0F*moneyWidth, moneyY, moneyWidth, moneyHeight);
		
		cosmocoinLine.setBounds(moneySprite.getX() + 1.45F*moneySprite.getWidth(), moneySprite.getY() + 0.25F*moneySprite.getHeight(), ((float)INF.money/(float)INF.moneyFull)*0.825F*line.getWidth() + 0.005F*curViewWidth, 0.5F*moneyHeight);
		fuelLine.setBounds(fuelSprite.getX() + 1.45F*fuelSprite.getWidth(), fuelSprite.getY() + 0.25F*fuelSprite.getHeight(), ((float)INF.fuel/(float)INF.fuelFull)*0.825F*line.getWidth() + 0.005F*curViewWidth, 0.5F*moneyHeight);
		metalLine.setBounds(metalSprite.getX() + 1.45F*metalSprite.getWidth(), metalSprite.getY() + 0.25F*metalSprite.getHeight(), ((float)INF.metal/(float)INF.metalFull)*0.825F*line.getWidth() + 0.005F*curViewWidth, 0.5F*moneyHeight);
	
		line.setBounds(moneyX + 1.15F*moneyWidth, moneyY, moneyWidth/0.28125F, moneyWidth);
	}	

	private void moneyInit(){
		moneySprite = RES.atlas.createSprite("cosmocoin");
		fuelSprite = RES.atlas.createSprite("fuel");
		metalSprite = RES.atlas.createSprite("metal", -1);
		
		line = RES.atlas.createSprite("line");
		cosmocoinLine = RES.atlas.createSprite("cosmocoinLine");
		fuelLine = RES.atlas.createSprite("fuelLine");
		metalLine = RES.atlas.createSprite("metalLine");
		
		moneyWidth = 0.05F*width;
		moneyHeight = moneyWidth;
		moneyX = camera.position.x - moneyWidth - (width/2 - 0.065F*width);
		moneyY = camera.position.y - moneyHeight + (height/2 - 0.015F*height);
		
		moneySprite.setBounds(moneyX, moneyY, moneyWidth, moneyHeight);
		fuelSprite.setBounds(moneyX + moneyWidth, moneyY, moneyWidth, moneyHeight);
		metalSprite.setBounds(moneyX + 2.0F*moneyWidth, moneyY, moneyWidth, moneyHeight);
		
		line.setBounds(moneyX + 1.1F*moneyWidth, moneyY - 2.1F*moneyHeight, moneyWidth/0.28125F, moneyWidth);
		cosmocoinLine.setBounds(moneySprite.getX() + 1.5F*moneySprite.getWidth(), moneySprite.getY() + moneySprite.getHeight(), 0.5F*moneyWidth, 0.5F*moneyHeight);
		fuelLine.setBounds(fuelSprite.getX() + 1.5F*fuelSprite.getWidth(), fuelSprite.getY() + fuelSprite.getHeight(), 0.5F*moneyWidth, 0.5F*moneyHeight);
		metalLine.setBounds(metalLine.getX() + 1.5F*metalLine.getWidth(), metalLine.getY() + metalLine.getHeight(), 0.5F*moneyWidth, 0.5F*moneyHeight);
		
	}
	private void fuelFactoryInit(){
		fuelFactorySpriteI = new Sprite[4];
		fuelFactorySpriteA = new Sprite[4];
		for(int i=0;i<4;i++){
			fuelFactorySpriteI[i] = RES.atlas.createSprite("fuelFactoryI", i+1);
			fuelFactoryTentionIndex = (float)fuelFactorySpriteI[i].getWidth()/fuelFactorySpriteI[i].getHeight();
			fuelFactoryWidthI = 0.25F*width;
			fuelFactoryHeightI = (float)fuelFactoryWidthI/fuelFactoryTentionIndex;
			fuelFactoryXI = 0.058F*backgroundSprite.getWidth();
			fuelFactoryYI = 0.335F*backgroundSprite.getHeight();
			fuelFactorySpriteI[i].setBounds(fuelFactoryXI, fuelFactoryYI, fuelFactoryWidthI, fuelFactoryHeightI);
			///
			fuelFactorySpriteA[i] = RES.atlas.createSprite("fuelFactoryA", i+1);
			fuelFactoryWidthA = 0.5F*width;
			fuelFactoryHeightA = (float)fuelFactoryWidthA/fuelFactoryTentionIndex;
			fuelFactoryXA = fuelFactoryXI - 0.5F*fuelFactoryWidthI;
			fuelFactoryYA = fuelFactoryYI - 0.5F*fuelFactoryHeightI;
			fuelFactorySpriteA[i].setBounds(fuelFactoryXA, fuelFactoryYA, fuelFactoryWidthA, fuelFactoryHeightA);
		}
	}
	private void coinFactoryInit(){
		coinFactorySprite = new Sprite[36];
		for(int i=0;i<36;i++){
			coinFactorySprite[i] = RES.atlas.createSprite("coinFactory", i+1);
			coinFactoryTentionIndex = (float)coinFactorySprite[i].getWidth()/coinFactorySprite[i].getHeight();
			coinFactoryWidth = 0.25F*width;
			coinFactoryHeight = (float)coinFactoryWidth/coinFactoryTentionIndex;
			coinFactoryX = 0.058F*backgroundSprite.getWidth() + 1.225F*coinFactoryWidth;
			coinFactoryY = 0.32F*backgroundSprite.getHeight();
			coinFactorySprite[i].setBounds(coinFactoryX, coinFactoryY, coinFactoryWidth, coinFactoryHeight);
			///
			coinFactorySpriteActive = RES.atlas.createSprite("coinFactoryA");
			coinFactorySpriteActive.setBounds(coinFactoryX - 0.5F*coinFactoryWidth, coinFactoryY - 0.5F*coinFactoryHeight, 2.0F*coinFactoryWidth, 2.0F*coinFactoryHeight);
			
		}
	}
	private void metalFactoryInit(){
		metalFactorySprite = new Sprite[24];
		for(int i=0;i<24;i++){
			metalFactorySprite[i] = RES.atlas.createSprite("metalFactory", i+1);
			metalFactoryTentionIndex = (float)metalFactorySprite[i].getWidth()/metalFactorySprite[i].getHeight();
			metalFactoryWidth = 0.25F*width;
			metalFactoryHeight = (float)metalFactoryWidth/metalFactoryTentionIndex;
			metalFactoryX = 0.07F*backgroundSprite.getWidth() + 2.35F*coinFactoryWidth;
			metalFactoryY = 0.315F*backgroundSprite.getHeight();
			metalFactorySprite[i].setBounds(metalFactoryX, metalFactoryY, metalFactoryWidth, metalFactoryHeight);
		}
		metalFactoryActive = RES.atlas.createSprite("metalFactoryA");
		metalFactoryActive.setBounds(metalFactoryX - 0.5F*metalFactoryWidth, metalFactoryY - 0.5F*metalFactoryHeight, 2.0F*metalFactoryWidth, 2.0F*metalFactoryHeight);
	}
	private void rocketsPreInit(){
		rocketBall = new SFButtonS("rocketBallI", "rocketBallA", 0.05F*height, 0.115F*width, 0.5F*height, 0.52727F, 3.0F, -1);
		rocketCircle = new SFButtonS("rocketCircleI", "rocketCircleA", 0.05F*height, 0.265F*width, 0.5F*height, 0.52727F, 3.0F, -1);
		rocketBasic = new SFButtonS("rocketBasicI", "rocketBasicA", 0.05926F*height, 0.415F*width, 0.5F*height, 0.32967F, 3.0F, -1);
		rocketKinetic = new SFButtonS("rocketKineticI", "rocketKineticA", 0.03884F*height, 0.135F*width, 0.5F*height, 0.25F, 3.0F, -1);
		rocketDelta = new SFButtonS("rocketDeltaI", "rocketDeltaA", 0.02F*height, 0.135F*width, 0.5F*height, 0.1796875F, 3.0F, -1);
		rocketInfinity = new SFButtonS("rocketInfinityI", "rocketInfinityA", 0.02F*height, 0.135F*width, 0.5F*height, 0.1751412F, 3.0F, -1);
	}
	private void rocketsInit(){
		/***/
		rocketBall.setActiveMode(false);
		rocketBall.setX(0.7535F*backgroundSprite.getWidth());
		rocketBall.setY(0.6F*backgroundSprite.getHeight());
		rocketBall.setWidth(0.04F*backgroundSprite.getHeight());
		rocketBall.setHeight(rocketBall.getWidth()/rocketBall.getAspectRatio());		
		/***/
		rocketCircle.setActiveMode(false);
		rocketCircle.setX(0.7535F*backgroundSprite.getWidth());
		rocketCircle.setY(0.6F*backgroundSprite.getHeight());
		rocketCircle.setWidth(0.04F*backgroundSprite.getHeight());
		rocketCircle.setHeight(rocketCircle.getWidth()/rocketCircle.getAspectRatio());		
		/***/
		rocketBasic.setActiveMode(false);
		rocketBasic.setX(0.7485F*backgroundSprite.getWidth());
		rocketBasic.setY(0.6065F*backgroundSprite.getHeight());
		rocketBasic.setWidth(0.05F*backgroundSprite.getHeight());
		rocketBasic.setHeight(rocketBasic.getWidth()/rocketBasic.getAspectRatio());		
		/***/
		rocketKinetic.setActiveMode(false);
		rocketKinetic.setX(0.7485F*backgroundSprite.getWidth());
		rocketKinetic.setY(0.6065F*backgroundSprite.getHeight());
		rocketKinetic.setWidth(0.05F*backgroundSprite.getHeight());
		rocketKinetic.setHeight(rocketKinetic.getWidth()/rocketKinetic.getAspectRatio());
		/***/
		rocketDelta.setActiveMode(false);
		rocketDelta.setX(0.751F*backgroundSprite.getWidth());
		rocketDelta.setY(0.6065F*backgroundSprite.getHeight());
		rocketDelta.setWidth(0.045F*backgroundSprite.getHeight());
		rocketDelta.setHeight(rocketDelta.getWidth()/rocketDelta.getAspectRatio());
		/***/
		rocketInfinity.setActiveMode(false);
		rocketInfinity.setX(0.7535F*backgroundSprite.getWidth());
		rocketInfinity.setY(0.6065F*backgroundSprite.getHeight());
		rocketInfinity.setWidth(0.04F*backgroundSprite.getHeight());
		rocketInfinity.setHeight(rocketInfinity.getWidth()/rocketInfinity.getAspectRatio());
	}
	
	private void resourcesCheck(){
		if(INF.money>INF.moneyFull) INF.money = INF.moneyFull;
		if(INF.fuel>INF.fuelFull) INF.fuel = INF.fuelFull;
		if(INF.metal>INF.metalFull) INF.metal = INF.metalFull;
	}
	
	private void touchUpdate(){
		if(ru.erked.spaceflight.controllers.SFIn.numTouch == 0){
			if(prevDragX != 0.0F && ru.erked.spaceflight.controllers.SFIn.tdrRX != 0.0F)
				camera.position.x -= ru.erked.spaceflight.controllers.SFIn.tdrRX - prevDragX;
			if(prevDragY != 0.0F && ru.erked.spaceflight.controllers.SFIn.tdrRY != 0.0F)
				camera.position.y -= ru.erked.spaceflight.controllers.SFIn.tdrRY - prevDragY;
			prevDragX = ru.erked.spaceflight.controllers.SFIn.tdrRX;
			prevDragY = ru.erked.spaceflight.controllers.SFIn.tdrRY;
		}else{
			controller.reset();
		}
		
		if(curViewWidth > backgroundSprite.getWidth()){
			curViewWidth = backgroundSprite.getWidth();
			float camX = camera.position.x;
			float camY = camera.position.y;
			camera.setToOrtho(false, curViewWidth, curViewHeight);
			camera.translate(new Vector3(camX, camY, 0));
		}
		if(curViewHeight > backgroundSprite.getHeight()){
			curViewHeight = backgroundSprite.getHeight();
			float camX = camera.position.x;
			float camY = camera.position.y;
			camera.setToOrtho(false, curViewWidth, curViewHeight);
			camera.translate(new Vector3(camX, camY, 0));
		}
		
		if(camera.position.x < backgroundSprite.getX() + curViewWidth/2)
			camera.position.set(new Vector3(backgroundSprite.getX() + curViewWidth/2, camera.position.y, 0));
		if(camera.position.y < backgroundSprite.getY() + curViewHeight/2)
			camera.position.set(new Vector3(camera.position.x, backgroundSprite.getY() + curViewHeight/2, 0));
		if(camera.position.x > (backgroundSprite.getX() + backgroundSprite.getWidth()) - curViewWidth/2)
			camera.position.set(new Vector3((backgroundSprite.getX() + backgroundSprite.getWidth()) - curViewWidth/2, camera.position.y, 0));
		if(camera.position.y > (backgroundSprite.getY() + backgroundSprite.getHeight()) - curViewHeight/2)
			camera.position.set(new Vector3(camera.position.x, (backgroundSprite.getY() + backgroundSprite.getHeight()) - curViewHeight/2, 0));
	}
	private void drawButtons(float delta){
		btnMN.setX(0.86F*curViewWidth);
		btnMN.setY(0.01F*curViewHeight);
		btnMN.setWidth(0.132F*curViewWidth);
		btnMN.setHeight(0.132F*curViewWidth/1.5F);
		btnMN.setCoordinates(camera, curViewWidth, curViewHeight);
		btnMN.getSprite().draw(batch);
		if(controller.isOnCam(btnMN.getX(camera, curViewWidth), btnMN.getY(camera, curViewHeight), btnMN.getWidth(), btnMN.getHeight(), camera, curViewWidth/width)){
			btnMN.setActiveMode(true);
			if(!INF.lngRussian){
				textBtn.draw(batch, "Main", 
						btnMN.getX(camera, curViewWidth) + 0.5F*btnMN.getWidth() - 0.5F*textBtn.getWidth("Main"), 
						btnMN.getY(camera, curViewHeight) + 0.725F*btnMN.getHeight());
				textBtn.draw(batch, "menu", 
						btnMN.getX(camera, curViewWidth) + 0.5F*btnMN.getWidth() - 0.5F*textBtn.getWidth("menu"), 
						btnMN.getY(camera, curViewHeight) + 0.475F*btnMN.getHeight());
			}else{
				textBtn.draw(batch, "Главное", 
						btnMN.getX(camera, curViewWidth) + 0.5F*btnMN.getWidth() - 0.5F*textBtn.getWidth("Главное"), 
						btnMN.getY(camera, curViewHeight) + 0.725F*btnMN.getHeight());
				textBtn.draw(batch, "меню", 
						btnMN.getX(camera, curViewWidth) + 0.5F*btnMN.getWidth() - 0.5F*textBtn.getWidth("меню"), 
						btnMN.getY(camera, curViewHeight) + 0.475F*btnMN.getHeight());
			}
		}else{
			btnMN.setActiveMode(false);
			if(!INF.lngRussian){
				textBtn.draw(batch, "Main", 
						btnMN.getX(camera, curViewWidth) + 0.5F*btnMN.getWidth() - 0.5F*textBtn.getWidth("Main"), 
						btnMN.getY(camera, curViewHeight) + 0.75F*btnMN.getHeight());
				textBtn.draw(batch, "menu", 
						btnMN.getX(camera, curViewWidth) + 0.5F*btnMN.getWidth() - 0.5F*textBtn.getWidth("menu"), 
						btnMN.getY(camera, curViewHeight) + 0.5F*btnMN.getHeight());
			}else{
				textBtn.draw(batch, "Главное", 
						btnMN.getX(camera, curViewWidth) + 0.5F*btnMN.getWidth() - 0.5F*textBtn.getWidth("Главное"), 
						btnMN.getY(camera, curViewHeight) + 0.75F*btnMN.getHeight());
				textBtn.draw(batch, "меню", 
						btnMN.getX(camera, curViewWidth) + 0.5F*btnMN.getWidth() - 0.5F*textBtn.getWidth("меню"), 
						btnMN.getY(camera, curViewHeight) + 0.5F*btnMN.getHeight());
			}
		}
		btnPlus.setX(0.86F*curViewWidth - 1.1F*btnMN.getWidth());
		btnPlus.setY(0.01F*curViewHeight);
		btnPlus.setWidth(0.132F*curViewWidth);
		btnPlus.setHeight(0.132F*curViewWidth/1.5F);
		btnPlus.setCoordinates(camera, curViewWidth, curViewHeight);
	//	btnPlus.getSprite().draw(batch);
		if(controller.isOnCam(btnPlus.getX(camera, curViewWidth), btnPlus.getY(camera, curViewHeight), btnPlus.getWidth(), btnPlus.getHeight(), camera, curViewWidth/width)){
			btnPlus.setActiveMode(true);
		}else{
			btnPlus.setActiveMode(false);
		}
		bPlus.setX(0.8885F*curViewWidth);
		bPlus.setY(0.32F*curViewHeight);
		bPlus.setWidth(0.075F*curViewWidth);
		bPlus.setHeight(0.075F*curViewWidth);
		bPlus.setCoordinates(camera, curViewWidth, curViewHeight);
		bPlus.getSprite().draw(batch);
		if(controller.isOnCam(bPlus.getX(camera, curViewWidth), bPlus.getY(camera, curViewHeight), bPlus.getWidth(), bPlus.getHeight(), camera, curViewWidth/width)){
			bPlus.setActiveMode(true);
		}else{
			bPlus.setActiveMode(false);
		}
		bMinus.setX(0.8885F*curViewWidth);
		bMinus.setY(0.175F*curViewHeight);
		bMinus.setWidth(0.075F*curViewWidth);
		bMinus.setHeight(0.075F*curViewWidth);
		bMinus.setCoordinates(camera, curViewWidth, curViewHeight);
		bMinus.getSprite().draw(batch);
		if(controller.isOnCam(bMinus.getX(camera, curViewWidth), bMinus.getY(camera, curViewHeight), bMinus.getWidth(), bMinus.getHeight(), camera, curViewWidth/width)){
			bMinus.setActiveMode(true);
		}else{
			bMinus.setActiveMode(false);
		}
		btnHome.setX(0.015F*curViewWidth);
		btnHome.setY(0.01F*curViewHeight);
		btnHome.setWidth(0.075F*curViewWidth);
		btnHome.setHeight(0.075F*curViewWidth);
		btnHome.setCoordinates(camera, curViewWidth, curViewHeight);
		btnHome.getSprite().draw(batch);
		if(controller.isOnCam(btnHome.getX(camera, curViewWidth), btnHome.getY(camera, curViewHeight), btnHome.getWidth(), btnHome.getHeight(), camera, curViewWidth/width)){
			btnHome.setActiveMode(true);
		}else{
			btnHome.setActiveMode(false);
		}
		btnAchi.setX(0.015F*curViewWidth);
		btnAchi.setY(0.01F*curViewHeight + 0.08F*curViewWidth);
		btnAchi.setWidth(0.075F*curViewWidth);
		btnAchi.setHeight(0.075F*curViewWidth);
		btnAchi.setCoordinates(camera, curViewWidth, curViewHeight);
		btnAchi.getSprite().draw(batch);
		if(controller.isOnCam(btnAchi.getX(camera, curViewWidth), btnAchi.getY(camera, curViewHeight), btnAchi.getWidth(), btnAchi.getHeight(), camera, curViewWidth/width)){
			btnAchi.setActiveMode(true);
		}else{
			btnAchi.setActiveMode(false);
		}
	}
	private void drawBuildings(){
		//Hangar
		if(controller.isOnCam(hangar.getX(), hangar.getY(), hangar.getWidth(), hangar.getHeight(), camera, curViewWidth/width)){
			hangar.setActiveMode(true);
		}else{
			hangar.setActiveMode(false);
		}
		hangar.getSprite().draw(batch);
		//Analytic center
		if(controller.isOnCam(analytic.getX(), analytic.getY(), analytic.getWidth(), analytic.getHeight(), camera, curViewWidth/width)){
			analytic.setActiveMode(true);
		}else{
			analytic.setActiveMode(false);
		}
		analytic.getSprite().draw(batch);
		//Control tower
		if(controller.isOnCam(control.getX(), control.getY(), control.getWidth(), control.getHeight(), camera, curViewWidth/width)){
			control.setActiveMode(true);
		}else{
			control.setActiveMode(false);
		}
		control.getSprite().draw(batch);
		//Armory
		if(controller.isOnCam(gunStore.getX(), gunStore.getY(), gunStore.getWidth(), gunStore.getHeight(), camera, curViewWidth/width)){
			gunStore.setActiveMode(true);
		}else{
			gunStore.setActiveMode(false);
		}
		gunStore.getSprite().draw(batch);
		if(INF.elapsedTime % (rand.nextInt(30)+1) == 0){
			int translateX = rand.nextInt(50);
			int translateY = translateX + rand.nextInt(10);
			smoke.add(new ParticleFX(gunStore.getX()+0.85F*gunStore.getWidth()+translateX, gunStore.getY()+0.85F*gunStore.getHeight()+translateY, (rand.nextFloat()/5.0F)*gunStore.getWidth() + 0.1F*gunStore.getWidth(), "smoke", -1, rand.nextInt(3)+1));
			smoke.get(smoke.size()-1).getSprite().setOriginCenter();
			smoke.add(new ParticleFX(gunStore.getX()+0.7F*gunStore.getWidth()+translateX, gunStore.getY()+0.9F*gunStore.getHeight()+translateY, (rand.nextFloat()/5.0F)*gunStore.getWidth() + 0.1F*gunStore.getWidth(), "smoke", -1, rand.nextInt(3)+1));
			smoke.get(smoke.size()-1).getSprite().setOriginCenter();
		}
		for(int i=0;i<smoke.size();i++){
			smoke.get(i).rotate(rand.nextFloat() + 2.5F);
			smoke.get(i).draw(batch);
			if(smoke.get(i).getTimer() <= 0.0F)
				smoke.get(i).setDeath(true);
			if(smoke.get(i).isDeath())
				smoke.remove(i);
		}
		//Library
		if(controller.isOnCam(library.getX(), library.getY(), library.getWidth(), library.getHeight(), camera, curViewWidth/width)){
			library.setActiveMode(true);
		}else{
			library.setActiveMode(false);
		}
		library.getSprite().draw(batch);
		/***/
		if(controller.isOnCam(fuelFactorySpriteI[schFuel].getX(), fuelFactorySpriteI[schFuel].getY(), fuelFactorySpriteI[schFuel].getWidth(), fuelFactorySpriteI[schFuel].getHeight(), camera, curViewWidth/width)){
			if(INF.elapsedTime % 30 == 0){
				if(schFuel<3) schFuel++;
				else schFuel=0;
			}
			fuelFactorySpriteA[schFuel].draw(batch);
		}else{
			if(INF.elapsedTime % 30 == 0){
				if(schFuel<3) schFuel++;
				else schFuel=0;
			}
			fuelFactorySpriteI[schFuel].draw(batch);
		}
		/***/
		if(controller.isOnCam(coinFactorySprite[schCoin].getX(), coinFactorySprite[schCoin].getY(), coinFactorySprite[schCoin].getWidth(), coinFactorySprite[schCoin].getHeight(), camera, curViewWidth/width)){
			coinFactorySpriteActive.draw(batch);
		}
		if(INF.elapsedTime % 4 == 0){
			if(schCoin<35) schCoin++;
			else schCoin=0;
		}
		coinFactorySprite[schCoin].draw(batch);
		/***/
		if(controller.isOnCam(metalFactorySprite[schMetal].getX(), metalFactorySprite[schMetal].getY(), metalFactorySprite[schMetal].getWidth(), metalFactorySprite[schMetal].getHeight(), camera, curViewWidth/width)){
			metalFactoryActive.draw(batch);
		}
		if(INF.elapsedTime % 6 == 0){
			if(schMetal<23) schMetal++;
			else schMetal=0;
		}
		metalFactorySprite[schMetal].draw(batch);
		/***/
		//Clouds
		if(INF.elapsedTime % (rand.nextInt(5000)+1) == 0){
			clouds.add(new ParticleFX((0.0F-0.25F*width), rand.nextFloat()*backgroundSprite.getHeight(), 0.25F*width, "cloud", (rand.nextInt(3)+1), 60.0F));
		}
		for(int i=0;i<clouds.size();i++){
			clouds.get(i).setX(clouds.get(i).getX() + 0.0005F*backgroundSprite.getWidth());
			clouds.get(i).draw(batch);
			if(clouds.get(i).getTimer() <= 0.0F)
				clouds.get(i).setDeath(true);
			if(clouds.get(i).isDeath())
				clouds.remove(i);
		}
	}
	private void drawMoney(){
		for(float i=0.25F;i<3.25F;i+=1.0F){
			line.setX(moneyX + (i*5.0F)*moneyWidth);
			line.draw(batch);
		}
		moneySprite.draw(batch);
		fuelSprite.draw(batch);
		metalSprite.draw(batch);
		cosmocoinLine.draw(batch);
		fuelLine.draw(batch);
		metalLine.draw(batch);
		text.draw(batch, ":     " + Long.toString((int)(INF.money)) + "/" + Long.toString((int)(INF.moneyFull)), moneySprite.getX() + 1.05F*moneySprite.getWidth(), moneySprite.getY() + 0.825F*moneySprite.getHeight() - 0.5F*text.getHeight("A"));
		text.draw(batch, ":     " + Long.toString((int)(INF.fuel)) + "/" + Long.toString((int)(INF.fuelFull)), fuelSprite.getX() + 1.05F*fuelSprite.getWidth(), fuelSprite.getY() + 0.825F*moneySprite.getHeight() - 0.5F*text.getHeight("A"));
		text.draw(batch, ":     " + Long.toString((int)(INF.metal)) + "/" + Long.toString((int)(INF.metalFull)), metalSprite.getX() + 1.05F*metalSprite.getWidth(), metalSprite.getY() + 0.825F*moneySprite.getHeight() - 0.5F*text.getHeight("A"));
	}
	private void drawRockets(){
		if(rocket != null){
			rocketS.getSprite().draw(batch);
		}
	}
	
	private void buttonListener(){
		if(controller.isClickedCam(btnMN.getX(camera, curViewWidth), btnMN.getY(camera, curViewHeight), btnMN.getWidth(), btnMN.getHeight(), true, camera, curViewWidth/width) || isTransGame){
			isTransGame = true;
			if(alp>1.0F){
				game.setScreen(new MainMenu(game));
				alp = 1.0F;
				this.dispose();
			}else{
				blackAlpha.setAlpha(alp);
				alp+=0.05F;
			}
		}
		if(controller.isClickedCam(hangar.getX(), hangar.getY(), hangar.getWidth(), hangar.getHeight(), true, camera, curViewWidth/width)){
			game.setScreen(new HangarPanelScreen(game));
			this.dispose();
		}
		if(controller.isClickedCam(analytic.getX(), analytic.getY(), analytic.getWidth(), analytic.getHeight(), true, camera, curViewWidth/width)){
			game.setScreen(new ResourceScreen(game));
			this.dispose();
		}
		if(controller.isClickedCam(control.getX(), control.getY(), control.getWidth(), control.getHeight(), true, camera, curViewWidth/width)){
			game.setScreen(new StartPanelScreen(game));
			this.dispose();
		}
		if(controller.isClickedCam(gunStore.getX(), gunStore.getY(), gunStore.getWidth(), gunStore.getHeight(), true, camera, curViewWidth/width)){
			game.setScreen(new ru.erked.spaceflight.game.ArmoryPanelScreen(game));
			this.dispose();
		}
		if(controller.isClickedCam(library.getX(), library.getY(), library.getWidth(), library.getHeight(), true, camera, curViewWidth/width)){
			game.setScreen(new ru.erked.spaceflight.game.LibraryScreen(game));
			this.dispose();
		}
		if(controller.isClickedCam(btnPlus.getX(camera, curViewWidth), btnPlus.getY(camera, curViewHeight), btnPlus.getWidth(), btnPlus.getHeight(), false, camera, curViewWidth/width)){			
//			INF.facts++;
			INF.planetLevel++;
			INF.money += 1000;
			INF.fuel += 1000;
			INF.metal += 1000;
			INF.moneyFull += 100;
			INF.fuelFull += 100;
			INF.metalFull += 100;
		}
		if(controller.isClickedCam(coinFactorySprite[schCoin].getX(), coinFactorySprite[schCoin].getY(), coinFactorySprite[schCoin].getWidth(), coinFactorySprite[schCoin].getHeight(), true, camera, curViewWidth/width)){
			game.setScreen(new CosmocoinScreen(game));
			this.dispose();
		}
		if(controller.isClickedCam(metalFactorySprite[schMetal].getX(), metalFactorySprite[schMetal].getY(), metalFactorySprite[schMetal].getWidth(), metalFactorySprite[schMetal].getHeight(), true, camera, curViewWidth/width)){
			game.setScreen(new MetalScreen(game));
			this.dispose();
		}
		if(controller.isClickedCam(fuelFactorySpriteI[schFuel].getX(), fuelFactorySpriteI[schFuel].getY(), fuelFactorySpriteI[schFuel].getWidth(), fuelFactorySpriteI[schFuel].getHeight(), true, camera, curViewWidth/width)){
			game.setScreen(new FuelScreen(game));
			this.dispose();
		}
		if(controller.isClickedCam(bPlus.getX(camera, curViewWidth), bPlus.getY(camera, curViewHeight), bPlus.getWidth(), bPlus.getHeight(), true, camera, curViewWidth/width)){			
			if(curViewWidth - 250.0F > 0.75F*width) curViewWidth -= 250.0F;
			else curViewWidth = 0.75F*width;
			curViewHeight = (float)(curViewWidth)/(float)(width/height);
			float camX = camera.position.x;
			float camY = camera.position.y;
			camera.setToOrtho(false, curViewWidth, curViewHeight);
			camera.translate(new Vector3(camX, camY, 0));
			textBtn.setScale((float)(curViewWidth/width));
			text.setScale((float)(curViewWidth/width));
		}
		if(controller.isClickedCam(bMinus.getX(camera, curViewWidth), bMinus.getY(camera, curViewHeight), bMinus.getWidth(), bMinus.getHeight(), true, camera, curViewWidth/width)){			
			if(curViewWidth + 250.0F < 0.85F*backgroundSprite.getWidth()) curViewWidth += 250.0F;
			else curViewWidth = 0.85F*backgroundSprite.getWidth();
			curViewHeight = (float)(curViewWidth)/(float)(width/height);
			float camX = camera.position.x;
			float camY = camera.position.y;
			camera.setToOrtho(false, curViewWidth, curViewHeight);
			camera.translate(new Vector3(camX, camY, 0));
			textBtn.setScale((float)(curViewWidth/width));
			text.setScale((float)(curViewWidth/width));
		}
		if(controller.isClickedCam(btnAchi.getX(camera, curViewWidth), btnAchi.getY(camera, curViewHeight), btnAchi.getWidth(), btnAchi.getHeight(), true, camera, curViewWidth/width)){
			gpgs.showAchievements();
		}
		if(controller.isClickedCam(btnHome.getX(camera, curViewWidth), btnHome.getY(camera, curViewHeight), btnHome.getWidth(), btnHome.getHeight(), true, camera, curViewWidth/width)){
			game.setScreen(new StatisticScreen(game));
			this.dispose();
		}
	}
	
	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
		MainMenu.music.pause();
		data.saveSF();
	}

	@Override
	public void resume() {
		admob.hide();
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
		textBtn.dispose();
		batch.dispose();
	}

}