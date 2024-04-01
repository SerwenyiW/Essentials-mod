package hu.serwenyi.essentials.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Objects;

public class FreezeCommand {
    public FreezeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("freeze")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(context -> freeze(context, EntityArgument.getPlayer(context, "target")))
                )
        );
    }

    public static int freeze(CommandContext<CommandSourceStack> context, ServerPlayer target) {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayer();
        assert player != null;
        if(target.getSpeed() == 0) {
            Objects.requireNonNull(target.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.1F);
            player.sendSystemMessage(Component.literal(target.getName().getString() + " kiolvasztva!"));
        } else {
            Objects.requireNonNull(target.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0);
            player.sendSystemMessage(Component.literal(target.getName().getString() + " lefagyasztva!"));
        }
        return 0;
    }
}
