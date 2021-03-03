package net.dark_roleplay.clientcommands.mixin;

import net.dark_roleplay.clientcommands.ClientCommandSuggestionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.CommandSuggestionHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

	@Redirect(method = "init", at = @At(value = "NEW", target = "Lnet/minecraft/client/gui/CommandSuggestionHelper;"))
	public CommandSuggestionHelper wrapSuggestionHelper(Minecraft mc, Screen screen, TextFieldWidget inputField, FontRenderer font, boolean commandsOnly, boolean hasCursor, int minAmountRendered, int maxAmountRendered, boolean isChat, int color) {
		return new ClientCommandSuggestionHelper(mc, screen, inputField, font, commandsOnly, hasCursor, minAmountRendered, maxAmountRendered, isChat, color);
	}
}
