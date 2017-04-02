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
import ru.erked.spaceflight.tech.SFButtonS;
import ru.erked.spaceflight.tech.SFFont;
import ru.erked.spaceflight.tech.SFTextListener;

public class LoginScreen implements Screen{

	private static final float width = Gdx.graphics.getWidth();
	private static final float height = Gdx.graphics.getHeight();
	
	private final StartSFlight game;
	private SFIn controller;
	
	private Sprite[] backgroundSprite;
	private int backIter = 0;
	
	private SFButtonS back;
	private SFButtonS next;
	
	private SFTextListener textListenerA;
	private SFTextListener textListenerL;
	private Sprite loginBoard;
	private Sprite passwordBoard;
	public static boolean isLoginActive = false;
	public static boolean isPasswordActive = false;
	private SFButtonS remCB;
	
	private static SFFont text;
	private AndroidOnlyInterface aoi;
	
	private Sprite blackAlpha = RES.atlas.createSprite("black");
	private float alp = 1.0F;
	private boolean isTrans;
	
	private Data data;
	
	public LoginScreen(final StartSFlight game){
		this.game = game;
		aoi = game.aoi;
		data = game.data;
	}
	
	@Override
	public void show() {

		controller = new SFIn();
		textListenerA = new SFTextListener("Registration");
		textListenerL = new SFTextListener("Login");
		
		backgroundSprite = new Sprite[13];
		for(int i=0;i<13;i++){
			backgroundSprite[i] = RES.atlas.createSprite("resource", i+1);
			backgroundSprite[i].setBounds(0.0F, 0.0F, width, height);
		}
		
		loginBoard = RES.atlas.createSprite("login");
		loginBoard.setBounds(0.1F*width, 0.65F*height, 0.35F*width, 0.07F*width);
		loginBoard.setColor(245, 255, 255, 1);
		passwordBoard = RES.atlas.createSprite("login");
		passwordBoard.setBounds(0.55F*width, 0.65F*height, 0.35F*width, 0.07F*width);
		passwordBoard.setColor(245, 255, 255, 1);
		remCB = new SFButtonS("checkBoxI", "checkBoxA", 0.05F*width, loginBoard.getX(), loginBoard.getY() - 0.075F*width, 1.0F, 1.0F, -1);
		remCB.getSprite().setColor(245, 255, 255, 1);
		remCB.setActiveMode(true);
		remCB.getSprite().setColor(245, 255, 255, 1);
		remCB.setActiveMode(false);
		
		if(!INF.lngRussian){
			back = new SFButtonS("backI", "backA", 0.225F*width, 0.77F*width, 0.015F*height, 2.98913F, 1.0F, -1);
			next = new SFButtonS("continueI", "continueA", 0.365F*width, 0.66F*width, 0.15F*height, 5.97826F, 1.0F, -1);
		}else{
			back = new SFButtonS("backRI", "backRA", 0.2F*width, 0.77F*width, 0.015F*height, 2.98913F, 1.0F, -1);
			next = new SFButtonS("continueRI", "continueRA", 0.365F*width, 0.605F*width, 0.15F*height, 5.97826F, 1.0F, -1);
		}
		
		text = new SFFont(30, Color.WHITE);
		
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
		
		if(!INF.lngRussian){
			if(INF.isRegistered) text.draw(game.batch, "Login the game", 0.5F*width - 0.5F*text.getWidth("Login the game"), 0.965F*height);
			else text.draw(game.batch, "Registration", 0.5F*width - 0.5F*text.getWidth("Registration"), 0.965F*height);
			text.draw(game.batch, "Login:", loginBoard.getX(), loginBoard.getY() + 1.55F*loginBoard.getHeight());
			text.draw(game.batch, "Password:", passwordBoard.getX(), passwordBoard.getY() + 1.55F*passwordBoard.getHeight());
			text.draw(game.batch, "Remember login and password ?", remCB.getX() + 1.25F*remCB.getWidth(), remCB.getY() + 0.8F*remCB.getHeight());
		}else{
			if(INF.isRegistered) text.draw(game.batch, "Вход в игру", 0.5F*width - 0.5F*text.getWidth("Вход в игру"), 0.965F*height);
			else text.draw(game.batch, "Регистрация", 0.5F*width - 0.5F*text.getWidth("Регистрация"), 0.965F*height);
			text.draw(game.batch, "Логин:", loginBoard.getX(), loginBoard.getY() + 1.55F*loginBoard.getHeight());
			text.draw(game.batch, "Пароль:", passwordBoard.getX(), passwordBoard.getY() + 1.55F*passwordBoard.getHeight());
			text.draw(game.batch, "Запомнить логин и пароль ?", remCB.getX() + 1.25F*remCB.getWidth(), remCB.getY() + 0.75F*remCB.getHeight());
		}
		
		loginBoard.draw(game.batch);
		passwordBoard.draw(game.batch);
		text.draw(game.batch, INF.login, loginBoard.getX() + 0.05F*loginBoard.getWidth(), loginBoard.getY() + 0.65F*loginBoard.getHeight());
		String password;
		if(INF.password.equals("")) password = "";
		else password = "*******";
		text.draw(game.batch, password, passwordBoard.getX() + 0.05F*passwordBoard.getWidth(), passwordBoard.getY() + 0.65F*passwordBoard.getHeight());
		
		if(controller.isOn(next.getX(), next.getY(), next.getWidth(), next.getHeight(), true)){
			next.setActiveMode(true);
		}else{
			next.setActiveMode(false);
		}
		next.getSprite().draw(game.batch);
		
		if(controller.isOn(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true)){
			back.setActiveMode(true);
		}else{
			back.setActiveMode(false);
		}
		back.getSprite().draw(game.batch);
		
		if(INF.rememberLogPass){
			remCB.setActiveMode(true);
		}else{
			remCB.setActiveMode(false);
		}
		remCB.getSprite().draw(game.batch);
		
		blackAlpha.draw(game.batch);
		
		game.batch.end();
		
		buttonListener();
		
		
	}
	
	private boolean logPassCheck(boolean color){
		boolean check = true;
		if(INF.isRegistered){
			if(!(SFTextListener.login.equals(INF.login) && SFTextListener.password.equals(INF.password)))
				check = false;
		}else{
			if(!(INF.login.length() >= 5 && INF.login.length() <= 10)){
				check = false;
				if(color) loginBoard.setColor(Color.SCARLET);
			}else{
				for(int i=0;i<INF.login.length();i++){
					if(!(
							((int)INF.login.charAt(i) >= 97 && 
							(int)INF.login.charAt(i) <= 122) ||
							((int)INF.login.charAt(i) >= 48 &&
							(int)INF.login.charAt(i) <= 57) ||
							((int)INF.login.charAt(i) >= 65 &&
							(int)INF.login.charAt(i) <= 90)
							))
					{
						if(color) loginBoard.setColor(Color.SCARLET);
						check = false;
					}else{
						if(color) loginBoard.setColor(245, 255, 255, 1);
					}
				}
			}
			if(!(INF.password.length() >= 5 && INF.password.length() <= 10)){
				check = false;
				if(color) passwordBoard.setColor(Color.SCARLET);
			}else{
				for(int i=0;i<INF.password.length();i++){
					if(!(
							((int)INF.password.charAt(i) >= 97 && 
							(int)INF.password.charAt(i) <= 122) ||
							((int)INF.password.charAt(i) >= 48 &&
							(int)INF.password.charAt(i) <= 57) ||
							((int)INF.password.charAt(i) >= 65 &&
							(int)INF.password.charAt(i) <= 90)
							))
					{
						if(color) passwordBoard.setColor(Color.SCARLET);
						check = false;
					}else{
						if(color) passwordBoard.setColor(245, 255, 255, 1);
					}
				}
			}
		}
		return check;
	}
	
	private void buttonListener(){
		if(controller.isClicked(next.getX(), next.getY(), next.getWidth(), next.getHeight(), logPassCheck(false), true)){
			if(logPassCheck(true)){
				loginBoard.setColor(245, 255, 255, 1);
				passwordBoard.setColor(245, 255, 255, 1);
				/**if(!INF.isRegistered){
					try {
						String addNewUserName = "INSERT INTO users (user_name) values ('" + INF.login + "');";
			            Class.forName("com.mysql.jdbc.Driver");
			            System.out.println("Driver loading success!");
			            String url = "jdbc:mysql://localhost/sfdb";
			            String name = "root";
			            String password = "root";
			            try {
			                Connection con = DriverManager.getConnection(url, name, password);
			                System.out.println("Connected.");
			                Statement st = con.createStatement();
			                st.executeUpdate(addNewUserName);
			            } catch (SQLException e) {
			                e.printStackTrace();
			            }
			        } catch (ClassNotFoundException e) {
			            e.printStackTrace();
			        }
				}*/
				INF.isRegistered = true;
				if(INF.isFirstLogin)
					game.setScreen(new GameScreen(game));
				else
					game.setScreen(new ru.erked.spaceflight.game.TutorialScreen(game));
				this.dispose();
			}else{
				if(INF.isRegistered){
					if(!INF.lngRussian) aoi.makeToast("Incorrect login or password");
					else aoi.makeToast("Неверный логин или пароль");
				}else{
					if(!INF.lngRussian) aoi.makeToast("From 5 to 10 characters, a-z, A-Z, 0-9");
					else aoi.makeToast("От 5 до 10 символов, a-z, A-Z, 0-9");
				}
			}
		}
		if(controller.isClicked(back.getX(), back.getY(), back.getWidth(), back.getHeight(), true, true)){
			game.setScreen(new MainMenu(game));
			this.dispose();
		}
		if(controller.isClicked(loginBoard.getX(), loginBoard.getY(), loginBoard.getWidth(), loginBoard.getHeight(), true, true)){
			isLoginActive = true;
			isPasswordActive = false;
			if(INF.isRegistered){
				if(!INF.lngRussian)	Gdx.input.getTextInput(textListenerL, "Type your login", INF.login, "login");
				else Gdx.input.getTextInput(textListenerL, "Введите Ваш логин", INF.login, "логин");
			}else{
				if(!INF.lngRussian) Gdx.input.getTextInput(textListenerA, "Type login", INF.login, "login");
				else Gdx.input.getTextInput(textListenerA, "Введите логин", INF.login, "логин");
				
			}
		}
		if(controller.isClicked(passwordBoard.getX(), passwordBoard.getY(), passwordBoard.getWidth(), passwordBoard.getHeight(), true, true)){
			isLoginActive = false;
			isPasswordActive = true;
			if(INF.isRegistered){
				if(!INF.lngRussian) Gdx.input.getTextInput(textListenerL, "Type your password", INF.password, "password");
				else Gdx.input.getTextInput(textListenerL, "Введите Ваш пароль", INF.password, "пароль");
				
			}else{
				if(!INF.lngRussian) Gdx.input.getTextInput(textListenerA, "Type password", INF.password, "password");
				else Gdx.input.getTextInput(textListenerA, "Введите пароль", INF.password, "пароль");
				
			}
		}
		if(controller.isClicked(remCB.getX(), remCB.getY(), remCB.getWidth(), remCB.getHeight(), true, true)){
			if(INF.rememberLogPass) INF.rememberLogPass = false;
			else INF.rememberLogPass = true;
		}
	}
	
	private void drawBackground(){
		if(INF.elapsedTime % 15 == 0){
			if(INF.elapsedTime % 15 == 0){
				if(backIter<12) backIter++;
				else backIter=0;
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

	}
	@Override
	public void pause() {
		data.saveSF();
	}
	@Override
	public void resume() {
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