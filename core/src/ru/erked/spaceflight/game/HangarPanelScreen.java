package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.AndroidOnlyInterface;
import ru.erked.spaceflight.Data;
import ru.erked.spaceflight.StartSFlight;
import ru.erked.spaceflight.controllers.SFIn;
import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.random.RES;
import ru.erked.spaceflight.tech.Rocket;
import ru.erked.spaceflight.tech.SFButtonS;
import ru.erked.spaceflight.tech.SFFont;

public class HangarPanelScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private SFIn controller;
	
	private Sprite backgroundSprite[];
	private int backIter = 0;
	
	private SFButtonS back;
	
	private Sprite metalIcon;
	private Sprite metalIcon1;
	private Sprite time;
	
	private SFFont text;

	private int page = 1;
	private SFButtonS next;
	private SFButtonS prev;
	private SFButtonS buyRocket;
	
	private Rocket activeRocket;
	private SFButtonS rocketBall;
	private SFButtonS rocketCircle;
	private SFButtonS rocketBasic;
	private SFButtonS rocketKinetic;
	private SFButtonS rocketDelta;
	private SFButtonS rocketInfinity;
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private AndroidOnlyInterface aoi;
	private Data data;
	
	public HangarPanelScreen(final StartSFlight game){
		this.game = game;
		aoi = game.aoi;
		data = game.data;
	}
	
	@Override
	public void show() {

		controller = new SFIn();
		
		MainMenu.music.play();
		
		backgroundSprite = new Sprite[13];
		for(int i=0;i<13;i++){
			backgroundSprite[i] = RES.atlas.createSprite("hangar", i+1);
			backgroundSprite[i].setBounds(0.0F, 0.0F, width, height);
		}
		
		metalIcon = RES.atlas.createSprite("metal", -1);
		metalIcon.setBounds(0.095F*width, 0.325F*height, 0.1F*height, 0.1F*height);
		metalIcon1 = RES.atlas.createSprite("metal", -1);
		metalIcon1.setBounds(0.095F*width, 0.325F*height, 0.075F*height, 0.075F*height);
		time = RES.atlas.createSprite("clock", 1);
		time.setBounds(0.095F*width, 0.325F*height, 0.065F*height, 0.065F*height);
		
		rocketsInit();
		SFButtonsInit();

		if(!INF.lngRussian)
			text = new SFFont(30, Color.WHITE, 1, 2, Color.BLACK);
		else
			text = new SFFont(35, Color.WHITE, 1, 2, Color.BLACK);
		
		isTrans = false;
		blackAlpha.setBounds(0.0F, 0.0F, width, height);
		blackAlpha.setAlpha(1.0F);
	}

	@Override
	public void render(float delta) {
		INF.elapsedTime++;
		resourceCheck();
		
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

		metalIcon.draw(game.batch);
		text.draw(game.batch, ": " + INF.metal + "/" + INF.metalFull, metalIcon.getX() + 1.1F*metalIcon.getWidth(), metalIcon.getY() + 0.65F*metalIcon.getHeight());
		
		drawBackButton();
		drawNextPrev();
		drawRockets();
		drawBuyButton();
		
		if(!INF.lngRussian){
			text.draw(game.batch, "Rocket market", 0.5F*(width-text.getWidth("Rocket market")), 0.965F*height);
			text.draw(game.batch, "Page: " + page, 
					next.getX() - 0.55F*text.getWidth("Page: 1"), 
					next.getY() - 0.15F*next.getHeight());
		}else{
			text.draw(game.batch, "Магазин ракет", 0.5F*(width-text.getWidth("Магазин ракет")), 0.965F*height);
			text.draw(game.batch, "Страница: " + page, 
					next.getX() - 0.55F*text.getWidth("Страница: 1"), 
					next.getY() - 0.15F*next.getHeight());
		}
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		buttonListener();
		
	}
	
	private void SFButtonsInit(){
		if(!INF.lngRussian){
			back = new SFButtonS("backI", "backA", 0.225F*width, width - 0.23F*width, 0.015F*height, 2.98913F, 1.0F, -1);
		}else{
			back = new SFButtonS("backRI", "backRA", 0.2F*width, width - 0.215F*width, 0.01F*height, 2.98913F, 1.0F, -1);
		}
		next = new SFButtonS("buttonI", "buttonA", 0.1F*width, 0.815F*width, 0.2F*height, 1.5F, 2.0F, -1);
		next.getSprite().setColor(Color.LIME);
		prev = new SFButtonS("buttonI", "buttonA", 0.1F*width, 0.7F*width, 0.2F*height, 1.5F, 2.0F, -1);
		prev.getSprite().setColor(Color.LIME);
		buyRocket = new SFButtonS("buttonI", "buttonA", 0.125F*width, 0.1F*width, 0.15F*height, 1.5F, 2.0F, -1);
		buyRocket.getSprite().setColor(Color.LIME);
	}
	private void rocketsInit(){
		rocketBall = new SFButtonS("rocketBallI", "rocketBallA", 0.15F*height, 0.515F*width, 0.55F*height, 0.52727F, 3.0F, -1);
		rocketCircle = new SFButtonS("rocketCircleI", "rocketCircleA", 0.15F*height, 0.665F*width, 0.55F*height, 0.52727F, 3.0F, -1);
		rocketBasic = new SFButtonS("rocketBasicI", "rocketBasicA", 0.09378F*height, 0.815F*width, 0.55F*height, 0.32967F, 3.0F, -1);
		rocketKinetic = new SFButtonS("rocketKineticI", "rocketKineticA", 0.07112F*height, 0.535F*width, 0.55F*height, 0.25F, 3.0F, -1);
		rocketDelta = new SFButtonS("rocketDeltaI", "rocketDeltaA", 0.05103125F*height, 0.685F*width, 0.55F*height, 0.1796875F, 3.0F, -1);
		rocketInfinity = new SFButtonS("rocketInfinityI", "rocketInfinityA", 0.052610169F*height, 0.835F*width, 0.55F*height, 0.1751412F, 3.0F, -1);
	}
	
	private void drawBackground(){
		if(INF.elapsedTime % 15 == 0){
			if(backIter<12) backIter++;
			else backIter=0;
		}
	}
	private void drawBackButton(){
		if(controller.isOn(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true)){
			back.setActiveMode(true);
		}else{
			back.setActiveMode(false);
		}
		back.getSprite().draw(game.batch);
	}
	private void drawNextPrev(){
		if(page == 1) prev.getSprite().setColor(Color.FOREST);
		else prev.getSprite().setColor(Color.LIME);
		if(page == 2) next.getSprite().setColor(Color.FOREST);
		else next.getSprite().setColor(Color.LIME);
		if(controller.isOn(next.getX(), next.getY(), next.getWidth(), next.getHeight(), true) && page != 2){
			next.setActiveMode(true);
		}else{
			next.setActiveMode(false);
		}
		if(controller.isOn(prev.getX(), prev.getY(), prev.getWidth(), prev.getHeight(), true) && page != 1){
			prev.setActiveMode(true);
		}else{
			prev.setActiveMode(false);
		}
		next.getSprite().draw(game.batch);
		prev.getSprite().draw(game.batch);
		buyRocket.getSprite().draw(game.batch);
		/***/
		text.setScale(2.0F);
		if(!next.isActiveMode())
			text.draw(game.batch, ">", next.getX() + 0.3F*next.getWidth(), next.getY() + 0.85F*next.getHeight());
		else
			text.draw(game.batch, ">", next.getX() + 0.3F*next.getWidth(), next.getY() + 0.8F*next.getHeight());
		if(!prev.isActiveMode())
			text.draw(game.batch, "<", prev.getX() + 0.3F*prev.getWidth(), prev.getY() + 0.85F*prev.getHeight());
		else
			text.draw(game.batch, "<", prev.getX() + 0.3F*prev.getWidth(), prev.getY() + 0.8F*prev.getHeight());
		text.setScale(1.0F);
		/***/
	}
	private void drawRockets(){
		if(page == 1){
			if(controller.isClicked(rocketBall.getX(), rocketBall.getY(), rocketBall.getWidth(), rocketBall.getHeight(), false, true)){
				if(rocketBall.isActiveMode()) rocketBall.setActiveMode(false);
				else{
					rocketBall.setActiveMode(true);
					rocketCircle.setActiveMode(false);
					rocketBasic.setActiveMode(false);
					activeRocket = INF.rocketBall;
				}
			}
			if(controller.isClicked(rocketCircle.getX(), rocketCircle.getY(), rocketCircle.getWidth(), rocketCircle.getHeight(), false, true)){
				if(rocketCircle.isActiveMode()) rocketCircle.setActiveMode(false);
				else{
					rocketBall.setActiveMode(false);
					rocketCircle.setActiveMode(true);
					rocketBasic.setActiveMode(false);
					activeRocket = INF.rocketCircle;
				}
			}
			if(controller.isClicked(rocketBasic.getX(), rocketBasic.getY(), rocketBasic.getWidth(), rocketBasic.getHeight(), false, true)){
				if(rocketBasic.isActiveMode()) rocketBasic.setActiveMode(false);
				else{
					rocketBall.setActiveMode(false);
					rocketCircle.setActiveMode(false);
					rocketBasic.setActiveMode(true);
					activeRocket = INF.rocketBasic;
				}
			}
			/***/
			if(INF.currentRocket.equals("rocketBall")){
				if(!INF.lngRussian){
					text.draw(game.batch, "Selected", 
							rocketBall.getX() + 0.5F*rocketBall.getWidth() - 0.5F*text.getWidth("Selected"), 
							rocketBall.getY() - 0.1F*rocketBall.getHeight());
				}else{
					text.draw(game.batch, "Выбрана", 
							rocketBall.getX() + 0.5F*rocketBall.getWidth() - 0.5F*text.getWidth("Выбрана"), 
							rocketBall.getY() - 0.1F*rocketBall.getHeight());
				}
			}
			rocketBall.getSprite().draw(game.batch);
			/***/
			if(INF.currentRocket.equals("rocketCircle")){
				if(!INF.lngRussian){
					text.draw(game.batch, "Selected", 
							rocketCircle.getX() + 0.5F*rocketCircle.getWidth() - 0.5F*text.getWidth("Selected"),  
							rocketCircle.getY() - 0.1F*rocketCircle.getHeight());
				}else{
					text.draw(game.batch, "Выбрана", 
							rocketCircle.getX() + 0.5F*rocketCircle.getWidth() - 0.5F*text.getWidth("Выбрана"),  
							rocketCircle.getY() - 0.1F*rocketCircle.getHeight());
				}
			}
			rocketCircle.getSprite().draw(game.batch);
			/***/
			if(INF.currentRocket.equals("rocketBasic")){
				if(!INF.lngRussian){
					text.draw(game.batch, "Selected", 
							rocketBasic.getX() + 0.5F*rocketBasic.getWidth() - 0.5F*text.getWidth("Selected"), 
							rocketBasic.getY() - 0.1F*rocketBasic.getHeight());
				}else{
					text.draw(game.batch, "Выбрана", 
							rocketBasic.getX() + 0.5F*rocketBasic.getWidth() - 0.5F*text.getWidth("Выбрана"), 
							rocketBasic.getY() - 0.1F*rocketBasic.getHeight());
				}
			}
			rocketBasic.getSprite().draw(game.batch);
			/***/
		}else if(page == 2){
			if(controller.isClicked(rocketKinetic.getX(), rocketKinetic.getY(), rocketKinetic.getWidth(), rocketKinetic.getHeight(), false, true)){
				if(rocketKinetic.isActiveMode()) rocketKinetic.setActiveMode(false);
				else{
					rocketKinetic.setActiveMode(true);
					rocketDelta.setActiveMode(false);
					rocketInfinity.setActiveMode(false);
					activeRocket = INF.rocketKinetic;
				}
			}
			if(controller.isClicked(rocketDelta.getX(), rocketDelta.getY(), rocketDelta.getWidth(), rocketDelta.getHeight(), false, true)){
				if(rocketDelta.isActiveMode()) rocketDelta.setActiveMode(false);
				else{
					rocketDelta.setActiveMode(true);
					rocketKinetic.setActiveMode(false);
					rocketInfinity.setActiveMode(false);
					activeRocket = INF.rocketDelta;
				}
			}
			if(controller.isClicked(rocketInfinity.getX(), rocketInfinity.getY(), rocketInfinity.getWidth(), rocketInfinity.getHeight(), false, true)){
				if(rocketInfinity.isActiveMode()) rocketInfinity.setActiveMode(false);
				else{
					rocketInfinity.setActiveMode(true);
					rocketKinetic.setActiveMode(false);
					rocketDelta.setActiveMode(false);
					activeRocket = INF.rocketInfinity;
				}
			}
			/***/
			if(INF.currentRocket.equals("rocketKinetic")){
				if(!INF.lngRussian){
					text.draw(game.batch, "Selected", 
							rocketKinetic.getX() + 0.5F*rocketKinetic.getWidth() - 0.5F*text.getWidth("Selected"), 
							rocketKinetic.getY() - 0.1F*rocketKinetic.getHeight());
				}else{
					text.draw(game.batch, "Выбрана", 
							rocketKinetic.getX() + 0.5F*rocketKinetic.getWidth() - 0.5F*text.getWidth("Выбрана"), 
							rocketKinetic.getY() - 0.1F*rocketKinetic.getHeight());
				}
			}
			rocketKinetic.getSprite().draw(game.batch);
			/***/
			if(INF.currentRocket.equals("rocketDelta")){
				if(!INF.lngRussian){
					text.draw(game.batch, "Selected", 
							rocketDelta.getX() + 0.5F*rocketDelta.getWidth() - 0.5F*text.getWidth("Selected"),  
							rocketDelta.getY() - 0.1F*rocketDelta.getHeight());
				}else{
					text.draw(game.batch, "Выбрана", 
							rocketDelta.getX() + 0.5F*rocketDelta.getWidth() - 0.5F*text.getWidth("Выбрана"),  
							rocketDelta.getY() - 0.1F*rocketDelta.getHeight());
				}
			}
			rocketDelta.getSprite().draw(game.batch);
			/***/
			if(INF.currentRocket.equals("rocketInfinity")){
				if(!INF.lngRussian){
					text.draw(game.batch, "Selected", 
							rocketInfinity.getX() + 0.5F*rocketInfinity.getWidth() - 0.5F*text.getWidth("Selected"),  
							rocketInfinity.getY() - 0.1F*rocketInfinity.getHeight());
				}else{
					text.draw(game.batch, "Выбрана", 
							rocketInfinity.getX() + 0.5F*rocketInfinity.getWidth() - 0.5F*text.getWidth("Выбрана"),  
							rocketInfinity.getY() - 0.1F*rocketInfinity.getHeight());
				}
			}
			rocketInfinity.getSprite().draw(game.batch);
			/***/
		}
		if(rocketBall.isActiveMode() || rocketCircle.isActiveMode() || rocketBasic.isActiveMode() || rocketKinetic.isActiveMode() || rocketDelta.isActiveMode() || rocketInfinity.isActiveMode()){
			if(!INF.lngRussian){
				metalIcon1.setX(0.1F*width + text.getWidth("Price: " + activeRocket.getCost() + " "));
				metalIcon1.setY(0.825F*height - 3.5F*text.getHeight("A") - 0.5F*metalIcon1.getHeight());
				metalIcon1.draw(game.batch);
				time.setX(0.1F*width + text.getWidth("Time bonus: " + activeRocket.getTimeBonus() + " "));
				time.setY(0.825F*height - 2.0F*text.getHeight("A") - 0.5F*time.getHeight());
				time.draw(game.batch);
				text.draw(game.batch, "Name: " + activeRocket.getNameUS(), 0.1F*width, 0.825F*height);
				text.draw(game.batch, "Time bonus: " + activeRocket.getTimeBonus(), 0.1F*width, 0.825F*height - 1.5F*text.getHeight("A"));
				text.draw(game.batch, "Price: " + activeRocket.getCost(), 0.1F*width, 0.825F*height - 3.0F*text.getHeight("A"));
			}else{
				metalIcon1.setX(0.1F*width + text.getWidth("Price: " + activeRocket.getCost() + " "));
				metalIcon1.setY(0.825F*height - 4.5F*text.getHeight("A") - 0.5F*metalIcon1.getHeight());
				metalIcon1.draw(game.batch);
				time.setX(0.1F*width + text.getWidth("Бонус ко времени: " + activeRocket.getTimeBonus() + " "));
				time.setY(0.825F*height - 2.5F*text.getHeight("A") - 0.5F*time.getHeight());
				time.draw(game.batch);
				text.draw(game.batch, "Название: " + activeRocket.getNameRU(), 0.1F*width, 0.825F*height);
				text.draw(game.batch, "Бонус ко времени: " + activeRocket.getTimeBonus(), 0.1F*width, 0.825F*height - 2.0F*text.getHeight("A"));
				text.draw(game.batch, "Цена: " + activeRocket.getCost(), 0.1F*width, 0.825F*height - 4.0F*text.getHeight("A"));
			}
		}
			/***/
	}
	private void drawBuyButton(){
		if(INF.currentRocket.equals("null")){
			if(page==1){
				if(rocketBall.isActiveMode() && INF.metal >= INF.rocketBall.getCost()){
					buyRocket.getSprite().setColor(Color.LIME);
					if(controller.isOn(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true)){
						buyRocket.setActiveMode(true);
					}else{
						buyRocket.setActiveMode(false);
					}
				}else if(rocketCircle.isActiveMode() && INF.metal >= INF.rocketCircle.getCost()){
					buyRocket.getSprite().setColor(Color.LIME);
					if(controller.isOn(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true)){
						buyRocket.setActiveMode(true);
					}else{
						buyRocket.setActiveMode(false);
					}
				}else if(rocketBasic.isActiveMode() && INF.metal >= INF.rocketBasic.getCost()){
					buyRocket.getSprite().setColor(Color.LIME);
					if(controller.isOn(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true)){
						buyRocket.setActiveMode(true);
					}else{
						buyRocket.setActiveMode(false);
					}
				}else{
					buyRocket.getSprite().setColor(Color.FOREST);
				}
			}else if(page==2){
				if(rocketKinetic.isActiveMode() && INF.metal >= INF.rocketKinetic.getCost()){
					buyRocket.getSprite().setColor(Color.LIME);
					if(controller.isOn(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true)){
						buyRocket.setActiveMode(true);
					}else{
						buyRocket.setActiveMode(false);
					}
				}else if(rocketDelta.isActiveMode() && INF.metal >= INF.rocketDelta.getCost()){
					buyRocket.getSprite().setColor(Color.LIME);
					if(controller.isOn(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true)){
						buyRocket.setActiveMode(true);
					}else{
						buyRocket.setActiveMode(false);
					}
				}else if(rocketInfinity.isActiveMode() && INF.metal >= INF.rocketInfinity.getCost()){
					buyRocket.getSprite().setColor(Color.LIME);
					if(controller.isOn(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true)){
						buyRocket.setActiveMode(true);
					}else{
						buyRocket.setActiveMode(false);
					}
				}
			}else{
				buyRocket.getSprite().setColor(Color.FOREST);
			}
		}else{
			buyRocket.getSprite().setColor(Color.FOREST);
		}
		buyRocket.getSprite().draw(game.batch);
		/***/
		if(!INF.lngRussian){
			if(buyRocket.isActiveMode())
				text.draw(game.batch, "Buy", 
						buyRocket.getX() + 0.5F*buyRocket.getWidth() - 0.5F*text.getWidth("Buy"), 
						buyRocket.getY() + 0.625F*buyRocket.getHeight());
			else
				text.draw(game.batch, "Buy", 
						buyRocket.getX() + 0.5F*buyRocket.getWidth() - 0.5F*text.getWidth("Buy"), 
						buyRocket.getY() + 0.65F*buyRocket.getHeight());
		}else{
			if(buyRocket.isActiveMode())
				text.draw(game.batch, "Купить", 
						buyRocket.getX() + 0.5F*buyRocket.getWidth() - 0.5F*text.getWidth("Купить"), 
						buyRocket.getY() + 0.625F*buyRocket.getHeight());
			else
				text.draw(game.batch, "Купить", 
						buyRocket.getX() + 0.5F*buyRocket.getWidth() - 0.5F*text.getWidth("Купить"), 
						buyRocket.getY() + 0.65F*buyRocket.getHeight());
		}
	}
	
	private void buttonListener(){
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, true)){
			game.setScreen(new GameScreen(game));
			this.dispose();
		}
		if(controller.isClicked(next.getX(), next.getY(), next.getWidth(), next.getHeight(), page < 2 ? true : false, true)){
			page++;
			if(page!=3){
				rocketBall.setActiveMode(false);
				rocketCircle.setActiveMode(false);
				rocketBasic.setActiveMode(false);
				rocketKinetic.setActiveMode(false);
				rocketDelta.setActiveMode(false);
				rocketInfinity.setActiveMode(false);
				buyRocket.getSprite().setColor(Color.FOREST);
			}
		}
		if(controller.isClicked(prev.getX(), prev.getY(), prev.getWidth(), prev.getHeight(), page > 1 ? true : false, true)){
			page--;
			if(page!=0){
				rocketBall.setActiveMode(false);
				rocketCircle.setActiveMode(false);
				rocketBasic.setActiveMode(false);
				rocketKinetic.setActiveMode(false);
				rocketDelta.setActiveMode(false);
				rocketInfinity.setActiveMode(false);
				buyRocket.getSprite().setColor(Color.FOREST);
			}
		}

		if(rocketBall.isActiveMode() && INF.metal>=INF.rocketBall.getCost()){
			if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
				INF.metal -= INF.rocketBall.getCost();
				INF.currentRocket = "rocketBall";
			}
		}else if(rocketCircle.isActiveMode() && INF.metal>=INF.rocketCircle.getCost()){
			if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
				INF.metal -= INF.rocketCircle.getCost();
				INF.currentRocket = "rocketCircle";
			}
		}else if(rocketBasic.isActiveMode() && INF.metal>=INF.rocketBasic.getCost()){
			if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
				INF.metal -= INF.rocketBasic.getCost();
				INF.currentRocket = "rocketBasic";
			}
		}else if(rocketKinetic.isActiveMode() && INF.metal>=INF.rocketKinetic.getCost()){
			if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
				INF.metal -= INF.rocketKinetic.getCost();
				INF.currentRocket = "rocketKinetic";
			}
		}else if(rocketDelta.isActiveMode() && INF.metal>=INF.rocketDelta.getCost()){
			if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
				INF.metal -= INF.rocketDelta.getCost();
				INF.currentRocket = "rocketDelta";
			}
		}else if(rocketInfinity.isActiveMode() && INF.metal>=INF.rocketInfinity.getCost()){
			if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
				INF.metal -= INF.rocketInfinity.getCost();
				INF.currentRocket = "rocketInfinity";
			}
		}
		
		if(!INF.lngRussian){
			if(rocketBall.isActiveMode() && INF.metal<INF.rocketBall.getCost()){
				if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
					aoi.makeToast("Not enough metal");
				}
			}else if(rocketCircle.isActiveMode() && INF.metal<INF.rocketCircle.getCost()){
				if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
					aoi.makeToast("Not enough metal");
				}
			}else if(rocketBasic.isActiveMode() && INF.metal<INF.rocketBasic.getCost()){
				if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
					aoi.makeToast("Not enough metal");
				}
			}else if(rocketKinetic.isActiveMode() && INF.metal<INF.rocketKinetic.getCost()){
				if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
					aoi.makeToast("Not enough metal");
				}
			}else if(rocketDelta.isActiveMode() && INF.metal<INF.rocketDelta.getCost()){
				if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
					aoi.makeToast("Not enough metal");
				}
			}else if(rocketInfinity.isActiveMode() && INF.metal<INF.rocketInfinity.getCost()){
				if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
					aoi.makeToast("Not enough metal");
				}
			}
		}else{
			if(rocketBall.isActiveMode() && INF.metal<INF.rocketBall.getCost()){
				if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
					aoi.makeToast("Недостаточно металла");
				}
			}else if(rocketCircle.isActiveMode() && INF.metal<INF.rocketCircle.getCost()){
				if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
					aoi.makeToast("Недостаточно металла");
				}
			}else if(rocketBasic.isActiveMode() && INF.metal<INF.rocketBasic.getCost()){
				if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
					aoi.makeToast("Недостаточно металла");
				}
			}else if(rocketKinetic.isActiveMode() && INF.metal<INF.rocketKinetic.getCost()){
				if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
					aoi.makeToast("Недостаточно металла");
				}
			}else if(rocketDelta.isActiveMode() && INF.metal<INF.rocketDelta.getCost()){
				if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
					aoi.makeToast("Недостаточно металла");
				}
			}else if(rocketInfinity.isActiveMode() && INF.metal<INF.rocketInfinity.getCost()){
				if(controller.isClicked(buyRocket.getX(), buyRocket.getY(), buyRocket.getWidth(), buyRocket.getHeight(), true, true) && (INF.currentRocket.equals("null"))){
					aoi.makeToast("Недостаточно металла");
				}
			}
		}
		
	}
	
	private void resourceCheck(){
		if(INF.money>INF.moneyFull) INF.money = INF.moneyFull;
		if(INF.fuel>INF.fuelFull) INF.fuel = INF.fuelFull;
		if(INF.metal>INF.metalFull) INF.metal = INF.metalFull;
		/***/
		if(page < 1) page = 1;
		if(page > 2) page = 2;
		/***/
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
		rocketBall.setActiveMode(false);
		rocketBasic.setActiveMode(false);
		rocketCircle.setActiveMode(false);
		rocketKinetic.setActiveMode(false);
		rocketDelta.setActiveMode(false);
		rocketInfinity.setActiveMode(false);
		text.dispose();
	}

}