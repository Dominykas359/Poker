package misc;

import models.Card;
import models.Hand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CalculateHand {

    public static Hand calculateBestHand(List<Card> playerCards, List<Card> communityCards) {
        List<Card> allCards = new ArrayList<>(playerCards);
        allCards.addAll(communityCards);

        List<Hand> possibleHands = new ArrayList<>();
        List<List<Card>> fiveCardCombinations = generateFiveCardCombinations(allCards);

        for (List<Card> combination : fiveCardCombinations) {
            Hand hand = evaluateHand(combination);
            possibleHands.add(hand);
        }

        return Collections.max(possibleHands, Comparator.comparingInt(Hand::getRankValue));
    }

    public static Hand evaluateHand(List<Card> hand) {
        if (isRoyalFlush(hand)) return new Hand(10, hand);        // Royal Flush
        if (isStraightFlush(hand)) return new Hand(9, hand);      // Straight Flush
        if (isFourOfAKind(hand)) return new Hand(8, hand);        // Four of a Kind
        if (isFullHouse(hand)) return new Hand(7, hand);          // Full House
        if (isFlush(hand)) return new Hand(6, hand);              // Flush
        if (isStraight(hand)) return new Hand(5, hand);           // Straight
        if (isThreeOfAKind(hand)) return new Hand(4, hand);       // Three of a Kind
        if (isTwoPair(hand)) return new Hand(3, hand);            // Two Pair
        if (isPair(hand)) return new Hand(2, hand);               // One Pair
        return new Hand(1, hand);                                 // High Card
    }

    private static boolean isRoyalFlush(List<Card> hand) {
        return isStraightFlush(hand) && hand.stream().anyMatch(card -> card.getRank() == 1);
    }

    private static boolean isStraightFlush(List<Card> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    private static boolean isFourOfAKind(List<Card> hand) {
        return hasNOfAKind(hand, 4);
    }

    private static boolean isFullHouse(List<Card> hand) {
        return hasNOfAKind(hand, 3) && hasNOfAKind(hand, 2);
    }

    private static boolean isFlush(List<Card> hand) {
        int suit = hand.get(0).getSuit();
        return hand.stream().allMatch(card -> card.getSuit() == suit);
    }

    private static boolean isStraight(List<Card> hand) {
        List<Integer> ranks = new ArrayList<>(hand.stream().map(Card::getRank).sorted().toList());

        // Handle Ace as high or low
        if (ranks.get(0) == 1 && ranks.get(4) == 13) {
            ranks.set(0, 14); // Treat Ace as highest rank
            Collections.sort(ranks);
        }

        for (int i = 1; i < ranks.size(); i++) {
            if (ranks.get(i) != ranks.get(i - 1) + 1) return false;
        }
        return true;
    }

    private static boolean isThreeOfAKind(List<Card> hand) {
        return hasNOfAKind(hand, 3);
    }

    private static boolean isTwoPair(List<Card> hand) {
        long distinctRanks = hand.stream().map(Card::getRank).distinct().count();
        return distinctRanks == 3;
    }

    private static boolean isPair(List<Card> hand) {
        long distinctRanks = hand.stream().map(Card::getRank).distinct().count();
        return distinctRanks == 4;
    }

    private static boolean hasNOfAKind(List<Card> hand, int n) {
        for (Card card : hand) {
            int count = (int) hand.stream().filter(c -> c.getRank() == card.getRank()).count();
            if (count == n) return true;
        }
        return false;
    }

    private static List<List<Card>> generateFiveCardCombinations(List<Card> cards) {
        List<List<Card>> combinations = new ArrayList<>();
        int n = cards.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    for (int l = k + 1; l < n; l++) {
                        for (int m = l + 1; m < n; m++) {
                            combinations.add(List.of(cards.get(i), cards.get(j), cards.get(k), cards.get(l), cards.get(m)));
                        }
                    }
                }
            }
        }
        return combinations;
    }
}
