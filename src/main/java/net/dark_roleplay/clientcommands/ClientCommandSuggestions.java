package net.dark_roleplay.clientcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.dark_roleplay.clientcommands.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.Collection;

public class ClientCommandSuggestions extends CommandSuggestions {
	public ClientCommandSuggestions(Minecraft mc, Screen screen, EditBox inputField, Font font, boolean commandsOnly, boolean hasCursor, int minAmountRenderer, int maxAmountRendered, boolean isChat, int color) {
		super(mc, screen, inputField, font, commandsOnly, hasCursor, minAmountRenderer, maxAmountRendered, isChat, color);
	}

	@Override
	public void updateCommandInfo() {
		var minecraft = Minecraft.getInstance();

		String chatInput = this.input.getValue();

		if (chatInput.startsWith(Config.commandStarter.get())) {
			if (this.currentParse != null && !this.currentParse.getReader().getString().equals(chatInput)) {
				this.currentParse = null;
			}

			if (!this.keepSuggestions) {
				this.input.setSuggestion(null);
				this.suggestions = null;
			}

			this.commandUsage.clear();
			StringReader reader = new StringReader(chatInput);
			for(int i = 0; i < Config.commandStarter.get().length(); i++)
				reader.skip();

			boolean isCommand = chatInput.startsWith(Config.commandStarter.get());
			boolean canParse = this.commandsOnly || isCommand;
			int cursorPosition = this.input.getCursorPosition();

			if (canParse) {
				CommandDispatcher<SharedSuggestionProvider> dispatcher = (CommandDispatcher<SharedSuggestionProvider>) (CommandDispatcher<?>) ClientCommands.getCommandDispatcher();

				if (this.currentParse == null) {
					this.currentParse = dispatcher.parse(reader, minecraft.player.connection.getSuggestionsProvider());
				}

				int errorPosition = this.onlyShowIfCursorPastError ? reader.getCursor() : 1;
				if (cursorPosition >= errorPosition && (this.suggestions == null || !this.keepSuggestions)) {
					this.pendingSuggestions = dispatcher.getCompletionSuggestions(this.currentParse, cursorPosition);
					this.pendingSuggestions.thenRun(() ->
					{
						if (this.pendingSuggestions.isDone()) {
							this.updateUsageInfo();
						}
					});
				}
			} else {
				String s1 = chatInput.substring(0, cursorPosition);
				int k = getLastWordIndex(s1);
				Collection<String> collection = minecraft.player.connection.getSuggestionsProvider().getOnlinePlayerNames();
				this.pendingSuggestions = SharedSuggestionProvider.suggest(collection, new SuggestionsBuilder(s1, k));
			}
		} else {
			super.updateCommandInfo();
		}
	}
}
