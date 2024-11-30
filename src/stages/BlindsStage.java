package stages;

import misc.Table;
import models.Player;

public class BlindsStage {

    public static void setBlinds(double smallBlind, double bigBlind, Player player, Table table){

        if (player.isSmallBlind() && player.getMoney() >= smallBlind) {
            player.setMoney(player.getMoney() - smallBlind);
            player.setBet(smallBlind);
            table.setPrizeMoney(table.getPrizeMoney() + smallBlind);
            table.setCurrentBet(smallBlind);
            player.setMoved(true);
            table.addActionHistory(STR."\{player.getName()} set the small blind for \{smallBlind}.");
        } else if (player.isBigBlind() && player.getMoney() >= bigBlind) {
            player.setMoney(player.getMoney() - bigBlind);
            player.setBet(bigBlind);
            table.setPrizeMoney(table.getPrizeMoney() + bigBlind);
            table.setCurrentBet(bigBlind);
            player.setMoved(true);
            table.addActionHistory(STR."\{player.getName()} set the big blind for \{bigBlind}.");
        }
    }
}
