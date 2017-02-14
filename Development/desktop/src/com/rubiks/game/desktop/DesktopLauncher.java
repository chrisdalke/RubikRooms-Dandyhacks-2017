package com.rubiks.game.desktop;

import Engine.System.Platforms.Desktop.Desktop;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Rubik's Room";
		config.samples = 4;
		config.resizable = false;
		config.vSyncEnabled = true;
		new LwjglApplication(new Desktop(), config);
	}
}
