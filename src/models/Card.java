package models;

public class Card {

    private int suit; // 1 - hearts, 2 - diamonds, 3 - spades, 4 - clubs
    private int rank; // from 1 to 13 ( 1 - Ace ... 12 - Queen, 13 - King)

    public Card(int suit, int rank){
        this.suit = suit;
        this.rank = rank;
    }

    public int getSuit() {
        return suit;
    }

    public void setSuit(int suit) {
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        String[] suits = {"", "Hearts", "Diamonds", "Spades", "Clubs"};
        String[] ranks = {"", "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
        return STR."\{ranks[rank]} of \{suits[suit]}";
    }
}
