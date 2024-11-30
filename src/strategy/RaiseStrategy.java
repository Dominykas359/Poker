package strategy;

import helpers.MoneyInput;
import misc.Game;
import misc.Table;
import models.Player;

import java.util.Scanner;

public class RaiseStrategy implements MoveStrategy{
    @Override
    public void execute(Table table, Player player, Scanner scanner, Game game) {
        System.out.print("Enter amount of money you want to raise: ");
        double raisedMoney = MoneyInput.inputMoney(scanner);
        player.setBet(player.getBet() + raisedMoney);
        table.setCurrentBet(player.getBet()); // Update current bet to player's total bet
        table.setPrizeMoney(table.getPrizeMoney() + raisedMoney);
        player.setMoney(player.getMoney() - raisedMoney);
        if (player.getMoney() <= 0) player.setFolded(true);
        player.setMoved(true);
        table.addActionHistory(STR."\{player.getName()} raised $ \{raisedMoney}.");
    }
}
