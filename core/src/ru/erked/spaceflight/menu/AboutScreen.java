package ru.erked.spaceflight.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class AboutScreen implements Screen{
	
	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final ru.erked.spaceflight.StartSFlight game;
	private ru.erked.spaceflight.controllers.SFIn controller;
	
	public static float planet1PrevRotation = 0.0F;
	public static float planet2PrevRotation = 0.0F;
	public static float cometPrevRotation;
	
	private String[] str;
	private float merge[];
	private static ru.erked.spaceflight.tech.SFFont header;
	private static ru.erked.spaceflight.tech.SFFont headerB;
	private static ru.erked.spaceflight.tech.SFFont text;
	private static ru.erked.spaceflight.tech.SFFont textB;
	private float textY;
	
	private ru.erked.spaceflight.tech.SFButtonS back;
	
	private Sprite blackAlpha = ru.erked.spaceflight.random.RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTransAbout;
	
	private static float prevDragY;
	
	private ru.erked.spaceflight.Data data;
	
	public AboutScreen(final ru.erked.spaceflight.StartSFlight game){
		this.game = game;
		data = game.data;
	}
	
	@Override
	public void show() {
		
		controller = new ru.erked.spaceflight.controllers.SFIn();
		isTransAbout = false;
		
		text = new ru.erked.spaceflight.tech.SFFont(30, Color.WHITE);
		textB = new ru.erked.spaceflight.tech.SFFont(30, Color.BLACK);
		header = new ru.erked.spaceflight.tech.SFFont(10, Color.WHITE);
		headerB = new ru.erked.spaceflight.tech.SFFont(10, Color.BLACK);
		
		MainMenu.music.play();
		
		if(!ru.erked.spaceflight.random.INF.lngRussian){
			back = new ru.erked.spaceflight.tech.SFButtonS("backI", "backA", 0.225F*width, width - 0.23F*width, 0.015F*height, 2.98913F, 1.0F, -1);
		}else{
			back = new ru.erked.spaceflight.tech.SFButtonS("backRI", "backRA", 0.2F*width, width - 0.215F*width, 0.01F*height, 2.98913F, 1.0F, -1);
		}
		
		blackAlpha.setBounds(0.0F, 0.0F, width, height);
		blackAlpha.setAlpha(1.0F);
		
		textY = 0;
		
		if(!ru.erked.spaceflight.random.INF.lngRussian)
			str = new String[]{"Space Flight","Developers:","Programmer, sound engineer","and game designer:","Egor Klementev","With support of:","Samsung SW&IT School", "IT-lyceum KFU","Contacts:","egor.klementev.99@mail.ru"};
		else
			str = new String[]{"Space Flight","Разработчики:","Программист, звукорежиссер","и геймдизайнер:","Егор Клементьев","При поддержке:","Samsung SW&IT School", "IT-лицей КФУ","Контакты:","egor.klementev.99@mail.ru"};
		
		if(!ru.erked.spaceflight.random.INF.lngRussian){
			merge = new float[]{0.0F,
					4.5F*text.getHeight("A"),
					6.0F*text.getHeight("A"),
					7.5F*text.getHeight("A"),
					10.0F*text.getHeight("A"),
					12.5F*text.getHeight("A"),
					15.0F*text.getHeight("A"),
					16.5F*text.getHeight("A"),
					19.0F*text.getHeight("A"),
					20.5F*text.getHeight("A")};
		}else{
			merge = new float[]{0.0F,
					4.5F*text.getHeight("A"),
					6.0F*text.getHeight("A"),
					7.5F*text.getHeight("A"),
					10.0F*text.getHeight("A"),
					12.5F*text.getHeight("A"),
					14.0F*text.getHeight("A"),
					15.5F*text.getHeight("A"),
					18.0F*text.getHeight("A"),
					19.5F*text.getHeight("A")};
		}
		
	}

	@Override
	public void render(float delta) {
		ru.erked.spaceflight.random.INF.elapsedTime++;
		
		if(alp>0.0F && !isTransAbout){
			blackAlpha.setAlpha(alp);
			alp-=0.05F;
		}else if(!isTransAbout){
			blackAlpha.setAlpha(0.0F);
			alp = 0.0F;
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		MainMenu.planet1Sprite.setOriginCenter();
		MainMenu.planet1Sprite.rotate(0.0175F);
		
		MainMenu.planet2Sprite.setOriginCenter();
		MainMenu.planet2Sprite.rotate(-0.01F);
		MainMenu.planet2Sprite.setY((float)(MainMenu.planet2Sprite.getY() + 0.03F));
		MainMenu.planet2Sprite.setX(MainMenu.planet2Sprite.getX() - 0.3F);
		if(MainMenu.planet2Sprite.getX() < (0-1)*width){
			MainMenu.planet2Sprite.setX(1.05F*width + MainMenu.planet2Sprite.getWidth());
			MainMenu.planet2Sprite.setY(0.05F*height);
		}

		MainMenu.cometSprite.setOrigin(2*width, 2*height);
		MainMenu.cometSprite.rotate(0.25F);
		
		textY+=0.85F;
		if(!((textY - 18.0F*text.getHeight("A")) < (height + 1.5F*header.getHeight("A")))){
			textY=-1.5F*header.getHeight("A");
		}
		if(textY < -1.5F*header.getHeight("A")){
			textY=height + 18.0F*text.getHeight("A");
		}
		
		if(prevDragY != 0.0F && ru.erked.spaceflight.controllers.SFIn.tdrRY != 0.0F)
			textY += ru.erked.spaceflight.controllers.SFIn.tdrRY - prevDragY;
		prevDragY = ru.erked.spaceflight.controllers.SFIn.tdrRY;
		
		game.batch.begin();
		
		MainMenu.backgroundSprite.draw(game.batch);
		MainMenu.planet1Sprite.draw(game.batch);
		MainMenu.planet2Sprite.draw(game.batch);		
		MainMenu.cometSprite.draw(game.batch);
		
		for(int i=0;i<str.length;i++){
			headerB.draw(game.batch, str[0], 0.505F*width - 0.5F*headerB.getWidth(str[0]), textY - 0.005F*height);
			header.draw(game.batch, str[0], 0.5F*width - 0.5F*header.getWidth(str[0]), textY);
			if(i!=0){
				textB.draw(game.batch, str[i], 0.5025F*width - 0.5F*textB.getWidth(str[i]), textY - merge[i] - 0.005F*height);
				text.draw(game.batch, str[i], 0.5F*width - 0.5F*text.getWidth(str[i]), textY - merge[i]);
			}
		}
		
		if(controller.isOn(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true)){
			back.setActiveMode(true);
		}else{
			back.setActiveMode(false);
		}
		back.getSprite().draw(game.batch);
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		cometPrevRotation = MainMenu.cometSprite.getRotation();
		planet1PrevRotation = MainMenu.planet1Sprite.getRotation();
		planet2PrevRotation = MainMenu.planet2Sprite.getRotation();
		
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, true) || isTransAbout){
			isTransAbout = true;
			if(alp>1.0F){
				this.dispose();
				game.setScreen(new MainMenu(game));
			}else{
				blackAlpha.setAlpha(alp);
				alp+=0.05F;
			}
		}
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
		data.loadSF();
	}

	@Override
	public void hide() {
		data.saveSF();
	}

	@Override
	public void dispose() {
		text.dispose();
		textB.dispose();
		header.dispose();
		headerB.dispose();
		
		data.saveSF();
		
		game.dispose();
		
		MainMenu.planet2X = MainMenu.planet2Sprite.getX();
		MainMenu.planet2Y = MainMenu.planet2Sprite.getY();
		
		MainMenu.isFirstScreen = false;
	}

}