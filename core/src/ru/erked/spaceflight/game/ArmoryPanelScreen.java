package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.AndroidOnlyInterface;
import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;

public class ArmoryPanelScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final ru.erked.spaceflight.StartSFlight game;
	private ru.erked.spaceflight.controllers.SFIn controller;
	
	private Sprite backgroundSprite[];
	private int backIter = 0;
	
	private ru.erked.spaceflight.tech.SFButtonS back;
	
	private Sprite coinIcon;
	private Sprite coinIcon1;
	
	private ru.erked.spaceflight.tech.SFFont text;
	
	private int page = 1;
	private ru.erked.spaceflight.tech.SFButtonS next;
	private ru.erked.spaceflight.tech.SFButtonS prev;
	private ru.erked.spaceflight.tech.SFButtonS buy;
	
	private ru.erked.spaceflight.tech.SFButtonS bomb;
	private ru.erked.spaceflight.tech.SFButtonS coloumn;
	private ru.erked.spaceflight.tech.SFButtonS line;
	private ru.erked.spaceflight.tech.SFButtonS time;
	private Sprite lock;
	
	private Sprite blackAlpha = ru.erked.spaceflight.random.RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private AndroidOnlyInterface aoi;
	private ru.erked.spaceflight.Data data;
	
	public ArmoryPanelScreen(final ru.erked.spaceflight.StartSFlight game){
		this.game = game;
		aoi = game.aoi;
		data = game.data;
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
		
		coinIcon = ru.erked.spaceflight.random.RES.atlas.createSprite("cosmocoin");
		coinIcon.setBounds(0.095F*width, 0.325F*height, 0.1F*height, 0.1F*height);
		coinIcon1 = ru.erked.spaceflight.random.RES.atlas.createSprite("cosmocoin");
		coinIcon1.setBounds(0.095F*width, 0.325F*height, 0.05F*height, 0.05F*height);
		lock = ru.erked.spaceflight.random.RES.atlas.createSprite("lock");
		
		SFButtonsInit();

		if(!INF.lngRussian)
			text = new ru.erked.spaceflight.tech.SFFont(30, Color.WHITE, 1, 2, Color.BLACK);
		else
			text = new ru.erked.spaceflight.tech.SFFont(35, Color.WHITE, 1, 2, Color.BLACK);
		
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

		coinIcon.draw(game.batch);
		text.draw(game.batch, ": " + INF.money + "/" + INF.moneyFull, coinIcon.getX() + 1.1F*coinIcon.getWidth(), coinIcon.getY() + 0.65F*coinIcon.getHeight());
		
		drawBackButton();
		drawNextPrev();
		drawBonuses();
		
		if(!INF.lngRussian){
			text.draw(game.batch, "Bonus market", 0.5F*(width-text.getWidth("Bonus market")), 0.965F*height);
			text.draw(game.batch, "Page: " + page, 
					next.getX() - 0.55F*text.getWidth("Page: 1"), 
					next.getY() - 0.15F*next.getHeight());
		}else{
			text.draw(game.batch, "Магазин бонусов", 0.5F*(width-text.getWidth("Магазин бонусов")), 0.965F*height);
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
			back = new ru.erked.spaceflight.tech.SFButtonS("backI", "backA", 0.225F*width, width - 0.23F*width, 0.015F*height, 2.98913F, 1.0F, -1);
		}else{
			back = new ru.erked.spaceflight.tech.SFButtonS("backRI", "backRA", 0.2F*width, width - 0.215F*width, 0.01F*height, 2.98913F, 1.0F, -1);
		}
		next = new ru.erked.spaceflight.tech.SFButtonS("buttonI", "buttonA", 0.1F*width, 0.815F*width, 0.2F*height, 1.5F, 2.0F, -1);
		next.getSprite().setColor(Color.LIME);
		prev = new ru.erked.spaceflight.tech.SFButtonS("buttonI", "buttonA", 0.1F*width, 0.7F*width, 0.2F*height, 1.5F, 2.0F, -1);
		prev.getSprite().setColor(Color.LIME);
		buy = new ru.erked.spaceflight.tech.SFButtonS("buttonI", "buttonA", 0.125F*width, 0.1F*width, 0.15F*height, 1.5F, 2.0F, -1);
		buy.getSprite().setColor(Color.LIME);
		
		bomb = new ru.erked.spaceflight.tech.SFButtonS("bombI", "bombA", 0.4F*height, 0.6F*width, 0.45F*height, 1.0F, 2.0F, -1);
		if(INF.planetLevel < 5) bomb.getSprite().setColor(Color.GRAY);
		coloumn = new ru.erked.spaceflight.tech.SFButtonS("coloumnI", "coloumnA", 0.4F*height, 0.6F*width, 0.45F*height, 1.0F, 2.0F, -1);
		if(INF.planetLevel < 15) coloumn.getSprite().setColor(Color.GRAY);
		line = new ru.erked.spaceflight.tech.SFButtonS("lineI", "lineA", 0.4F*height, 0.6F*width, 0.45F*height, 1.0F, 2.0F, -1);
		if(INF.planetLevel < 25) line.getSprite().setColor(Color.GRAY);
		time = new ru.erked.spaceflight.tech.SFButtonS("timeI", "timeA", 0.4F*height, 0.6F*width, 0.45F*height, 1.0F, 2.0F, -1);
		if(INF.planetLevel < 35) time.getSprite().setColor(Color.GRAY);
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
		if(page == 4) next.getSprite().setColor(Color.FOREST);
		else next.getSprite().setColor(Color.LIME);
		if(controller.isOn(next.getX(), next.getY(), next.getWidth(), next.getHeight(), true) && page != 4){
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
		if(page == 1){
			if(bomb.isActiveMode()){
				if(INF.money >= 30 && !INF.isBombActive && !INF.isColoumnActive && !INF.isLineActive && !INF.isTimeActive){
					buy.getSprite().setColor(Color.LIME);
					if(controller.isOn(buy.getX(), buy.getY(), buy.getWidth(), buy.getHeight(), true))
						buy.setActiveMode(true);
					else
						buy.setActiveMode(false);
				}else{
					buy.getSprite().setColor(Color.FOREST);
				}
			}else{
				buy.getSprite().setColor(Color.FOREST);
			}
		}else if(page == 2){
			if(coloumn.isActiveMode()){
				if(INF.money >= 50 && !INF.isBombActive && !INF.isColoumnActive && !INF.isLineActive && !INF.isTimeActive){
					buy.getSprite().setColor(Color.LIME);
					if(controller.isOn(buy.getX(), buy.getY(), buy.getWidth(), buy.getHeight(), true))
						buy.setActiveMode(true);
					else
						buy.setActiveMode(false);
				}else{
					buy.getSprite().setColor(Color.FOREST);
				}
			}else{
				buy.getSprite().setColor(Color.FOREST);
			}
		}else if(page == 3){
			if(line.isActiveMode()){
				if(INF.money >= 100 && !INF.isBombActive && !INF.isColoumnActive && !INF.isLineActive && !INF.isTimeActive){
					buy.getSprite().setColor(Color.LIME);
					if(controller.isOn(buy.getX(), buy.getY(), buy.getWidth(), buy.getHeight(), true))
						buy.setActiveMode(true);
					else
						buy.setActiveMode(false);
				}else{
					buy.getSprite().setColor(Color.FOREST);
				}
			}else{
				buy.getSprite().setColor(Color.FOREST);
			}
		}else if(page == 4){
			if(time.isActiveMode()){
				if(INF.money >= 150 && !INF.isBombActive && !INF.isColoumnActive && !INF.isLineActive && !INF.isTimeActive){
					buy.getSprite().setColor(Color.LIME);
					if(controller.isOn(buy.getX(), buy.getY(), buy.getWidth(), buy.getHeight(), true))
						buy.setActiveMode(true);
					else
						buy.setActiveMode(false);
				}else{
					buy.getSprite().setColor(Color.FOREST);
				}
			}else{
				buy.getSprite().setColor(Color.FOREST);
			}
		}
		buy.getSprite().draw(game.batch);
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
		if(!INF.lngRussian){
			if(buy.isActiveMode())
				text.draw(game.batch, "Buy", 
						buy.getX() + 0.5F*buy.getWidth() - 0.5F*text.getWidth("Buy"), 
						buy.getY() + 0.625F*buy.getHeight());
			else
				text.draw(game.batch, "Buy", 
						buy.getX() + 0.5F*buy.getWidth() - 0.5F*text.getWidth("Buy"), 
						buy.getY() + 0.65F*buy.getHeight());
		}else{
			if(buy.isActiveMode())
				text.draw(game.batch, "Купить", 
						buy.getX() + 0.5F*buy.getWidth() - 0.5F*text.getWidth("Купить"), 
						buy.getY() + 0.625F*buy.getHeight());
			else
				text.draw(game.batch, "Купить", 
						buy.getX() + 0.5F*buy.getWidth() - 0.5F*text.getWidth("Купить"), 
						buy.getY() + 0.65F*buy.getHeight());
		}
		/***/
	}
	private void drawBonuses(){
		if(page == 1){
			bomb.getSprite().draw(game.batch);
			if(controller.isClicked(bomb.getX(), bomb.getY(), bomb.getWidth(), bomb.getHeight(), true, true) && INF.planetLevel >= 5){
				bomb.getSprite().setColor(Color.WHITE);
				if(!bomb.isActiveMode()) bomb.setActiveMode(true);
				else bomb.setActiveMode(false);
			}else if(INF.planetLevel < 5){
				bomb.getSprite().setColor(Color.GRAY);
				lock.setBounds(bomb.getX() + 0.2F*bomb.getWidth(), bomb.getY() + 0.2F*bomb.getHeight(), 0.6F*bomb.getWidth(), 0.6F*bomb.getHeight());
				lock.draw(game.batch);
				if(!INF.lngRussian){
					text.draw(game.batch, "Will be available after level 5", 0.1F*width, 0.825F*height);
				}else{
					text.draw(game.batch, "Будет доступно после уровня 5", 0.1F*width, 0.825F*height);
				}
			}
			if(bomb.isActiveMode()){
				if(!INF.lngRussian){
					coinIcon1.setX(0.1F*width + text.getWidth("Price: 30 "));
					coinIcon1.setY(0.825F*height - 3.5F*text.getHeight("A") - 0.5F*coinIcon1.getHeight());
				}else{
					coinIcon1.setX(0.1F*width + text.getWidth("Цена: 30 "));
					coinIcon1.setY(0.825F*height - 3.5F*text.getHeight("A") - 0.5F*coinIcon1.getHeight());
				}
				coinIcon1.draw(game.batch);
				if(!INF.lngRussian){
					text.draw(game.batch, "Name: Bomb", 0.1F*width, 0.825F*height);
					text.draw(game.batch, "Destroys gems around itself", 0.1F*width, 0.825F*height - 1.5F*text.getHeight("A"));
					text.draw(game.batch, "Price: 30", 0.1F*width, 0.825F*height - 3.0F*text.getHeight("A"));
				}else{
					text.draw(game.batch, "Название: Бомба", 0.1F*width, 0.825F*height);
					text.draw(game.batch, "Уничтожает гемы вокруг себя", 0.1F*width, 0.825F*height - 1.5F*text.getHeight("A"));
					text.draw(game.batch, "Цена: 30", 0.1F*width, 0.825F*height - 3.0F*text.getHeight("A"));
				}
			}
			if(INF.isBombActive)
				if(!INF.lngRussian)
					text.draw(game.batch, "Selected", bomb.getX() + 0.5F*(bomb.getWidth()-text.getWidth("Selected")) , bomb.getY() + 1.0F*text.getHeight("A"));
				else
					text.draw(game.batch, "Выбрано", bomb.getX() + 0.5F*(bomb.getWidth()-text.getWidth("Выбрано")), bomb.getY() + 1.0F*text.getHeight("A"));	
		}else if(page == 2){
			coloumn.getSprite().draw(game.batch);
			if(controller.isClicked(coloumn.getX(), coloumn.getY(), coloumn.getWidth(), coloumn.getHeight(), true, true) && INF.planetLevel >= 15){
				coloumn.getSprite().setColor(Color.WHITE);
				if(!coloumn.isActiveMode()) coloumn.setActiveMode(true);
				else coloumn.setActiveMode(false);
			}else if(INF.planetLevel < 15){
				coloumn.getSprite().setColor(Color.GRAY);
				lock.setBounds(coloumn.getX() + 0.2F*coloumn.getWidth(), coloumn.getY() + 0.2F*coloumn.getHeight(), 0.6F*coloumn.getWidth(), 0.6F*coloumn.getHeight());
				lock.draw(game.batch);
				if(!INF.lngRussian){
					text.draw(game.batch, "Will be available after level 15", 0.1F*width, 0.825F*height);
				}else{
					text.draw(game.batch, "Будет доступно после уровня 15", 0.1F*width, 0.825F*height);
				}
			}
			if(coloumn.isActiveMode()){
				if(!INF.lngRussian){
					coinIcon1.setX(0.1F*width + text.getWidth("Price: 50 "));
					coinIcon1.setY(0.825F*height - 3.5F*text.getHeight("A") - 0.5F*coinIcon1.getHeight());
				}else{
					coinIcon1.setX(0.1F*width + text.getWidth("Цена: 50 "));
					coinIcon1.setY(0.825F*height - 3.5F*text.getHeight("A") - 0.5F*coinIcon1.getHeight());
				}
				coinIcon1.draw(game.batch);
				if(!INF.lngRussian){
					text.draw(game.batch, "Name: Coloumn", 0.1F*width, 0.825F*height);
					text.draw(game.batch, "Destroys coloumn of gems", 0.1F*width, 0.825F*height - 1.5F*text.getHeight("A"));
					text.draw(game.batch, "Price: 50", 0.1F*width, 0.825F*height - 3.0F*text.getHeight("A"));
				}else{
					text.draw(game.batch, "Название: Столбец", 0.1F*width, 0.825F*height);
					text.draw(game.batch, "Уничтожает столбец гемов", 0.1F*width, 0.825F*height - 1.5F*text.getHeight("A"));
					text.draw(game.batch, "Цена: 50", 0.1F*width, 0.825F*height - 3.0F*text.getHeight("A"));
				}
			}
			if(INF.isColoumnActive)
				if(!INF.lngRussian)
					text.draw(game.batch, "Selected", coloumn.getX() + 0.5F*(coloumn.getWidth()-text.getWidth("Selected")), coloumn.getY() - 1.0F*text.getHeight("A"));
				else
					text.draw(game.batch, "Выбрано", coloumn.getX() + 0.5F*(coloumn.getWidth()-text.getWidth("Выбрано")), coloumn.getY() - 1.0F*text.getHeight("A"));	
		}else if(page == 3){
			line.getSprite().draw(game.batch);
			if(controller.isClicked(line.getX(), line.getY(), line.getWidth(), line.getHeight(), true, true) && INF.planetLevel >= 25){
				line.getSprite().setColor(Color.WHITE);
				if(!line.isActiveMode()) line.setActiveMode(true);
				else line.setActiveMode(false);
			}else if(INF.planetLevel < 25){
				line.getSprite().setColor(Color.GRAY);
				lock.setBounds(line.getX() + 0.2F*line.getWidth(), line.getY() + 0.2F*line.getHeight(), 0.6F*line.getWidth(), 0.6F*line.getHeight());
				lock.draw(game.batch);
				if(!INF.lngRussian){
					text.draw(game.batch, "Will be available after level 25", 0.1F*width, 0.825F*height);
				}else{
					text.draw(game.batch, "Будет доступно после уровня 25", 0.1F*width, 0.825F*height);
				}
			}
			if(line.isActiveMode()){
				if(!INF.lngRussian){
					coinIcon1.setX(0.1F*width + text.getWidth("Price: 100 "));
					coinIcon1.setY(0.825F*height - 3.5F*text.getHeight("A") - 0.5F*coinIcon1.getHeight());
				}else{
					coinIcon1.setX(0.1F*width + text.getWidth("Цена: 100 "));
					coinIcon1.setY(0.825F*height - 3.5F*text.getHeight("A") - 0.5F*coinIcon1.getHeight());
				}
				coinIcon1.draw(game.batch);
				if(!INF.lngRussian){
					text.draw(game.batch, "Name: Line", 0.1F*width, 0.825F*height);
					text.draw(game.batch, "Destroys line of gems", 0.1F*width, 0.825F*height - 1.5F*text.getHeight("A"));
					text.draw(game.batch, "Price: 100", 0.1F*width, 0.825F*height - 3.0F*text.getHeight("A"));
				}else{
					text.draw(game.batch, "Название: Линия", 0.1F*width, 0.825F*height);
					text.draw(game.batch, "Уничтожает линию гемов", 0.1F*width, 0.825F*height - 1.5F*text.getHeight("A"));
					text.draw(game.batch, "Цена: 100", 0.1F*width, 0.825F*height - 3.0F*text.getHeight("A"));
				}
			}
			if(INF.isLineActive)
				if(!INF.lngRussian)
					text.draw(game.batch, "Selected", line.getX() + 0.5F*(line.getWidth()-text.getWidth("Selected")), line.getY() + 1.0F*text.getHeight("A"));
				else
					text.draw(game.batch, "Выбрано", line.getX() + 0.5F*(line.getWidth()-text.getWidth("Выбрано")), line.getY() + 1.0F*text.getHeight("A"));	
		}else if(page == 4){
			time.getSprite().draw(game.batch);
			if(controller.isClicked(time.getX(), time.getY(), time.getWidth(), time.getHeight(), true, true) && INF.planetLevel >= 35){
				time.getSprite().setColor(Color.WHITE);
				if(!time.isActiveMode()) time.setActiveMode(true);
				else time.setActiveMode(false);
			}else if(INF.planetLevel < 35){
				time.getSprite().setColor(Color.GRAY);
				lock.setBounds(time.getX() + 0.2F*time.getWidth(), time.getY() + 0.2F*time.getHeight(), 0.6F*time.getWidth(), 0.6F*time.getHeight());
				lock.draw(game.batch);
				if(!INF.lngRussian){
					text.draw(game.batch, "Will be available after level 35", 0.1F*width, 0.825F*height);
				}else{
					text.draw(game.batch, "Будет доступно после уровня 35", 0.1F*width, 0.825F*height);
				}
			}
			if(time.isActiveMode()){
				if(!INF.lngRussian){
					coinIcon1.setX(0.1F*width + text.getWidth("Price: 150 "));
					coinIcon1.setY(0.825F*height - 3.5F*text.getHeight("A") - 0.5F*coinIcon1.getHeight());
				}else{
					coinIcon1.setX(0.1F*width + text.getWidth("Цена: 150 "));
					coinIcon1.setY(0.825F*height - 3.5F*text.getHeight("A") - 0.5F*coinIcon1.getHeight());
				}
				coinIcon1.draw(game.batch);
				if(!INF.lngRussian){
					text.draw(game.batch, "Name: Time", 0.1F*width, 0.825F*height);
					text.draw(game.batch, "Adds 5 seconds", 0.1F*width, 0.825F*height - 1.5F*text.getHeight("A"));
					text.draw(game.batch, "Price: 150", 0.1F*width, 0.825F*height - 3.0F*text.getHeight("A"));
				}else{
					text.draw(game.batch, "Название: Время", 0.1F*width, 0.825F*height);
					text.draw(game.batch, "Добавляет 5 секунд", 0.1F*width, 0.825F*height - 1.5F*text.getHeight("A"));
					text.draw(game.batch, "Цена: 150", 0.1F*width, 0.825F*height - 3.0F*text.getHeight("A"));
				}
			}
			if(INF.isTimeActive)
				if(!INF.lngRussian)
					text.draw(game.batch, "Selected", time.getX() + 0.5F*(time.getWidth()-text.getWidth("Selected")), time.getY() + 1.0F*text.getHeight("A"));
				else
					text.draw(game.batch, "Выбрано", time.getX() + 0.5F*(time.getWidth()-text.getWidth("Выбрано")), time.getY() + 1.0F*text.getHeight("A"));	
		}
	}
	
	private void buttonListener(){
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, true)){
			game.setScreen(new GameScreen(game));
			this.dispose();
		}
		if(controller.isClicked(next.getX(), next.getY(), next.getWidth(), next.getHeight(), page < 4, true)){
			bomb.setActiveMode(false);
			coloumn.setActiveMode(false);
			line.setActiveMode(false);
			if(page < 3)
				time.setActiveMode(false);
			page++;
		}
		if(controller.isClicked(prev.getX(), prev.getY(), prev.getWidth(), prev.getHeight(), page > 1, true)){
			if(page > 1)
				bomb.setActiveMode(false);
			coloumn.setActiveMode(false);
			line.setActiveMode(false);
			time.setActiveMode(false);
			page--;
		}
		if(bomb.isActiveMode() && !INF.isBombActive && !INF.isColoumnActive && !INF.isLineActive && !INF.isTimeActive){
			if(INF.money >= 30){
				if(controller.isClicked(buy.getX(), buy.getY(), buy.getWidth(), buy.getHeight(), true, true)){
					INF.isBombActive = true;
					INF.money -= 30;
				}
			}
		}
		if(coloumn.isActiveMode() && !INF.isBombActive && !INF.isColoumnActive && !INF.isLineActive && !INF.isTimeActive){
			if(INF.money >= 50){
				if(controller.isClicked(buy.getX(), buy.getY(), buy.getWidth(), buy.getHeight(), true, true)){
					INF.isColoumnActive = true;
					INF.money -= 50;
				}
			}
		}
		if(line.isActiveMode() && !INF.isBombActive && !INF.isColoumnActive && !INF.isLineActive && !INF.isTimeActive){
			if(INF.money >= 100){
				if(controller.isClicked(buy.getX(), buy.getY(), buy.getWidth(), buy.getHeight(), true, true)){
					INF.isLineActive = true;
					INF.money -= 100;
				}
			}
		}
		if(time.isActiveMode() && !INF.isBombActive && !INF.isColoumnActive && !INF.isLineActive && !INF.isTimeActive){
			if(INF.money >= 150){
				if(controller.isClicked(buy.getX(), buy.getY(), buy.getWidth(), buy.getHeight(), true, true)){
					INF.isTimeActive = true;
					INF.money -= 150;
				}
			}
		}
		if(!INF.lngRussian){
			if(bomb.isActiveMode() && INF.money<30){
				if(controller.isClicked(bomb.getX(), bomb.getY(), bomb.getWidth(), bomb.getHeight(), true, true)){
					aoi.makeToast("Not enough coins");
				}
			}else if(coloumn.isActiveMode() && INF.money<50){
				if(controller.isClicked(coloumn.getX(), coloumn.getY(), coloumn.getWidth(), coloumn.getHeight(), true, true)){
					aoi.makeToast("Not enough coins");
				}
			}else if(line.isActiveMode() && INF.money<100){
				if(controller.isClicked(line.getX(), line.getY(), line.getWidth(), line.getHeight(), true, true)){
					aoi.makeToast("Not enough coins");
				}
			}else if(time.isActiveMode() && INF.money<150){
				if(controller.isClicked(time.getX(), time.getY(), time.getWidth(), time.getHeight(), true, true)){
					aoi.makeToast("Not enough coins");
				}
			}
		}else{
			if(bomb.isActiveMode() && INF.money<30){
				if(controller.isClicked(bomb.getX(), bomb.getY(), bomb.getWidth(), bomb.getHeight(), true, true)){
					aoi.makeToast("Недостаточно монет");
				}
			}else if(coloumn.isActiveMode() && INF.money<50){
				if(controller.isClicked(coloumn.getX(), coloumn.getY(), coloumn.getWidth(), coloumn.getHeight(), true, true)){
					aoi.makeToast("Недостаточно монет");
				}
			}else if(line.isActiveMode() && INF.money<100){
				if(controller.isClicked(line.getX(), line.getY(), line.getWidth(), line.getHeight(), true, true)){
					aoi.makeToast("Недостаточно монет");
				}
			}else if(time.isActiveMode() && INF.money<150){
				if(controller.isClicked(time.getX(), time.getY(), time.getWidth(), time.getHeight(), true, true)){
					aoi.makeToast("Недостаточно монет");
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
		if(page > 4) page = 4;
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
		game.dispose();
		text.dispose();
		data.saveSF();
	}

}