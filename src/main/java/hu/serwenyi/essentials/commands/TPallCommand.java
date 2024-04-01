package hu.serwenyi.essentials.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import hu.serwenyi.essentials.utils.ServerUtils;
import hu.serwenyi.essentials.utils.TeleportPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class TPallCommand {
    public TPallCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tpall")
             .requires(cs -> cs.hasPermission(2))
                .then(Commands.literal("from")
                        .then(Commands.argument("dimension", DimensionArgument.dimension()))
                            .executes(context -> TeleportFrom(context, DimensionArgument.getDimension(context,"dimension")))
                )
                .executes(this::TpAll)
        );
    }
    public int TeleportFrom(CommandContext<CommandSourceStack> context, ServerLevel dimension) {
        CommandSourceStack source = context.getSource();
        MinecraftServer server = source.getServer();
        ServerPlayer player = source.getPlayer();
        assert player != null;
        if(!EnoughPlayer(server, player)) return 1;
        for(ServerPlayer targets : server.getPlayerList().getPlayers()) {
            if(!targets.is(player) && targets.serverLevel().dimension() == dimension.dimension()) {
                BlockPos bp = targets.getOnPos();
                TeleportPos tp = new TeleportPos(dimension.dimension(), bp);
                tp.teleport(targets);
            }
        }
        return 0;
    }

    private int TpAll(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        MinecraftServer server = source.getServer();
        Player exPlayer = source.getPlayer();
        assert exPlayer != null;
        if(!EnoughPlayer(server, exPlayer)) return 1;
        exPlayer.sendSystemMessage(Component.literal("Összes játékos teleportálása hozzád..."));
        source.getServer().getPlayerList().getPlayers().forEach(player ->{
            player.teleportTo(exPlayer.getX(), exPlayer.getY(), exPlayer.getZ());
            player.sendSystemMessage(Component.literal("Elettél teleportálva!"));
          }
        );
        return 0;
    }

    private boolean EnoughPlayer(MinecraftServer server, Player player) {
        if(ServerUtils.getPlayerCount(server) <= 1  || Objects.requireNonNull(player.getServer()).isSingleplayer()) {
            player.sendSystemMessage(Component.literal("Nincs fent elég játékos, vagy/és nem játékos futatta le a parancsot!"));
            return false;
        }
        return true;
    }
}
