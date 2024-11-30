package strategy;

import misc.Game;
import misc.Table;
import models.Player;

import java.util.Scanner;

public class CallStrategy implements MoveStrategy{
    @Override
    public void execute(Table table, Player player, Scanner scanner, Game game) {
        double currentBet = table.getCurrentBet();  // Get the current bet
        double amountToBet = currentBet - player.getBet(); // Calculate how much more the player needs to bet to call

        // Check if the player can cover the amount they need to bet
        if (amountToBet <= 0) {
            player.setMoved(true);
            // No need to bet anything (already matched or over bet)
            table.addActionHistory(STR."\{player.getName()} called for $\{amountToBet}.");
            return;
        }

        if(player.getBet() == 0 && table.getCurrentBet() == 0){
            player.setBet(0);
            player.setMoved(true);
            table.addActionHistory(STR."\{player.getName()} called for $\{amountToBet}.");
            return;
        }

        if (player.getMoney() >= amountToBet) {
            // Player can call the bet
            player.setMoney(player.getMoney() - amountToBet); // Deduct from player's money
            player.setBet(player.getBet() + amountToBet); // Update player's total bet
            player.setMoved(true);
            table.addActionHistory(STR."\{player.getName()} called for $\{amountToBet}.");
        } else {
            // Player goes all-in
            amountToBet = player.getMoney(); // They can only bet what they have
            player.setBet(player.getBet() + amountToBet); // Update player's bet
            player.setMoney(0); // Player is now out of money
            player.setMoved(true);
            table.addActionHistory(STR."\{player.getName()} called for $\{amountToBet}.");
        }

        // Update the prize money with the amount bet
        table.setPrizeMoney(table.getPrizeMoney() + amountToBet);
        if (player.getMoney() <= 0) player.setFolded(true);
    }
}
