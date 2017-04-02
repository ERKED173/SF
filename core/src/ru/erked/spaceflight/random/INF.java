package ru.erked.spaceflight.random;

import ru.erked.spaceflight.tech.Planet;
import ru.erked.spaceflight.tech.Rocket;

public class INF {

    public static boolean isFirstLogin;
    public static long elapsedTime;
    public static int launch;
    public static int money;
    public static int fuel;
    public static int metal;
    public static int moneyFull;
    public static int fuelFull;
    public static int metalFull;
    public static int moneyLevel;
    public static int fuelLevel;
    public static int metalLevel;
    public static int moneyAll;
    public static int fuelAll;
    public static int metalAll;

    public static String login;
    public static String password;
    public static boolean rememberLogPass;
    public static boolean isRegistered;

    public static String currentRocket = "null";
    public static String currentPlanet = "null";

    public static boolean isBombActive;
    public static boolean isColoumnActive;
    public static boolean isLineActive;
    public static boolean isTimeActive;

    public static Rocket rocketBall = new Rocket("Circle-1", "Круг-1", 0, 20);
    public static Rocket rocketCircle = new Rocket("Sphere-M", "Сфера-М", 5, 40);
    public static Rocket rocketBasic = new Rocket("Basis-S", "Базис-С", 10, 60);
    public static Rocket rocketKinetic = new Rocket("Kinetic-UF", "Кинетик-УФ", 15, 80);
    public static Rocket rocketDelta = new Rocket("Delta-R", "Дельта-Р", 20, 100);
    public static Rocket rocketInfinity = new Rocket("Infinity-SN", "Инфинити-СН", 25, 120);

    public static Planet planetLoon = new Planet("Loon","Мун",0,25,false);
    public static Planet planetEmion = new Planet("Emion","Эмион",10,50,false);
    public static Planet planetDerten = new Planet("Derten","Дертен",20,75,false);
    public static Planet planetUnar = new Planet("Unar","Унар",30,125,false);
    public static Planet planetIngmar = new Planet("Ingmar","Ингмар",40,150,false);

    public static int loonRecord;
    public static int emionRecord;
    public static int dertenRecord;
    public static int unarRecord;
    public static int ingmarRecord;

    public static int planetLevel;
    public static int facts;

    public static boolean lngRussian = false;
}