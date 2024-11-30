package misc;

import models.Card;
import models.Player;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private static Table instance;
    private double prizeMoney;
    private String currentAction;
    private double currentBet;

    private final List<Player> players = new ArrayList<>();
    private final List<Card> cards = new ArrayList<>();
    private List<String> actionHistory = new ArrayList<>();

    public List<String> getActionHistory() {
        return actionHistory;
    }

    // Private constructor to prevent external instantiation
    private Table() {}

    // Method to get the single instance of Table
    public static Table getInstance() {
        if (instance == null) {
            synchronized (Table.class) {
                if (instance == null) {
                    instance = new Table();
                }
            }
        }
        return instance;
    }

    public void addActionHistory(String action) {
        if(actionHistory.size() == 5) actionHistory.remove(0);
        actionHistory.add(action);
    }

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

        Misc.cleanScreen();

        System.out.println("Last 5 actions: ");
        if (actionHistory.isEmpty()) {
            System.out.println("No actions yet.");
        } else {
            for (String action : actionHistory) {
                System.out.println(action);
            }
        }
        System.out.println(" ");
        System.out.println(STR."Stage of the game: \{currentAction}");
        System.out.println(" ");

        for(Player player: players){
            System.out.println(STR."\{player.getName()} $\{player.getMoney()}");
        }

        System.out.println(" ");

        System.out.print("Community cards: ");
        for(Card card: cards){
            System.out.print(STR."[\{card}] ");
        }
        System.out.println(" ");

        System.out.println(STR."Prize money: \{prizeMoney}");
        System.out.println(" ");

        System.out.println(STR."Your cards: \{players.get(0).getCards()}");

    }
}
