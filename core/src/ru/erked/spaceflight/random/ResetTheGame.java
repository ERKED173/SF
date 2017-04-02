package ru.erked.spaceflight.random;

public class ResetTheGame {

	public static void reset(){
		INF.elapsedTime = 0;
		INF.launch = 0;
		INF.money = 15;
		INF.fuel = 15;
		INF.metal = 15;
		INF.moneyFull = 25;
		INF.fuelFull = 25;
		INF.metalFull = 25;
		INF.moneyLevel = 1;
		INF.fuelLevel = 1;
		INF.metalLevel = 1;
		INF.moneyAll = 0;
		INF.fuelAll = 0;
		INF.metalAll = 0;
		INF.currentRocket = "null";
		INF.currentPlanet = "null";
		INF.planetLevel = 0;
		INF.facts = 1;
		INF.login = "";
		INF.password = "";
		INF.rememberLogPass = false;
		INF.isBombActive = false;
		INF.isColoumnActive = false;
		INF.isLineActive = false;
		INF.isTimeActive = false;
	}
	
}
