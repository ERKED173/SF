package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import ru.erked.spaceflight.AndroidOnlyInterface;
import ru.erked.spaceflight.Data;
import ru.erked.spaceflight.GPGS;
import ru.erked.spaceflight.StartSFlight;
import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;
import ru.erked.spaceflight.tech.SFButtonA;
import ru.erked.spaceflight.tech.SFButtonS;
import ru.erked.spaceflight.tech.SFFont;

public class PlanetScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private SpriteBatch batch;
	private ru.erked.spaceflight.controllers.SFIn controller;
	public static OrthographicCamera camera;
	
	private Sprite backgroundSprite;
	
	private Sprite lock;
	private Sprite earth;
	private SFButtonS loon;
	private SFButtonS emion;
	private SFButtonS derten;
	private SFButtonS unar;
	private SFButtonS ingmar;

    private SFButtonS loonRate;
    private SFButtonS emionRate;
    private SFButtonS dertenRate;
    private SFButtonS unarRate;
    private SFButtonS ingmarRate;

	private Sprite fuel;

	private SFButtonS select;

	private SFButtonA back;

	private static float prevDragX;
	private static float prevDragY;

	private static SFFont text;
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private AndroidOnlyInterface aoi;
	private Data data;
    private GPGS gpgs;
	
	public PlanetScreen(final StartSFlight game){
		this.game = game;
		aoi = game.aoi;
		data = game.data;
        gpgs = game.gpgs;
	}
	
	@Override
	public void show() {

		batch = new SpriteBatch();
		
		controller = new ru.erked.spaceflight.controllers.SFIn();
		
		camera = new OrthographicCamera(width, height);
		camera.position.set(new Vector3(0.0F, 0.0F, 0));
		
		MainMenu.music.play();
		
		backgroundSprite = RES.atlas.createSprite("planetChoice");
		backgroundSprite.setBounds(0.0F, 0.0F, width, 4.0F*width);
		
		lock = RES.atlas.createSprite("lock");
		lock.setBounds(0.0F, 0.0F, 0.15F*width, 0.15F*width);
		earth = RES.atlas.createSprite("menuPlanet1");
		earth.setBounds(-0.125F*width, -0.85F*width, 1.25F*width, 1.25F*width);
		earth.setOriginCenter();
		earth.setRotation((float)(359.0D*Math.random()));
		fuel  = RES.atlas.createSprite("fuel");
		fuel.setBounds(0.025F*width, 0.025F*width, 0.05F*width, 0.05F*width);
		
		if(!INF.lngRussian){
			back = new SFButtonA("backI", "backA", 0.225F*width, width - 0.23F*width, 0.015F*height, 2.98913F, 1.0F, -1);
		}else{
			back = new SFButtonA("backRI", "backRA", 0.2F*width, width - 0.215F*width, 0.01F*height, 2.98913F, 1.0F, -1);
		}

		loon = new SFButtonS("loonI", "loonA", 0.15F*width, 0.1F*width, 0.15F*backgroundSprite.getHeight(), 1.0F, 1.97368F, -1);
        loonRate = new SFButtonS("loonRate", "loonRate", 0.05F*width, 0.15F*width, 0.135F*backgroundSprite.getHeight(), 1.0F, 1.0F, -1);
		emion = new SFButtonS("emionI", "emionA", 0.225F*width, 0.75F*width, 0.2F*backgroundSprite.getHeight(), 1.0F, 2.0F, -1);
		emion.getSprite().setColor(Color.GRAY);
		emionRate = new SFButtonS("emionRate", "emionRate", 0.05F*width, 0.8375F*width, 0.185F*backgroundSprite.getHeight(), 1.0F, 1.0F, -1);
		derten = new SFButtonS("dertenI", "dertenA", 0.2F*width, 0.1F*width, 0.26F*backgroundSprite.getHeight(), 1.0F, 2.0F, -1);
		derten.getSprite().setColor(Color.GRAY);
        dertenRate = new SFButtonS("dertenRate", "dertenRate", 0.05F*width, 0.175F*width, 0.245F*backgroundSprite.getHeight(), 1.0F, 1.0F, -1);
		unar = new SFButtonS("unarI", "unarA", 0.275F*width, 0.65F*width, 0.32F*backgroundSprite.getHeight(), 1.0F, 2.0F, -1);
		unar.getSprite().setColor(Color.GRAY);
        unarRate = new SFButtonS("unarRate", "unarRate", 0.05F*width, 0.7625F*width, 0.305F*backgroundSprite.getHeight(), 1.0F, 1.0F, -1);
		ingmar = new SFButtonS("ingmarI", "ingmarA", 0.325F*width, 0.075F*width, 0.4F*backgroundSprite.getHeight(), 1.0F, 2.0F, -1);
		ingmar.getSprite().setColor(Color.GRAY);
        ingmarRate = new SFButtonS("ingmarRate", "ingmarRate", 0.05F*width, 0.2125F*width, 0.385F*backgroundSprite.getHeight(), 1.0F, 1.0F, -1);
		
		select = new SFButtonS("buttonI", "buttonA", 0.132F*width, 0.6F*width, 0.16F*backgroundSprite.getHeight(), 1.5F, 2.0F, -1);
		select.getSprite().setColor(Color.CYAN);
		select.setX(loon.getX() + 3.75F*loon.getWidth());
		select.setY(loon.getY() + 0.5F*loon.getHeight() - 0.5F*select.getHeight());
		
		text = new SFFont(40, Color.WHITE, 1, 2, Color.BLACK);
		
		isTrans = false;
		blackAlpha.setBounds(0.0F, 0.0F, width, 4.0F*width);
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
		
		fuel.setX((float)(camera.position.x - (float)width/2.0F) + (float)0.025F*width);
		fuel.setY((float)(camera.position.y - (float)height/2.0F) + (float)0.025F*width);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		
		backgroundSprite.draw(batch);
		
		earth.rotate(0.0166666666666667F);
		earth.draw(batch);
		fuel.draw(batch);
		
		loon.getSprite().draw(batch);
        loonRate.getSprite().draw(batch);
        if(INF.planetEmion.isAvailable()) emionRate.getSprite().draw(batch);
        emion.getSprite().draw(batch);
        if(INF.planetDerten.isAvailable()) dertenRate.getSprite().draw(batch);
		derten.getSprite().draw(batch);
        if(INF.planetUnar.isAvailable())  unarRate.getSprite().draw(batch);
        unar.getSprite().draw(batch);
        if(INF.planetIngmar.isAvailable())  ingmarRate.getSprite().draw(batch);
		ingmar.getSprite().draw(batch);


            lock.setX(emion.getX() + 0.5F*emion.getWidth() - 0.5F*lock.getWidth());
		lock.setY(emion.getY() + 0.55F*emion.getHeight() - 0.5F*lock.getHeight());
		if(!(INF.planetEmion.isAvailable())) lock.draw(batch);
		lock.setX(derten.getX() + 0.5F*derten.getWidth() - 0.5F*lock.getWidth());
		lock.setY(derten.getY() + 0.55F*derten.getHeight() - 0.5F*lock.getHeight());
		if(!(INF.planetDerten.isAvailable())) lock.draw(batch);
		lock.setX(unar.getX() + 0.5F*unar.getWidth() - 0.5F*lock.getWidth());
		lock.setY(unar.getY() + 0.55F*unar.getHeight() - 0.5F*lock.getHeight());
		if(!(INF.planetUnar.isAvailable())) lock.draw(batch);
		lock.setX(ingmar.getX() + 0.5F*ingmar.getWidth() - 0.5F*lock.getWidth());
		lock.setY(ingmar.getY() + 0.55F*ingmar.getHeight() - 0.5F*lock.getHeight());
		if(!(INF.planetIngmar.isAvailable())) lock.draw(batch);
		
		text.draw(batch, ": " + INF.fuel + "/" + INF.fuelFull, (camera.position.x - width/2.0F) + 0.085F*width, (camera.position.y - height/2.0F) + 0.06F*width);
		
		back.setCoordinates(camera, width, height);
		back.getSprite().draw(batch);
		if(controller.isOnCam(back.getX(camera, width), back.getY(camera, height), back.getWidth(), back.getHeight(), camera, 1.0F)){
			back.setActiveMode(true);
		}else{
			back.setActiveMode(false);
		}
		
		if(!INF.lngRussian){
			text.draw(batch, "Selecting the planet", camera.position.x - 0.475F*width, camera.position.y + 0.465F*height);
		}else{
			text.draw(batch, "Выбор планеты", camera.position.x - 0.475F*width, camera.position.y + 0.465F*height);
		}
		
		drawPlanetInformation();
		
		blackAlpha.draw(batch);
		
		batch.end();
		
		if(controller.isClickedCam(back.getX(camera, width), back.getY(camera, height), back.getWidth(), back.getHeight(), true, camera, 1.0F)){
			game.setScreen(new StartPanelScreen(game));
			this.dispose();
		}
	
		buttonListener();
		
	}
	
	private void drawPlanetInformation(){
		/***/
		if(loon.isActiveMode()){
			select.getSprite().draw(batch);
			select.setX(loon.getX() + 3.75F*loon.getWidth());
			select.setY(loon.getY() + 0.5F*loon.getHeight() - 0.5F*select.getHeight());
			if(!INF.currentPlanet.equals("planetLoon") && INF.fuel >= INF.planetLoon.getFuelTo()){
				select.getSprite().setColor(Color.CYAN);
				if(controller.isOnCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), camera, 1.0F)){
					select.setActiveMode(true);
				}else{
					select.setActiveMode(false);
				}
				if(controller.isClickedCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), true, camera, 1.0F)){
					INF.currentPlanet = "planetLoon";
				}
			}else{
				if(controller.isClickedCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), true, camera, 1.0F)){
					if(INF.currentPlanet.equals("null")){
						if(!INF.lngRussian)
							aoi.makeToast("Not enough fuel");
						else
							aoi.makeToast("Недостаточно топлива");
					}
				}
				select.getSprite().setColor(Color.TEAL);
			}
			if(!INF.lngRussian){
				text.draw(batch, "Select", 
						select.getX() + 0.5F*(select.getWidth()-text.getWidth("Select")), 
						select.getY() + 0.5F*(select.getHeight()+text.getHeight("A")));
				text.draw(batch, "Planet's name: " + INF.planetLoon.getNameUS(), 
						loon.getX() + 1.5F*loon.getWidth(), 
						loon.getY() + 0.5F*loon.getHeight() + 3.5F*text.getHeight("A"));
				text.draw(batch, "Levels: 1-10", 
						loon.getX() + 1.5F*loon.getWidth(), 
						loon.getY() + 0.5F*loon.getHeight() + 2.0F*text.getHeight("A"));
				text.draw(batch, "You need " + INF.planetLoon.getFuelTo() + " fuel", 
						loon.getX() + 1.5F*loon.getWidth(), 
						loon.getY() + 0.5F*loon.getHeight() + 0.5F*text.getHeight("A"));
				text.draw(batch, "to reach the planet", 
						loon.getX() + 1.5F*loon.getWidth(), 
						loon.getY() + 0.5F*loon.getHeight() - 1.0F*text.getHeight("A"));
				if(INF.currentPlanet.equals("planetLoon")) text.draw(batch, "Selected", 
						loon.getX() + 1.5F*loon.getWidth(), 
						loon.getY() + 0.5F*loon.getHeight() - 2.5F*text.getHeight("A"));
			}else{
				text.draw(batch, "Выбрать", 
						select.getX() + 0.5F*(select.getWidth()-text.getWidth("Выбрать")), 
						select.getY() + 0.5F*(select.getHeight()+text.getHeight("A")));
				text.draw(batch, "Название планеты: " + INF.planetLoon.getNameRU(), 
						loon.getX() + 1.5F*loon.getWidth(), 
						loon.getY() + 0.5F*loon.getHeight() + 3.5F*text.getHeight("A"));
				text.draw(batch, "Уровни: 1-10", 
						loon.getX() + 1.5F*loon.getWidth(), 
						loon.getY() + 0.5F*loon.getHeight() + 2.0F*text.getHeight("A"));
				text.draw(batch, "Чтобы достичь планеты", 
						loon.getX() + 1.5F*loon.getWidth(), 
						loon.getY() + 0.5F*loon.getHeight() + 0.5F*text.getHeight("A"));
				text.draw(batch, "необходимо " + INF.planetLoon.getFuelTo() + " топлива", 
						loon.getX() + 1.5F*loon.getWidth(), 
						loon.getY() + 0.5F*loon.getHeight() - 1.0F*text.getHeight("A"));
				if(INF.currentPlanet.equals("planetLoon")) text.draw(batch, "Выбрана", 
						loon.getX() + 1.5F*loon.getWidth(), 
						loon.getY() + 0.5F*loon.getHeight() - 2.5F*text.getHeight("A"));
			}
		}
		/***/
		if(!INF.planetEmion.isAvailable()) emion.getSprite().setColor(Color.GRAY);
		else emion.getSprite().setColor(Color.WHITE);
		if(emion.isActiveMode()){
			select.setX(emion.getX() - 2.5F*emion.getWidth());
			select.setY(emion.getY() + 0.5F*emion.getHeight() - 0.5F*select.getHeight());
			select.getSprite().draw(batch);
			if(INF.planetEmion.isAvailable() && !INF.currentPlanet.equals("planetEmion") && INF.fuel >= INF.planetEmion.getFuelTo()){
				select.getSprite().setColor(Color.CYAN);
				if(controller.isOnCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), camera, 1.0F)){
					select.setActiveMode(true);
				}else{
					select.setActiveMode(false);
				}
				if(controller.isClickedCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), true, camera, 1.0F)){
					INF.currentPlanet = "planetEmion";
				}
			}else{
				if(controller.isClickedCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), true, camera, 1.0F)){
					if(!INF.lngRussian)
						aoi.makeToast("Not enough fuel");
					else
						aoi.makeToast("Недостаточно топлива");
				}
				select.getSprite().setColor(Color.TEAL);
			}
			if(!INF.lngRussian){
				text.draw(batch, "Select", 
						select.getX() + 0.5F*(select.getWidth()-text.getWidth("Select")), 
						select.getY() + 0.5F*(select.getHeight()+text.getHeight("A")));
				text.draw(batch, "Planet's name: " + INF.planetEmion.getNameUS(), 
						emion.getX() - 0.25F*emion.getWidth() - text.getWidth("Planet's name: " + INF.planetEmion.getNameUS()), 
						emion.getY() + 0.5F*emion.getHeight() + 3.5F*text.getHeight("A"));
				text.draw(batch, "Levels: 11-20", 
						emion.getX() - 0.25F*emion.getWidth() - text.getWidth("Levels: 11-20"), 
						emion.getY() + 0.5F*emion.getHeight() + 2.0F*text.getHeight("A"));
				text.draw(batch, "You need " + INF.planetEmion.getFuelTo() + " fuel", 
						emion.getX() - 0.25F*emion.getWidth() - text.getWidth("You need " + INF.planetEmion.getFuelTo() + " fuel"), 
						emion.getY() + 0.5F*emion.getHeight() + 0.5F*text.getHeight("A"));
				text.draw(batch, "to reach the planet", 
						emion.getX() - 0.25F*emion.getWidth() - text.getWidth("to reach the planet"), 
						emion.getY() + 0.5F*emion.getHeight() - 1.0F*text.getHeight("A"));
				if(INF.currentPlanet.equals("planetEmion")) text.draw(batch, "Selected", 
						emion.getX() - 0.25F*emion.getWidth() - text.getWidth("Selected"), 
						emion.getY() + 0.5F*emion.getHeight() - 2.5F*text.getHeight("A"));
			}else{
				text.draw(batch, "Выбрать", 
						select.getX() + 0.5F*(select.getWidth()-text.getWidth("Выбрать")), 
						select.getY() + 0.5F*(select.getHeight()+text.getHeight("A")));
				text.draw(batch, "Название планеты: " + INF.planetEmion.getNameRU(), 
						emion.getX() - 0.25F*emion.getWidth() - text.getWidth("Название планеты: " + INF.planetEmion.getNameRU()), 
						emion.getY() + 0.5F*emion.getHeight() + 3.5F*text.getHeight("A"));
				text.draw(batch, "Уровни: 11-20", 
						emion.getX() - 0.25F*emion.getWidth() - text.getWidth("Уровни: 11-20"), 
						emion.getY() + 0.5F*emion.getHeight() + 2.0F*text.getHeight("A"));
				text.draw(batch, "Для достижения планеты", 
						emion.getX() - 0.25F*emion.getWidth() - text.getWidth("Для достижения планеты"), 
						emion.getY() + 0.5F*emion.getHeight() + 0.5F*text.getHeight("A"));
				text.draw(batch, "необходимо " + INF.planetEmion.getFuelTo() + " топлива", 
						emion.getX() - 0.25F*emion.getWidth() - text.getWidth("необходимо " + INF.planetEmion.getFuelTo() + " топлива"), 
						emion.getY() + 0.5F*emion.getHeight() - 1.0F*text.getHeight("A"));
				if(INF.currentPlanet.equals("planetEmion")) text.draw(batch, "Выбрана", 
						emion.getX() - 0.25F*emion.getWidth() - text.getWidth("Выбрана"), 
						emion.getY() + 0.5F*emion.getHeight() - 2.5F*text.getHeight("A"));
			}
		}
		/***/
		if(!INF.planetDerten.isAvailable()) derten.getSprite().setColor(Color.GRAY);
		else derten.getSprite().setColor(Color.WHITE);
		if(derten.isActiveMode()){
			select.setX(derten.getX() + 3.35F*derten.getWidth());
			select.setY(derten.getY() + 0.5F*derten.getHeight() - 0.5F*select.getHeight());
			select.getSprite().draw(batch);
			if(INF.planetDerten.isAvailable() && !INF.currentPlanet.equals("planetDerten") && INF.fuel >= INF.planetDerten.getFuelTo()){
				select.getSprite().setColor(Color.CYAN);
				if(controller.isOnCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), camera, 1.0F)){
					select.setActiveMode(true);
				}else{
					select.setActiveMode(false);
				}
				if(controller.isClickedCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), true, camera, 1.0F)){
					INF.currentPlanet = "planetDerten";
				}
			}else{
				if(controller.isClickedCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), true, camera, 1.0F)){
					if(!INF.lngRussian)
						aoi.makeToast("Not enough fuel");
					else
						aoi.makeToast("Недостаточно топлива");
				}
				select.getSprite().setColor(Color.TEAL);
			}
			if(!INF.lngRussian){
				text.draw(batch, "Select", 
						select.getX() + 0.5F*(select.getWidth()-text.getWidth("Select")), 
						select.getY() + 0.5F*(select.getHeight()+text.getHeight("A")));
				text.draw(batch, "Planet's name: " + INF.planetDerten.getNameUS(),
						derten.getX() + 1.35F*derten.getWidth(), 
						derten.getY() + 0.5F*derten.getHeight() + 3.5F*text.getHeight("A"));
				text.draw(batch, "Levels: 21-30", 
						derten.getX() + 1.35F*derten.getWidth(), 
						derten.getY() + 0.5F*derten.getHeight() + 2.0F*text.getHeight("A"));
				text.draw(batch, "You need " + INF.planetDerten.getFuelTo() + " fuel",
						derten.getX() + 1.35F*derten.getWidth(), 
						derten.getY() + 0.5F*derten.getHeight() + 0.5F*text.getHeight("A"));
				text.draw(batch, "to reach the planet", 
						derten.getX() + 1.35F*derten.getWidth(), 
						derten.getY() + 0.5F*derten.getHeight() - 1.0F*text.getHeight("A"));
				if(INF.currentPlanet.equals("planetDerten")) text.draw(batch, "Selected",
						derten.getX() + 1.35F*derten.getWidth(), 
						derten.getY() + 0.5F*derten.getHeight() - 2.5F*text.getHeight("A"));
			}else{
				text.draw(batch, "Выбрать",
						select.getX() + 0.5F*(select.getWidth()-text.getWidth("Выбрать")), 
						select.getY() + 0.5F*(select.getHeight()+text.getHeight("A")));
				text.draw(batch, "Название планеты: " + INF.planetDerten.getNameRU(), 
						derten.getX() + 1.35F*derten.getWidth(), 
						derten.getY() + 0.5F*derten.getHeight() + 3.5F*text.getHeight("A"));
				text.draw(batch, "Уровни: 21-30", 
						derten.getX() + 1.35F*derten.getWidth(), 
						derten.getY() + 0.5F*derten.getHeight() + 2.0F*text.getHeight("A"));
				text.draw(batch, "Для достижения планеты", 
						derten.getX() + 1.35F*derten.getWidth(), 
						derten.getY() + 0.5F*derten.getHeight() + 0.5F*text.getHeight("A"));
				text.draw(batch, "необходимо " + INF.planetDerten.getFuelTo() + " топлива",
						derten.getX() + 1.35F*derten.getWidth(), 
						derten.getY() + 0.5F*derten.getHeight() - 1.0F*text.getHeight("A"));
				if(INF.currentPlanet.equals("planetDerten")) text.draw(batch, "Выбрана",
						derten.getX() + 1.35F*derten.getWidth(), 
						derten.getY() + 0.5F*derten.getHeight() - 2.5F*text.getHeight("A"));
			}
		}
		/***/
		if(!INF.planetUnar.isAvailable()) unar.getSprite().setColor(Color.GRAY);
		else unar.getSprite().setColor(Color.WHITE);
		if(unar.isActiveMode()){
			select.setX(unar.getX() - 2.0F*unar.getWidth());
			select.setY(unar.getY() + 0.5F*unar.getHeight() - 0.5F*select.getHeight());
			select.getSprite().draw(batch);
			if(INF.planetUnar.isAvailable() && !INF.currentPlanet.equals("planetUnar") && INF.fuel >= INF.planetUnar.getFuelTo()){
				select.getSprite().setColor(Color.CYAN);
				if(controller.isOnCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), camera, 1.0F)){
					select.setActiveMode(true);
				}else{
					select.setActiveMode(false);
				}
				if(controller.isClickedCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), true, camera, 1.0F)){
					INF.currentPlanet = "planetUnar";
				}
			}else{
				if(controller.isClickedCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), true, camera, 1.0F)){
					if(!INF.lngRussian)
						aoi.makeToast("Not enough fuel");
					else
						aoi.makeToast("Недостаточно топлива");
				}
				select.getSprite().setColor(Color.TEAL);
			}
			if(!INF.lngRussian){
				text.draw(batch, "Select", 
						select.getX() + 0.5F*(select.getWidth()-text.getWidth("Select")), 
						select.getY() + 0.5F*(select.getHeight()+text.getHeight("A")));
				text.draw(batch, "Planet's name: " + INF.planetUnar.getNameUS(),
						unar.getX() - 0.25F*unar.getWidth() - text.getWidth("Planet's name: " + INF.planetUnar.getNameUS()), 
						unar.getY() + 0.5F*unar.getHeight() + 3.5F*text.getHeight("A"));
				text.draw(batch, "Levels: 31-40",
						unar.getX() - 0.25F*unar.getWidth() - text.getWidth("Levels: 31-50"), 
						unar.getY() + 0.5F*unar.getHeight() + 2.0F*text.getHeight("A"));
				text.draw(batch, "You need " + INF.planetUnar.getFuelTo() + " fuel",
						unar.getX() - 0.25F*unar.getWidth() - text.getWidth("You need " + INF.planetUnar.getFuelTo() + " fuel"), 
						unar.getY() + 0.5F*unar.getHeight() + 0.5F*text.getHeight("A"));
				text.draw(batch, "to reach the planet",
						unar.getX() - 0.25F*unar.getWidth() - text.getWidth("to reach the planet"), 
						unar.getY() + 0.5F*unar.getHeight() - 1.0F*text.getHeight("A"));
				if(INF.currentPlanet.equals("planetUnar")) text.draw(batch, "Selected", 
						unar.getX() - 0.25F*unar.getWidth() - text.getWidth("Selected"), 
						unar.getY() + 0.5F*unar.getHeight() - 2.5F*text.getHeight("A"));
			}else{
				text.draw(batch, "Выбрать", 
						select.getX() + 0.5F*(select.getWidth()-text.getWidth("Выбрать")), 
						select.getY() + 0.5F*(select.getHeight()+text.getHeight("A")));
				text.draw(batch, "Название планеты: " + INF.planetUnar.getNameRU(), 
						unar.getX() - 0.25F*unar.getWidth() - text.getWidth("Название планеты: " + INF.planetUnar.getNameRU()), 
						unar.getY() + 0.5F*unar.getHeight() + 3.5F*text.getHeight("A"));
				text.draw(batch, "Уровни: 31-40",
						unar.getX() - 0.25F*unar.getWidth() - text.getWidth("Уровни: 31-50"), 
						unar.getY() + 0.5F*unar.getHeight() + 2.0F*text.getHeight("A"));
				text.draw(batch, "Для достижения планеты", 
						unar.getX() - 0.25F*unar.getWidth() - text.getWidth("Для достижения планеты"), 
						unar.getY() + 0.5F*unar.getHeight() + 0.5F*text.getHeight("A"));
				text.draw(batch, "необходимо " + INF.planetUnar.getFuelTo() + " топлива",
						unar.getX() - 0.25F*unar.getWidth() - text.getWidth("необходимо " + INF.planetUnar.getFuelTo() + " топлива"), 
						unar.getY() + 0.5F*unar.getHeight() - 1.0F*text.getHeight("A"));
				if(INF.currentPlanet.equals("planetUnar")) text.draw(batch, "Выбрана", 
						unar.getX() - 0.25F*unar.getWidth() - text.getWidth("Выбрана"), 
						unar.getY() + 0.5F*unar.getHeight() - 2.5F*text.getHeight("A"));
			}
		}
		/***/
		if(!INF.planetIngmar.isAvailable()) ingmar.getSprite().setColor(Color.GRAY);
		else ingmar.getSprite().setColor(Color.WHITE);
		if(ingmar.isActiveMode()){
			select.setX(ingmar.getX() + 2.25F*ingmar.getWidth());
			select.setY(ingmar.getY() + 0.5F*ingmar.getHeight() - 0.5F*select.getHeight());
			select.getSprite().draw(batch);
			if(INF.planetIngmar.isAvailable() && !INF.currentPlanet.equals("planetIngmar") && INF.fuel >= INF.planetIngmar.getFuelTo()){
				select.getSprite().setColor(Color.CYAN);
				if(controller.isOnCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), camera, 1.0F)){
					select.setActiveMode(true);
				}else{
					select.setActiveMode(false);
				}
				if(controller.isClickedCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), true, camera, 1.0F)){
					INF.currentPlanet = "planetIngmar";
				}
			}else{
				if(controller.isClickedCam(select.getX(), select.getY(), select.getWidth(), select.getHeight(), true, camera, 1.0F)){
					if(!INF.lngRussian)
						aoi.makeToast("Not enough fuel");
					else
						aoi.makeToast("Недостаточно топлива");
				}
				select.getSprite().setColor(Color.TEAL);
			}
			if(!INF.lngRussian){
				text.draw(batch, "Select", 
						select.getX() + 0.5F*(select.getWidth()-text.getWidth("Select")), 
						select.getY() + 0.5F*(select.getHeight()+text.getHeight("A")));
				text.draw(batch, "Planet's name: " + INF.planetIngmar.getNameUS(), 
						ingmar.getX() + 1.1F*ingmar.getWidth(), 
						ingmar.getY() + 0.5F*ingmar.getHeight() + 3.5F*text.getHeight("A"));
				text.draw(batch, "Levels: 41-50", 
						ingmar.getX() + 1.1F*ingmar.getWidth(), 
						ingmar.getY() + 0.5F*ingmar.getHeight() + 2.0F*text.getHeight("A"));
				text.draw(batch, "You need " + INF.planetIngmar.getFuelTo() + " fuel",
						ingmar.getX() + 1.1F*ingmar.getWidth(), 
						ingmar.getY() + 0.5F*ingmar.getHeight() + 0.5F*text.getHeight("A"));
				text.draw(batch, "to reach the planet", 
						ingmar.getX() + 1.1F*ingmar.getWidth(), 
						ingmar.getY() + 0.5F*ingmar.getHeight() - 1.0F*text.getHeight("A"));
				if(INF.currentPlanet.equals("planetIngmar")) text.draw(batch, "Selected",
						ingmar.getX() + 1.1F*ingmar.getWidth(), 
						ingmar.getY() + 0.5F*ingmar.getHeight() - 2.5F*text.getHeight("A"));
			}else{
				text.draw(batch, "Выбрать", 
						select.getX() + 0.5F*(select.getWidth()-text.getWidth("Выбрать")), 
						select.getY() + 0.5F*(select.getHeight()+text.getHeight("A")));
				text.draw(batch, "Название планеты: " + INF.planetIngmar.getNameRU(),
						ingmar.getX() + 1.1F*ingmar.getWidth(), 
						ingmar.getY() + 0.5F*ingmar.getHeight() + 3.5F*text.getHeight("A"));
				text.draw(batch, "Уровни: 41-50", 
						ingmar.getX() + 1.1F*ingmar.getWidth(), 
						ingmar.getY() + 0.5F*ingmar.getHeight() + 2.0F*text.getHeight("A"));
				text.draw(batch, "Для достижения планеты",
						ingmar.getX() + 1.1F*ingmar.getWidth(), 
						ingmar.getY() + 0.5F*ingmar.getHeight() + 0.5F*text.getHeight("A"));
				text.draw(batch, "необходимо " + INF.planetIngmar.getFuelTo() + " топлива",
						ingmar.getX() + 1.1F*ingmar.getWidth(), 
						ingmar.getY() + 0.5F*ingmar.getHeight() - 1.0F*text.getHeight("A"));
				if(INF.currentPlanet.equals("planetIngmar")) text.draw(batch, "Выбрана", 
						ingmar.getX() + 1.1F*ingmar.getWidth(), 
						ingmar.getY() + 0.5F*ingmar.getHeight() - 2.5F*text.getHeight("A"));
			}
		}
		/***/
	}
	
	private void buttonListener(){
		if(controller.isClickedCam(loon.getX(), loon.getY(), loon.getWidth(), loon.getHeight(), false, camera, 1.0F)){
			if(!loon.isActiveMode()){
				loon.setActiveMode(true);
				emion.setActiveMode(false);
				derten.setActiveMode(false);
				unar.setActiveMode(false);
				ingmar.setActiveMode(false);
			}else{
				loon.setActiveMode(false);
			}
		}
		if(controller.isClickedCam(loonRate.getX(), loonRate.getY(), loonRate.getWidth(), loonRate.getHeight(), false, camera, 1.0F)){
            gpgs.showLeaderboard(0);
        }
		/***/
		if(controller.isClickedCam(emion.getX(), emion.getY(), emion.getWidth(), emion.getHeight(), false, camera, 1.0F)){
			if(!emion.isActiveMode() && INF.planetEmion.isAvailable()){
				emion.setActiveMode(true);
				loon.setActiveMode(false);
				derten.setActiveMode(false);
				unar.setActiveMode(false);
				ingmar.setActiveMode(false);
			}else{
				emion.setActiveMode(false);
			}
		}
		if(INF.planetEmion.isAvailable())
            if(controller.isClickedCam(emionRate.getX(), emionRate.getY(), emionRate.getWidth(), emionRate.getHeight(), false, camera, 1.0F))
                gpgs.showLeaderboard(1);
		/***/
		if(controller.isClickedCam(derten.getX(), derten.getY(), derten.getWidth(), derten.getHeight(), false, camera, 1.0F)){
			if(!derten.isActiveMode() && INF.planetDerten.isAvailable()){
				derten.setActiveMode(true);
				loon.setActiveMode(false);
				emion.setActiveMode(false);
				unar.setActiveMode(false);
				ingmar.setActiveMode(false);
			}else{
				derten.setActiveMode(false);
			}
		}
        if(INF.planetDerten.isAvailable())
            if(controller.isClickedCam(dertenRate.getX(), dertenRate.getY(), dertenRate.getWidth(), dertenRate.getHeight(), false, camera, 1.0F))
                gpgs.showLeaderboard(2);
		/***/
		if(controller.isClickedCam(unar.getX(), unar.getY(), unar.getWidth(), unar.getHeight(), false, camera, 1.0F)){
			if(!unar.isActiveMode() && INF.planetUnar.isAvailable()){
				unar.setActiveMode(true);
				loon.setActiveMode(false);
				emion.setActiveMode(false);
				derten.setActiveMode(false);
				ingmar.setActiveMode(false);
			}else{
				unar.setActiveMode(false);
			}
		}
        if(INF.planetUnar.isAvailable())
            if(controller.isClickedCam(unarRate.getX(), unarRate.getY(), unarRate.getWidth(), unarRate.getHeight(), false, camera, 1.0F))
                gpgs.showLeaderboard(3);
		/***/
		if(controller.isClickedCam(ingmar.getX(), ingmar.getY(), ingmar.getWidth(), ingmar.getHeight(), false, camera, 1.0F)){
			if(!ingmar.isActiveMode() && INF.planetIngmar.isAvailable()){
				ingmar.setActiveMode(true);
				loon.setActiveMode(false);
				emion.setActiveMode(false);
				derten.setActiveMode(false);
				unar.setActiveMode(false);
			}else{
				ingmar.setActiveMode(false);
			}
		}
        if(INF.planetIngmar.isAvailable())
            if(controller.isClickedCam(ingmarRate.getX(), ingmarRate.getY(), ingmarRate.getWidth(), ingmarRate.getHeight(), false, camera, 1.0F))
                gpgs.showLeaderboard(4);
		/***/
	}
	
	private void touchUpdate(){
		if(prevDragX != 0.0F && ru.erked.spaceflight.controllers.SFIn.tdrRX != 0.0F)
			camera.position.x -= ru.erked.spaceflight.controllers.SFIn.tdrRX - prevDragX;
		if(prevDragY != 0.0F && ru.erked.spaceflight.controllers.SFIn.tdrRY != 0.0F)
			camera.position.y -= ru.erked.spaceflight.controllers.SFIn.tdrRY - prevDragY;
		prevDragX = ru.erked.spaceflight.controllers.SFIn.tdrRX;
		prevDragY = ru.erked.spaceflight.controllers.SFIn.tdrRY;
			
		if(camera.position.x < backgroundSprite.getX() + width/2)
			camera.position.set(new Vector3(backgroundSprite.getX() + width/2, camera.position.y, 0));
		if(camera.position.y < backgroundSprite.getY() + height/2)
			camera.position.set(new Vector3(camera.position.x, backgroundSprite.getY() + height/2, 0));
		if(camera.position.x > (backgroundSprite.getX() + backgroundSprite.getWidth()) - width/2)
			camera.position.set(new Vector3((backgroundSprite.getX() + backgroundSprite.getWidth()) - width/2, camera.position.y, 0));
		if(camera.position.y > (backgroundSprite.getY() + 0.5F*backgroundSprite.getHeight()) - height/2)
			camera.position.set(new Vector3(camera.position.x, (backgroundSprite.getY() + 0.5F*backgroundSprite.getHeight()) - height/2, 0));
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
		batch.dispose();
		text.dispose();
	}

}