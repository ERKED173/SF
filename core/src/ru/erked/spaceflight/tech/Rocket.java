package ru.erked.spaceflight.tech;

public class Rocket {

	private String nameUS;
	private String nameRU;
	private int timeBonus;
	private int cost;
	
	public Rocket(String nameUS, String nameRU, int timeBonus, int cost){
		this.setNameUS(nameUS);
		this.setNameRU(nameRU);
		this.setTimeBonus(timeBonus);
		this.setCost(cost);
	}

	public String getNameUS() {
		return nameUS;
	}

	public void setNameUS(String nameUS) {
		this.nameUS = nameUS;
	}

	public String getNameRU() {
		return nameRU;
	}

	public void setNameRU(String nameRU) {
		this.nameRU = nameRU;
	}
	
	public int getTimeBonus() {
		return timeBonus;
	}

	public void setTimeBonus(int timeBonus) {
		this.timeBonus = timeBonus;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
}
