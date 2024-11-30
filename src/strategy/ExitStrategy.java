package strategy;

import misc.Game;
import misc.Table;
import models.Player;

import java.util.Scanner;

public class ExitStrategy implements MoveStrategy{
    @Override
    public void execute(Table table, Player player, Scanner scanner, Game game) {
        System.exit(0);
    }
}
