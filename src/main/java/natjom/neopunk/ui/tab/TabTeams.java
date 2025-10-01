package natjom.neopunk.ui.tab;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

public final class TabTeams {
    private TabTeams(){}

    public static void applyTeamDecor(ServerPlayer player, Component prefix, Component suffix) {
        Scoreboard sb = player.getServer().getScoreboard();
        String teamName = "ac_" + player.getScoreboardName(); // unique, sans espaces

        PlayerTeam team = sb.getPlayerTeam(teamName);
        if (team == null) {
            team = sb.addPlayerTeam(teamName);
        }

        team.setPlayerPrefix(prefix);
        team.setPlayerSuffix(suffix);

        // S’assurer que le joueur est bien dans sa team
        if (!sb.getPlayersTeam(player.getScoreboardName()).equals(team)) {
            sb.addPlayerToTeam(player.getScoreboardName(), team);
        }
    }
}
