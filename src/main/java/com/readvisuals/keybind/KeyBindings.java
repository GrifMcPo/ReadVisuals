package com.readvisuals.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static KeyBinding OPEN_MENU;

    public static void register() {
        OPEN_MENU = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.readvisuals.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.readvisuals"
        ));
    }
}
