package ru.erked.spaceflight.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import ru.erked.spaceflight.AndroidOnlyInterface;
import ru.erked.spaceflight.Data;
import ru.erked.spaceflight.StartSFlight;

public class DesktopLauncher implements AndroidOnlyInterface, Data{

	private static DesktopLauncher deskLauncher;

	public static void main (String[] arg) {
		deskLauncher = new DesktopLauncher();

//		TexturePacker.Settings s = new TexturePacker.Settings();
//		s.maxWidth = 2048;
//		s.maxHeight = 2048;
//		TexturePacker.process(s, "D:/SPACE/Backups/other/assets/textures", "D:/SPACE/SFProject/android/assets/textureAtlas", "textures");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new StartSFlight(deskLauncher, deskLauncher, new AdMobImpl(), new GPGSImpl()), config);
		config.width = 960;
		config.height = 600;

	}

	enum SCREEN_CFG {
		GALAXY_TAB2( 1024, 554 ),// not 600
		SQUARE( 1024, 768 ),
		FULL_HD( 1920, 1080 ),
		SMALL( 320, 240 ),
		SCREEN_SHOT( 1024, 500 ),
		DEFAULT( 800, 480 );

		final boolean LANDSCAPE         = true;
		final boolean PORTRAIT          = false;
		final boolean screenOrientation = LANDSCAPE;

		private final int width;
		private final int height;

		private SCREEN_CFG( final int width, final int height ) {
			this.width = width;
			this.height = height;
		}

		public int getWidth() {
			return screenOrientation ? width : height;
		}

		public int getHeight() {
			return screenOrientation ? height : width;
		}
	}

	@Override
	public void makeToast(String text) {
		Gdx.app.log("[Desktop] ", text);
	}

	@Override
	public void makeNotify(String title, String text) {
		Gdx.app.log("[Desktop] ", title + " " + text);
	}

	@Override
	public void saveSF() {
		Gdx.app.log("[Data] ", "Saving...");
	}

	@Override
	public void loadSF() {
		Gdx.app.log("[Data] ", "Loading...");
	}
}
