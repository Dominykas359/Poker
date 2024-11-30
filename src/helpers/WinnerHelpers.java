package helpers;

import misc.Table;
import models.Player;

import java.util.Comparator;
import java.util.List;

public class WinnerHelpers {

    public static Player determineWinner(List<Player> players) {
        return players.stream()
                .filter(player -> !player.isFolded())
                .max(Comparator.comparingInt(Player::getScore))
                .orElse(null);
    }

    public static void awardPrizeMoney(Player winner, Table table) {
        if (winner != null) {
            winner.setMoney(winner.getMoney() + table.getPrizeMoney());
            table.addActionHistory(STR."\{winner.getName()} wins the round with $\{table.getPrizeMoney()}");
        }
        table.setPrizeMoney(0); // Reset the prize money for the next round
    }

    public static boolean onlyOnePlayerRemaining(List<Player> players) {
        return players.stream().filter(player -> !player.isFolded()).count() == 1;
    }

    public static boolean countPlayersWithMoney(List<Player> players) {
        int count = 0;
        for(Player player: players){
            if(player.getMoney() == 0) count++;
        }

        return count == 3;
    }
}
