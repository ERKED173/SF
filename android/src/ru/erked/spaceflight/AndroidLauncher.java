package ru.erked.spaceflight;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.MobileAds;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ru.erked.spaceflight.controllers.SFIn;
import ru.erked.spaceflight.menu.MainMenu;
import ru.erked.spaceflight.random.INF;

public class AndroidLauncher extends AndroidApplication implements AndroidOnlyInterface, Data {

	SharedPreferences stats;
	public static final String ELAPSED_TIME = "elapsed_time";
	public static final String LAUNCH = "launch";

	public static final String MONEY = "money";
	public static final String FUEL = "fuel";
	public static final String METAL = "metal";
	public static final String MONEY_FULL = "money_full";
	public static final String FUEL_FULL = "fuel_full";
	public static final String METAL_FULL = "metal_full";
	public static final String MONEY_LEVEL = "money_level";
	public static final String FUEL_LEVEL = "fuel_level";
	public static final String METAL_LEVEL = "metal_level";
	public static final String PLANET_LEVEL = "planet_level";
	public static final String FACTS = "facts";
	public static final String MONEY_ALL = "planet_all";
	public static final String FUEL_ALL = "fuel_all";
	public static final String METAL_ALL = "metal_all";

	public static final String CURRENT_ROCKET = "current_rocket";
	public static final String CURRENT_PLANET = "current_planet";

	public static final String BOMB = "bomb";
	public static final String COLOUMN = "coloumn";
	public static final String LINE = "line";
	public static final String TIME = "time";

	public static final String LOON_RECORD = "loon_record";
	public static final String EMION_RECORD = "emion_record";
	public static final String DERTEN_RECORD = "derten_record";
	public static final String UNAR_RECORD = "unar_record";

	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	public static final String REMEMBER = "remember";

	public static final String IS_FIRST_LOGIN = "is_first_login";

	final ru.erked.spaceflight.StartSFlight game;
	final AdMobImpl adMob;
	final GPGSImpl gpgs;
	final AndroidLauncher context = this;

	public AndroidLauncher(){
		adMob = new AdMobImpl("ca-app-pub-7260640348404144/3295291546");
		gpgs = new GPGSImpl();
		game = new StartSFlight(this, this, adMob, gpgs);
	}

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );

		adMob.init( context );
		gpgs.init( context );

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock = true;
		config.useAccelerometer = false;
		config.useCompass = false;

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		View gameView = initializeForView( game, config );

		RelativeLayout layout = new RelativeLayout( this );
		layout.addView( gameView );
		layout.addView( adMob.adView, adMob.adParams );

		setContentView( layout );

		MobileAds.initialize(getApplicationContext(), "ca-app-pub-7260640348404144~2097759949");

		load();
	}

	public void save(){
		stats = getPreferences(MODE_PRIVATE);
		Editor ed = stats.edit();

		ed.putLong(ELAPSED_TIME, INF.elapsedTime);
		ed.putInt(LAUNCH, INF.launch);

		ed.putInt(MONEY, INF.money);
		ed.putInt(FUEL, INF.fuel);
		ed.putInt(METAL, INF.metal);
		ed.putInt(MONEY_FULL, INF.moneyFull);
		ed.putInt(FUEL_FULL, INF.fuelFull);
		ed.putInt(METAL_FULL, INF.metalFull);
		ed.putInt(MONEY_LEVEL, INF.moneyLevel);
		ed.putInt(FUEL_LEVEL, INF.fuelLevel);
		ed.putInt(METAL_LEVEL, INF.metalLevel);
		ed.putInt(PLANET_LEVEL, INF.planetLevel);
		ed.putInt(FACTS, INF.facts);
		ed.putInt(MONEY_ALL, INF.moneyAll);
		ed.putInt(FUEL_ALL, INF.fuelAll);
		ed.putInt(METAL_ALL, INF.metalAll);

		if(INF.rememberLogPass){
			ed.putString(LOGIN, INF.login);
			ed.putString(PASSWORD, INF.password);
		}else{
			INF.login = "";
			INF.password = "";
		}
		ed.putBoolean(REMEMBER, INF.rememberLogPass);

		ed.putBoolean(BOMB, INF.isBombActive);
		ed.putBoolean(COLOUMN, INF.isColoumnActive);
		ed.putBoolean(LINE, INF.isLineActive);
		ed.putBoolean(TIME, INF.isTimeActive);

		ed.putInt(LOON_RECORD, INF.loonRecord);
		ed.putInt(EMION_RECORD, INF.emionRecord);
		ed.putInt(DERTEN_RECORD, INF.dertenRecord);
		ed.putInt(UNAR_RECORD, INF.unarRecord);

		ed.putString(CURRENT_ROCKET, INF.currentRocket);
		ed.putString(CURRENT_PLANET, INF.currentPlanet);

		ed.putBoolean(IS_FIRST_LOGIN, INF.isFirstLogin);

		ed.apply();
	}

	public void load(){
		stats = getPreferences(MODE_PRIVATE);

		INF.elapsedTime = stats.getLong(ELAPSED_TIME, 0);
		INF.launch = stats.getInt(LAUNCH, 0);
		INF.money = stats.getInt(MONEY, 15);
		INF.fuel = stats.getInt(FUEL, 15);
		INF.metal = stats.getInt(METAL, 15);
		INF.moneyFull = stats.getInt(MONEY_FULL, 25);
		INF.fuelFull = stats.getInt(FUEL_FULL, 25);
		INF.metalFull = stats.getInt(METAL_FULL, 25);
		INF.moneyLevel = stats.getInt(MONEY_LEVEL, 1);
		INF.fuelLevel = stats.getInt(FUEL_LEVEL, 1);
		INF.metalLevel = stats.getInt(METAL_LEVEL, 1);
		INF.planetLevel = stats.getInt(PLANET_LEVEL, 0);
		INF.facts = stats.getInt(FACTS, 1);
		INF.moneyAll = stats.getInt(MONEY_ALL, 0);
		INF.fuelAll = stats.getInt(FUEL_ALL, 0);
		INF.metalAll = stats.getInt(METAL_ALL, 0);

		INF.login = stats.getString(LOGIN, "");
		INF.password = stats.getString(PASSWORD, "");
		INF.rememberLogPass = stats.getBoolean(REMEMBER, false);

		INF.isBombActive = stats.getBoolean(BOMB, false);
		INF.isColoumnActive = stats.getBoolean(COLOUMN, false);
		INF.isLineActive = stats.getBoolean(LINE, false);
		INF.isTimeActive = stats.getBoolean(TIME, false);

		INF.loonRecord = stats.getInt(LOON_RECORD, 0);
		INF.emionRecord = stats.getInt(EMION_RECORD, 0);
		INF.dertenRecord = stats.getInt(DERTEN_RECORD, 0);
		INF.unarRecord = stats.getInt(UNAR_RECORD, 0);

		INF.currentRocket = stats.getString(CURRENT_ROCKET, "null");
		INF.currentPlanet = stats.getString(CURRENT_PLANET, "null");

		INF.isFirstLogin = stats.getBoolean(IS_FIRST_LOGIN, false);

	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		SFIn.clickSound.dispose();
		MainMenu.music.dispose();
		save();
	}

	@Override
	protected void onPause(){
		super.onPause();
		MainMenu.music.pause();
		SFIn.clickSound.pause();
		save();
	}

	@Override
	protected void onResume(){
		super.onResume();
		load();
	}

	@Override
	public void onStart() {
		super.onStart();
		gpgs.connect();
	}

	@Override
	public void onStop() {
		super.onStop();
		gpgs.disconnect();
	}

	@Override
	public void onActivityResult( int request, int response, Intent intent ) {
		super.onActivityResult( request, response, intent );
		gpgs.onActivityResult( request, response, intent );
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
	}


	@Override
	public void makeToast(final String text) {
		handler.post(new Runnable(){
			@Override
			public void run() {
				Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
				toast.show();
			}
		});
	}


	@Override
	public void makeNotify(String title, String text) {
		/**
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.ic_launcher) // Можно заменить
						.setContentTitle(title)
						.setContentText(text);
		Intent resultIntent = new Intent(this, AndroidLauncher.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(AndroidLauncher.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(
						0,
						PendingIntent.FLAG_UPDATE_CURRENT
				);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
		 */
	}


	@Override
	public void saveSF() {
		save();

	}

	@Override
	public void loadSF() {
		load();

	}
}
