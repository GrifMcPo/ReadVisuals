package com.readvisuals;

import com.readvisuals.config.ModConfig;
import com.readvisuals.hud.TimerHUD;
import com.readvisuals.keybind.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadVisuals implements ClientModInitializer {
    public static final String MOD_ID = "readvisuals";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig CONFIG;
    private static TimerHUD timerHUD;
    private static long sessionStartTime;
    private static boolean isPaused = false;
    private static long pauseTime = 0;

    @Override
    public void onInitializeClient() {
        LOGGER.info("ReadVisuals инициализирован!");
        
        CONFIG = ModConfig.load();
        timerHUD = new TimerHUD();
        sessionStartTime = System.currentTimeMillis();
        
        KeyBindings.register();
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (KeyBindings.OPEN_MENU.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(com.readvisuals.config.ConfigScreen.createScreen(null));
                }
            }
        });
    }

    public static long getSessionStartTime() {
        return sessionStartTime;
    }

    public static boolean isPaused() {
        return isPaused;
    }

    public static void setPaused(boolean paused) {
        if (paused && !isPaused) {
            pauseTime = System.currentTimeMillis();
        } else if (!paused && isPaused) {
            sessionStartTime += System.currentTimeMillis() - pauseTime;
        }
        isPaused = paused;
    }

    public static long getPauseTime() {
        return pauseTime;
    }
}
