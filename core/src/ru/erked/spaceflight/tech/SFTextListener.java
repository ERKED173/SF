package ru.erked.spaceflight.tech;

import com.badlogic.gdx.Input.TextInputListener;

import ru.erked.spaceflight.random.INF;

public class SFTextListener implements TextInputListener {
	
	public static String inputLine;

	public static String login;
	public static String password;
	
	private String context = "";
	
	public SFTextListener(String context){
		this.context = context;
	}
	
	@Override
	public void input (String text) {
		///
		if(context.equals("Registration")){
			if(ru.erked.spaceflight.game.LoginScreen.isLoginActive){
				INF.login = text;
			}
			if(ru.erked.spaceflight.game.LoginScreen.isPasswordActive){
				INF.password = text;
			}
		}else if(context.equals("Login")){
			if(ru.erked.spaceflight.game.LoginScreen.isLoginActive){
				login = text;
			}
			if(ru.erked.spaceflight.game.LoginScreen.isPasswordActive){
				password = text;
			}
		}
		///
		inputLine = text;
	}
	
	@Override
	public void canceled () {
		inputLine = "";
	}
}