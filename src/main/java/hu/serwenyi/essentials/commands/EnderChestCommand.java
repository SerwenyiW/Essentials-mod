package hu.serwenyi.essentials.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.server.commands.GameModeCommand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;

public class EnderChestCommand {

    private static final Component CONTAINER_TITLE = Component.translatable("container.enderchest");
    public EnderChestCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("enderchest")
                .requires(cs -> cs.hasPermission(4))
                        .then(Commands.literal("player")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> EnderChestMenu(context, EntityArgument.getPlayer(context, "player")))
                                )
                        )
                .requires(cs -> cs.hasPermission(2))
                .executes(context -> EnderChestMenu(context, context.getSource().getPlayer())));
    }

    public static int EnderChestMenu(CommandContext<CommandSourceStack> context, Player target) {
        CommandSourceStack source = context.getSource();
        Player player = source.getPlayer();
        assert player != null;
        PlayerEnderChestContainer playerenderchestcontainer = target.getEnderChestInventory();
        player.openMenu(new SimpleMenuProvider((p_53124_, p_53125_, p_53126_) -> {
            return ChestMenu.threeRows(p_53124_, p_53125_, playerenderchestcontainer);
        }, CONTAINER_TITLE));
        return 0;
    }
}