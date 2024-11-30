package strategy;

import misc.Game;
import misc.Table;
import models.Player;

import java.util.Scanner;

public class RaiseStrategy implements MoveStrategy{
    @Override
    public void execute(Table table, Player player, Scanner scanner, Game game) {
        System.out.print("Enter amount of money you want to raise: ");
        double raisedMoney;
        while (true) {
            if (scanner.hasNextDouble()) {
                raisedMoney = scanner.nextDouble();
                scanner.nextLine(); // Clear newline
                if (raisedMoney > 0 && raisedMoney <= player.getMoney()) {
                    break;
                } else {
                    System.out.print("Please enter a valid amount: ");
                }
            } else {
                System.out.print("Invalid input. Enter a valid amount: ");
                scanner.next(); // Clear invalid input
            }
        }
        player.setBet(player.getBet() + raisedMoney);
        table.setCurrentBet(player.getBet()); // Update current bet to player's total bet
        table.setPrizeMoney(table.getPrizeMoney() + raisedMoney);
        player.setMoney(player.getMoney() - raisedMoney);
        if (player.getMoney() <= 0) player.setFolded(true);
        player.setMoved(true);
        table.addActionHistory(STR."\{player.getName()} raised $ \{raisedMoney}.");
    }
}
