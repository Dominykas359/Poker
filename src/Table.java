import models.Card;
import models.Player;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private double prizeMoney;
    private String currentAction;
    private double currentBet;

    private List<Player> players = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public List<Card> getCards(){
        return cards;
    }

    public void setPrizeMoney(double amount){
        this.prizeMoney = amount;
    }

    public double getPrizeMoney(){
        return prizeMoney;
    }

    public String getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(String currentAction) {
        this.currentAction = currentAction;
    }

    public double getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(double currentBet) {
        this.currentBet = currentBet;
    }

    public void resetCards(){
        cards.clear();
    }

    public void displayTable(){
        for(Player player: players){
            System.out.println(STR."\{player.getName()} \{player.getMoney()}");
        }
        System.out.println(STR."Prize money: \{prizeMoney}");

        System.out.println(STR."Stage if the game: \{currentAction}");

        System.out.println(" ");
        for(Card card: cards){
            System.out.print(STR."[\{card}] ");
        }
        System.out.println(" ");

        System.out.println(STR."Your cards: \{players.get(0).getCards()}");
    }
}
