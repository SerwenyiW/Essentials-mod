package hu.serwenyi.essentials.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class WhereAmICommand {
    public WhereAmICommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("whereami")
               .requires(cs -> cs.hasPermission(2))
                .executes(context -> WheremAmI(context, Objects.requireNonNull(context.getSource().getPlayer())))
                .then(Commands.argument("who", EntityArgument.player())
                                .executes(context -> WheremAmI(context, EntityArgument.getPlayer(context, "who")))
                )
        );
    }

    private int WheremAmI(CommandContext<CommandSourceStack> context, Player target) {
        CommandSourceStack source = context.getSource();
        Player player = source.getPlayer();
        double x = target.getOnPos().getX();
        double y = target.getOnPos().getY();
        double z = target.getOnPos().getZ();
        MutableComponent component = Component.literal(target.getName().getString() + " kordinátái:\nDimenzió: " + player.level().dimension().location() + "\nX: "+x+"\nY: "+y+"\nZ: "+z)
                .withStyle(style -> style.withColor(ChatFormatting.YELLOW));
        player.sendSystemMessage(component);
        return 0;
    }

}
