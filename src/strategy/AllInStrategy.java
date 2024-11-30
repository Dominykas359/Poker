package strategy;

import misc.Game;
import misc.Table;
import models.Player;

import java.util.Scanner;

public class AllInStrategy implements MoveStrategy{
    @Override
    public void execute(Table table, Player player, Scanner scanner, Game game) {
        table.setCurrentBet(player.getBet() + player.getMoney());
        table.setPrizeMoney(table.getPrizeMoney() + player.getMoney());
        player.setBet(player.getBet() + player.getMoney());
        player.setMoney(0);
        game.setFlop(false);
        game.setTurn(false);
        game.setRiver(false);
        if (player.getMoney() <= 0) player.setFolded(true);
        player.setMoved(true);
        table.addActionHistory(STR."\{player.getName()} went all in.");
    }
}
