package models;

import java.util.List;

public class Player {

    private String name;
    private double money;
    private boolean smallBlind;
    private boolean bigBlind;
    private boolean move;
    private boolean folded;
    private double bet;
    private boolean lost;
    private List<Card> cards;

    public Player(String name, double money, boolean smallBlind, boolean bigBlind, double bet, boolean folded, boolean lost){
        this.name = name;
        this.money = money;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.bet = bet;
        this.folded = folded;
        this.lost = lost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public boolean isSmallBlind() {
        return smallBlind;
    }

    public void setSmallBlind(boolean smallBlind) {
        this.smallBlind = smallBlind;
    }

    public boolean isBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(boolean bigBlind) {
        this.bigBlind = bigBlind;
    }

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public double getBet() {
        return bet;
    }

    public void setBet(double bet) {
        this.bet = bet;
    }

    public boolean isFolded() {
        return folded;
    }

    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }
}
