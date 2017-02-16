package com.rubiks.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tests.bullet.CharacterTest;

public class DesktopTestLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Rubik's Room";
		config.samples = 4;
		config.resizable = false;
		config.vSyncEnabled = true;
		new LwjglApplication(new CharacterTest(), config);
	}
}
