package hu.serwenyi.essentials.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class ServerUtils {

    public static boolean isSingleplayer() {
        return Minecraft.getInstance().isSingleplayer();
    }

    public static boolean isNetherEnabled(MinecraftServer server) {
        return server.isNetherEnabled();
    }

    public static int getPlayerCount(MinecraftServer server) {
        return server.getPlayerCount();
    }
}
