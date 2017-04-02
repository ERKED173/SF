package ru.erked.spaceflight.tech;

public class Planet {

	private String nameUS;
	private String nameRU;
	private int level;
	private int fuelTo;
	private boolean isSelected;
	
	public Planet(String nameUS, String nameRU, int level, int fuelTo, boolean isSelected){
		this.setNameUS(nameUS);
		this.setNameRU(nameRU);
		this.setLevel(level);
		this.setFuelTo(fuelTo);
		this.setSelected(isSelected);
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getFuelTo() {
		return fuelTo;
	}

	public void setFuelTo(int fuelTo) {
		this.fuelTo = fuelTo;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isAvailable() {
		if(ru.erked.spaceflight.random.INF.planetLevel >= this.getLevel()){
			return true;
		}else{
			return false;
		}
	}
	
}
