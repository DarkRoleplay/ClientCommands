package net.dark_roleplay.clientcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ClientCommands.MODID)
public class ClientCommands {

	public static final String	MODID	= "clientcommands";
	public static final Logger LOGGER = LogManager.getLogger();

	private static final CommandDispatcher<CommandSource> COMMANDS_DISPATCHER = new CommandDispatcher();

	public ClientCommands() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcCallback);
	}

	public void imcCallback(InterModProcessEvent event){
		event.getIMCStream("client_commands"::equals)
				.map(message -> message.getMessageSupplier().get())
				.filter(o -> o instanceof LiteralArgumentBuilder<?>)
				.map(o -> (LiteralArgumentBuilder<CommandSource>) o)
				.forEachOrdered(COMMANDS_DISPATCHER::register);
	}

	public static char getMarker(){
		return '!';
	}

	public static CommandDispatcher<CommandSource> getCommandDispatcher(){
		return COMMANDS_DISPATCHER;
	}
}
