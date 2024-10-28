import models.Card;
import models.Deck;
import models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Deck deck = new Deck();
        Game game = new Game();
        Menu menu = new Menu(game);
        Table table = new Table();
        Scanner scanner = new Scanner(System.in);

        String name;
        int money;

        while(!game.isPlaying()){

            menu.menu();
        }

        System.out.print("Enter your name: ");
        name = scanner.nextLine();

        System.out.print("Enter how much money you want to play: ");
        while (true) {
            if (scanner.hasNextInt()) {
                money = scanner.nextInt();
                if (money > 0) {
                    break;
                } else {
                    System.out.print("Please enter a positive amount: ");
                }
            } else {
                System.out.print("Invalid input. Enter a valid amount: ");
                scanner.next(); // Clear invalid input
            }
        }

        Misc.cleanScreen();

        Player human = new Player(name, money);
        Player computer1 = new Player("Computer1", money);
        Player computer2 = new Player("Computer2", money);
        Player computer3 = new Player("Computer3", money);

        table.addPlayer(human);
        table.addPlayer(computer1);
        table.addPlayer(computer2);
        table.addPlayer(computer3);

        while(game.isPlaying()){
            deck.resetDeck();

            List<Card> humanCards = new ArrayList<>();
            List<Card> computer1Cards = new ArrayList<>();
            List<Card> computer2Cards = new ArrayList<>();
            List<Card> computer3Cards = new ArrayList<>();

            // Each player gets two unique cards
            humanCards.add(deck.dealCard());
            humanCards.add(deck.dealCard());
            computer1Cards.add(deck.dealCard());
            computer1Cards.add(deck.dealCard());
            computer2Cards.add(deck.dealCard());
            computer2Cards.add(deck.dealCard());
            computer3Cards.add(deck.dealCard());
            computer3Cards.add(deck.dealCard());

            // Set the dealt cards for each player
            human.setCards(humanCards);
            computer1.setCards(computer1Cards);
            computer2.setCards(computer2Cards);
            computer3.setCards(computer3Cards);

            table.displayTable();

            game.setPlaying(false);

            System.out.print("Choose your new move(b - bid, f - fold, a - all in, t - terminate game): ");

        }

        System.out.println("Game started");
    }
}