import helpers.MoneyInput;
import helpers.WinnerHelpers;
import misc.*;
import helpers.GameFlowHelpers;
import models.Deck;
import models.Hand;
import models.Player;
import stages.BlindsStage;
import strategy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Deck deck = new Deck();
        Game game = new Game();
        Menu menu = new Menu(game);
        Table table = Table.getInstance();
        PlayerAction playerAction = new PlayerAction();
        Scanner scanner = new Scanner(System.in);

        String name;

        // Display menu until the game starts
        while (!game.isPlaying()) {
            menu.menu();
        }

        System.out.print("Enter your name: ");
        name = scanner.nextLine();

        double money = MoneyInput.inputMoney(scanner);

        Misc.cleanScreen();

        Player human = new Player(name, money, true, false, 0, false, false);
        Player computer1 = new Player("Computer1", money, false, true, 0, false, false);
        Player computer2 = new Player("Computer2", money, false, false, 0, false, false);
        Player computer3 = new Player("Computer3", money, false, false, 0, false, false);

        List<Player> players = new ArrayList<>(List.of(human, computer1, computer2, computer3));
        table.addPlayer(human);
        table.addPlayer(computer1);
        table.addPlayer(computer2);
        table.addPlayer(computer3);

        double smallBlind = money * 0.05;
        double bigBlind = smallBlind * 2;

        game.setBlinds(true);

        while (game.isPlaying()) {
            deck.resetDeck();
            players.forEach(player -> player.setFolded(false));

            // Deal two cards to each player
            players.forEach(player -> player.setCards(List.of(deck.dealCard(), deck.dealCard())));

            GameFlowHelpers.shiftBlinds(players);

            int turnIndex = GameFlowHelpers.getIndexOfNextPlayerAfterBigBlind(players);

            //blind bets of the game
            table.setCurrentAction("blind betting");
            table.displayTable();
            if (game.isBlinds()) {
                // Set blinds
                players.forEach(player -> {
                    BlindsStage.setBlinds(smallBlind, bigBlind, player, table);
                });

                turnIndex = GameFlowHelpers.matchedBets(players, table, turnIndex, scanner, playerAction, human, game);

                game.setBlinds(false);
                game.setFlop(true);

                //resetting bets after each round
                GameFlowHelpers.resetBets(players, table);
            }
            if (WinnerHelpers.onlyOnePlayerRemaining(players)) {
                Player winner = players.stream().filter(player -> !player.isFolded()).findFirst().orElse(null);
                if (winner != null) {
                    WinnerHelpers.awardPrizeMoney(winner, table);
                    continue; // Move to the next round
                }
            }
            if (WinnerHelpers.countPlayersWithMoney(players)) { // Adjust this line
                game.setPlaying(false);
            }
            //after blind bets 3 cards are opened
            table.addCard(deck.dealCard());
            table.addCard(deck.dealCard());
            table.addCard(deck.dealCard());

            Misc.cleanScreen();
            players.removeIf(player -> player.getMoney() <= 0);

            //flop stage of the game where 3 cards are open and bets are placed
            table.setCurrentAction("flop betting");
            table.displayTable();
            if (game.isFlop()) {

                turnIndex = GameFlowHelpers.matchedBets(players, table, turnIndex, scanner, playerAction, human, game);

                game.setFlop(false); // This indicates the end of the flop betting round
                game.setTurn(true);

                //resetting bets after each round
                GameFlowHelpers.resetBets(players, table);
            }
            if (WinnerHelpers.onlyOnePlayerRemaining(players)) {
                Player winner = players.stream().filter(player -> !player.isFolded()).findFirst().orElse(null);
                if (winner != null) {
                    WinnerHelpers.awardPrizeMoney(winner, table);
                    continue; // Move to the next round
                }
            }
            if (WinnerHelpers.countPlayersWithMoney(players)) {
                game.setPlaying(false);
            }

            //after flop stage of the game another card is opened
            table.addCard(deck.dealCard());

            Misc.cleanScreen();
            players.removeIf(player -> player.getMoney() <= 0);

            //turn stage of the game where 4 cards are opened and bets are placed
            table.setCurrentAction("turn betting");
            table.displayTable();
            if(game.isTurn()){

                turnIndex = GameFlowHelpers.matchedBets(players, table, turnIndex, scanner, playerAction, human, game);

                game.setTurn(false); // This indicates the end of the flop betting round
                game.setRiver(true);

                //resetting bets after each round
                GameFlowHelpers.resetBets(players, table);

            }
            if (WinnerHelpers.onlyOnePlayerRemaining(players)) {
                Player winner = players.stream().filter(player -> !player.isFolded()).findFirst().orElse(null);
                if (winner != null) {
                    WinnerHelpers.awardPrizeMoney(winner, table);
                    continue; // Move to the next round
                }
            }
            if (WinnerHelpers.countPlayersWithMoney(players)) {
                game.setPlaying(false);
            }

            //after turn fifth card is opened
            table.addCard(deck.dealCard());

            Misc.cleanScreen();
            players.removeIf(player -> player.getMoney() <= 0);

            //in the river stage of the game all 5 cards are opened and final bets are placed
            table.setCurrentAction("river betting");
            table.displayTable();
            if(game.isRiver()){

                turnIndex = GameFlowHelpers.matchedBets(players, table, turnIndex, scanner, playerAction, human, game);

                game.setRiver(false);

                //resetting bets after each round
                GameFlowHelpers.resetBets(players, table);
            }
            if (WinnerHelpers.onlyOnePlayerRemaining(players)) {
                Player winner = players.stream().filter(player -> !player.isFolded()).findFirst().orElse(null);
                if (winner != null) {
                    WinnerHelpers.awardPrizeMoney(winner, table);
                    continue; // Move to the next round
                }
            }
            if (WinnerHelpers.countPlayersWithMoney(players)) {
                game.setPlaying(false);
            }
            Misc.cleanScreen();
            table.displayTable();

            players.forEach(player -> {
                Hand hand = CalculateHand.calculateBestHand(player.getCards(), table.getCards());
                player.setScore(hand.getRankValue());
            });

            //after final bet the winner is calculated
            Player winner = WinnerHelpers.determineWinner(players);
            WinnerHelpers.awardPrizeMoney(winner, table);
            table.resetCards();
            players.forEach(player -> {
                if (player.equals(human) && player.getMoney() == 0){
                    game.setPlaying(false);
                }
            });
            if(players.size() == 1){
                game.setPlaying(false);
                break;
            }
            else {
                game.setBlinds(true);
            }
            players.removeIf(player -> player.getMoney() <= 0);
        }
        Misc.cleanScreen();
        for(Player player: players){
            System.out.println(STR."\{player.getName()} won the game");
        }
    }
}