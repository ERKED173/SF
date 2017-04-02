package ru.erked.spaceflight.tech;

import ru.erked.spaceflight.random.INF;

public class CurPR {

	public static Planet getCurPlanet(){
		switch(INF.currentPlanet){
		case "planetLoon":{
			return INF.planetLoon;
		}case "planetEmion":{
			return INF.planetEmion;
		}case "planetDerten":{
			return INF.planetDerten;
		}case "planetUnar":{
			return INF.planetUnar;
		}case "planetIngmar":{
			return INF.planetIngmar;
		}default:{
			return null;
		}
		}
	}
	
	public static ru.erked.spaceflight.tech.Rocket getCurRocket(){
		switch(INF.currentRocket){
		case "rocketBall":{
			return INF.rocketBall;
		}case "rocketBasic":{
			return INF.rocketBasic;
		}case "rocketCircle":{
			return INF.rocketCircle;
		}case "rocketKinetic":{
			return INF.rocketKinetic;
		}case "rocketDelta":{
			return INF.rocketDelta;
		}case "rocketInfinity":{
			return INF.rocketInfinity;
		}default:{
			return null;
		}
		}
	}
	
}
