package com.readvisuals.config;

import com.readvisuals.ReadVisuals;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Text.literal("ReadVisuals Настройки"));
        this.parent = parent;
    }

    public static Screen createScreen(Screen parent) {
        return new ConfigScreen(parent);
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = 30;

        addDrawableChild(CyclingButtonWidget.<ModConfig.TimerType>builder(
                timerType -> Text.literal(timerType.getDisplayName()))
                .values(ModConfig.TimerType.values())
                .initially(ReadVisuals.CONFIG.timerType)
                .build(centerX - 100, y, 200, 20, Text.literal("Тип таймера"), (button, value) -> {
                    ReadVisuals.CONFIG.timerType = value;
                    ReadVisuals.CONFIG.save();
                }));
        y += 30;

        addDrawableChild(CyclingButtonWidget.<ModConfig.ColorMode>builder(
                colorMode -> Text.literal(colorMode.getDisplayName()))
                .values(ModConfig.ColorMode.values())
                .initially(ReadVisuals.CONFIG.colorMode)
                .build(centerX - 100, y, 200, 20, Text.literal("Цвет"), (button, value) -> {
                    ReadVisuals.CONFIG.colorMode = value;
                    ReadVisuals.CONFIG.save();
                }));
        y += 30;

        addDrawableChild(CyclingButtonWidget.<ModConfig.FrameType>builder(
                frameType -> Text.literal(frameType.getDisplayName()))
                .values(ModConfig.FrameType.values())
                .initially(ReadVisuals.CONFIG.frameType)
                .build(centerX - 100, y, 200, 20, Text.literal("Рамка"), (button, value) -> {
                    ReadVisuals.CONFIG.frameType = value;
                    ReadVisuals.CONFIG.save();
                }));
        y += 30;

        addDrawableChild(new SliderWidget(centerX - 100, y, 200, 20, Text.literal("Время: " + ReadVisuals.CONFIG.countdownTime + "с"), 0.0) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal("Время: " + ReadVisuals.CONFIG.countdownTime + "с"));
            }

            @Override
            protected void applyValue() {
                ReadVisuals.CONFIG.countdownTime = (int) (60 + this.value * 5940);
                ReadVisuals.CONFIG.save();
            }
        });
        y += 30;

        addDrawableChild(new SliderWidget(centerX - 100, y, 200, 20, Text.literal("Масштаб: " + String.format("%.1f", ReadVisuals.CONFIG.scale)), 0.0) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal("Масштаб: " + String.format("%.1f", ReadVisuals.CONFIG.scale)));
            }

            @Override
            protected void applyValue() {
                ReadVisuals.CONFIG.scale = 0.5f + (float) this.value * 1.5f;
                ReadVisuals.CONFIG.save();
            }
        });

        addDrawableChild(ButtonWidget.builder(Text.literal("Готово"), button -> {
                    if (this.client != null) {
                        this.client.setScreen(this.parent);
                    }
                })
                .dimensions(centerX - 50, this.height - 30, 100, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
