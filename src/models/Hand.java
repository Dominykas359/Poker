package models;

import java.util.List;

public class Hand {

    private int rankValue;
    private List<Card> cards;

    public Hand(int rankValue, List<Card> cards) {
        this.rankValue = rankValue;
        this.cards = cards;
    }

    public int getRankValue() {
        return rankValue;
    }

    public void setRankValue(int rankValue) {
        this.rankValue = rankValue;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
