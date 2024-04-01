package hu.serwenyi.essentials.commands;

import com.mojang.brigadier.CommandDispatcher;
import hu.serwenyi.essentials.utils.ServerUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class TphereCommand {

    public TphereCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tphere")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.player())
                .executes(context -> tphere(context.getSource().getPlayerOrException(), EntityArgument.getPlayer(context, "target")))));
    }

    public static int tphere(ServerPlayer player, ServerPlayer target) {
        if(ServerUtils.getPlayerCount(Objects.requireNonNull(player.getServer())) <= 1) {
            player.sendSystemMessage(Component.literal("Nincs fent elég játékos!"));
            return 1;
        }
        target.teleportTo(player.getX(), player.getY(), target.getZ());
        player.sendSystemMessage(Component.literal("Sikeresen magadhoz teleportáltad x".replace("x", target.getName().toString())));
        target.sendSystemMessage(Component.literal("{} elteleportált téged hozzá!".replace("{}", player.getName().toString())));
        return 0;
    }
}
