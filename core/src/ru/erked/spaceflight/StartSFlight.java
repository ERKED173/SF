package ru.erked.spaceflight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.splash.SplashScreen;
import ru.erked.spaceflight.tech.SFTextSystem;

public class StartSFlight extends Game {

	public SpriteBatch batch;

	public AndroidOnlyInterface aoi;
	public Data data;
	public final AdMob adMob;
	public final GPGS gpgs;
    public SFTextSystem ts;

	public StartSFlight(AndroidOnlyInterface aoi, Data data, AdMob adMob, GPGS gpgs){
		this.aoi = aoi;
		this.data = data;
		this.adMob = adMob;
		this.gpgs = gpgs;
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
