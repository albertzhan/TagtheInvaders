package com.albert.tron.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.albert.tron.Tron;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 640;
		config.width = 992;
		config.title = "Tag the Invaders";
		config.useHDPI = true;
		new LwjglApplication(new Tron(), config);
	}
}
