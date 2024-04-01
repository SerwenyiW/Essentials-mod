package hu.serwenyi.essentials.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import hu.serwenyi.essentials.utils.TeleportPos;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import java.util.Objects;

public class TpLastDeathCommand {

    public TpLastDeathCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("otp")
                .requires(cs -> cs.hasPermission(2))
                        .then(Commands.literal("get")
                                .executes(context -> otp(context, true))
                        )
                        .executes(context -> otp(context, false))
        );
    }

    public int otp(CommandContext<CommandSourceStack> context, boolean getLastDeathInfo) {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayer();

        assert player != null;
        String json = Objects.requireNonNull(player.serializeNBT().get("LastDeathLocation")).toString();
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String dimension = jsonObject.get("dimension").getAsString();
        JsonArray posArray = jsonObject.get("pos").getAsJsonArray();
        int x = posArray.get(1).getAsInt();
        int y = posArray.get(2).getAsInt();
        int z = posArray.get(3).getAsInt();
        ResourceKey<Level> resourcekey = dimension.contains("minecraft:overworld") ? Level.OVERWORLD : dimension.contains("minecraft:the_end") ? Level.END : dimension.contains("minecraft:the_nether") ? Level.NETHER : null;
        assert resourcekey != null;
        if(getLastDeathInfo) {
            MutableComponent component = Component.literal("Itt haltál meg utoljára: ");
             component.append(ComponentUtils.wrapInSquareBrackets(Component.translatable("chat.coordinates", x, y, z))
                    .withStyle(style -> style.withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + x + " " + y + " " + z))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.coordinates.tooltip")))));
            player.sendSystemMessage(component);
        } else {
            player.sendSystemMessage(Component.literal("Ellettél teleportálva az utolsó halál kordinátádra! X:" + x + " Y:" + y + " Z:" + z));
            BlockPos bp = new BlockPos(x, (y+1), z);
            TeleportPos tp = new TeleportPos(resourcekey , bp);
            tp.teleport(player);
        }
        return 0;
    }
}
