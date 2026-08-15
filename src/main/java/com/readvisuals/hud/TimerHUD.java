package com.readvisuals.hud;

import com.readvisuals.ReadVisuals;
import com.readvisuals.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.awt.*;

public class TimerHUD {
    private boolean dragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen != null && !(client.currentScreen instanceof com.readvisuals.config.ConfigScreen)) {
            return;
        }

        ModConfig config = ReadVisuals.CONFIG;
        if (config == null) return;

        String timeText = getTimeText();
        if (timeText == null) return;

        int x = config.hudX;
        int y = config.hudY;
        float scale = config.scale;

        // Рисуем рамку
        drawFrame(context, x, y, timeText, config);

        // Рисуем текст
        drawText(context, x, y, timeText, config);
    }

    private String getTimeText() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - ReadVisuals.getSessionStartTime();

        if (ReadVisuals.isPaused()) {
            elapsed -= (currentTime - ReadVisuals.getPauseTime());
        }

        ModConfig config = ReadVisuals.CONFIG;

        if (config.timerType == ModConfig.TimerType.COUNTDOWN) {
            long remaining = Math.max(0, config.countdownTime * 1000L - elapsed);
            long seconds = remaining / 1000;
            long minutes = seconds / 60;
            seconds %= 60;
            long hours = minutes / 60;
            minutes %= 60;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            long seconds = elapsed / 1000;
            long minutes = seconds / 60;
            seconds %= 60;
            long hours = minutes / 60;
            minutes %= 60;
            long days = hours / 24;
            hours %= 24;
            if (days > 0) {
                return String.format("%dд %02d:%02d:%02d", days, hours, minutes, seconds);
            }
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    private void drawFrame(DrawContext context, int x, int y, String text, ModConfig config) {
        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(text);
        int padding = 8;
        int width = textWidth + padding * 2;
        int height = 24;

        int color = config.frameColor;

        if (config.frameType == ModConfig.FrameType.ROUNDED) {
            drawRoundedRect(context, x - padding, y - 2, width, height, 8, color);
        } else {
            drawRect(context, x - padding, y - 2, width, height, color);
        }
    }

    private void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        // Простая реализация закругленного прямоугольника через рисование линий
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int a = (color >> 24) & 0xFF;

        // Основной прямоугольник
        context.fill(x + radius, y, x + width - radius, y + height, color);
        context.fill(x, y + radius, x + radius, y + height - radius, color);
        context.fill(x + width - radius, y + radius, x + width, y + height - radius, color);

        // Скругления (квадраты вместо кругов для простоты)
        context.fill(x, y, x + radius, y + radius, color);
        context.fill(x + width - radius, y, x + width, y + radius, color);
        context.fill(x, y + height - radius, x + radius, y + height, color);
        context.fill(x + width - radius, y + height - radius, x + width, y + height, color);
    }

    private void drawRect(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    private void drawText(DrawContext context, int x, int y, String text, ModConfig config) {
        int color = getTextColor();
        context.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, color, true);
    }

    private int getTextColor() {
        ModConfig config = ReadVisuals.CONFIG;
        switch (config.colorMode) {
            case WHITE:
                return 0xFFFFFF;
            case RED:
                return 0xFF0000;
            case PURPLE:
                return 0x800080;
            case CUSTOM_RGB:
                // Анимация между двумя цветами
                float progress = (System.currentTimeMillis() % 3000) / 3000f;
                float sinProgress = (float) (0.5f + 0.5f * Math.sin(progress * 2 * Math.PI));
                int r1 = (config.customColor1 >> 16) & 0xFF;
                int g1 = (config.customColor1 >> 8) & 0xFF;
                int b1 = config.customColor1 & 0xFF;
                int r2 = (config.customColor2 >> 16) & 0xFF;
                int g2 = (config.customColor2 >> 8) & 0xFF;
                int b2 = config.customColor2 & 0xFF;
                int r = (int) (r1 + (r2 - r1) * sinProgress);
                int g = (int) (g1 + (g2 - g1) * sinProgress);
                int b = (int) (b1 + (b2 - b1) * sinProgress);
                return (r << 16) | (g << 8) | b;
            default:
                return 0xFFFFFF;
        }
    }

    public void handleMouseClick(double mouseX, double mouseY) {
        ModConfig config = ReadVisuals.CONFIG;
        String text = getTimeText();
        if (text == null) return;

        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(text);
        int padding = 8;
        int x = config.hudX - padding;
        int y = config.hudY - 2;
        int width = textWidth + padding * 2;
        int height = 24;

        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            dragging = true;
            dragOffsetX = (int) (mouseX - config.hudX);
            dragOffsetY = (int) (mouseY - config.hudY);
        }
    }

    public void handleMouseRelease() {
        dragging = false;
    }

    public void handleMouseDrag(double mouseX, double mouseY) {
        if (dragging) {
            ModConfig config = ReadVisuals.CONFIG;
            config.hudX = (int) (mouseX - dragOffsetX);
            config.hudY = (int) (mouseY - dragOffsetY);
            config.save();
        }
    }
}
