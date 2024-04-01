package hu.serwenyi.essentials.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class SpeedCommand {
    public SpeedCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("speed")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.player())
                        .then(Commands.literal("walkspeed")
                                .then(Commands.literal("set")
                                    .then(Commands.argument("speed", DoubleArgumentType.doubleArg())
                                        .executes(context -> setSpeed(context, EntityArgument.getPlayer(context, "target"), DoubleArgumentType.getDouble(context, "speed"), "walk")))
                                )
                                .then(Commands.literal("get")
                                        .executes(context -> getSpeed(context, EntityArgument.getPlayer(context, "target"), "walk"))
                                )

                        )
                        .then(Commands.literal("flyspeed")
                                .then(Commands.literal("set")
                                        .then(Commands.argument("speed", DoubleArgumentType.doubleArg())
                                                .executes(context -> setSpeed(context, EntityArgument.getPlayer(context, "target"), DoubleArgumentType.getDouble(context, "speed"), "fly")))
                                )
                                .then(Commands.literal("get")
                                        .executes(context -> getSpeed(context, EntityArgument.getPlayer(context, "target"), "fly"))
                                )
                        )
                )
        );
    }

    public static int setSpeed(CommandContext<CommandSourceStack> context, Player target, double cspeed, String movement) {
        CommandSourceStack source = context.getSource();
        Player player = source.getPlayer();
        assert player != null;
        if (Objects.equals(movement, "walk")) {
            Objects.requireNonNull(target.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(cspeed);
            player.sendSystemMessage(Component.literal(target.getName().getString() + " játékos séta gyorsasága erre változott: " + cspeed));
            target.sendSystemMessage(Component.literal("Megváltozott a séta gyorsaságod ennyire: " + cspeed));
        } else if (Objects.equals(movement, "fly")) {
            Objects.requireNonNull(target.getAttribute(Attributes.FLYING_SPEED)).setBaseValue(cspeed);
            player.sendSystemMessage(Component.literal(target.getName().getString() + " játékos repülés gyorsasága erre változott: " + cspeed));
            target.sendSystemMessage(Component.literal("Megváltozott a repülés gyorsaságod ennyire: " + cspeed));
        }
        return 0;
    }

    public static int getSpeed(CommandContext<CommandSourceStack> context, Player target, String movement) {
        CommandSourceStack source = context.getSource();
        Player player = source.getPlayer();
        assert player != null;
        if (Objects.equals(movement, "walk")) {
            double speed = target.getSpeed();
            player.sendSystemMessage(Component.literal(target.getName().getString() + " séta gyorsassága: " + speed));
        } else if (Objects.equals(movement, "fly")) {
            double speed = target.getAbilities().getFlyingSpeed();
            player.sendSystemMessage(Component.literal(target.getName().getString() + " repülés gyorsassága: " + speed));
        }
        return 0;
    }
}
