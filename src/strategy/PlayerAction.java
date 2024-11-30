package strategy;

import misc.Game;
import misc.Table;
import models.Player;

import java.util.Scanner;

public class PlayerAction {
    private MoveStrategy moveStrategy;

    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    public void performMove(Table table, Player player, Scanner scanner, Game game) {
        if (moveStrategy != null) {
            moveStrategy.execute(table, player, scanner, game);
        } else {
            System.out.println("No strategy selected!");
        }
    }
}
