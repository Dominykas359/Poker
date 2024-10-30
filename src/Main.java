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
        double money;

        // Display menu until the game starts
        while (!game.isPlaying()) {
            menu.menu();
        }

        System.out.print("Enter your name: ");
        name = scanner.nextLine();

        System.out.print("Enter the amount of money you want to play with: ");
        while (true) {
            if (scanner.hasNextDouble()) {
                money = scanner.nextDouble();
                scanner.nextLine();  // Clear the newline character
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

            //blind bets of the game
            table.setCurrentAction("blind betting");
            table.displayTable();
            if (game.isBlinds()) {
                // Set blinds
                players.forEach(player -> {
                    if (player.isSmallBlind() && player.getMoney() >= smallBlind) {
                        player.setMoney(player.getMoney() - smallBlind);
                        player.setBet(smallBlind);
                        table.setPrizeMoney(table.getPrizeMoney() + smallBlind);
                        table.setCurrentBet(smallBlind);
                        player.setSmallBlind(false);
                    } else if (player.isBigBlind() && player.getMoney() >= bigBlind) {
                        player.setMoney(player.getMoney() - bigBlind);
                        player.setBet(bigBlind);
                        table.setPrizeMoney(table.getPrizeMoney() + bigBlind);
                        table.setCurrentBet(bigBlind);
                        player.setBigBlind(false);
                    }
                });

                int turnIndex = 2; // Start turns after the big blind player
                while (!allPlayersHaveMatchedBet(players, table.getCurrentBet())) {
                    Player currentPlayer = players.get(turnIndex);

                    // Skip folded players or players with no money
                    if (currentPlayer.isFolded() || currentPlayer.getMoney() <= 0) {
                        turnIndex = (turnIndex + 1) % players.size();
                        continue;
                    }

                    if (currentPlayer.equals(human)) {
                        System.out.println("Actions: c - call/check, r - raise, f - fold, a - all in");
                        System.out.print("Your action: ");
                        String action = scanner.nextLine();

                        switch (action) {
                            case "c":
                                call(table, currentPlayer);
                                break;
                            case "r":
                                raise(scanner, table, currentPlayer);
                                break;
                            case "f":
                                currentPlayer.setFolded(true);
                                break;
                            case "a":
                                allIn(table, currentPlayer);
                                break;
                            default:
                                System.out.println("Choose a valid option!");
                        }
                    } else {
                        call(table, currentPlayer);  // Automated call for computer players
                    }

                    table.displayTable();
                    turnIndex = (turnIndex + 1) % players.size();
                }

                game.setBlinds(false);
                game.setFlop(true);
                players.forEach(player -> {
                    player.setBet(-1);
                });
                table.setCurrentBet(0);
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
                int turnIndex = 0;

                while (!allPlayersHaveMatchedBet(players, table.getCurrentBet())) {
                    Player currentPlayer = players.get(turnIndex);

                    // Skip folded players or players with no money
                    if (currentPlayer.isFolded() || currentPlayer.getMoney() <= 0) {
                        turnIndex = (turnIndex + 1) % players.size();
                        continue;
                    }

                    if (currentPlayer.equals(human)) {
                        System.out.println("Actions: c - call/check, r - raise, f - fold, a - all in");
                        System.out.print("Your action: ");
                        String action = scanner.nextLine();

                        switch (action) {
                            case "c":
                                call(table, currentPlayer);
                                break;
                            case "r":
                                raise(scanner, table, currentPlayer);
                                break;
                            case "f":
                                currentPlayer.setFolded(true);
                                break;
                            case "a":
                                allIn(table, currentPlayer);
                                break;
                            default:
                                System.out.println("Choose a valid option!");
                        }
                    } else {
                        call(table, currentPlayer);  // Automated call for computer players
                    }

                    table.displayTable();
                    turnIndex = (turnIndex + 1) % players.size();
                }

                game.setFlop(false); // This indicates the end of the flop betting round
                game.setTurn(true);

                players.forEach(player -> {
                    player.setBet(-1);
                });
                table.setCurrentBet(0);
            }

            //after flop stage of the game another card is opened
            table.addCard(deck.dealCard());

            Misc.cleanScreen();
            players.removeIf(player -> player.getMoney() <= 0);

            //turn stage of the game where 4 cards are opened and bets are placed
            table.setCurrentAction("turn betting");
            table.displayTable();
            if(game.isTurn()){
                int turnIndex = 0;

                while (!allPlayersHaveMatchedBet(players, table.getCurrentBet())) {
                    Player currentPlayer = players.get(turnIndex);

                    // Skip folded players or players with no money
                    if (currentPlayer.isFolded() || currentPlayer.getMoney() <= 0) {
                        turnIndex = (turnIndex + 1) % players.size();
                        continue;
                    }

                    if (currentPlayer.equals(human)) {
                        System.out.println("Actions: c - call/check, r - raise, f - fold, a - all in");
                        System.out.print("Your action: ");
                        String action = scanner.nextLine();

                        switch (action) {
                            case "c":
                                call(table, currentPlayer);
                                break;
                            case "r":
                                raise(scanner, table, currentPlayer);
                                break;
                            case "f":
                                currentPlayer.setFolded(true);
                                break;
                            case "a":
                                allIn(table, currentPlayer);
                                break;
                            default:
                                System.out.println("Choose a valid option!");
                        }
                    } else {
                        call(table, currentPlayer);  // Automated call for computer players
                    }

                    table.displayTable();
                    turnIndex = (turnIndex + 1) % players.size();
                }

                game.setTurn(false); // This indicates the end of the flop betting round
                game.setRiver(true);

                players.forEach(player -> {
                    player.setBet(-1);
                });
                table.setCurrentBet(0);

            }

            //after turn fifth card are opened
            table.addCard(deck.dealCard());

            Misc.cleanScreen();
            players.removeIf(player -> player.getMoney() <= 0);

            //in the river stage of the game all 5 cards are opened and final bets are placed
            table.setCurrentAction("river betting");
            table.displayTable();
            if(game.isRiver()){
                int turnIndex = 0;

                while (!allPlayersHaveMatchedBet(players, table.getCurrentBet())) {
                    Player currentPlayer = players.get(turnIndex);

                    // Skip folded players or players with no money
                    if (currentPlayer.isFolded() || currentPlayer.getMoney() <= 0) {
                        turnIndex = (turnIndex + 1) % players.size();
                        continue;
                    }

                    if (currentPlayer.equals(human)) {
                        System.out.println("Actions: c - call/check, r - raise, f - fold, a - all in");
                        System.out.print("Your action: ");
                        String action = scanner.nextLine();

                        switch (action) {
                            case "c":
                                call(table, currentPlayer);
                                break;
                            case "r":
                                raise(scanner, table, currentPlayer);
                                break;
                            case "f":
                                currentPlayer.setFolded(true);
                                break;
                            case "a":
                                allIn(table, currentPlayer);
                                break;
                            default:
                                System.out.println("Choose a valid option!");
                        }
                    } else {
                        call(table, currentPlayer);  // Automated call for computer players
                    }

                    table.displayTable();
                    turnIndex = (turnIndex + 1) % players.size();
                }

                game.setRiver(false);

                players.forEach(player -> {
                    player.setBet(-1);
                });
                table.setCurrentBet(0);
            }
            Misc.cleanScreen();
            table.displayTable();
            players.removeIf(player -> player.getMoney() <= 0);

            //after final bet the winner is calculated

        }
    }

    private static boolean allPlayersHaveMatchedBet(List<Player> players, double currentBet) {
        return players.stream()
                .allMatch(player -> player.getBet() == currentBet);
    }

    // Helper function to call a bet
    private static void call(Table table, Player player) {
        double currentBet = table.getCurrentBet();  // Get the current bet
        double amountToBet = currentBet - player.getBet(); // Calculate how much more the player needs to bet to call

        // Check if the player can cover the amount they need to bet
        if (amountToBet <= 0) {
            // No need to bet anything (already matched or over bet)
            return;
        }

        if(player.getBet() == -1 && table.getCurrentBet() == 0){
            player.setBet(0);
            return;
        }

        if (player.getMoney() >= amountToBet) {
            // Player can call the bet
            player.setMoney(player.getMoney() - amountToBet); // Deduct from player's money
            player.setBet(player.getBet() + amountToBet); // Update player's total bet
        } else {
            // Player goes all-in
            amountToBet = player.getMoney(); // They can only bet what they have
            player.setBet(player.getBet() + amountToBet); // Update player's bet
            player.setMoney(0); // Player is now out of money
        }

        // Update the prize money with the amount bet
        table.setPrizeMoney(table.getPrizeMoney() + amountToBet);
    }


    // Helper function to handle raise action
    private static void raise(Scanner scanner, Table table, Player player) {
        System.out.print("Enter amount of money you want to raise: ");
        double raisedMoney;
        while (true) {
            if (scanner.hasNextDouble()) {
                raisedMoney = scanner.nextDouble();
                scanner.nextLine(); // Clear newline
                if (raisedMoney > 0 && raisedMoney <= player.getMoney()) {
                    break;
                } else {
                    System.out.print("Please enter a valid amount: ");
                }
            } else {
                System.out.print("Invalid input. Enter a valid amount: ");
                scanner.next(); // Clear invalid input
            }
        }
        player.setBet(player.getBet() + raisedMoney);
        table.setCurrentBet(player.getBet()); // Update current bet to player's total bet
        table.setPrizeMoney(table.getPrizeMoney() + raisedMoney);
        player.setMoney(player.getMoney() - raisedMoney);
    }

    // Helper function to handle all-in action
    private static void allIn(Table table, Player player) {
        table.setCurrentBet(player.getBet() + player.getMoney());
        table.setPrizeMoney(table.getPrizeMoney() + player.getMoney());
        player.setBet(player.getBet() + player.getMoney());
        player.setMoney(0);
    }
}
