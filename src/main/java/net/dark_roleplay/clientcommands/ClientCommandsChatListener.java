package net.dark_roleplay.clientcommands;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ClientCommands.MODID, value = Dist.CLIENT)
public class ClientCommandsChatListener {
	@SubscribeEvent
	public static void playerChat(ClientChatEvent event){
		LocalPlayer player = Minecraft.getInstance().player;
		ClientCommandSource source = new ClientCommandSource(player, player.position(), player.getRotationVector(), null, 4, player.getName().getString(), player.getDisplayName(), null, player);

		if(event.getMessage().startsWith(ClientCommands.getMarker() + "")){
			try{
				ParseResults<CommandSourceStack> parse = ClientCommands.getCommandDispatcher().parse(event.getMessage().substring(1), source);

				if(parse.getContext().getNodes().size() > 0){
					event.setCanceled(true);
					Minecraft.getInstance().gui.getChat().addRecentChat(event.getOriginalMessage());
					ClientCommands.getCommandDispatcher().execute(parse);
				}
			}
			catch (CommandSyntaxException e)
			{
				source.sendFailure(ComponentUtils.fromMessage(e.getRawMessage()));
			}
		}
	}
}
