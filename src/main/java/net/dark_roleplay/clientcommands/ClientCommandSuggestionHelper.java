package net.dark_roleplay.clientcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.CommandSuggestionHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.ISuggestionProvider;

public class ClientCommandSuggestionHelper extends CommandSuggestionHelper {

	public ClientCommandSuggestionHelper(Minecraft mc, Screen screen, TextFieldWidget inputField, FontRenderer font, boolean commandsOnly, boolean hasCursor, int minAmountRendered, int maxAmountRendered, boolean isChat, int color) {
		super(mc, screen, inputField, font, commandsOnly, hasCursor, minAmountRendered, maxAmountRendered, isChat, color);
	}

	@Override
	public void init() {
		String s = this.inputField.getText();
		if(s.startsWith(ClientCommands.getMarker() + "")){
			if (this.parseResults != null && !this.parseResults.getReader().getString().equals(s)) {
				this.parseResults = null;
			}

			if (!this.isApplyingSuggestion) {
				this.inputField.setSuggestion((String)null);
				this.suggestions = null;
			}

			this.exceptionList.clear();
			StringReader stringreader = new StringReader(s);
			boolean flag = stringreader.canRead() && stringreader.peek() == ClientCommands.getMarker();
			if (flag) {
				stringreader.skip();
			}

			int i = this.inputField.getCursorPosition();
			if (flag) {
				CommandDispatcher<ISuggestionProvider> commanddispatcher = (CommandDispatcher<ISuggestionProvider>)(CommandDispatcher<?>) ClientCommands.getCommandDispatcher();
				if (this.parseResults == null) {
					this.parseResults = commanddispatcher.parse(stringreader, Minecraft.getInstance().player.getCommandSource());
				}

				int j = this.hasCursor ? stringreader.getCursor() : 1;
				if (i >= j && (this.suggestions == null || !this.isApplyingSuggestion)) {
					this.suggestionsFuture = commanddispatcher.getCompletionSuggestions(this.parseResults, i);
					this.suggestionsFuture.thenRun(() -> {
						if (this.suggestionsFuture.isDone()) {
							this.recompileSuggestions();
						}
					});
				}
			}
		}else{
			super.init();
		}
	}
}
