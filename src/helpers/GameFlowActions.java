import misc.Game;
import misc.Table;
import models.Player;
import strategy.*;

import java.util.Scanner;

public class GameFlowActions {

    public void gameActions(Scanner scanner, PlayerAction playerAction, Player player, Player human, Table table, Game game){

        if (player.equals(human) && !player.isFolded()) {
            System.out.println("Actions: c - call/check, r - raise, f - fold, a - all in");
            System.out.print("Your action: ");
            String action = scanner.nextLine();

            switch (action) {
                case "c":
                    playerAction.setMoveStrategy(new CallStrategy());
                    break;
                case "r":
                    playerAction.setMoveStrategy(new RaiseStrategy());
                    break;
                case "f":
                    playerAction.setMoveStrategy(new FoldStrategy());
                    break;
                case "a":
                    playerAction.setMoveStrategy(new AllInStrategy());
                    break;
                case "x":
                    playerAction.setMoveStrategy(new ExitStrategy());
                    break;
                default:
                    System.out.println("Choose a valid option!");
            }
            playerAction.performMove(table, player, scanner, game);
        } else {
            new CallStrategy().execute(table, player, scanner, game);
        }
    }
}
