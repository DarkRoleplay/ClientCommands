package net.dark_roleplay.clientcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.Collection;

public class ClientCommandSuggestions extends CommandSuggestions
{
    public ClientCommandSuggestions(Minecraft p_93871_, Screen p_93872_, EditBox p_93873_, Font p_93874_, boolean p_93875_, boolean p_93876_, int p_93877_, int p_93878_, boolean p_93879_, int p_93880_)
    {
        super(p_93871_, p_93872_, p_93873_, p_93874_, p_93875_, p_93876_, p_93877_, p_93878_, p_93879_, p_93880_);
    }

    @Override
    public void updateCommandInfo()
    {
        var minecraft = Minecraft.getInstance();

        String chatInput = this.input.getValue();

        if (chatInput.startsWith(ClientCommands.getMarker() + ""))
        {
            if (this.currentParse != null && !this.currentParse.getReader().getString().equals(chatInput))
            {
                this.currentParse = null;
            }

            if (!this.keepSuggestions)
            {
                this.input.setSuggestion(null);
                this.suggestions = null;
            }

            this.commandUsage.clear();
            StringReader reader = new StringReader(chatInput);
            boolean isCommand = reader.canRead() && reader.peek() == ClientCommands.getMarker();

            if (isCommand)
            {
                reader.skip();
            }

            boolean canParse = this.commandsOnly || isCommand;
            int cursorPosition = this.input.getCursorPosition();

            if (canParse)
            {
                CommandDispatcher<SharedSuggestionProvider> dispatcher = (CommandDispatcher<SharedSuggestionProvider>) (CommandDispatcher<?>) ClientCommands.getCommandDispatcher();

                if (this.currentParse == null)
                {
                    this.currentParse = dispatcher.parse(reader, minecraft.player.connection.getSuggestionsProvider());
                }

                int errorPosition = this.onlyShowIfCursorPastError ? reader.getCursor() : 1;
                if (cursorPosition >= errorPosition && (this.suggestions == null || !this.keepSuggestions))
                {
                    this.pendingSuggestions = dispatcher.getCompletionSuggestions(this.currentParse, cursorPosition);
                    this.pendingSuggestions.thenRun(() ->
                    {
                        if (this.pendingSuggestions.isDone())
                        {
                            this.updateUsageInfo();
                        }
                    });
                }
            }
            else
            {
                String s1 = chatInput.substring(0, cursorPosition);
                int k = getLastWordIndex(s1);
                Collection<String> collection = minecraft.player.connection.getSuggestionsProvider().getOnlinePlayerNames();
                this.pendingSuggestions = SharedSuggestionProvider.suggest(collection, new SuggestionsBuilder(s1, k));
            }
        }
        else
        {
            super.updateCommandInfo();
        }
    }
}
