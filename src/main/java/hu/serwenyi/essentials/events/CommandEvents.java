package hu.serwenyi.essentials.events;

import hu.serwenyi.essentials.SerwEssentials;
import hu.serwenyi.essentials.commands.*;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = SerwEssentials.MODID)
public class CommandEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new TPallCommand(event.getDispatcher());
        new TphereCommand(event.getDispatcher());
        new TpLastDeathCommand(event.getDispatcher());
        new SpeedCommand(event.getDispatcher());
        new EnderChestCommand(event.getDispatcher());
        new WhereAmICommand(event.getDispatcher());
        new FreezeCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}
