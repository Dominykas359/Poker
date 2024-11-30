package strategy;

import misc.Game;
import misc.Table;
import models.Player;

import java.util.Scanner;

public class FoldStrategy implements MoveStrategy{
    @Override
    public void execute(Table table, Player player, Scanner scanner, Game game) {
        player.setFolded(true);
        player.setMoved(true);
        table.addActionHistory(STR."\{player.getName()} folded.");
    }
}
