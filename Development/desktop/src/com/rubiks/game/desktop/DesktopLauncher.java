package com.rubiks.game.desktop;

import Engine.System.Platforms.Desktop.Desktop;
import Engine.System.Platforms.PlatformManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.util.Arrays;

public class DesktopLauncher {
	public static void main (String[] arg) {

		if (Arrays.asList(arg).contains("ios")){
			PlatformManager.setPlatform(PlatformManager.IOS);
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Rubik's Room";
		config.samples = 4;
		config.resizable = false;
		config.vSyncEnabled = true;
		new LwjglApplication(new Desktop(), config);
	}
}
