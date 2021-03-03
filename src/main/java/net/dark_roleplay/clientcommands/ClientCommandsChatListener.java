package net.dark_roleplay.clientcommands;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ClientCommands.MODID, value = Dist.CLIENT)
public class ClientCommandsChatListener {
	@SubscribeEvent
	public static void playerChat(ClientChatEvent event){
		if(event.getMessage().startsWith(ClientCommands.getMarker() + "")){
			try{
				ClientPlayerEntity player = Minecraft.getInstance().player;
				ClientCommandSource source = new ClientCommandSource(player, player.getPositionVec(), player.getPitchYaw(), null, 4, player.getName().getString(), player.getDisplayName(), null, player);

				ParseResults<CommandSource> parse = ClientCommands.getCommandDispatcher().parse(event.getMessage().substring(1), source);
				if(parse.getContext().getNodes().size() > 0){
					event.setCanceled(true);
					ClientCommands.getCommandDispatcher().execute(parse);
				}
			} catch (CommandSyntaxException e) {}
		}
	}
}
