package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final List<Card> deck = new ArrayList<>();

    public Deck(){
        initializeDeck();
    }

    public void initializeDeck(){

        for(int i = 1; i <= 4; i++){
            for(int j = 1; j <= 13; j++){
                deck.add(new Card(i, j));
            }
        }

        shuffleDeck();
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public Card dealCard() {
        if (!deck.isEmpty()) {
            return deck.remove(deck.size() - 1);
        }
        return null; // Returns null if no cards are left
    }

    public void resetDeck() {
        initializeDeck(); // Resets and reshuffles the deck
    }
}
