package natjom.neopunk.commands.social;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.ChatFormatting;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NickColorCommandSelect {

    private static final List<String> VALID_COLORS = Arrays.stream(ChatFormatting.values())
            .filter(ChatFormatting::isColor)
            .map(format -> format.getName().toLowerCase())
            .toList();

    public static CompletableFuture<Suggestions> suggestColors(
            CommandContext<CommandSourceStack> context,
            SuggestionsBuilder builder
    ) {
        for (String color : VALID_COLORS) {
            if (color.startsWith(builder.getRemaining().toLowerCase())) {
                builder.suggest(color);
            }
        }
        return builder.buildFuture();
    }
}
