package net.dark_roleplay.clientcommands.mixin;

import net.dark_roleplay.clientcommands.ClientCommandSuggestions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

	@Redirect(method = "init", at = @At(value = "NEW", target = "Lnet/minecraft/client/gui/components/CommandSuggestions;"))
	public CommandSuggestions wrapSuggestionHelper(Minecraft mc, Screen screen, EditBox inputField, Font font, boolean commandsOnly, boolean hasCursor, int minAmountRendered, int maxAmountRendered, boolean isChat, int color) {
		return new ClientCommandSuggestions(mc, screen, inputField, font, commandsOnly, hasCursor, minAmountRendered, maxAmountRendered, isChat, color);
	}
}
