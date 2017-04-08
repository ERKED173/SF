package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.tech.SFButtonS;

public class ResourceScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final ru.erked.spaceflight.StartSFlight game;
	private ru.erked.spaceflight.controllers.SFIn controller;
	
	private Sprite[] backgroundSprite;
	private int backIter = 0;
	
	private SFButtonS back;
	
	private SFButtonS upC;
	private SFButtonS upF;
	private SFButtonS upM;
	
	private Sprite moneySprite;
	private Sprite fuelSprite;
	private Sprite metalSprite;
	private Sprite moneySprite1;
	private Sprite fuelSprite1;
	private Sprite metalSprite1;
	
	private static ru.erked.spaceflight.tech.SFFont text;
	
	private Sprite blackAlpha = ru.erked.spaceflight.random.RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private ru.erked.spaceflight.Data data;
	private ru.erked.spaceflight.AdMob adMob;
	
	public ResourceScreen(final ru.erked.spaceflight.StartSFlight game){
		this.game = game;
		data = game.data;
		adMob = game.adMob;
	}
	
	@Override
	public void show() {

		controller = new ru.erked.spaceflight.controllers.SFIn();
		
		ru.erked.spaceflight.menu.MainMenu.music.play();

		backgroundSprite = new Sprite[13];
		for(int i=0;i<13;i++){
			backgroundSprite[i] = ru.erked.spaceflight.random.RES.atlas.createSprite("resource", i+1);
			backgroundSprite[i].setBounds(0.0F, 0.0F, width, height);
		}
		
		if(!INF.lngRussian){
			back = new SFButtonS("backI", "backA", 0.225F*width, width - 0.23F*width, 0.015F*height, 2.98913F, 1.0F, -1);
		}else{
			back = new SFButtonS("backRI", "backRA", 0.2F*width, width - 0.215F*width, 0.01F*height, 2.98913F, 1.0F, -1);
		}
		
		if(!INF.lngRussian)
			text = new ru.erked.spaceflight.tech.SFFont(35, Color.WHITE, 1, 2, Color.BLACK);
		else
			text = new ru.erked.spaceflight.tech.SFFont(36, Color.WHITE, 1, 2, Color.BLACK);
		
		upC = new SFButtonS("buttonI", "buttonA", 0.275F*height, 0.525F*width, 0.675F*height, 1.5F, 2.0F, -1);
		upC.getSprite().setColor(Color.TEAL);
		upF = new SFButtonS("buttonI", "buttonA", 0.275F*height, 0.525F*width, 0.45F*height, 1.5F, 2.0F, -1);
		upF.getSprite().setColor(Color.TEAL);
		upM = new SFButtonS("buttonI", "buttonA", 0.275F*height, 0.525F*width, 0.225F*height, 1.5F, 2.0F, -1);
		upM.getSprite().setColor(Color.TEAL);
		
		resourcesInit();
		
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

		if(!INF.lngRussian){
			text.draw(game.batch, "Information about resources", 0.5F*(width-text.getWidth("Information about resources")), 0.965F*height);
			text.draw(game.batch, INF.money + "/" + INF.moneyFull + " coins", 0.125F*width, moneySprite.getY() + 0.5F*moneySprite.getHeight() + 0.5F*text.getHeight("A"));
			text.draw(game.batch, INF.fuel + "/" + INF.fuelFull + " fuel", 0.125F*width, fuelSprite.getY() + 0.5F*fuelSprite.getHeight() + 0.5F*text.getHeight("A"));
			text.draw(game.batch, INF.metal + "/" + INF.metalFull + " metal", 0.125F*width, metalSprite.getY() + 0.5F*metalSprite.getHeight() + 0.5F*text.getHeight("A"));
		}else{
			text.draw(game.batch, "Информация о ресурсах", 0.5F*(width-text.getWidth("Информация о ресурсах")), 0.965F*height);
			text.draw(game.batch, INF.money + "/" + INF.moneyFull + " коинов", 0.125F*width, moneySprite.getY() + 0.5F*moneySprite.getHeight() + 0.5F*text.getHeight("A"));
			text.draw(game.batch, INF.fuel + "/" + INF.fuelFull + " топлива", 0.125F*width, fuelSprite.getY() + 0.5F*fuelSprite.getHeight() + 0.5F*text.getHeight("A"));
			text.draw(game.batch, INF.metal + "/" + INF.metalFull + " металла", 0.125F*width, metalSprite.getY() + 0.5F*metalSprite.getHeight() + 0.5F*text.getHeight("A"));
		}
		
		drawResources();
		drawUpBtns();
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		btnListener();
		
	}

	private void resourcesInit(){
		moneySprite = ru.erked.spaceflight.random.RES.atlas.createSprite("cosmocoin");
		fuelSprite = ru.erked.spaceflight.random.RES.atlas.createSprite("fuel");
		metalSprite = ru.erked.spaceflight.random.RES.atlas.createSprite("metal", -1);
		moneySprite.setBounds(0.025F*width, upC.getY()+0.5F*upC.getHeight()-0.04F*width, 0.08F*width, 0.08F*width);
		fuelSprite.setBounds(0.025F*width, upF.getY()+0.5F*upF.getHeight()-0.04F*width, 0.08F*width, 0.08F*width);
		metalSprite.setBounds(0.025F*width, upM.getY()+0.5F*upM.getHeight()-0.04F*width, 0.08F*width, 0.08F*width);
		/***/
		moneySprite1 = ru.erked.spaceflight.random.RES.atlas.createSprite("cosmocoin");
		fuelSprite1 = ru.erked.spaceflight.random.RES.atlas.createSprite("fuel");
		metalSprite1 = ru.erked.spaceflight.random.RES.atlas.createSprite("metal", -1);
		moneySprite1.setBounds(
				0.015F*width, 
				0.8F*height - 3.0F*text.getHeight("A"), 
				0.03F*width, 0.03F*width);
		fuelSprite1.setBounds(
				0.015F*width, 
				0.8F*height - 9.0F*text.getHeight("A"), 
				0.03F*width, 0.03F*width);
		metalSprite1.setBounds(
				0.015F*width, 
				0.8F*height - 15.0F*text.getHeight("A"), 
				0.03F*width, 0.03F*width);
	}

	
	private void drawBackground(){
		if(INF.elapsedTime % 15 == 0){
			if(INF.elapsedTime % 15 == 0){
				if(backIter<12) backIter++;
				else backIter=0;
			}
		}
	}
	private void drawResources(){
		moneySprite.draw(game.batch);
		fuelSprite.draw(game.batch);
		metalSprite.draw(game.batch);
		for(int i=0;i<2;i++){
			switch(i){
			case 0:{
				moneySprite1.setX(upF.getX() + 1.1F*upF.getWidth()+text.getWidth(20*INF.fuelLevel + "")+text.getWidth("  "));
				moneySprite1.setY(upF.getY() + 0.5F*(upF.getHeight()-moneySprite1.getHeight()));
				fuelSprite1.setX(upC.getX() + 1.1F*upC.getWidth()+text.getWidth(20*INF.moneyLevel + "")+text.getWidth("  "));
				fuelSprite1.setY(upC.getY() + 0.5F*(upC.getHeight()-fuelSprite1.getHeight()));
				metalSprite1.setX(upC.getX() + 1.1F*upC.getWidth()+text.getWidth(20*INF.moneyLevel + "")+text.getWidth("  "));
				metalSprite1.setY(upC.getY() + 0.5F*(upC.getHeight()-metalSprite1.getHeight()) - 2.0F*text.getHeight("A"));
				break;
			}case 1:{
				moneySprite1.setX(upM.getX() + 1.1F*upM.getWidth()+text.getWidth(20*INF.metalLevel + "")+text.getWidth("  "));
				moneySprite1.setY(upM.getY() + 0.5F*(upM.getHeight()-moneySprite1.getHeight()));
				fuelSprite1.setX(upM.getX() + 1.1F*upM.getWidth()+text.getWidth(20*INF.metalLevel + "")+text.getWidth("  "));
				fuelSprite1.setY(upM.getY() + 0.5F*(upM.getHeight()-fuelSprite1.getHeight()) - 2.0F*text.getHeight("A"));
				metalSprite1.setX(upF.getX() + 1.1F*upF.getWidth()+text.getWidth(20*INF.fuelLevel + "")+text.getWidth("  "));
				metalSprite1.setY(upF.getY() + 0.5F*(upF.getHeight()-metalSprite1.getHeight()) - 2.0F*text.getHeight("A"));
				break;
			}default:{
				break;
			}
			}
			///
			moneySprite1.draw(game.batch);
			fuelSprite1.draw(game.batch);
			metalSprite1.draw(game.batch);
		}
	}
	private void drawUpBtns(){
		/***/
		upC.getSprite().draw(game.batch);
		if(controller.isOn(upC.getX(), upC.getY(), upC.getWidth(), upC.getHeight(), true) && !(upC.getSprite().getColor().equals(Color.TEAL))){
			upC.setActiveMode(true);
			if(!INF.lngRussian){
				text.draw(game.batch, "Upgrade", 
						upC.getX() + 0.5F*(upC.getWidth()-text.getWidth("Upgrade")), 
						upC.getY() + 0.5F*(upC.getHeight()+2.05F*text.getHeight("A")));
				text.draw(game.batch, "(+25)", 
						upC.getX() + 0.5F*(upC.getWidth()-text.getWidth("(+25)")), 
						upC.getY() + 0.5F*(upC.getHeight()-1.0F*text.getHeight("A")));
			}else{
				text.draw(game.batch, "Улучшить", 
						upC.getX() + 0.5F*(upC.getWidth()-text.getWidth("Улучшить")), 
						upC.getY() + 0.5F*(upC.getHeight()+2.05F*text.getHeight("A")));
				text.draw(game.batch, "(+25)", 
						upC.getX() + 0.5F*(upC.getWidth()-text.getWidth("(+25)")), 
						upC.getY() + 0.5F*(upC.getHeight()-1.0F*text.getHeight("A")));
			}
		}else{
			upC.setActiveMode(false);
			if(!INF.lngRussian){
				text.draw(game.batch, "Upgrade", 
						upC.getX() + 0.5F*(upC.getWidth()-text.getWidth("Upgrade")), 
						upC.getY() + 0.5F*(upC.getHeight()+2.3F*text.getHeight("A")));
				text.draw(game.batch, "(+25)", 
						upC.getX() + 0.5F*(upC.getWidth()-text.getWidth("(+25)")), 
						upC.getY() + 0.5F*(upC.getHeight()-0.75F*text.getHeight("A")));
			}else{
				text.draw(game.batch, "Улучшить", 
						upC.getX() + 0.5F*(upC.getWidth()-text.getWidth("Улучшить")), 
						upC.getY() + 0.5F*(upC.getHeight()+2.3F*text.getHeight("A")));
				text.draw(game.batch, "(+25)", 
						upC.getX() + 0.5F*(upC.getWidth()-text.getWidth("(+25)")), 
						upC.getY() + 0.5F*(upC.getHeight()-0.75F*text.getHeight("A")));
			}
		}
		upF.getSprite().draw(game.batch);
		if(controller.isOn(upF.getX(), upF.getY(), upF.getWidth(), upF.getHeight(), true) && !(upF.getSprite().getColor().equals(Color.TEAL))){
			upF.setActiveMode(true);
			if(!INF.lngRussian){
				text.draw(game.batch, "Upgrade", 
						upF.getX() + 0.5F*(upF.getWidth()-text.getWidth("Upgrade")), 
						upF.getY() + 0.5F*(upF.getHeight()+2.05F*text.getHeight("A")));
				text.draw(game.batch, "(+25)", 
						upF.getX() + 0.5F*(upF.getWidth()-text.getWidth("(+25)")), 
						upF.getY() + 0.5F*(upF.getHeight()-1.0F*text.getHeight("A")));
			}else{
				text.draw(game.batch, "Улучшить", 
						upF.getX() + 0.5F*(upF.getWidth()-text.getWidth("Улучшить")), 
						upF.getY() + 0.5F*(upF.getHeight()+2.05F*text.getHeight("A")));
				text.draw(game.batch, "(+25)", 
						upF.getX() + 0.5F*(upF.getWidth()-text.getWidth("(+25)")), 
						upF.getY() + 0.5F*(upF.getHeight()-1.0F*text.getHeight("A")));
			}
		}else{
			upF.setActiveMode(false);
			if(!INF.lngRussian){
				text.draw(game.batch, "Upgrade", 
						upF.getX() + 0.5F*(upF.getWidth()-text.getWidth("Upgrade")), 
						upF.getY() + 0.5F*(upF.getHeight()+2.3F*text.getHeight("A")));
				text.draw(game.batch, "(+25)", 
						upF.getX() + 0.5F*(upF.getWidth()-text.getWidth("(+25)")), 
						upF.getY() + 0.5F*(upF.getHeight()-0.75F*text.getHeight("A")));
			}else{
				text.draw(game.batch, "Улучшить", 
						upF.getX() + 0.5F*(upF.getWidth()-text.getWidth("Улучшить")), 
						upF.getY() + 0.5F*(upF.getHeight()+2.3F*text.getHeight("A")));
				text.draw(game.batch, "(+25)", 
						upF.getX() + 0.5F*(upF.getWidth()-text.getWidth("(+25)")), 
						upF.getY() + 0.5F*(upF.getHeight()-0.75F*text.getHeight("A")));
			}
		}
		upM.getSprite().draw(game.batch);
		if(controller.isOn(upM.getX(), upM.getY(), upM.getWidth(), upM.getHeight(), true) && !(upM.getSprite().getColor().equals(Color.TEAL))){
			upM.setActiveMode(true);
			if(!INF.lngRussian){
				text.draw(game.batch, "Upgrade", 
						upM.getX() + 0.5F*(upM.getWidth()-text.getWidth("Upgrade")), 
						upM.getY() + 0.5F*(upM.getHeight()+2.05F*text.getHeight("A")));
				text.draw(game.batch, "(+25)", 
						upM.getX() + 0.5F*(upM.getWidth()-text.getWidth("(+25)")), 
						upM.getY() + 0.5F*(upM.getHeight()-1.0F*text.getHeight("A")));
			}else{
				text.draw(game.batch, "Улучшить", 
						upM.getX() + 0.5F*(upM.getWidth()-text.getWidth("Улучшить")), 
						upM.getY() + 0.5F*(upM.getHeight()+2.05F*text.getHeight("A")));
				text.draw(game.batch, "(+25)", 
						upM.getX() + 0.5F*(upM.getWidth()-text.getWidth("(+25)")), 
						upM.getY() + 0.5F*(upM.getHeight()-1.0F*text.getHeight("A")));
			}
		}else{
			upM.setActiveMode(false);
			if(!INF.lngRussian){
				text.draw(game.batch, "Upgrade", 
						upM.getX() + 0.5F*(upM.getWidth()-text.getWidth("Upgrade")), 
						upM.getY() + 0.5F*(upM.getHeight()+2.3F*text.getHeight("A")));
				text.draw(game.batch, "(+25)", 
						upM.getX() + 0.5F*(upM.getWidth()-text.getWidth("(+25)")), 
						upM.getY() + 0.5F*(upM.getHeight()-0.75F*text.getHeight("A")));
			}else{
				text.draw(game.batch, "Улучшить", 
						upM.getX() + 0.5F*(upM.getWidth()-text.getWidth("Улучшить")), 
						upM.getY() + 0.5F*(upM.getHeight()+2.3F*text.getHeight("A")));
				text.draw(game.batch, "(+25)", 
						upM.getX() + 0.5F*(upM.getWidth()-text.getWidth("(+25)")), 
						upM.getY() + 0.5F*(upM.getHeight()-0.75F*text.getHeight("A")));
			}
		}
		/***/
		drawUpInfo();
		/***/
	}
	private void drawUpInfo(){
		/***/
		if(!INF.lngRussian){
			text.draw(game.batch, "Cost:", 
					upC.getX() + 1.1F*upC.getWidth(), 
					upC.getY() + 0.5F*(upC.getHeight()+text.getWidth("A")) + 2.0F*text.getWidth("A"));
			if(INF.fuel < 20*INF.moneyLevel || INF.metal < 20*INF.moneyLevel || INF.moneyFull == 50000){
				upC.getSprite().setColor(Color.TEAL);
			}else{
				upC.getSprite().setColor(Color.CYAN);
			}
			if(INF.moneyFull != 50000){
				text.draw(game.batch, (int)(20*INF.moneyLevel) + "", 
						upC.getX() + 1.1F*upC.getWidth(), 
						upC.getY() + 0.5F*(upC.getHeight()+text.getWidth("A")));
				text.draw(game.batch, (int)(20*INF.moneyLevel) + "", 
						upC.getX() + 1.1F*upC.getWidth(), 
						upC.getY() + 0.5F*(upC.getHeight()+text.getWidth("A")) - 2.0F*text.getWidth("A"));
			}else{
				text.draw(game.batch, "--------", 
						upC.getX() + 1.1F*upC.getWidth(), 
						upC.getY() + 0.5F*(upC.getHeight()+text.getWidth("A")));
				text.draw(game.batch, "--------", 
						upC.getX() + 1.1F*upC.getWidth(), 
						upC.getY() + 0.5F*(upC.getHeight()+text.getWidth("A")) - 2.0F*text.getWidth("A"));
			}
			////
			text.draw(game.batch, "Cost:", 
					upF.getX() + 1.1F*upF.getWidth(), 
					upF.getY() + 0.5F*(upF.getHeight()+text.getWidth("A")) + 2.0F*text.getWidth("A"));
			if(INF.money < 20*INF.fuelLevel || INF.metal < 20*INF.fuelLevel || INF.fuelFull == 50000){
				upF.getSprite().setColor(Color.TEAL);
			}else{
				upF.getSprite().setColor(Color.CYAN);
			}
			if(INF.fuelFull != 50000){
				text.draw(game.batch, (int)(20*INF.fuelLevel) + "", 
						upF.getX() + 1.1F*upF.getWidth(), 
						upF.getY() + 0.5F*(upF.getHeight()+text.getWidth("A")));
				text.draw(game.batch, (int)(20*INF.fuelLevel) + "", 
						upF.getX() + 1.1F*upF.getWidth(), 
						upF.getY() + 0.5F*(upF.getHeight()+text.getWidth("A")) - 2.0F*text.getWidth("A"));
			}else{
				text.draw(game.batch, "--------", 
						upF.getX() + 1.1F*upF.getWidth(), 
						upF.getY() + 0.5F*(upF.getHeight()+text.getWidth("A")));
				text.draw(game.batch, "--------", 
						upF.getX() + 1.1F*upF.getWidth(), 
						upF.getY() + 0.5F*(upF.getHeight()+text.getWidth("A")) - 2.0F*text.getWidth("A"));
			}
			////
			text.draw(game.batch, "Cost:", 
					upM.getX() + 1.1F*upM.getWidth(), 
					upM.getY() + 0.5F*(upM.getHeight()+text.getWidth("A")) + 2.0F*text.getWidth("A"));
			if(INF.money < 20*INF.metalLevel || INF.fuel < 20*INF.metalLevel || INF.metalFull == 50000){
				upM.getSprite().setColor(Color.TEAL);
			}else{
				upM.getSprite().setColor(Color.CYAN);
			}
			if(INF.metalFull != 50000){
				text.draw(game.batch, (int)(20*INF.metalLevel) + "", 
						upM.getX() + 1.1F*upM.getWidth(), 
						upM.getY() + 0.5F*(upM.getHeight()+text.getWidth("A")));
				text.draw(game.batch, (int)(20*INF.metalLevel) + "", 
						upM.getX() + 1.1F*upM.getWidth(), 
						upM.getY() + 0.5F*(upM.getHeight()+text.getWidth("A")) - 2.0F*text.getWidth("A"));
			}else{
				text.draw(game.batch, "--------", 
						upM.getX() + 1.1F*upM.getWidth(), 
						upM.getY() + 0.5F*(upM.getHeight()+text.getWidth("A")));
				text.draw(game.batch, "--------", 
						upM.getX() + 1.1F*upM.getWidth(), 
						upM.getY() + 0.5F*(upM.getHeight()+text.getWidth("A")) - 2.0F*text.getWidth("A"));
			}
			////
		}else{
			text.draw(game.batch, "Цена:", 
					upC.getX() + 1.1F*upC.getWidth(), 
					upC.getY() + 0.5F*(upC.getHeight()+text.getWidth("A")) + 2.0F*text.getWidth("A"));
			if(INF.fuel < 20*INF.moneyLevel || INF.metal < 20*INF.moneyLevel || INF.moneyFull == 50000){
				upC.getSprite().setColor(Color.TEAL);
			}else{
				upC.getSprite().setColor(Color.CYAN);
			}
			if(INF.moneyFull != 50000){
				text.draw(game.batch, (int)(20*INF.moneyLevel) + "", 
						upC.getX() + 1.1F*upC.getWidth(), 
						upC.getY() + 0.5F*(upC.getHeight()+text.getWidth("A")));
				text.draw(game.batch, (int)(20*INF.moneyLevel) + "", 
						upC.getX() + 1.1F*upC.getWidth(), 
						upC.getY() + 0.5F*(upC.getHeight()+text.getWidth("A")) - 2.0F*text.getWidth("A"));
			}else{
				text.draw(game.batch, "--------", 
						upC.getX() + 1.1F*upC.getWidth(), 
						upC.getY() + 0.5F*(upC.getHeight()+text.getWidth("A")));
				text.draw(game.batch, "--------", 
						upC.getX() + 1.1F*upC.getWidth(), 
						upC.getY() + 0.5F*(upC.getHeight()+text.getWidth("A")) - 2.0F*text.getWidth("A"));
			}
			////
			text.draw(game.batch, "Цена:", 
					upF.getX() + 1.1F*upF.getWidth(), 
					upF.getY() + 0.5F*(upF.getHeight()+text.getWidth("A")) + 2.0F*text.getWidth("A"));
			if(INF.money < 20*INF.fuelLevel || INF.metal < 20*INF.fuelLevel || INF.fuelFull == 50000){
				upF.getSprite().setColor(Color.TEAL);
			}else{
				upF.getSprite().setColor(Color.CYAN);
			}
			if(INF.fuelFull != 50000){
				text.draw(game.batch, (int)(20*INF.fuelLevel) + "", 
						upF.getX() + 1.1F*upF.getWidth(), 
						upF.getY() + 0.5F*(upF.getHeight()+text.getWidth("A")));
				text.draw(game.batch, (int)(20*INF.fuelLevel) + "", 
						upF.getX() + 1.1F*upF.getWidth(), 
						upF.getY() + 0.5F*(upF.getHeight()+text.getWidth("A")) - 2.0F*text.getWidth("A"));
			}else{
				text.draw(game.batch, "--------", 
						upF.getX() + 1.1F*upF.getWidth(), 
						upF.getY() + 0.5F*(upF.getHeight()+text.getWidth("A")));
				text.draw(game.batch, "--------", 
						upF.getX() + 1.1F*upF.getWidth(), 
						upF.getY() + 0.5F*(upF.getHeight()+text.getWidth("A")) - 2.0F*text.getWidth("A"));
			}
			////
			text.draw(game.batch, "Цена:", 
					upM.getX() + 1.1F*upM.getWidth(), 
					upM.getY() + 0.5F*(upM.getHeight()+text.getWidth("A")) + 2.0F*text.getWidth("A"));
			if(INF.money < 20*INF.metalLevel || INF.fuel < 20*INF.metalLevel || INF.metalFull == 50000){
				upM.getSprite().setColor(Color.TEAL);
			}else{
				upM.getSprite().setColor(Color.CYAN);
			}
			if(INF.metalFull != 50000){
				text.draw(game.batch, (int)(20*INF.metalLevel) + "", 
						upM.getX() + 1.1F*upM.getWidth(), 
						upM.getY() + 0.5F*(upM.getHeight()+text.getWidth("A")));
				text.draw(game.batch, (int)(20*INF.metalLevel) + "", 
						upM.getX() + 1.1F*upM.getWidth(), 
						upM.getY() + 0.5F*(upM.getHeight()+text.getWidth("A")) - 2.0F*text.getWidth("A"));
			}else{
				text.draw(game.batch, "--------", 
						upM.getX() + 1.1F*upM.getWidth(), 
						upM.getY() + 0.5F*(upM.getHeight()+text.getWidth("A")));
				text.draw(game.batch, "--------", 
						upM.getX() + 1.1F*upM.getWidth(), 
						upM.getY() + 0.5F*(upM.getHeight()+text.getWidth("A")) - 2.0F*text.getWidth("A"));
			}
			////
		}
		/***/
	}
	
	private void btnListener(){
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, true)){
			adMob.hide();
			game.setScreen(new GameScreen(game));
			this.dispose();
		}
		if(controller.isClicked(upC.getX(), upC.getY(), upC.getWidth(), upC.getHeight(), true, true) && INF.moneyFull != 50000){
			if(!(INF.fuel < 20*INF.moneyLevel) && !(INF.metal < 20*INF.moneyLevel)){
				INF.fuel -= 20*INF.moneyLevel;
				INF.metal -= 20*INF.moneyLevel;
				if(25*(INF.moneyLevel+1) > 50000){
					INF.moneyFull = 50000;
				}else{
					INF.moneyLevel++;
					INF.moneyFull = (int)(25*INF.moneyLevel);
				}
			}
		}
		//---//
		if(controller.isClicked(upF.getX(), upF.getY(), upF.getWidth(), upF.getHeight(), true, true) && INF.metalFull != 50000){
			if(!(INF.money < 20*INF.fuelLevel) && !(INF.metal < 20*INF.fuelLevel)){
				INF.money -= 20*INF.fuelLevel;
				INF.metal -= 20*INF.fuelLevel;
				if(25*INF.fuelLevel+1 > 50000){
					INF.fuelFull = 50000;
				}else{
					INF.fuelLevel++;
					INF.fuelFull = (int)(25*INF.fuelLevel);
				}
			}
		}
		//---//
		if(controller.isClicked(upM.getX(), upM.getY(), upM.getWidth(), upM.getHeight(), true, true) && INF.metalFull != 50000){
			if(!(INF.money < 20*INF.metalLevel) && !(INF.fuel < 20*INF.metalLevel)){
				INF.money -= 20*INF.metalLevel;
				INF.fuel -= 20*INF.metalLevel;
				if(25*INF.metalLevel+1 > 50000){
					INF.metalFull = 50000;
				}else{
					INF.metalLevel++;
					INF.metalFull = (int)(25*INF.metalLevel);
				}
			}
		}
		//---//
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
		text.dispose();
	}

}