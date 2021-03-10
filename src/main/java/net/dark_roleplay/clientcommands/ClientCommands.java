package net.dark_roleplay.clientcommands;

import com.ibm.icu.impl.Pair;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

@Mod(ClientCommands.MODID)
public class ClientCommands {

	public static final String	MODID	= "clientcommands";
	public static final Logger LOGGER = LogManager.getLogger();

	private static final CommandDispatcher<CommandSource> COMMANDS_DISPATCHER = new CommandDispatcher();

	public ClientCommands() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcCallback);
	}

	public void imcCallback(InterModProcessEvent event){
		event.getIMCStream("register_command"::equals)
				.map(message -> Pair.of(message.getSenderModId(), message.getMessageSupplier().get()))
				.filter(command -> command.second instanceof LiteralArgumentBuilder<?>)
				.map(command -> Pair.of(command.first, (LiteralArgumentBuilder<CommandSource>) command.second))
				.forEachOrdered(command -> {
					LOGGER.debug("Registering client command '{1}' for '{0}'", command.first, command.second.getLiteral());
					COMMANDS_DISPATCHER.register(command.second);
				});

		event.getIMCStream("register_commands"::equals)
				.map(message -> Pair.of(message.getSenderModId(), message.getMessageSupplier().get()))
				.filter(command -> command.second instanceof Consumer<?>)
				.map(command -> Pair.of(command.first, (Consumer<CommandDispatcher<CommandSource>>) command.second))
				.forEachOrdered(command -> {
					LOGGER.debug("Calling command registry callback for '{0}'", command.first);
					command.second.accept(COMMANDS_DISPATCHER);
				});
	}

	public static char getMarker(){
		return '!';
	}

	public static CommandDispatcher<CommandSource> getCommandDispatcher(){
		return COMMANDS_DISPATCHER;
	}
}
