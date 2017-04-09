package ru.erked.spaceflight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;

public class TutorialScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final ru.erked.spaceflight.StartSFlight game;
	private ru.erked.spaceflight.controllers.SFIn controller;
	
	private Sprite backgroundSprite[];
	private int backIter = 0;
	
	private ru.erked.spaceflight.tech.SFButtonS back;
	private ru.erked.spaceflight.tech.SFButtonS menu;
	private Sprite image1;
	private Sprite image2;
	private Sprite image3;
	private Sprite image4;
	private Sprite image5;
	
	private ru.erked.spaceflight.tech.SFFont text;
	private String[] str1 = new String[]{};
	private String[] str2;
	private String[] str3;
	private String[] str4;
	private String[] str5;
	private int page = 1;
	private ru.erked.spaceflight.tech.SFButtonS next;
	private ru.erked.spaceflight.tech.SFButtonS prev;
	
	private Sprite blackAlpha = ru.erked.spaceflight.random.RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private ru.erked.spaceflight.Data data;
	
	public TutorialScreen(final ru.erked.spaceflight.StartSFlight game){
		this.game = game;
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
		
		image1 = ru.erked.spaceflight.random.RES.atlas.createSprite("schemeOfResources");
		image1.setBounds(0.1F*width, 0.15F*height, 0.7F*height, 0.7F*0.50515F*height);
		image3 = ru.erked.spaceflight.random.RES.atlas.createSprite("schemeOfGame2");
		image4 = ru.erked.spaceflight.random.RES.atlas.createSprite("schemeOfBase");
		image4.setBounds(0.1F*width, 0.15F*height, 0.6F*height, 0.6F*0.68571F*height);
		image5 = ru.erked.spaceflight.random.RES.atlas.createSprite("schemeFirst");
		image5.setBounds(0.1F*width, 0.035F*height, 0.35F*height, 0.35F*1.791962F*height);
		next = new ru.erked.spaceflight.tech.SFButtonS("buttonI", "buttonA", 0.1F*width, 0.815F*width, 0.2F*height, 1.5F, 2.0F, -1);
		next.getSprite().setColor(Color.LIME);
		prev = new ru.erked.spaceflight.tech.SFButtonS("buttonI", "buttonA", 0.1F*width, 0.7F*width, 0.2F*height, 1.5F, 2.0F, -1);
		prev.getSprite().setColor(Color.LIME);
		
		if(!INF.lngRussian){
			image2 = ru.erked.spaceflight.random.RES.atlas.createSprite("schemeOfGame1EN");
            image2.setBounds(0.015F*width, 0.0F*height, 1.1F*height, 1.1F*0.63362F*height);
            image3.setBounds(0.015F*width, 0.0F*height, 1.2F*height, 1.2F*0.68571F*height);
			str1 = new String[]{
					"Welcome! ","                                                                                                  ",
					"You ","are ","a director ","of the ","spaceport. ","                                                         ",
					"Your ","task ","is to ","launch ","rockets ","into ","the ","space ", "to ",
                    "extract ", "there ", "on ", "planets ", "gems. "
			};
			str2 = new String[]{
					"Rocket ","need ","some ","resources. ","                                                               ",
					"Earn it ","on ","the ","factories. ","                                                       ",
					"Tap ","on the ","screen ","and ","you'll ","get ","resources. ","                                             ",
					"You need ","metal ","to ","build ","rockets, ","fuel - ","to ","fill ","it ",
                    "and ","coins - ","to ","buy ","various ","bonuses."
			};
			str3 = new String[]{
					"On ","the ","planet ","you'll ","have ","to ","extract ","gems. ","                                       ",
					"Move ","gems ","on ","the ","area ","to ","make ","new ","chain ","of ","three ","or ",
					"more ","same ","color ","gems ","and ","then ","it ","will ","be ","extracted."
			};
			str4 = new String[]{
					"To ","complete ","the ","level ","you ","need ","to ","keep ","within ","the ",
                    "specified ","time ","and ","get ", "earn ","some ","score. ","You ","will ",
                    "get ","the reward ","for ","successful ", "flight, ","and also ","sometimes ",
                    "you ","will ","get ", "new ","information ","about ","space."
			};
			str5 = new String[]{
                    "Learn ","new ", "interesting ","information ","about ","space! ","Make ","a new ","records! ",
					"Become ","the ","best ","in a ","galaxy! ","Good luck, ","director! "
			};
		}else{
			image2 = ru.erked.spaceflight.random.RES.atlas.createSprite("schemeOfGame1RU");
            image2.setBounds(0.015F*width, 0.0F*height, 1.1F*height, 1.1F*0.63362F*height);
            image3.setBounds(0.015F*width, 0.0F*height, 1.2F*height, 1.2F*0.68571F*height);
			str1 = new String[]{
					"Приветствуем! ","                                                                                          ",
					"Вы ","- ","директор ","космического ","порта. ","                                                          ",
					"Ваша ","задача ","- ","отправлять ","в ","космос ","ракеты, ","чтобы ", "на ",
                    "других ", "планетах ", "добывать ", "драгоценные ", "камни. "
			};
			str2 = new String[]{
					"Для ","ракеты ","нужны ","ресурсы. ","Их ","можно ","добыть ","на ","фабриках.                                                                                               ",
					"Кликайте ","по ","экрану, ","чтобы ","накопить ","ресурсы.                                                                                                                   ",
					"Металл ","нужен ","для ","постройки ","ракеты, ","топливо - ","для ","её ","заправки, "
					,"монеты - ", "для ","приобретения ","различных ","бонусов."
			};
			str3 = new String[]{
					"На ","планете ","Вас ","ждёт ","добыча ","драгоценных ","камней. ",
					"Двигайте ","камни ","по ","полю, ","составляйте ","цепочки ","по ","три ",
					"и ","больше ","камней ","одинакового ","цвета, ","и после ","чего ","они ","будут ","добыты."
			};
			str4 = new String[]{
					"Чтобы ","пройти ","уровень ","нужно ","уложиться ","в ","заданное ","время ",
                    "и ", "добыть ","определённое ","количество ","очков. ",
					"Вы ","получите ","награду, ","каждые ","5 уровней ","будут ",
                    "открываться ","бонусы ","или ","новая ","информация ","о ","космосе."
			};
			str5 = new String[]{
                    "Узнавайте ","новую ", "познавательную ","информацию ","о ","космосе! ","Ставьте ","новые ","рекорды!",
					"Станьте ","лучшим ","в ","этой ","галактике! ","Удачи, ","директор! "
			};
		}
		
		
		SFButtonsInit();
		
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

		drawBackButton();
		switch(page){
		case 1:{
			image5.draw(game.batch);
			drawText(str1);
			break;
		}case 2:{
			image1.draw(game.batch);
			drawText(str2);
			break;
		}case 3:{
			image3.draw(game.batch);
			drawText(str3);
			break;
		}case 4:{
			image2.draw(game.batch);
			drawText(str4);
			break;
		}case 5:{
			image4.draw(game.batch);
			drawText(str5);
			break;
		}
		}
		
		if(page == 1) prev.getSprite().setColor(Color.FOREST);
		else prev.getSprite().setColor(Color.LIME);
		if(page == 5) next.getSprite().setColor(Color.FOREST);
		else next.getSprite().setColor(Color.LIME);
		if(controller.isOn(next.getX(), next.getY(), next.getWidth(), next.getHeight(), true) && page != 5){
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
		
		if(!INF.lngRussian){
			text.draw(game.batch, "Training system", 0.5F*width - 0.5F*text.getWidth("Training system"), 0.965F*height);
			text.draw(game.batch, "Page: " + page, 0.765F*width, 0.175F*height);
		}else{
			text.draw(game.batch, "Система обучения",  0.5F*width - 0.5F*text.getWidth("Система обучения"), 0.965F*height);
			text.draw(game.batch, "Страница: " + page, 0.72F*width, 0.175F*height);
		}
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		buttonListener();
		
	}
	
	private void SFButtonsInit(){
		if(!INF.lngRussian){
			back = new ru.erked.spaceflight.tech.SFButtonS("continueI", "continueA", 0.365F*width, width - 0.38F*width, 0.015F*height, 5.97826F, 1.0F, -1);
			menu = new ru.erked.spaceflight.tech.SFButtonS("backI", "backA", 0.225F*width, 0.01F*width, 0.015F*height, 2.98913F, 1.0F, -1);
		}else{
			back = new ru.erked.spaceflight.tech.SFButtonS("continueRI", "continueRA", 0.365F*width, width - 0.38F*width, 0.015F*height, 5.97826F, 1.0F, -1);
			menu = new ru.erked.spaceflight.tech.SFButtonS("backRI", "backRA", 0.2F*width, 0.01F*width, 0.01F*height, 2.98913F, 1.0F, -1);
		}
	}
	
	private void drawText(String[] str){
		float marginLeft = 0.0F;
		float marginTop = 0.0F;
		for(int i=0;i<str.length;i++){
			if(i!=0)
				if(marginLeft + text.getWidth(str[i-1]) < 0.6F*width){
					marginLeft += text.getWidth(str[i-1]);
				}else{
					marginLeft = 0.0F;
					marginTop += 1.5F*text.getHeight("A");
				}
			text.draw(game.batch, str[i], 0.085F*width + marginLeft, 0.875F*height - marginTop);
		}
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
        if(controller.isOn(menu.getX(), menu.getY(), menu.getWidth(), menu.getHeight(), true)){
            menu.setActiveMode(true);
        }else{
            menu.setActiveMode(false);
        }
		if(page == 5) back.getSprite().draw(game.batch);
		menu.getSprite().draw(game.batch);
	}
	
	private void buttonListener(){
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, true) && page == 5){
			if(!INF.isFirstLogin){
				INF.isFirstLogin = true;
				game.setScreen(new GameScreen(game));
				this.dispose();
			}else{
				game.setScreen(new ru.erked.spaceflight.menu.MainMenu(game));
				this.dispose();
			}
		}
		if(controller.isClicked(next.getX(), next.getY(), next.getWidth(), next.getHeight(), page < 5, true)){
			page++;
		}
		if(controller.isClicked(prev.getX(), prev.getY(), prev.getWidth(), prev.getHeight(), page > 1, true)){
			page--;
		}
		if(controller.isClicked(menu.getX(), menu.getY(), menu.getWidth(), menu.getHeight(), true, true)){
			game.setScreen(new MainMenu(game));
			this.dispose();
		}
	}
	
	private void resourceCheck(){
		if(INF.money>INF.moneyFull) INF.money = INF.moneyFull;
		if(INF.fuel>INF.fuelFull) INF.fuel = INF.fuelFull;
		if(INF.metal>INF.metalFull) INF.metal = INF.metalFull;
		/***/
		if(page < 1) page = 1;
		if(page > 5) page = 5;
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