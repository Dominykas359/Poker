package helpers;

import misc.Game;
import misc.Table;
import models.Player;
import strategy.PlayerAction;

import java.util.List;
import java.util.Scanner;

public class GameFlowHelpers {

    public static boolean allPlayersHaveMatchedBet(List<Player> players, double currentBet) {
        return players.stream()
                .filter(player -> !player.isFolded())
                .allMatch(player -> player.getBet() == currentBet && player.isMoved());
    }

    public static void shiftBlinds(List<Player> players) {
        int numPlayers = players.size();

        // Find the current small and big blinds
        int smallBlindIndex = -1;
        int bigBlindIndex = -1;
        for (int i = 0; i < numPlayers; i++) {
            if (players.get(i).isSmallBlind()) {
                smallBlindIndex = i;
            }
            if (players.get(i).isBigBlind()) {
                bigBlindIndex = i;
            }
        }

        // Clear current blinds
        if (smallBlindIndex != -1) {
            players.get(smallBlindIndex).setSmallBlind(false);
        }
        if (bigBlindIndex != -1) {
            players.get(bigBlindIndex).setBigBlind(false);
        }

        // Set new blinds, rotating to the next players in the list
        players.get((smallBlindIndex + 1) % numPlayers).setSmallBlind(true);
        players.get((bigBlindIndex + 1) % numPlayers).setBigBlind(true);
    }

    public static int getIndexOfNextPlayerAfterBigBlind(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isBigBlind()) {
                return (i + 1) % players.size();
            }
        }
        return 0;
    }

    public static int matchedBets(List<Player> players, Table table, int turnIndex, Scanner scanner, PlayerAction playerAction, Player human, Game game){

        while (!GameFlowHelpers.allPlayersHaveMatchedBet(players, table.getCurrentBet())) {
            Player currentPlayer = players.get(turnIndex);

            // Skip folded players or players with no money
            if (currentPlayer.isFolded()) {
                turnIndex = (turnIndex + 1) % players.size();
                continue;
            }

            GameFlowActions.gameActions(scanner, playerAction, currentPlayer, human, table, game);

            table.displayTable();
            turnIndex = (turnIndex + 1) % players.size();
        }

        return  turnIndex;
    }

    public static void resetBets(List<Player> players, Table table){

        players.forEach(player -> {
            player.setBet(0);
            player.setMoved(false);
        });
        table.setCurrentBet(0);
    }
}
