package com.readvisuals.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "readvisuals.json");

    public TimerType timerType = TimerType.PLAY_TIME;
    public ColorMode colorMode = ColorMode.WHITE;
    public int customColor1 = 0xFF0000;
    public int customColor2 = 0x0000FF;
    public FrameType frameType = FrameType.ROUNDED;
    public int frameColor = 0xFFFFFF;
    public int textColor = 0xFFFFFF;
    public int hudX = 10;
    public int hudY = 10;
    public float scale = 1.0f;
    public int countdownTime = 3600;

    public enum TimerType {
        PLAY_TIME("Статус (время игры)"),
        COUNTDOWN("Обратный отсчет");

        private final String displayName;

        TimerType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum ColorMode {
        WHITE("Белый"),
        RED("Красный"),
        PURPLE("Фиолетовый"),
        CUSTOM_RGB("RGB (своя настройка)");

        private final String displayName;

        ColorMode(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum FrameType {
        ROUNDED("Закругленная рамка"),
        SQUARE("Прямоугольник");

        private final String displayName;

        FrameType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public static ModConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ModConfig();
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
