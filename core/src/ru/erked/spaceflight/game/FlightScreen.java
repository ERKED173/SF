package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.Data;
import ru.erked.spaceflight.StartSFlight;
import ru.erked.spaceflight.planets.DertenScreen;
import ru.erked.spaceflight.planets.EmionScreen;
import ru.erked.spaceflight.planets.IngmarScreen;
import ru.erked.spaceflight.planets.LoonScreen;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;
import ru.erked.spaceflight.tech.CurPR;
import ru.erked.spaceflight.tech.Rocket;
import ru.erked.spaceflight.tech.SFButtonS;

public class FlightScreen implements Screen{
	
	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	private float fps = Gdx.graphics.getFramesPerSecond();
	
	private final StartSFlight game;
	private Screen planetScreen;
	private Music flight = Gdx.audio.newMusic(Gdx.files.internal("sounds/misc/startFlight.mp3"));
	
	private Sprite backgroundSprite;
	
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
	private float speed = 0.001F*((float)60.0F/fps);
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private Data data;
	
	public FlightScreen(final StartSFlight game){
		this.game = game;
		data = game.data;
	}
	
	@Override
	public void show() {

		if(CurPR.getCurPlanet() != null){
			if(INF.fuel < CurPR.getCurPlanet().getFuelTo()){
                INF.currentPlanet = "null";
            }
		}

		backgroundSprite = RES.atlas.createSprite("flight");
		backgroundSprite.setBounds(0.0F, 0.0F, width, height);
		
		flight.setVolume(0.5F);
		flight.play();
		
		fire = new Sprite[2];
		for(int i=0;i<2;i++)
			fire[i] = RES.atlas.createSprite("fire",i+1);
		rocketsInit();
		
		if(CurPR.getCurPlanet().equals(INF.planetLoon)){
			planetScreen = new LoonScreen(game);
		}else if(CurPR.getCurPlanet().equals(INF.planetEmion)){
			planetScreen = new EmionScreen(game);
		}else if(CurPR.getCurPlanet().equals(INF.planetDerten)){
			planetScreen = new DertenScreen(game);
		}else if(CurPR.getCurPlanet().equals(INF.planetUnar)){
			planetScreen = new ru.erked.spaceflight.planets.UnarScreen(game);
		}else if(CurPR.getCurPlanet().equals(INF.planetIngmar)){
			planetScreen = new IngmarScreen(game);
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
			alp-=0.025F;
		}else if(!isTrans){
			blackAlpha.setAlpha(0.0F);
			alp = 0.0F;
		}
		
		speed *= 1.025F;
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
		
		if(rocketS.getY() > 1.5F*height){
			isTrans = true;
			if(alp>1.0F){
				flight.stop();
				game.setScreen(planetScreen);
				alp = 1.0F;
				this.dispose();
			}else{
				blackAlpha.setAlpha(alp);
				alp+=0.025F;
			}
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		backgroundSprite.draw(game.batch);
		
		fire[fireIter].draw(game.batch);
		rocketS.getSprite().draw(game.batch);
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
	}

	private void rocketsInit(){
		/***/
		rocketBall = new SFButtonS("rocketBallI", "rocketBallA", 0.225F*height, 0.5F*width-0.1125F*height, 0.15F*height, 0.52727F, 3.0F, -1);
		rocketCircle = new SFButtonS("rocketCircleI", "rocketCircleA", 0.225F*height, 0.5F*width-0.1125F*height, 0.15F*height, 0.52727F, 3.0F, -1);
		rocketBasic = new SFButtonS("rocketBasicI", "rocketBasicA", 0.225F*height, 0.5F*width-0.1125F*height, 0.2F*height, 0.32967F, 3.0F, -1);
		rocketKinetic = new SFButtonS("rocketKineticI", "rocketKineticA", 0.175F*height, 0.5F*width-0.0875F*height, 0.215F*height, 0.25F, 3.0F, -1);
		rocketDelta = new SFButtonS("rocketDeltaI", "rocketDeltaA", 0.135F*height, 0.5F*width-0.0675F*height, 0.2F*height, 0.1796875F, 3.0F, -1);
		rocketInfinity = new SFButtonS("rocketInfinityI", "rocketInfinityA", 0.15F*height, 0.5F*width-0.075F*height, 0.2F*height, 0.1751412F, 3.0F, -1);
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
	
	private void resourcesCheck(){
		if(INF.money>INF.moneyFull) INF.money = INF.moneyFull;
		if(INF.fuel>INF.fuelFull) INF.fuel = INF.fuelFull;
		if(INF.metal>INF.metalFull) INF.metal = INF.metalFull;
	}
	
	@Override
	public void resize(int width, int height) {
		data.saveSF();
	}

	@Override
	public void pause() {
		flight.pause();
		data.saveSF();
	}

	@Override
	public void resume() {
		flight.play();
		data.loadSF();
	}

	@Override
	public void hide() {
		data.saveSF();
	}

	@Override
	public void dispose() {
		game.dispose();
		data.saveSF();
	}

}