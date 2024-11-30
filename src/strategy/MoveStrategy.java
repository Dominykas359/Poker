package strategy;

import misc.Game;
import misc.Table;
import models.Player;

import java.util.Scanner;

public interface MoveStrategy {

    void execute(Table table, Player player, Scanner scanner, Game game);
}
