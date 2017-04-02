package ru.erked.spaceflight.planets;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector3;

import ru.erked.spaceflight.StartSFlight;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;
import ru.erked.spaceflight.tech.CurPR;
import ru.erked.spaceflight.tech.ParticleFX;
import ru.erked.spaceflight.tech.Rocket;
import ru.erked.spaceflight.tech.SFButtonA;
import ru.erked.spaceflight.tech.SFButtonS;
import ru.erked.spaceflight.tech.SFFont;

public class LoonScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private SpriteBatch batch;
	private ru.erked.spaceflight.controllers.SFIn controller;
	public static OrthographicCamera camera;
	public static Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/Voice Over Under.mp3"));
	public static Music siren = Gdx.audio.newMusic(Gdx.files.internal("sounds/misc/siren.wav"));
	public static Sound explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/misc/explosion.wav"));
	public static Sound time = Gdx.audio.newSound(Gdx.files.internal("sounds/misc/time.wav"));
	private boolean isSirenPlaying = false;
	private Sound[] crash = new Sound[] {Gdx.audio.newSound(Gdx.files.internal("sounds/misc/crash1.mp3")), Gdx.audio.newSound(Gdx.files.internal("sounds/misc/crash2.mp3")), Gdx.audio.newSound(Gdx.files.internal("sounds/misc/crash3.mp3"))};
	private RandomXS128 rand = new RandomXS128();
	
	private Sprite backgroundSprite;

	private SFButtonA back;
	
	private Rocket rocket;
	private SFButtonS rocketS;
	private SFButtonS rocketBall;
	private SFButtonS rocketCircle;
	private SFButtonS rocketBasic;
	private SFButtonS rocketKinetic;
	private SFButtonS rocketDelta;
	private SFButtonS rocketInfinity;
	private Sprite[] fire;
	private int fireIter = 0;
	private float speed;

	private int[][] gemMatrix;
	private Sprite[][] ruby;
	private Sprite[][] sapphire;
	private Sprite[][] topaz;
	private Sprite[][] emerald;
	private float gemVan = 1.0F;
	private int actI = -1;
	private int actJ = -1;
	private boolean isSwipe = false;
	private int[] swipeI = new int[50], swipeJ = new int[50], swipeA = new int[50], swipeB = new int[50];
	private int swipeCount = 0;
	private int swipeIter = 0;
	private float swipeSpeed = 0.001F*width;
	private boolean[][] route1 = new boolean[5][10];
	private boolean[][] route2 = new boolean[5][10];
	private int routeCount1 = 0;
	private int routeCount2 = 0;
	private boolean isVanishing = false;
	private boolean isGravity = false;
	
	private static SFFont text;
	private static SFFont header;
	
	private boolean isStartedTimer1 = false;
	private boolean isStartedTimer2 = false;
	private long time1 = 0;
	private long time2 = 0;
	private long timer = 0;
	private long rocketTimer = 0;
	private long bonusTime = 0;
	private int score = 0;
	private Sprite gems;
	private Sprite[] clock;
	private int clockIter = 0;
	private int gemsToWin = 0;
	private boolean isVictory = false;
	private boolean isGameOver = false;
	private boolean isEndlessMode = false;
	private float overTimer = 3.0F;
	private float startTimer = 6.0F;
	private int timeLeft;
	private boolean isTimeLeft = false;
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private ArrayList<ParticleFX[]> explosions;
	
	public LoonScreen(final StartSFlight game){
		this.game = game;
	}
	
	@Override
	public void show() {

		batch = new SpriteBatch();
		controller = new ru.erked.spaceflight.controllers.SFIn();
		music.setVolume(0.5F);
		music.setLooping(true);
		music.play();
		
		speed = 5.0F;
		
		camera = new OrthographicCamera(width, height);
		camera.position.set(new Vector3(0.0F, 2.0F*height, 0));
		
		backgroundSprite = RES.atlas.createSprite("loon");
		backgroundSprite.setBounds(0.0F, 0.0F, width, 2.0F*height);
		
		if(INF.planetLevel >= 10){
			timer = 60*60;
			rocketTimer = CurPR.getCurRocket().getTimeBonus()*60 + 60;
			gemsToWin = 0;
			isEndlessMode = true;
		}else{
			timer = 60*60 + (INF.planetLevel - CurPR.getCurPlanet().getLevel())*5*60;
			rocketTimer = CurPR.getCurRocket().getTimeBonus()*60 + 60;
			gemsToWin = 200 + (INF.planetLevel - CurPR.getCurPlanet().getLevel())*15;
		}
		
		gems = RES.atlas.createSprite("gems");
		gems.setBounds(0.375F*width, 0.8F*height, 0.15F*height, 0.15F*height);
		clock = new Sprite[4];
		for(int i=0;i<4;i++){
			clock[i] = RES.atlas.createSprite("clock", i+1);
			clock[i].setBounds(0.025F*width, 0.8F*height, 0.15F*height, 0.15F*height);
		}
		
		gemInit();
		rocketsInit();
		
		if(!INF.lngRussian){
			back = new SFButtonA("continueI", "continueA", 0.3F*width, width - 0.315F*width, 0.875F*height, 5.97826F, 1.0F, -1);
		}else{
			back = new SFButtonA("continueRI", "continueRA", 0.3F*width, width - 0.315F*width, 0.875F*height, 5.97826F, 1.0F, -1);
		}

		text = new SFFont(30, Color.WHITE, 1, 2, Color.BLACK);
		header = new SFFont(12, Color.WHITE, 4.0F, Color.BLACK);
		
		isTrans = false;
		blackAlpha.setBounds(0.0F, 0.0F, width, 2.0F*height);
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
		
		touchUpdate();
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		
		backgroundSprite.draw(batch);
		
		interfaceDraw(delta);
		rocketDraw(delta);
		if(isSwipe)
			swipe(swipeI, swipeJ, swipeA, swipeB, swipeCount, swipeSpeed);
		if(isVanishing)
			vanish();
		gemDraw();
		
		blackAlpha.draw(batch);
		
//		text.draw(batch, "[FPS] " + Gdx.graphics.getFramesPerSecond(), 0.01F*width, 0.07F*height);
		
		batch.end();
	
		buttonListener();
	}

	private void gemInit(){
		ruby = new Sprite[5][10];
		sapphire = new Sprite[5][10];
		topaz = new Sprite[5][10];
		emerald = new Sprite[5][10];
		gemMatrix = new int[5][10];
		for(int i=0;i<5;i++){
			for(int j=0;j<10;j++){
				gemMatrix[i][j] = rand.nextInt(4) + 1;
				
				if(INF.isBombActive)
					if(rand.nextInt(30) == 0)
						gemMatrix[i][j] += 10;
				if(INF.isColoumnActive)
					if(rand.nextInt(35) == 0)
						gemMatrix[i][j] += 20;
				if(INF.isLineActive)
					if(rand.nextInt(45) == 0)
						gemMatrix[i][j] += 30;
				if(INF.isTimeActive)
					if(rand.nextInt(50) == 0)
						gemMatrix[i][j] += 40;
				
				switch (gemMatrix[i][j]){
				case 1:{
					topaz[i][j] = RES.atlas.createSprite("topaz");
					break;
				}case 2:{
					sapphire[i][j] = RES.atlas.createSprite("sapphire");
					break;
				}case 3:{
					ruby[i][j] = RES.atlas.createSprite("ruby");
					break;
				}case 4:{
					emerald[i][j] = RES.atlas.createSprite("emerald");
					break;
				}case 11:{
					topaz[i][j] = RES.atlas.createSprite("topazB");
					break;
				}case 21:{
					topaz[i][j] = RES.atlas.createSprite("topazC");
					break;
				}case 31:{
					topaz[i][j] = RES.atlas.createSprite("topazL");
					break;
				}case 41:{
					topaz[i][j] = RES.atlas.createSprite("topazT");
					break;
				}case 12:{
					sapphire[i][j] = RES.atlas.createSprite("sapphireB");
					break;
				}case 22:{
					sapphire[i][j] = RES.atlas.createSprite("sapphireC");
					break;
				}case 32:{
					sapphire[i][j] = RES.atlas.createSprite("sapphireL");
					break;
				}case 42:{
					sapphire[i][j] = RES.atlas.createSprite("sapphireT");
					break;
				}case 13:{
					ruby[i][j] = RES.atlas.createSprite("rubyB");
					break;
				}case 23:{
					ruby[i][j] = RES.atlas.createSprite("rubyC");
					break;
				}case 33:{
					ruby[i][j] = RES.atlas.createSprite("rubyL");
					break;
				}case 43:{
					ruby[i][j] = RES.atlas.createSprite("rubyT");
					break;
				}case 14:{
					emerald[i][j] = RES.atlas.createSprite("emeraldB");
					break;
				}case 24:{
					emerald[i][j] = RES.atlas.createSprite("emeraldC");
					break;
				}case 34:{
					emerald[i][j] = RES.atlas.createSprite("emeraldL");
					break;
				}case 44:{
					emerald[i][j] = RES.atlas.createSprite("emeraldT");
					break;
				}default:{
					break;
				}
				}
			}
		}
		explosions = new ArrayList<ParticleFX[]>();
	}
	private void rocketsInit(){
		fire = new Sprite[2];
		for(int i=0;i<2;i++)
			fire[i] = RES.atlas.createSprite("fire",i+1);
		/***/
		rocketBall = new SFButtonS("rocketBallI", "rocketBallA", 0.225F*height, 0.5F*width - 0.1125F*height, 2.5F*height, 0.52727F, 3.0F, -1);
		rocketCircle = new SFButtonS("rocketCircleI", "rocketCircleA", 0.225F*height, 0.5F*width - 0.1125F*height, 2.5F*height, 0.52727F, 3.0F, -1);
		rocketBasic = new SFButtonS("rocketBasicI", "rocketBasicA", 0.225F*height, 0.5F*width - 0.1125F*height, 2.5F*height, 0.32967F, 3.0F, -1);
		rocketKinetic = new SFButtonS("rocketKineticI", "rocketKineticA", 0.175F*height, 0.5F*width - 0.0875F*height, 2.5F*height, 0.25F, 3.0F, -1);
		rocketDelta = new SFButtonS("rocketDeltaI", "rocketDeltaA", 0.115F*height, 0.5F*width - 0.0575F*height, 2.5F*height, 0.1796875F, 3.0F, -1);
		rocketInfinity = new SFButtonS("rocketInfinityI", "rocketInfinityA", 0.125F*height, 0.5F*width - 0.0625F*height, 2.5F*height, 0.1796875F, 3.0F, -1);
		/***/
		rocket = CurPR.getCurRocket();
		if(rocket != null){
			if(rocket.equals(INF.rocketBall)){
				rocketS = rocketBall;
				for(int i=0;i<2;i++)
					fire[i].setBounds(rocketS.getX() + 0.27625F*rocketS.getWidth(), rocketS.getY() + 0.2F*rocketS.getHeight(), 0.45F*rocketS.getWidth(), 0.9F*rocketS.getWidth());
			}else if(rocket.equals(INF.rocketCircle)){
				rocketS = rocketCircle;
				for(int i=0;i<2;i++)
					fire[i].setBounds(rocketS.getX() + 0.27625F*rocketS.getWidth(), rocketS.getY() + 0.2F*rocketS.getHeight(), 0.45F*rocketS.getWidth(), 0.9F*rocketS.getWidth());
			}else if(rocket.equals(INF.rocketBasic)){
				rocketS = rocketBasic;
				for(int i=0;i<2;i++)
					fire[i].setBounds(rocketS.getX() + 0.3215F*rocketS.getWidth(), rocketS.getY() - 0.125F*rocketS.getHeight(), 0.35F*rocketS.getWidth(), 0.7F*rocketS.getWidth());
			}else if(rocket.equals(INF.rocketKinetic)){
				rocketS = rocketKinetic;
				for(int i=0;i<2;i++)
					fire[i].setBounds(rocketS.getX() + 0.201F*rocketS.getWidth(), rocketS.getY() - 0.25F*rocketS.getHeight(), 0.6F*rocketS.getWidth(), 1.2F*rocketS.getWidth());
			}else if(rocket.equals(INF.rocketDelta)){
				rocketS = rocketDelta;
				for(int i=0;i<2;i++)
					fire[i].setBounds(rocketS.getX() + 0.19F*rocketS.getWidth(), rocketS.getY() - 0.2F*rocketS.getHeight(), 0.6F*rocketS.getWidth(), 1.2F*rocketS.getWidth());
			}else if(rocket.equals(INF.rocketInfinity)){
				rocketS = rocketInfinity;
				for(int i=0;i<2;i++)
					fire[i].setBounds(rocketS.getX() + 0.275F*rocketS.getWidth(), rocketS.getY() - 0.19F*rocketS.getHeight(), 0.45F*rocketS.getWidth(), 1.2F*rocketS.getWidth());
			}
		}
	}
	
	private ParticleFX[] coloumn$LineExplosion(int i, int j, float timer){
		ParticleFX[] explosion = new ParticleFX[6];
		for(int g=0;g<6;g++){
			explosion[g] = new ParticleFX(
					0.085F*width + j*0.083765F*width - 0.25F*0.07615F*width, 
					0.625F*height - i*0.083765F*width - 0.25F*0.07615F*width, 
					0.114225F*width, 
					"explosion", 
					g+1, 
					timer);
			explosion[g].setDeath(false);
		}
		return explosion;
	}
	private ParticleFX[] bombExplosion(int i, int j, float timer){
		ParticleFX[] explosion = new ParticleFX[6];
		for(int g=0;g<6;g++){
			explosion[g] = new ParticleFX(
					0.085F*width + j*0.083765F*width - 1.0F*0.07615F*width, 
					0.625F*height - i*0.083765F*width - 1.0F*0.07615F*width, 
					0.22845F*width, 
					"explosion", 
					g+1, 
					timer);
			explosion[g].setDeath(false);
		}
		return explosion;
	}
	private void interfaceDraw(float delta){
		if(!isGameOver){
			if(INF.elapsedTime % 30 == 0) clockIter++;
			if(clockIter == 4) clockIter = 0;
		}
		if(isGameOver){
			back.setCoordinates(camera, width, height);
			back.getSprite().draw(batch);
			if(controller.isOnCam(back.getX(camera, width), back.getY(camera, height), back.getWidth(), back.getHeight(), camera, 1.0F)){
				back.setActiveMode(true);
			}else{
				back.setActiveMode(false);
			}
		}
		if(rocketS.getY() <= 1.0F*height && startTimer <= 0.0F && !isGameOver){
			if(score >= gemsToWin && gemsToWin != 0 && !isEndlessMode){
				if(!isTimeLeft){
					isTimeLeft = true;
					timeLeft = (int)(timer - INF.elapsedTime + time2)/60;
				}
				isGameOver = true;
				isVictory = true;
			}
			if(!isStartedTimer1){
				isStartedTimer1 = true;
				time1 = INF.elapsedTime;
			}
			gems.draw(batch);
			clock[clockIter].draw(batch);
			if((rocketTimer - INF.elapsedTime + time1)/60 <= 0){
				if(!isStartedTimer2){
					isStartedTimer2 = true;
					time2 = INF.elapsedTime;
				}
				if((timer - INF.elapsedTime + time2)/60 <= 0){
					isGameOver = true;
					isVictory = false;
				}
			}
		}else if(rocketS.getY() <= 1.0F*height && startTimer > 0.0F){
			startTimer -= delta;
		}
		
		if(!INF.lngRussian){
			if(INF.planetLevel >= 10){
				if(!isGameOver)
					header.draw(batch, "Endless", 0.5F*width - 0.5F*header.getWidth("Endless"), 1.5F*height + 0.5F*header.getHeight("A"));
				else
					header.draw(batch, "Game over!", 0.5F*width - 0.5F*header.getWidth("Game over!"), 1.5F*height + 0.5F*header.getHeight("A"));
			}else{
				if(!isGameOver)
					header.draw(batch, "Level " + (INF.planetLevel + 1), 0.5F*width - 0.5F*header.getWidth("Level " + (INF.planetLevel + 1)), 1.5F*height + 0.5F*header.getHeight("A"));
				else if(!isVictory)
					header.draw(batch, "Time is up!", 0.5F*width - 0.5F*header.getWidth("Time is up!"), 1.5F*height + 0.5F*header.getHeight("A"));
				else
					header.draw(batch, "Victory!", 0.5F*width - 0.5F*header.getWidth("Victory!"), 1.5F*height + 0.5F*header.getHeight("A"));
			}
			if(rocketS.getY() <= 1.0F*height && startTimer <= 0.0F){
				if(!isGameOver){
					text.draw(batch, score + " / " + gemsToWin, 
							gems.getX() + 1.2F*gems.getWidth(), 
							gems.getY() + 0.5F*gems.getHeight() + 0.5F*text.getHeight("A"));
					if((rocketTimer - INF.elapsedTime + time1)/60 <= 0){
						text.draw(batch, (timer - INF.elapsedTime + time2)/60 + " + ", 
								clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth(), 
								clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
						text.setColor(Color.SKY);
						text.draw(batch, "0", 
								clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth() + text.getWidth((timer - INF.elapsedTime + time2)/60 + " + "), 
								clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
						text.setColor(Color.WHITE);
						text.draw(batch, " sec", 
								clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth() + text.getWidth((timer - INF.elapsedTime + time2)/60 + " + " + "0"), 
								clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
					}else{
						if(INF.planetLevel >= 10){
							text.draw(batch, 60 + bonusTime + " + ", 
									clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth(), 
									clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
							text.setColor(Color.SKY);
							text.draw(batch, (rocketTimer - INF.elapsedTime + time1)/60 + "", 
									clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth() + text.getWidth(60 + bonusTime + " + "), 
									clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
							text.setColor(Color.WHITE);
							text.draw(batch, " sec", 
									clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth() + text.getWidth(60 + bonusTime + " + " + (rocketTimer - INF.elapsedTime + time1)/60), 
									clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
						}else{
							text.draw(batch, 60 + (INF.planetLevel - CurPR.getCurPlanet().getLevel())*5 + bonusTime + " + ", 
									clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth(), 
									clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
							text.setColor(Color.SKY);
							text.draw(batch, (rocketTimer - INF.elapsedTime + time1)/60 + "", 
									clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth() + text.getWidth(60 + (INF.planetLevel - CurPR.getCurPlanet().getLevel())*5 + bonusTime + " + "), 
									clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
							text.setColor(Color.WHITE);
							text.draw(batch, " sec", 
									clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth() + text.getWidth(60 + (INF.planetLevel - CurPR.getCurPlanet().getLevel())*5 + bonusTime + " + " + (rocketTimer - INF.elapsedTime + time1)/60), 
									clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
						}
					}
				}
			}else if(camera.position.y <= backgroundSprite.getY() + height/2){
				if(startTimer <= 3)
					text.draw(batch, "3..", 0.8F*width, 0.9F*height);
				if(startTimer <= 2)
					text.draw(batch, "3.. 2..", 0.8F*width, 0.9F*height);
				if(startTimer <= 1)
					text.draw(batch, "3.. 2.. 1..", 0.8F*width, 0.9F*height);
			}
		}else{
			if(INF.planetLevel >= 10){
				if(!isGameOver)
					header.draw(batch, "Бесконечность", 0.5F*width - 0.5F*header.getWidth("Бесконечность"), 1.5F*height + 0.5F*header.getHeight("A"));
				else
					header.draw(batch, "Игра закончена!", 0.5F*width - 0.5F*header.getWidth("Игра закончена!"), 1.5F*height + 0.5F*header.getHeight("A"));
			}else{
				if(!isGameOver)
					header.draw(batch, "Уровень " + (INF.planetLevel + 1), 0.5F*width - 0.5F*header.getWidth("Уровень " + (INF.planetLevel + 1)), 1.5F*height + 0.5F*header.getHeight("A"));
				else if(!isVictory)
					header.draw(batch, "Время истекло!", 0.5F*width - 0.5F*header.getWidth("Время истекло!"), 1.5F*height + 0.5F*header.getHeight("A"));
				else
					header.draw(batch, "Победа!", 0.5F*width - 0.5F*header.getWidth("Победа!"), 1.5F*height + 0.5F*header.getHeight("A"));
			}
			if(rocketS.getY() <= 1.0F*height && startTimer <= 0.0F){
				if(!isGameOver){
					text.draw(batch, score + " / " + gemsToWin, 
							gems.getX() + 1.2F*gems.getWidth(), 
							gems.getY() + 0.5F*gems.getHeight() + 0.5F*text.getHeight("A"));
					if((rocketTimer - INF.elapsedTime + time1)/60 <= 0){
						text.draw(batch, (timer - INF.elapsedTime + time2)/60 + " + ", 
								clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth(), 
								clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
						text.setColor(Color.SKY);
						text.draw(batch, "0", 
								clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth() + text.getWidth((timer - INF.elapsedTime + time2)/60 + " + "), 
								clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
						text.setColor(Color.WHITE);
						text.draw(batch, " сек", 
								clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth() + text.getWidth((timer - INF.elapsedTime + time2)/60 + " + " + "0"), 
								clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
					}else{
						if(INF.planetLevel >= 10){
							text.draw(batch, 60 + bonusTime + " + ", 
									clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth(), 
									clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
							text.setColor(Color.SKY);
							text.draw(batch, (rocketTimer - INF.elapsedTime + time1)/60 + "", 
									clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth() + text.getWidth(60 + bonusTime + " + "), 
									clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
							text.setColor(Color.WHITE);
							text.draw(batch, " сек", 
									clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth() + text.getWidth(60 + bonusTime + " + " + (rocketTimer - INF.elapsedTime + time1)/60), 
									clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
						}else{
							text.draw(batch, 60 + (INF.planetLevel - CurPR.getCurPlanet().getLevel())*5 + bonusTime + " + ", 
									clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth(), 
									clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
							text.setColor(Color.SKY);
							text.draw(batch, (rocketTimer - INF.elapsedTime + time1)/60 + "", 
									clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth() + text.getWidth(60 + (INF.planetLevel - CurPR.getCurPlanet().getLevel())*5 + bonusTime + " + "), 
									clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
							text.setColor(Color.WHITE);
							text.draw(batch, " сек", 
									clock[clockIter].getX() + 1.1F*clock[clockIter].getWidth() + text.getWidth(60 + (INF.planetLevel - CurPR.getCurPlanet().getLevel())*5 + bonusTime + " + " + (rocketTimer - INF.elapsedTime + time1)/60), 
									clock[clockIter].getY() + 0.5F*clock[clockIter].getHeight() + 0.5F*text.getHeight("A"));
						}
					}
				}
			}else if(camera.position.y <= backgroundSprite.getY() + height/2){
				if(startTimer <= 3)
					text.draw(batch, "3..", 0.8F*width, 0.9F*height);
				if(startTimer <= 2)
					text.draw(batch, "3.. 2..", 0.8F*width, 0.9F*height);
				if(startTimer <= 1)
					text.draw(batch, "3.. 2.. 1..", 0.8F*width, 0.9F*height);
			}
		}
	}
	private void gemDraw(){
		for(int i=0;i<5;i++){
			for(int j=0;j<10;j++){
				switch (gemMatrix[i][j]){
				case 1:case 11:case 21:case 31:case 41:{
					if(!isVanishing) topaz[i][j].setAlpha(1.0F);
					if(!isSwipe) topaz[i][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height - i*0.083765F*width, 0.07615F*width, 0.07615F*width);
					if(actI != -1 && actJ != -1 && gemMatrix[actI][actJ] != 0){
						if(!isSwipe && (gemMatrix[actI][actJ] == 1 ||
								gemMatrix[actI][actJ] == 11 ||
								gemMatrix[actI][actJ] == 21 ||
								gemMatrix[actI][actJ] == 31 ||
								gemMatrix[actI][actJ] == 41) && i == actI && j == actJ)
							topaz[i][j].setBounds(0.085F*width + j*0.083765F*width - 0.005F*width, 0.625F*height - i*0.083765F*width - 0.005F*width, 0.08615F*width, 0.08615F*width);
					}
					topaz[i][j].draw(batch);
					break;
				}case 2:case 12:case 22:case 32:case 42:{
					if(!isVanishing) sapphire[i][j].setAlpha(1.0F);
					if(!isSwipe) sapphire[i][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height - i*0.083765F*width, 0.07615F*width, 0.07615F*width);
					if(actI != -1 && actJ != -1 && gemMatrix[actI][actJ] != 0){
						if(!isSwipe && (gemMatrix[actI][actJ] == 2 ||
								gemMatrix[actI][actJ] == 12 ||
								gemMatrix[actI][actJ] == 22 ||
								gemMatrix[actI][actJ] == 32 ||
								gemMatrix[actI][actJ] == 42) && i == actI && j == actJ)
							sapphire[i][j].setBounds(0.085F*width + j*0.083765F*width - 0.005F*width, 0.625F*height - i*0.083765F*width - 0.005F*width, 0.08615F*width, 0.08615F*width);
					}
					sapphire[i][j].draw(batch);
					break;
				}case 3:case 13:case 23:case 33:case 43:{
					if(!isVanishing) ruby[i][j].setAlpha(1.0F);
					if(!isSwipe) ruby[i][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height - i*0.083765F*width, 0.07615F*width, 0.07615F*width);
					if(actI != -1 && actJ != -1 && gemMatrix[actI][actJ] != 0){
						if(!isSwipe && (gemMatrix[actI][actJ] == 3 ||
								gemMatrix[actI][actJ] == 13 ||
								gemMatrix[actI][actJ] == 23 ||
								gemMatrix[actI][actJ] == 33 ||
								gemMatrix[actI][actJ] == 43) && i == actI && j == actJ)
							ruby[i][j].setBounds(0.085F*width + j*0.083765F*width - 0.005F*width, 0.625F*height - i*0.083765F*width - 0.005F*width, 0.08615F*width, 0.08615F*width);
					}
					ruby[i][j].draw(batch);
					break;
				}case 4:case 14:case 24:case 34:case 44:{
					if(!isVanishing) emerald[i][j].setAlpha(1.0F);
					if(!isSwipe) emerald[i][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height - i*0.083765F*width, 0.07615F*width, 0.07615F*width);
					if(actI != -1 && actJ != -1 && gemMatrix[actI][actJ] != 0){
						if(!isSwipe && (gemMatrix[actI][actJ] == 4 ||
								gemMatrix[actI][actJ] == 14 ||
								gemMatrix[actI][actJ] == 24 ||
								gemMatrix[actI][actJ] == 34 ||
								gemMatrix[actI][actJ] == 44) && i == actI && j == actJ)
							emerald[i][j].setBounds(0.085F*width + j*0.083765F*width - 0.005F*width, 0.625F*height - i*0.083765F*width - 0.005F*width, 0.08615F*width, 0.08615F*width);
					}
					emerald[i][j].draw(batch);
					break;
				}default:{
					break;
				}
				}
			}
		}
		for(int f=0;f<explosions.size();f++){
			if(!explosions.get(f)[0].isDeath()){
				if(explosions.get(f)[0].getTimer() <= 0.0F) explosions.get(f)[0].setDeath(true);
				explosions.get(f)[0].draw(batch);
			}
			if(explosions.get(f)[0].isDeath() && !explosions.get(f)[1].isDeath()){
				if(explosions.get(f)[1].getTimer() <= 0.0F) explosions.get(f)[1].setDeath(true);
				explosions.get(f)[1].draw(batch);
			}
			if(explosions.get(f)[0].isDeath() && explosions.get(f)[1].isDeath() && !explosions.get(f)[2].isDeath()){
				if(explosions.get(f)[2].getTimer() <= 0.0F) explosions.get(f)[2].setDeath(true);
				explosions.get(f)[2].draw(batch);
			}
			if(explosions.get(f)[0].isDeath() && explosions.get(f)[1].isDeath() && explosions.get(f)[2].isDeath() && !explosions.get(f)[3].isDeath()){
				if(explosions.get(f)[3].getTimer() <= 0.0F) explosions.get(f)[3].setDeath(true);
				explosions.get(f)[3].draw(batch);
			}
			if(explosions.get(f)[0].isDeath() && explosions.get(f)[1].isDeath() && explosions.get(f)[2].isDeath() && explosions.get(f)[3].isDeath() && !explosions.get(f)[4].isDeath()){
				if(explosions.get(f)[4].getTimer() <= 0.0F) explosions.get(f)[4].setDeath(true);
				explosions.get(f)[4].draw(batch);
			}
			if(explosions.get(f)[0].isDeath() && explosions.get(f)[1].isDeath() && explosions.get(f)[2].isDeath() && explosions.get(f)[3].isDeath() && explosions.get(f)[4].isDeath() && !explosions.get(f)[5].isDeath()){
				if(explosions.get(f)[5].getTimer() <= 0.0F) explosions.get(f)[5].setDeath(true);
				explosions.get(f)[5].draw(batch);
			}else if(explosions.get(f)[0].isDeath() && explosions.get(f)[1].isDeath() && explosions.get(f)[2].isDeath() && explosions.get(f)[3].isDeath() && explosions.get(f)[4].isDeath() && explosions.get(f)[5].isDeath()){
				explosions.remove(f);
			}
		}
	}
	private void rocketDraw(float delta){
		if(rocketS.getY() <= 1.0F*height && !isGameOver){
			rocketS.setY(1.0F*height);
			camera.position.y -= 5.0F;
		}else{
			if(!isGameOver){
				rocketS.setY(rocketS.getY() - speed);
				fire[0].setY(fire[0].getY() - speed);
				fire[1].setY(fire[1].getY() - speed);
				if(INF.elapsedTime % 5 == 0){
					if(fireIter == 0){
						fireIter = 1;
					}else{
						fireIter = 0;
					}
				}
				fire[fireIter].draw(batch);
			}else if(camera.position.y >= (backgroundSprite.getY() + backgroundSprite.getHeight()) - height/2){
				rocketS.setY(rocketS.getY() + speed);
				fire[0].setY(fire[0].getY() + speed);
				fire[1].setY(fire[1].getY() + speed);
				if(INF.elapsedTime % 5 == 0){
					if(fireIter == 0){
						fireIter = 1;
					}else{
						fireIter = 0;
					}
				}
				fire[fireIter].draw(batch);
			}else if(overTimer <= 0.0F){
				camera.position.y += 5.0F;
			}else{
				music.stop();
				if(!isSirenPlaying){
					siren.play();
					isSirenPlaying = true;
				}
				overTimer -= delta;
			}
		}
		rocketS.getSprite().draw(batch);
	}

	private boolean addition(){
		boolean check = false;
		for(int j=0;j<10;j++){
			if(gemMatrix[0][j] == 0){
				check = true;
				gemMatrix[0][j] = rand.nextInt(4) + 1;
				
				if(INF.isBombActive)
					if(rand.nextInt(30) == 0)
						gemMatrix[0][j] += 10;
				if(INF.isColoumnActive)
					if(rand.nextInt(40) == 0)
						gemMatrix[0][j] += 20;
				if(INF.isLineActive)
					if(rand.nextInt(50) == 0)
						gemMatrix[0][j] += 30;
				if(INF.isTimeActive)
					if(rand.nextInt(60) == 0)
						gemMatrix[0][j] += 40;
				
				switch (gemMatrix[0][j]){
				case 1:{
					topaz[0][j] = RES.atlas.createSprite("topaz");
					topaz[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 2:{
					sapphire[0][j] = RES.atlas.createSprite("sapphire");
					sapphire[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 3:{
					ruby[0][j] = RES.atlas.createSprite("ruby");
					ruby[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 4:{
					emerald[0][j] = RES.atlas.createSprite("emerald");
					emerald[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 11:{
					topaz[0][j] = RES.atlas.createSprite("topazB");
					topaz[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 21:{
					topaz[0][j] = RES.atlas.createSprite("topazC");
					topaz[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 31:{
					topaz[0][j] = RES.atlas.createSprite("topazL");
					topaz[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 41:{
					topaz[0][j] = RES.atlas.createSprite("topazT");
					topaz[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 12:{
					sapphire[0][j] = RES.atlas.createSprite("sapphireB");
					sapphire[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 22:{
					sapphire[0][j] = RES.atlas.createSprite("sapphireC");
					sapphire[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 32:{
					sapphire[0][j] = RES.atlas.createSprite("sapphireL");
					sapphire[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 42:{
					sapphire[0][j] = RES.atlas.createSprite("sapphireT");
					sapphire[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 13:{
					ruby[0][j] = RES.atlas.createSprite("rubyB");
					ruby[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 23:{
					ruby[0][j] = RES.atlas.createSprite("rubyC");
					ruby[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 33:{
					ruby[0][j] = RES.atlas.createSprite("rubyL");
					ruby[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 43:{
					ruby[0][j] = RES.atlas.createSprite("rubyT");
					ruby[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 14:{
					emerald[0][j] = RES.atlas.createSprite("emeraldB");
					emerald[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 24:{
					emerald[0][j] = RES.atlas.createSprite("emeraldC");
					emerald[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 34:{
					emerald[0][j] = RES.atlas.createSprite("emeraldL");
					emerald[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}case 44:{
					emerald[0][j] = RES.atlas.createSprite("emeraldT");
					emerald[0][j].setBounds(0.085F*width + j*0.083765F*width, 0.625F*height, 0.07615F*width, 0.07615F*width);
					break;
				}default:{
					break;
				}
				}
			}
		}
		if(!check) return false;
		else return true;
	}
	private void vanish(){
		if(gemVan >= 0.1F && !isGravity){
			gemVan -= 0.1F;
		}else{
			if(routeCount1 > 2){
				crash[rand.nextInt(3)].play(0.1F);
				for(int i=0;i<5;i++){
					for(int j=0;j<10;j++){
						switch(gemMatrix[i][j]){
						case 11:case 12:case 13:case 14:{
							if(route1[i][j]){
								if(i!=0 && (gemMatrix[i-1][j] != gemMatrix[i][j] && gemMatrix[i-1][j] != gemMatrix[i][j] - 10
										&& gemMatrix[i-1][j] != gemMatrix[i][j] - 20 && gemMatrix[i-1][j] != gemMatrix[i][j] - 30
										&& gemMatrix[i-1][j] != gemMatrix[i][j] - 40 && gemMatrix[i-1][j] - 10 != gemMatrix[i][j]
										&& gemMatrix[i-1][j] - 20 != gemMatrix[i][j] && gemMatrix[i-1][j] - 30 != gemMatrix[i][j]
										&& gemMatrix[i-1][j] - 40 != gemMatrix[i][j]) && gemMatrix[i-1][j] != 9)
								{
									route1[i-1][j] = true; routeCount1++;
								}
								if(i!=4 && (gemMatrix[i+1][j] != gemMatrix[i][j] && gemMatrix[i+1][j] != gemMatrix[i][j] - 10
										&& gemMatrix[i+1][j] != gemMatrix[i][j] - 20 && gemMatrix[i+1][j] != gemMatrix[i][j] - 30
										&& gemMatrix[i+1][j] != gemMatrix[i][j] - 40 && gemMatrix[i+1][j] - 10 != gemMatrix[i][j]
										&& gemMatrix[i+1][j] - 20 != gemMatrix[i][j] && gemMatrix[i+1][j] - 30 != gemMatrix[i][j]
										&& gemMatrix[i+1][j] - 40 != gemMatrix[i][j]) && gemMatrix[i+1][j] != 9)
								{
									route1[i+1][j] = true; routeCount1++;
								}
								if(j!=0 && (gemMatrix[i][j-1] != gemMatrix[i][j] && gemMatrix[i][j-1] != gemMatrix[i][j] - 10
										&& gemMatrix[i][j-1] != gemMatrix[i][j] - 20 && gemMatrix[i][j-1] != gemMatrix[i][j] - 30
										&& gemMatrix[i][j-1] != gemMatrix[i][j] - 40 && gemMatrix[i][j-1] - 10 != gemMatrix[i][j]
										&& gemMatrix[i][j-1] - 20 != gemMatrix[i][j] && gemMatrix[i][j-1] - 30 != gemMatrix[i][j]
										&& gemMatrix[i][j-1] - 40 != gemMatrix[i][j]) && gemMatrix[i][j-1] != 9)
								{ 
									route1[i][j-1] = true; routeCount1++;
								}
								if(j!=9 && (gemMatrix[i][j+1] != gemMatrix[i][j] && gemMatrix[i][j+1] != gemMatrix[i][j] - 10
										&& gemMatrix[i][j+1] != gemMatrix[i][j] - 20 && gemMatrix[i][j+1] != gemMatrix[i][j] - 30
										&& gemMatrix[i][j+1] != gemMatrix[i][j] - 40 && gemMatrix[i][j+1] - 10 != gemMatrix[i][j]
										&& gemMatrix[i][j+1] - 20 != gemMatrix[i][j] && gemMatrix[i][j+1] - 30 != gemMatrix[i][j]
										&& gemMatrix[i][j+1] - 40 != gemMatrix[i][j]) && gemMatrix[i][j+1] != 9)
								{
									route1[i][j+1] = true; routeCount1++;
								}
								explosions.add(bombExplosion(i, j, 0.045F));
								explosion.play();
							}
							break;
						}case 21:case 22:case 23:case 24:{
							if(route1[i][j]){
								for(int f=0;f<5;f++){	
									if(!(route1[f][j] || route2[f][j]) && gemMatrix[f][j] != 9)
										route1[f][j] = true; routeCount1++;
										explosions.add(coloumn$LineExplosion(f, j, 0.045F));
										explosion.play();
								}
							}
							break;
						}case 31:case 32:case 33:case 34:{
							if(route1[i][j]){
								for(int f=0;f<10;f++){
									if(!(route1[i][f] || route2[i][f]) && gemMatrix[i][f] != 9)
										route1[i][f] = true; routeCount1++;
									explosions.add(coloumn$LineExplosion(i, f, 0.045F));
									explosion.play();
								}
							}
							break;
						}case 41:case 42:case 43:case 44:{
							if(route1[i][j]){
								explosions.add(coloumn$LineExplosion(i, j, 0.045F));
								timer += 5*60;
								bonusTime += 5;
								time.play();
							}
							break;
						}
						}
					}
				}
				for(int i=0;i<5;i++){
					for(int j=0;j<10;j++){
						if(route1[i][j]){
							switch(gemMatrix[i][j]){
							case 1:case 11:case 21:case 31:case 41:{
								topaz[i][j] = null;
								break;
							}case 2:case 12:case 22:case 32:case 42:{
								sapphire[i][j] = null;
								break;
							}case 3:case 13:case 23:case 33:case 43:{
								ruby[i][j] = null;
								break;
							}case 4:case 14:case 24:case 34:case 44:{
								emerald[i][j] = null;
								break;
							}default:{
								break;
							}
							}
							gemMatrix[i][j] = 0;
						}
					}
				}
			}
			if(routeCount2 > 2){
				crash[rand.nextInt(3)].play(0.1F);
				for(int i=0;i<5;i++){
					for(int j=0;j<10;j++){
						switch(gemMatrix[i][j]){
						case 11:case 12:case 13:case 14:{
							if(route2[i][j]){
								if(i!=0 && (gemMatrix[i-1][j] != gemMatrix[i][j] && gemMatrix[i-1][j] != gemMatrix[i][j] - 10
										&& gemMatrix[i-1][j] != gemMatrix[i][j] - 20 && gemMatrix[i-1][j] != gemMatrix[i][j] - 30
										&& gemMatrix[i-1][j] != gemMatrix[i][j] - 40 && gemMatrix[i-1][j] - 10 != gemMatrix[i][j]
										&& gemMatrix[i-1][j] - 20 != gemMatrix[i][j] && gemMatrix[i-1][j] - 30 != gemMatrix[i][j]
										&& gemMatrix[i-1][j] - 40 != gemMatrix[i][j]) && gemMatrix[i-1][j] != 9)
								{
									route2[i-1][j] = true; routeCount2++;
								}
								if(i!=4 && (gemMatrix[i+1][j] != gemMatrix[i][j] && gemMatrix[i+1][j] != gemMatrix[i][j] - 10
										&& gemMatrix[i+1][j] != gemMatrix[i][j] - 20 && gemMatrix[i+1][j] != gemMatrix[i][j] - 30
										&& gemMatrix[i+1][j] != gemMatrix[i][j] - 40 && gemMatrix[i+1][j] - 10 != gemMatrix[i][j]
										&& gemMatrix[i+1][j] - 20 != gemMatrix[i][j] && gemMatrix[i+1][j] - 30 != gemMatrix[i][j]
										&& gemMatrix[i+1][j] - 40 != gemMatrix[i][j]) && gemMatrix[i+1][j] != 9)
								{
									route2[i+1][j] = true; routeCount2++;
								}
								if(j!=0 && (gemMatrix[i][j-1] != gemMatrix[i][j] && gemMatrix[i][j-1] != gemMatrix[i][j] - 10
										&& gemMatrix[i][j-1] != gemMatrix[i][j] - 20 && gemMatrix[i][j-1] != gemMatrix[i][j] - 30
										&& gemMatrix[i][j-1] != gemMatrix[i][j] - 40 && gemMatrix[i][j-1] - 10 != gemMatrix[i][j]
										&& gemMatrix[i][j-1] - 20 != gemMatrix[i][j] && gemMatrix[i][j-1] - 30 != gemMatrix[i][j]
										&& gemMatrix[i][j-1] - 40 != gemMatrix[i][j]) && gemMatrix[i][j-1] != 9)
								{ 
									route2[i][j-1] = true; routeCount2++;
								}
								if(j!=9 && (gemMatrix[i][j+1] != gemMatrix[i][j] && gemMatrix[i][j+1] != gemMatrix[i][j] - 10
										&& gemMatrix[i][j+1] != gemMatrix[i][j] - 20 && gemMatrix[i][j+1] != gemMatrix[i][j] - 30
										&& gemMatrix[i][j+1] != gemMatrix[i][j] - 40 && gemMatrix[i][j+1] - 10 != gemMatrix[i][j]
										&& gemMatrix[i][j+1] - 20 != gemMatrix[i][j] && gemMatrix[i][j+1] - 30 != gemMatrix[i][j]
										&& gemMatrix[i][j+1] - 40 != gemMatrix[i][j]) && gemMatrix[i][j+1] != 9)
								{
									route2[i][j+1] = true; routeCount2++;
								}
								explosions.add(bombExplosion(i, j, 0.045F));
								explosion.play();
							}
							break;
						}case 21:case 22:case 23:case 24:{
							if(route2[i][j]){
								for(int f=0;f<5;f++){
									if(!(route2[f][j] || route1[f][j]) && gemMatrix[f][j] != 9)
										route2[f][j] = true; routeCount2++;
									explosions.add(coloumn$LineExplosion(f, j, 0.045F));
									explosion.play();
								}
							}
							break;
						}case 31:case 32:case 33:case 34:{
							if(route2[i][j]){
								for(int f=0;f<10;f++){
									if(!(route2[i][f] || route1[i][f]) && gemMatrix[i][f] != 9)
										route2[i][f] = true; routeCount2++;
									explosions.add(coloumn$LineExplosion(i, f, 0.045F));
									explosion.play();
								}
							}
							break;
						}case 41:case 42:case 43:case 44:{
							if(route2[i][j]){
								explosions.add(coloumn$LineExplosion(i, j, 0.045F));
								timer += 5*60;
								bonusTime += 5;
								time.play();
							}
							break;
						}
						}
					}
				}
				for(int i=0;i<5;i++){
					for(int j=0;j<10;j++){
						if(route2[i][j]){
							switch(gemMatrix[i][j]){
							case 1:case 11:case 21:case 31:case 41:{
								topaz[i][j] = null;
								break;
							}case 2:case 12:case 22:case 32:case 42:{
								sapphire[i][j] = null;
								break;
							}case 3:case 13:case 23:case 33:case 43:{
								ruby[i][j] = null;
								break;
							}case 4:case 14:case 24:case 34:case 44:{
								emerald[i][j] = null;
								break;
							}default:{
								break;
							}
							}
							gemMatrix[i][j] = 0;
						}
					}
				}
			}
			gravity();
			routeReset();
			gemVan = 1.0F;
			isVanishing = false;
			return;
		}
		for(int i=0;i<5;i++){
			for(int j=0;j<10;j++){
				if(route1[i][j] && routeCount1 > 2){
					switch(gemMatrix[i][j]){
					case 1:case 11:case 21:case 31:case 41:{
						topaz[i][j].setAlpha(gemVan);
						break;
					}case 2:case 12:case 22:case 32:case 42:{
						sapphire[i][j].setAlpha(gemVan);
						break;
					}case 3:case 13:case 23:case 33:case 43:{
						ruby[i][j].setAlpha(gemVan);
						break;
					}case 4:case 14:case 24:case 34:case 44:{
						emerald[i][j].setAlpha(gemVan);
						break;
					}default:{
						break;
					}
					}
				}
				if(route2[i][j] && routeCount2 > 2){
					switch(gemMatrix[i][j]){
					case 1:case 11:case 21:case 31:case 41:{
						topaz[i][j].setAlpha(gemVan);
						break;
					}case 2:case 12:case 22:case 32:case 42:{
						sapphire[i][j].setAlpha(gemVan);
						break;
					}case 3:case 13:case 23:case 33:case 43:{
						ruby[i][j].setAlpha(gemVan);
						break;
					}case 4:case 14:case 24:case 34:case 44:{
						emerald[i][j].setAlpha(gemVan);
						break;
					}default:{
						break;
					}
					}
				}
			}
		}
	}
	private void routeReset(){
		for(int m=0;m<5;m++){
			for(int n=0;n<10;n++){
				route1[m][n] = false;
				route2[m][n] = false;
			}
		}
		if(routeCount1 > 2) score += routeCount1;
		if(routeCount2 > 2) score += routeCount2;
		routeCount1 = 0;
		routeCount2 = 0;
	}
	private void gemCheck(int i, int j, boolean isFirst){
		if(isFirst){
			route1[i][j] = true;
			routeCount1++;
			if(i != 0){
				if((gemMatrix[i-1][j] == gemMatrix[i][j]
						|| gemMatrix[i-1][j] == gemMatrix[i][j] - 10
						|| gemMatrix[i-1][j] == gemMatrix[i][j] - 20
						|| gemMatrix[i-1][j] == gemMatrix[i][j] - 30
						|| gemMatrix[i-1][j] == gemMatrix[i][j] - 40
						|| gemMatrix[i-1][j] - 10 == gemMatrix[i][j]
						|| gemMatrix[i-1][j] - 20 == gemMatrix[i][j]
						|| gemMatrix[i-1][j] - 30 == gemMatrix[i][j]
						|| gemMatrix[i-1][j] - 40 == gemMatrix[i][j]) && !route1[i-1][j] && gemMatrix[i][j] != 0){
					gemCheck(i-1, j, isFirst);
				}
			}
			if(i != 4){
				if((gemMatrix[i+1][j] == gemMatrix[i][j]
						|| gemMatrix[i+1][j] == gemMatrix[i][j] - 10
						|| gemMatrix[i+1][j] == gemMatrix[i][j] - 20
						|| gemMatrix[i+1][j] == gemMatrix[i][j] - 30
						|| gemMatrix[i+1][j] == gemMatrix[i][j] - 40
						|| gemMatrix[i+1][j] - 10 == gemMatrix[i][j]
						|| gemMatrix[i+1][j] - 20 == gemMatrix[i][j]
						|| gemMatrix[i+1][j] - 30 == gemMatrix[i][j]
						|| gemMatrix[i+1][j] - 40 == gemMatrix[i][j]) && !route1[i+1][j] && gemMatrix[i][j] != 0){
					gemCheck(i+1, j, isFirst);
				}
			}
			if(j != 0){
				if((gemMatrix[i][j-1] == gemMatrix[i][j]
						|| gemMatrix[i][j-1] == gemMatrix[i][j] - 10
						|| gemMatrix[i][j-1] == gemMatrix[i][j] - 20
						|| gemMatrix[i][j-1] == gemMatrix[i][j] - 30
						|| gemMatrix[i][j-1] == gemMatrix[i][j] - 40
						|| gemMatrix[i][j-1] - 10 == gemMatrix[i][j]
						|| gemMatrix[i][j-1] - 20 == gemMatrix[i][j]
						|| gemMatrix[i][j-1] - 30 == gemMatrix[i][j]
						|| gemMatrix[i][j-1] - 40 == gemMatrix[i][j]) && !route1[i][j-1] && gemMatrix[i][j] != 0){
					gemCheck(i, j-1, isFirst);
				}
			}
			if(j != 9){
				if((gemMatrix[i][j+1] == gemMatrix[i][j]
						|| gemMatrix[i][j+1] == gemMatrix[i][j] - 10
						|| gemMatrix[i][j+1] == gemMatrix[i][j] - 20
						|| gemMatrix[i][j+1] == gemMatrix[i][j] - 30
						|| gemMatrix[i][j+1] == gemMatrix[i][j] - 40
						|| gemMatrix[i][j+1] - 10 == gemMatrix[i][j]
						|| gemMatrix[i][j+1] - 20 == gemMatrix[i][j]
						|| gemMatrix[i][j+1] - 30 == gemMatrix[i][j]
						|| gemMatrix[i][j+1] - 40 == gemMatrix[i][j]) && !route1[i][j+1] && gemMatrix[i][j] != 0){
					gemCheck(i, j+1, isFirst);
				}
			}
		}else{
			route2[i][j] = true;
			routeCount2++;
			if(i != 0){
				if((gemMatrix[i-1][j] == gemMatrix[i][j]
						|| gemMatrix[i-1][j] == gemMatrix[i][j] - 10
						|| gemMatrix[i-1][j] == gemMatrix[i][j] - 20
						|| gemMatrix[i-1][j] == gemMatrix[i][j] - 30
						|| gemMatrix[i-1][j] == gemMatrix[i][j] - 40
						|| gemMatrix[i-1][j] - 10 == gemMatrix[i][j]
						|| gemMatrix[i-1][j] - 20 == gemMatrix[i][j]
						|| gemMatrix[i-1][j] - 30 == gemMatrix[i][j]
						|| gemMatrix[i-1][j] - 40 == gemMatrix[i][j]) && !route2[i-1][j] && gemMatrix[i][j] != 0){
					gemCheck(i-1, j, isFirst);
				}
			}
			if(i != 4){
				if((gemMatrix[i+1][j] == gemMatrix[i][j]
						|| gemMatrix[i+1][j] == gemMatrix[i][j] - 10
						|| gemMatrix[i+1][j] == gemMatrix[i][j] - 20
						|| gemMatrix[i+1][j] == gemMatrix[i][j] - 30
						|| gemMatrix[i+1][j] == gemMatrix[i][j] - 40
						|| gemMatrix[i+1][j] - 10 == gemMatrix[i][j]
						|| gemMatrix[i+1][j] - 20 == gemMatrix[i][j]
						|| gemMatrix[i+1][j] - 30 == gemMatrix[i][j]
						|| gemMatrix[i+1][j] - 40 == gemMatrix[i][j]) && !route2[i+1][j] && gemMatrix[i][j] != 0){
					gemCheck(i+1, j, isFirst);
				}
			}
			if(j != 0){
				if((gemMatrix[i][j-1] == gemMatrix[i][j]
						|| gemMatrix[i][j-1] == gemMatrix[i][j] - 10
						|| gemMatrix[i][j-1] == gemMatrix[i][j] - 20
						|| gemMatrix[i][j-1] == gemMatrix[i][j] - 30
						|| gemMatrix[i][j-1] == gemMatrix[i][j] - 40
						|| gemMatrix[i][j-1] - 10 == gemMatrix[i][j]
						|| gemMatrix[i][j-1] - 20 == gemMatrix[i][j]
						|| gemMatrix[i][j-1] - 30 == gemMatrix[i][j]
						|| gemMatrix[i][j-1] - 40 == gemMatrix[i][j]) && !route2[i][j-1] && gemMatrix[i][j] != 0){
					gemCheck(i, j-1, isFirst);
				}
			}
			if(j != 9){
				if((gemMatrix[i][j+1] == gemMatrix[i][j]
						|| gemMatrix[i][j+1] == gemMatrix[i][j] - 10
						|| gemMatrix[i][j+1] == gemMatrix[i][j] - 20
						|| gemMatrix[i][j+1] == gemMatrix[i][j] - 30
						|| gemMatrix[i][j+1] == gemMatrix[i][j] - 40
						|| gemMatrix[i][j+1] - 10 == gemMatrix[i][j]
						|| gemMatrix[i][j+1] - 20 == gemMatrix[i][j]
						|| gemMatrix[i][j+1] - 30 == gemMatrix[i][j]
						|| gemMatrix[i][j+1] - 40 == gemMatrix[i][j]) && !route2[i][j+1] && gemMatrix[i][j] != 0){
					gemCheck(i, j+1, isFirst);
				}
			}
		}
		return;
	}
	private void buttonListener(){
		if(!isGameOver && startTimer <= 0.0F){
			for(int i=0;i<5;i++){
				for(int j=0;j<10;j++){
					if(controller.isOnCam(0.085F*width + j*0.083765F*width, 0.625F*height - i*0.083765F*width, 0.07615F*width, 0.07615F*width, camera, 1.0F) && !isSwipe){
						if(actI != -1 && actJ != -1 
								&& gemMatrix[actI][actJ] != 0 
								&& gemMatrix[i][j] != 0 
								&& (gemMatrix[i][j] != gemMatrix[actI][actJ] &&
								gemMatrix[i][j] - 10 != gemMatrix[actI][actJ] &&
								gemMatrix[i][j] - 20 != gemMatrix[actI][actJ] &&
								gemMatrix[i][j] - 30 != gemMatrix[actI][actJ] &&
								gemMatrix[i][j] - 40 != gemMatrix[actI][actJ] &&
								gemMatrix[i][j] != gemMatrix[actI][actJ] - 10 &&
								gemMatrix[i][j] != gemMatrix[actI][actJ] - 20 &&	
								gemMatrix[i][j] != gemMatrix[actI][actJ] - 30 &&
								gemMatrix[i][j] != gemMatrix[actI][actJ] - 40 && 
								gemMatrix[actI][actJ] != 9 && gemMatrix[i][j] != 9)){
							if(i == actI-1 && j == actJ){
								swipeI[0] = i; swipeJ[0] = j; swipeA[0] = actI; swipeB[0] = actJ;
								swipeSpeed = 5.0F*0.001F*width;
								swipeCount = 1;
								isSwipe = true;
							}
							if(i == actI+1 && j == actJ){
								swipeI[0] = i; swipeJ[0] = j; swipeA[0] = actI; swipeB[0] = actJ;
								swipeSpeed = 5.0F*0.001F*width;
								swipeCount = 1;
								isSwipe = true;
							}
							if(i == actI && j == actJ-1){
								swipeI[0] = i; swipeJ[0] = j; swipeA[0] = actI; swipeB[0] = actJ;
								swipeSpeed = 5.0F*0.001F*width;
								swipeCount = 1;
								isSwipe = true;
							}
							if(i == actI && j == actJ+1){
								swipeI[0] = i; swipeJ[0] = j; swipeA[0] = actI; swipeB[0] = actJ;
								swipeSpeed = 5.0F*0.001F*width;
								swipeCount = 1;
								isSwipe = true;
							}
							actI = -1;
							actJ = -1;
						}else if(gemMatrix[i][j] != 0){
							actI = i;
							actJ = j;
						}
					}
				}
			}
		}
		if(isGameOver){
			if(controller.isClickedCam(back.getX(camera, width), back.getY(camera, height), back.getWidth(), back.getHeight(), true, camera, 1.0F)){
				game.setScreen(new ru.erked.spaceflight.game.ResultsScreen(game, gemsToWin, score, timeLeft, isVictory, isEndlessMode));
				this.dispose();
			}
		}
	}
	private void swap(int i, int j, int a, int b, boolean isGravity){
		//
		if(emerald[i][j] != null && emerald[a][b] == null){
			emerald[a][b] = emerald[i][j];
			emerald[i][j] = null;
		}else if(emerald[a][b] != null && emerald[i][j] == null){
			emerald[i][j] = emerald[a][b];
			emerald[a][b] = null;
		}
		if(ruby[i][j] != null && ruby[a][b] == null){
			ruby[a][b] = ruby[i][j];
			ruby[i][j] = null;
		}else if(ruby[a][b] != null && ruby[i][j] == null){
			ruby[i][j] = ruby[a][b];
			ruby[a][b] = null;
		}
		if(sapphire[i][j] != null && sapphire[a][b] == null){
			sapphire[a][b] = sapphire[i][j];
			sapphire[i][j] = null;
		}else if(sapphire[a][b] != null && sapphire[i][j] == null){
			sapphire[i][j] = sapphire[a][b];
			sapphire[a][b] = null;
		}
		if(topaz[i][j] != null && topaz[a][b] == null){
			topaz[a][b] = topaz[i][j];
			topaz[i][j] = null;
		}else if(topaz[a][b] != null && topaz[i][j] == null){
			topaz[i][j] = topaz[a][b];
			topaz[a][b] = null;
		}
		//
		int c = gemMatrix[i][j];
		gemMatrix[i][j] = gemMatrix[a][b];
		gemMatrix[a][b] = c;
		//
		if(!isGravity){
			gemCheck(i, j, true);
			gemCheck(a, b, false);
		}
		gemVan = 1.0F;
		isVanishing = true;
	}
	private void swipe(int[] i, int[] j, int[] a, int[] b, int n, float speed){
		swipeIter = 0;
		for(int f=0;f<n;f++){
			float 
			x1 = 0.085F*width + j[f]*0.083765F*width,
			y1 = 0.625F*height - i[f]*0.083765F*width,
			x2 = 0.085F*width + b[f]*0.083765F*width, 
			y2 = 0.625F*height - a[f]*0.083765F*width;
			if(emerald[i[f]][j[f]] != null && emerald[a[f]][b[f]] == null){
				if(emerald[i[f]][j[f]].getX() - speed > x2)
					emerald[i[f]][j[f]].setX(emerald[i[f]][j[f]].getX() - speed);
				else if(emerald[i[f]][j[f]].getX() + speed < x2)
					emerald[i[f]][j[f]].setX(emerald[i[f]][j[f]].getX() + speed);
				else if(emerald[i[f]][j[f]].getY() - speed > y2)
					emerald[i[f]][j[f]].setY(emerald[i[f]][j[f]].getY() - speed);
				else if(emerald[i[f]][j[f]].getY() + speed < y2)
					emerald[i[f]][j[f]].setY(emerald[i[f]][j[f]].getY() + speed);
				else{
					emerald[i[f]][j[f]].setX(x2);
					emerald[i[f]][j[f]].setY(y2);
					swipeIter++;
					swap(i[f], j[f], a[f], b[f], isGravity);
				}
			}else if(emerald[a[f]][b[f]] != null && emerald[i[f]][j[f]] == null){
				if(emerald[a[f]][b[f]].getX() - speed > x1)
					emerald[a[f]][b[f]].setX(emerald[a[f]][b[f]].getX() - speed);
				else if(emerald[a[f]][b[f]].getX() + speed < x1)
					emerald[a[f]][b[f]].setX(emerald[a[f]][b[f]].getX() + speed);
				else if(emerald[a[f]][b[f]].getY() - speed > y1)
					emerald[a[f]][b[f]].setY(emerald[a[f]][b[f]].getY() - speed);
				else if(emerald[a[f]][b[f]].getY() + speed < y1)
					emerald[a[f]][b[f]].setY(emerald[a[f]][b[f]].getY() + speed);
				else{
					emerald[a[f]][b[f]].setX(x1);
					emerald[a[f]][b[f]].setY(y1);
					swipeIter++;
					swap(i[f], j[f], a[f], b[f], isGravity);
				}
			}
			if(ruby[i[f]][j[f]] != null && ruby[a[f]][b[f]] == null){
				if(ruby[i[f]][j[f]].getX() - speed > x2)
					ruby[i[f]][j[f]].setX(ruby[i[f]][j[f]].getX() - speed);
				else if(ruby[i[f]][j[f]].getX() + speed < x2)
					ruby[i[f]][j[f]].setX(ruby[i[f]][j[f]].getX() + speed);
				else if(ruby[i[f]][j[f]].getY() - speed > y2)
					ruby[i[f]][j[f]].setY(ruby[i[f]][j[f]].getY() - speed);
				else if(ruby[i[f]][j[f]].getY() + speed < y2)
					ruby[i[f]][j[f]].setY(ruby[i[f]][j[f]].getY() + speed);
				else{
					ruby[i[f]][j[f]].setX(x2);
					ruby[i[f]][j[f]].setY(y2);
					swipeIter++;
					swap(i[f], j[f], a[f], b[f], isGravity);
				}
			}else if(ruby[a[f]][b[f]] != null && ruby[i[f]][j[f]] == null){
				if(ruby[a[f]][b[f]].getX() - speed > x1)
					ruby[a[f]][b[f]].setX(ruby[a[f]][b[f]].getX() - speed);
				else if(ruby[a[f]][b[f]].getX() + speed < x1)
					ruby[a[f]][b[f]].setX(ruby[a[f]][b[f]].getX() + speed);
				else if(ruby[a[f]][b[f]].getY() - speed > y1)
					ruby[a[f]][b[f]].setY(ruby[a[f]][b[f]].getY() - speed);
				else if(ruby[a[f]][b[f]].getY() + speed < y1)
					ruby[a[f]][b[f]].setY(ruby[a[f]][b[f]].getY() + speed);
				else{
					ruby[a[f]][b[f]].setX(x1);
					ruby[a[f]][b[f]].setY(y1);
					swipeIter++;
					swap(i[f], j[f], a[f], b[f], isGravity);
				}
			}
			if(sapphire[i[f]][j[f]] != null && sapphire[a[f]][b[f]] == null){
				if(sapphire[i[f]][j[f]].getX() - speed > x2)
					sapphire[i[f]][j[f]].setX(sapphire[i[f]][j[f]].getX() - speed);
				else if(sapphire[i[f]][j[f]].getX() + speed < x2)
					sapphire[i[f]][j[f]].setX(sapphire[i[f]][j[f]].getX() + speed);
				else if(sapphire[i[f]][j[f]].getY() - speed > y2)
					sapphire[i[f]][j[f]].setY(sapphire[i[f]][j[f]].getY() - speed);
				else if(sapphire[i[f]][j[f]].getY() + speed < y2)
					sapphire[i[f]][j[f]].setY(sapphire[i[f]][j[f]].getY() + speed);
				else{
					sapphire[i[f]][j[f]].setX(x2);
					sapphire[i[f]][j[f]].setY(y2);
					swipeIter++;
					swap(i[f], j[f], a[f], b[f], isGravity);
				}
			}else if(sapphire[a[f]][b[f]] != null && sapphire[i[f]][j[f]] == null){
				if(sapphire[a[f]][b[f]].getX() - speed > x1)
					sapphire[a[f]][b[f]].setX(sapphire[a[f]][b[f]].getX() - speed);
				else if(sapphire[a[f]][b[f]].getX() + speed < x1)
					sapphire[a[f]][b[f]].setX(sapphire[a[f]][b[f]].getX() + speed);
				else if(sapphire[a[f]][b[f]].getY() - speed > y1)
					sapphire[a[f]][b[f]].setY(sapphire[a[f]][b[f]].getY() - speed);
				else if(sapphire[a[f]][b[f]].getY() + speed < y1)
					sapphire[a[f]][b[f]].setY(sapphire[a[f]][b[f]].getY() + speed);
				else{
					sapphire[a[f]][b[f]].setX(x1);
					sapphire[a[f]][b[f]].setY(y1);
					swipeIter++;
					swap(i[f], j[f], a[f], b[f], isGravity);
				}
			}
			if(topaz[i[f]][j[f]] != null && topaz[a[f]][b[f]] == null){
					if(topaz[i[f]][j[f]].getX() - speed > x2)
					topaz[i[f]][j[f]].setX(topaz[i[f]][j[f]].getX() - speed);
				else if(topaz[i[f]][j[f]].getX() + speed < x2)
					topaz[i[f]][j[f]].setX(topaz[i[f]][j[f]].getX() + speed);
				else if(topaz[i[f]][j[f]].getY() - speed > y2)
					topaz[i[f]][j[f]].setY(topaz[i[f]][j[f]].getY() - speed);
				else if(topaz[i[f]][j[f]].getY() + speed < y2)
					topaz[i[f]][j[f]].setY(topaz[i[f]][j[f]].getY() + speed);
				else{
					topaz[i[f]][j[f]].setX(x2);
					topaz[i[f]][j[f]].setY(y2);
					swipeIter++;
					swap(i[f], j[f], a[f], b[f], isGravity);
					}
				}else if(topaz[a[f]][b[f]] != null && topaz[i[f]][j[f]] == null){
					if(topaz[a[f]][b[f]].getX() - speed > x1)
						topaz[a[f]][b[f]].setX(topaz[a[f]][b[f]].getX() - speed);
					else if(topaz[a[f]][b[f]].getX() + speed < x1)
					topaz[a[f]][b[f]].setX(topaz[a[f]][b[f]].getX() + speed);
				else if(topaz[a[f]][b[f]].getY() - speed > y1)
					topaz[a[f]][b[f]].setY(topaz[a[f]][b[f]].getY() - speed);
				else if(topaz[a[f]][b[f]].getY() + speed < y1)
					topaz[a[f]][b[f]].setY(topaz[a[f]][b[f]].getY() + speed);
				else{
					topaz[a[f]][b[f]].setX(x1);
					topaz[a[f]][b[f]].setY(y1);
					swipeIter++;
					swap(i[f], j[f], a[f], b[f], isGravity);
				}
			}
		}
		if(swipeIter==n){
			if(swipeIter == 0){ 
				isGravity = false;
				swipeSpeed = 5.0F*0.001F*width;
			}
			if(addition()){
				gravity();
			}else{
				isSwipe = false;
				swipeIter = 0;
			}
		}
	}
	private void gravity(){
		isGravity = true;
		swipeSpeed = 20.0F*0.001F*width;
		int count = 0;
		for(int i=0;i<4;i++){
			for(int j=0;j<10;j++){
				if(gemMatrix[i][j] != 0 && gemMatrix[i+1][j] == 0){
					swipeI[count] = i;
					swipeJ[count] = j;
					swipeA[count] = i+1;
					swipeB[count] = j;
					count++;
				}
			}
		}
		if(count == 0){
			for(int m=0;m<50;m++){
				swipeI[m] = -1;
				swipeJ[m] = -1;
				swipeA[m] = -1;
				swipeB[m] = -1;
			}
			swipeCount = 0;
		}
		swipeCount = count;
		isSwipe = true;
	}
	
	private void touchUpdate(){
		if(camera.position.x < backgroundSprite.getX() + width/2)
			camera.position.set(new Vector3(backgroundSprite.getX() + width/2, camera.position.y, 0));
		if(camera.position.y < backgroundSprite.getY() + height/2)
			camera.position.set(new Vector3(camera.position.x, backgroundSprite.getY() + height/2, 0));
		if(camera.position.x > (backgroundSprite.getX() + backgroundSprite.getWidth()) - width/2)
			camera.position.set(new Vector3((backgroundSprite.getX() + backgroundSprite.getWidth()) - width/2, camera.position.y, 0));
		if(camera.position.y > (backgroundSprite.getY() + backgroundSprite.getHeight()) - height/2)
			camera.position.set(new Vector3(camera.position.x, (backgroundSprite.getY() + backgroundSprite.getHeight()) - height/2, 0));
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
		music.pause();
	}
	@Override
	public void resume() {
		music.play();
	}
	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		game.dispose();
		batch.dispose();
		text.dispose();
		music.stop();
	}

}