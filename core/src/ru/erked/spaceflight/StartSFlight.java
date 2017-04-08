package ru.erked.spaceflight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.splash.SplashScreen;

public class StartSFlight extends Game {

	public SpriteBatch batch;

	public AndroidOnlyInterface aoi;
	public Data data;
	public final AdMob adMob;
	public final GPGS gpgs;

	public StartSFlight(AndroidOnlyInterface aoi, Data data, AdMob adMob, GPGS gpgs, String lang){
		this.aoi = aoi;
		this.data = data;
		this.adMob = adMob;
		this.gpgs = gpgs;
		if(lang.equals("RU")){
			INF.lngRussian = true;
		}else{
			INF.lngRussian = false;
		}
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		Gdx.input.setCatchBackKey(true);
		setScreen(new SplashScreen(this));
	}

	@Override
	public void render(){
		super.render();
	}

}
