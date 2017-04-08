package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.menu.MainMenu;

public class LibraryScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final ru.erked.spaceflight.StartSFlight game;
	private ru.erked.spaceflight.controllers.SFIn controller;
	
	private Sprite backgroundSprite[];
	private int backIter = 0;
	
	private ru.erked.spaceflight.tech.SFButtonS back;
	
	private ru.erked.spaceflight.tech.SFFont text;
	
	private static int page = 1;
	private ru.erked.spaceflight.tech.SFButtonS next;
	private ru.erked.spaceflight.tech.SFButtonS prev;
	private Sprite[] dataPackets1;
	private Sprite[] dataPackets2;
	private Sprite[] dataPackets3;
	private Sprite[] dataPackets4;
	private Sprite[] dataPackets5;
	
	private Sprite blackAlpha = ru.erked.spaceflight.random.RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private ru.erked.spaceflight.AndroidOnlyInterface aoi;
	private ru.erked.spaceflight.Data data;
	private ru.erked.spaceflight.AdMob adMob;

	public LibraryScreen(final ru.erked.spaceflight.StartSFlight game){
		this.game = game;
		this.aoi = game.aoi;
		data = game.data;
		adMob = game.adMob;
	}
	
	@Override
	public void show() {

		controller = new ru.erked.spaceflight.controllers.SFIn();
		
		ru.erked.spaceflight.menu.MainMenu.music.play();
		
		backgroundSprite = new Sprite[13];
		for(int i=0;i<13;i++){
			backgroundSprite[i] = ru.erked.spaceflight.random.RES.atlas.createSprite("hangar", i+1);
			backgroundSprite[i].setBounds(0.0F, 0.0F, width, height);
		}
		
		SFButtonsInit();
		
		text = new ru.erked.spaceflight.tech.SFFont(30, Color.WHITE, 1, 2, Color.BLACK);
		
		isTrans = false;
		blackAlpha.setBounds(0.0F, 0.0F, width, height);
		blackAlpha.setAlpha(1.0F);

		adMob.show();
	}

	@Override
	public void render(float delta) {
		ru.erked.spaceflight.random.INF.elapsedTime++;
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

		drawBackButton();
		drawNextPrev();
		drawText();
		
		if(!ru.erked.spaceflight.random.INF.lngRussian){
			text.draw(game.batch, "Data packets", 0.5F*(width-text.getWidth("Data packets")), 0.965F*height);
			text.draw(game.batch, "Page: " + page + "/5", 
					next.getX() - 0.55F*text.getWidth("Page: " + page + "/5"), 
					next.getY() - 0.15F*next.getHeight());
		}else{
			text.draw(game.batch, "Пакеты данных", 0.5F*(width-text.getWidth("Пакеты данных")), 0.965F*height);
			text.draw(game.batch, "Страница: " + page + "/5", 
					next.getX() - 0.55F*text.getWidth("Страница: " + page + "/5"), 
					next.getY() - 0.15F*next.getHeight());
		}
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		buttonListener();
		
	}
	
	private void SFButtonsInit(){
		if(!ru.erked.spaceflight.random.INF.lngRussian)
			back = new ru.erked.spaceflight.tech.SFButtonS("backI", "backA", 0.225F*width, width - 0.23F*width, 0.015F*height, 2.98913F, 1.0F, -1);
		else
			back = new ru.erked.spaceflight.tech.SFButtonS("backRI", "backRA", 0.2F*width, width - 0.215F*width, 0.01F*height, 2.98913F, 1.0F, -1);
		next = new ru.erked.spaceflight.tech.SFButtonS("buttonI", "buttonA", 0.1F*width, 0.815F*width, 0.2F*height, 1.5F, 2.0F, -1);
		next.getSprite().setColor(Color.LIME);
		prev = new ru.erked.spaceflight.tech.SFButtonS("buttonI", "buttonA", 0.1F*width, 0.7F*width, 0.2F*height, 1.5F, 2.0F, -1);
		prev.getSprite().setColor(Color.LIME);
//CYAN,GOLD,MAGNETA
		dataPackets1 = new Sprite[1];
		dataPackets1[0] = ru.erked.spaceflight.random.RES.atlas.createSprite("dataPacket");
		dataPackets1[0].setBounds(0.1F*width, 0.55F*height, 0.25F*height, 0.25F*height);
///
		dataPackets2 = new Sprite[2];
		for(int i=0;i<2;i++){
			dataPackets2[i] = ru.erked.spaceflight.random.RES.atlas.createSprite("dataPacket");
			dataPackets2[i].setBounds(0.1F*width + i*0.275F*height, 0.55F*height, 0.25F*height, 0.25F*height);
		}
		dataPackets2[1].setColor(Color.CYAN);
///
		dataPackets3 = new Sprite[3];
		for(int i=0;i<3;i++){
			dataPackets3[i] = ru.erked.spaceflight.random.RES.atlas.createSprite("dataPacket");
			dataPackets3[i].setBounds(0.1F*width + i*0.275F*height, 0.55F*height, 0.25F*height, 0.25F*height);
		}
		dataPackets3[1].setColor(Color.CYAN);
		dataPackets3[2].setColor(Color.GOLD);
///
		dataPackets4 = new Sprite[4];
		for(int i=0;i<4;i++){
			dataPackets4[i] = ru.erked.spaceflight.random.RES.atlas.createSprite("dataPacket");
			dataPackets4[i].setBounds(0.1F*width + i*0.275F*height, 0.55F*height, 0.25F*height, 0.25F*height);
		}
		dataPackets4[2].setColor(Color.CYAN);
		dataPackets4[3].setColor(Color.GOLD);
///
		dataPackets5 = new Sprite[5];
		for(int i=0;i<5;i++){
			dataPackets5[i] = ru.erked.spaceflight.random.RES.atlas.createSprite("dataPacket");
			dataPackets5[i].setBounds(0.1F*width + i*0.275F*height, 0.55F*height, 0.25F*height, 0.25F*height);
		}
		dataPackets5[2].setColor(Color.CYAN);
		dataPackets5[3].setColor(Color.CYAN);
		dataPackets5[4].setColor(Color.GOLD);
		
	}
	
	private void drawText(){
		if(page==0){
			if(!ru.erked.spaceflight.random.INF.lngRussian){
				text.draw(game.batch, "No one data packet is available yet", 
						0.5F*(width-text.getWidth("No one data packet is available yet")), 
						0.575F*height);
			}else{
				text.draw(game.batch, "Ни один пакет данных не доступен", 
						0.5F*(width-text.getWidth("Ни один пакет данных не доступен")), 
						0.575F*height);
			}
		}else{
			drawDataPackets(page);
		}
	}
	private void drawBackground(){
		if(ru.erked.spaceflight.random.INF.elapsedTime % 15 == 0){
			if(backIter<12) backIter++;
			else backIter=0;
		}
	}
	private void drawBackButton(){
		if(controller.isOn(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true))
			back.setActiveMode(true);
		else
			back.setActiveMode(false);
		back.getSprite().draw(game.batch);
	}
	private void drawNextPrev(){
		if(page <= 1) prev.getSprite().setColor(Color.FOREST);
		else prev.getSprite().setColor(Color.LIME);
		if(page == ru.erked.spaceflight.random.INF.facts) next.getSprite().setColor(Color.FOREST);
		else next.getSprite().setColor(Color.LIME);
		if(controller.isOn(next.getX(), next.getY(), next.getWidth(), next.getHeight(), true) && page != ru.erked.spaceflight.random.INF.facts)
			next.setActiveMode(true);
		else
			next.setActiveMode(false);
		if(controller.isOn(prev.getX(), prev.getY(), prev.getWidth(), prev.getHeight(), true) && page > 1)
			prev.setActiveMode(true);
		else
			prev.setActiveMode(false);
		next.getSprite().draw(game.batch);
		prev.getSprite().draw(game.batch);
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
	private void drawDataPackets(int page){
		switch(page){
		case 0:{
			break;	
		}case 1:{
			for(int i=0;i<1;i++){
				dataPackets1[i].draw(game.batch);
				text.draw(game.batch, (i+1) + "", 
						dataPackets1[i].getX() + 0.5F*(dataPackets1[i].getWidth()-text.getWidth((i+1) + "")), 
						dataPackets1[i].getY() - 0.1F*dataPackets1[i].getHeight());
			}
			break;
		}case 2:{
			for(int i=0;i<2;i++){
				dataPackets2[i].draw(game.batch);
				text.draw(game.batch, (i+2) + "", 
						dataPackets2[i].getX() + 0.5F*(dataPackets2[i].getWidth()-text.getWidth((i+2) + "")), 
						dataPackets2[i].getY() - 0.1F*dataPackets2[i].getHeight());
			}
			break;
		}case 3:{
			for(int i=0;i<3;i++){
				dataPackets3[i].draw(game.batch);
				text.draw(game.batch, (i+4) + "", 
						dataPackets3[i].getX() + 0.5F*(dataPackets3[i].getWidth()-text.getWidth((i+4) + "")), 
						dataPackets3[i].getY() - 0.1F*dataPackets3[i].getHeight());
			}
			break;
		}case 4:{
			for(int i=0;i<4;i++){
				dataPackets4[i].draw(game.batch);
				text.draw(game.batch, (i+7) + "", 
						dataPackets4[i].getX() + 0.5F*(dataPackets4[i].getWidth()-text.getWidth((i+7) + "")), 
						dataPackets4[i].getY() - 0.1F*dataPackets4[i].getHeight());
			}
			break;
		}case 5:{
			for(int i=0;i<5;i++){
				dataPackets5[i].draw(game.batch);
				text.draw(game.batch, (i+11) + "", 
						dataPackets5[i].getX() + 0.5F*(dataPackets5[i].getWidth()-text.getWidth((i+11) + "")), 
						dataPackets5[i].getY() - 0.1F*dataPackets5[i].getHeight());
			}
			break;
		}default:{
			break;
		}
		}
	}
	
	private void buttonListener(){
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, true)){
			adMob.hide();
			game.setScreen(new GameScreen(game));
			this.dispose();
		}
		if(controller.isClicked(next.getX(), next.getY(), next.getWidth(), next.getHeight(), page < ru.erked.spaceflight.random.INF.facts, true)){
			if(page < ru.erked.spaceflight.random.INF.facts)
				page++;
			else
				if(!ru.erked.spaceflight.random.INF.lngRussian)
					aoi.makeToast("Not available");
				else
					aoi.makeToast("Недоступно");
		}
		if(controller.isClicked(prev.getX(), prev.getY(), prev.getWidth(), prev.getHeight(), page > 1, true))
			page--;
		switch(page){
		case 1:{
			for(int i=0;i<1;i++)
				if(controller.isClicked(dataPackets1[i].getX(), dataPackets1[i].getY(), dataPackets1[i].getWidth(), dataPackets1[i].getHeight(), true, true)) {
                    game.setScreen(new InformationScreen(game, i + 1, new LibraryScreen(game)));
                    adMob.hide();
                }
			break;
		}case 2:{
			for(int i=0;i<2;i++)
				if(controller.isClicked(dataPackets2[i].getX(), dataPackets2[i].getY(), dataPackets2[i].getWidth(), dataPackets2[i].getHeight(), true, true)) {
                    game.setScreen(new InformationScreen(game, i + 2, new LibraryScreen(game)));
                    adMob.hide();
                }
			break;
		}case 3:{
			for(int i=0;i<3;i++)
				if(controller.isClicked(dataPackets3[i].getX(), dataPackets3[i].getY(), dataPackets3[i].getWidth(), dataPackets3[i].getHeight(), true, true)) {
                    game.setScreen(new InformationScreen(game, i + 4, new LibraryScreen(game)));
                    adMob.hide();
			}
			break;
		}case 4:{
			for(int i=0;i<4;i++)
				if(controller.isClicked(dataPackets4[i].getX(), dataPackets4[i].getY(), dataPackets4[i].getWidth(), dataPackets4[i].getHeight(), true, true)) {
                    game.setScreen(new InformationScreen(game, i + 7, new LibraryScreen(game)));
                    adMob.hide();
			}
			break;
		}case 5:{
			for(int i=0;i<5;i++)
				if(controller.isClicked(dataPackets5[i].getX(), dataPackets5[i].getY(), dataPackets5[i].getWidth(), dataPackets5[i].getHeight(), true, true)) {
                    game.setScreen(new InformationScreen(game, i + 11, new LibraryScreen(game)));
                    adMob.hide();
			}
			break;
		}default:{
			break;
		}
		}
	}
	
	private void resourceCheck(){
		if(ru.erked.spaceflight.random.INF.money> ru.erked.spaceflight.random.INF.moneyFull) ru.erked.spaceflight.random.INF.money = ru.erked.spaceflight.random.INF.moneyFull;
		if(ru.erked.spaceflight.random.INF.fuel> ru.erked.spaceflight.random.INF.fuelFull) ru.erked.spaceflight.random.INF.fuel = ru.erked.spaceflight.random.INF.fuelFull;
		if(ru.erked.spaceflight.random.INF.metal> ru.erked.spaceflight.random.INF.metalFull) ru.erked.spaceflight.random.INF.metal = ru.erked.spaceflight.random.INF.metalFull;
		/***/
		if(page < 1) page = 1;
		if(page > ru.erked.spaceflight.random.INF.facts) page = ru.erked.spaceflight.random.INF.facts;
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
		text.dispose();
	}

}