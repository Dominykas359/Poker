import models.Player;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private List<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void displayTable(){
        for(Player player: players){
            System.out.println(STR."\{player.getName()} \{player.getMoney()}");
        }

        System.out.println(STR."Your cards: \{players.get(0).getCards()}");
    }
}
