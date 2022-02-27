package net.dark_roleplay.clientcommands;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dark_roleplay.clientcommands.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ClientCommands.MODID, value = Dist.CLIENT)
public class ClientCommandsChatListener {
	@SubscribeEvent
	public static void playerChat(ClientChatEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		ClientCommandSource source = new ClientCommandSource(player, player.position(), player.getRotationVector(), null, 4, player.getName().getString(), player.getDisplayName(), null, player);

		if (event.getMessage().startsWith(Config.commandStarter.get())) {
			try {
				ParseResults<CommandSourceStack> parse = ClientCommands.getCommandDispatcher().parse(event.getMessage().substring(Config.commandStarter.get().length()), source);

				if (parse.getContext().getNodes().size() > 0) {
					event.setCanceled(true);
					Minecraft.getInstance().gui.getChat().addRecentChat(event.getOriginalMessage());
					ClientCommands.getCommandDispatcher().execute(parse);
				}
			} catch (CommandSyntaxException exception) {
				source.sendFailure(ComponentUtils.fromMessage(exception.getRawMessage()));

				if (exception.getInput() != null && exception.getCursor() >= 0) {
					int position = Math.min(exception.getInput().length(), exception.getCursor());

					MutableComponent details = (new TextComponent("")).withStyle(ChatFormatting.GRAY).withStyle((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, event.getMessage())));

					if (position > 10) {
						details.append("...");
					}

					details.append(exception.getInput().substring(Math.max(0, position - 10), position));
					if (position < exception.getInput().length()) {
						details.append((new TextComponent(exception.getInput().substring(position))).withStyle(ChatFormatting.RED, ChatFormatting.UNDERLINE));
					}

					details.append((new TranslatableComponent("command.context.here")).withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
					Minecraft.getInstance().player.sendMessage((new TextComponent("")).append(details).withStyle(ChatFormatting.RED), Util.NIL_UUID);
				}
			}
		}
	}
}
