import models.Deck;
import models.Hand;
import models.Player;

import java.util.ArrayList;
import java.util.Comparator;
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

            shiftBlinds(players);

            int turnIndex = getIndexOfNextPlayerAfterBigBlind(players);

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
                        player.setMoved(true);
                    } else if (player.isBigBlind() && player.getMoney() >= bigBlind) {
                        player.setMoney(player.getMoney() - bigBlind);
                        player.setBet(bigBlind);
                        table.setPrizeMoney(table.getPrizeMoney() + bigBlind);
                        table.setCurrentBet(bigBlind);
                        player.setMoved(true);
                    }
                });

                while (!allPlayersHaveMatchedBet(players, table.getCurrentBet())) {
                    Player currentPlayer = players.get(turnIndex);

                    // Skip folded players or players with no money
                    if (currentPlayer.isFolded()) {
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
                                allIn(table, currentPlayer, game);
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
                    player.setBet(0);
                    player.setMoved(false);
                });
                table.setCurrentBet(0);
            }
            if (onlyOnePlayerRemaining(players)) {
                Player winner = players.stream().filter(player -> !player.isFolded()).findFirst().orElse(null);
                if (winner != null) {
                    awardPrizeMoney(winner, table);
                    continue; // Move to the next round
                }
            }
            if (countPlayersWithMoney(players)) { // Adjust this line
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

                while (!allPlayersHaveMatchedBet(players, table.getCurrentBet())) {
                    if (onlyOnePlayerRemaining(players)) {
                        break; // Exit the loop if only one player remains
                    }
                    Player currentPlayer = players.get(turnIndex);

                    // Skip folded players or players with no money
                    if (currentPlayer.isFolded()) {
                        turnIndex = (turnIndex + 1) % players.size();
                        continue;
                    }

                    if (currentPlayer.equals(human) && !currentPlayer.isFolded()) {
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
                                allIn(table, currentPlayer, game);
                                break;
                            default:
                                System.out.println("Choose a valid option!");
                        }
                    } else if(!currentPlayer.equals(human) && !currentPlayer.isFolded()){
                        call(table, currentPlayer);  // Automated call for computer players
                    }

                    table.displayTable();
                    turnIndex = (turnIndex + 1) % players.size();
                }

                game.setFlop(false); // This indicates the end of the flop betting round
                game.setTurn(true);

                players.forEach(player -> {
                    player.setBet(0);
                    player.setMoved(false);
                });
                table.setCurrentBet(0);
            }
            if (onlyOnePlayerRemaining(players)) {
                Player winner = players.stream().filter(player -> !player.isFolded()).findFirst().orElse(null);
                if (winner != null) {
                    awardPrizeMoney(winner, table);
                    continue; // Move to the next round
                }
            }
            if (countPlayersWithMoney(players)) { // Adjust this line
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

                while (!allPlayersHaveMatchedBet(players, table.getCurrentBet())) {
                    if (onlyOnePlayerRemaining(players)) {
                        break; // Exit the loop if only one player remains
                    }
                    Player currentPlayer = players.get(turnIndex);

                    // Skip folded players or players with no money
                    if (currentPlayer.isFolded()) {
                        turnIndex = (turnIndex + 1) % players.size();
                        continue;
                    }

                    if (currentPlayer.equals(human) && !currentPlayer.isFolded()) {
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
                                allIn(table, currentPlayer, game);
                                break;
                            default:
                                System.out.println("Choose a valid option!");
                        }
                    } else if(!currentPlayer.equals(human) && !currentPlayer.isFolded()) {
                        call(table, currentPlayer);  // Automated call for computer players
                    }

                    table.displayTable();
                    turnIndex = (turnIndex + 1) % players.size();
                }

                game.setTurn(false); // This indicates the end of the flop betting round
                game.setRiver(true);

                players.forEach(player -> {
                    player.setBet(0);
                    player.setMoved(false);
                });
                table.setCurrentBet(0);

            }
            if (onlyOnePlayerRemaining(players)) {
                Player winner = players.stream().filter(player -> !player.isFolded()).findFirst().orElse(null);
                if (winner != null) {
                    awardPrizeMoney(winner, table);
                    continue; // Move to the next round
                }
            }
            if (countPlayersWithMoney(players)) { // Adjust this line
                game.setPlaying(false);
            }

            //after turn fifth card are opened
            table.addCard(deck.dealCard());

            Misc.cleanScreen();
            players.removeIf(player -> player.getMoney() <= 0);

            //in the river stage of the game all 5 cards are opened and final bets are placed
            table.setCurrentAction("river betting");
            table.displayTable();
            if(game.isRiver()){

                while (!allPlayersHaveMatchedBet(players, table.getCurrentBet())) {
                    if (onlyOnePlayerRemaining(players)) {
                        break; // Exit the loop if only one player remains
                    }
                    Player currentPlayer = players.get(turnIndex);

                    // Skip folded players or players with no money
                    if (currentPlayer.isFolded()) {
                        turnIndex = (turnIndex + 1) % players.size();
                        continue;
                    }

                    if (currentPlayer.equals(human) && !currentPlayer.isFolded()) {
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
                                allIn(table, currentPlayer, game);
                                break;
                            default:
                                System.out.println("Choose a valid option!");
                        }
                    } else if(!currentPlayer.equals(human) && !currentPlayer.isFolded()){
                        call(table, currentPlayer);  // Automated call for computer players
                    }

                    table.displayTable();
                    turnIndex = (turnIndex + 1) % players.size();
                }

                game.setRiver(false);

                players.forEach(player -> {
                    player.setBet(0);
                    player.setMoved(false);
                });
                table.setCurrentBet(0);
            }
            if (onlyOnePlayerRemaining(players)) {
                Player winner = players.stream().filter(player -> !player.isFolded()).findFirst().orElse(null);
                if (winner != null) {
                    awardPrizeMoney(winner, table);
                    continue; // Move to the next round
                }
            }
            if (countPlayersWithMoney(players)) { // Adjust this line
                game.setPlaying(false);
            }
            Misc.cleanScreen();
            table.displayTable();

            players.forEach(player -> {
                Hand hand = CalculateHand.calculateBestHand(player.getCards(), table.getCards());
                player.setScore(hand.getRankValue());
            });

            //after final bet the winner is calculated
            Player winner = determineWinner(players);
            awardPrizeMoney(winner, table);
            table.resetCards();
            players.forEach(player -> {
                if (player.equals(human) && player.getMoney() == 0) game.setPlaying(false);
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
    }

    private static boolean allPlayersHaveMatchedBet(List<Player> players, double currentBet) {
        return players.stream()
                .filter(player -> !player.isFolded())  // Only consider active players
                .allMatch(player -> player.getBet() == currentBet && player.isMoved());
    }

    // Helper function to call a bet
    private static void call(Table table, Player player) {
        double currentBet = table.getCurrentBet();  // Get the current bet
        double amountToBet = currentBet - player.getBet(); // Calculate how much more the player needs to bet to call

        // Check if the player can cover the amount they need to bet
        if (amountToBet <= 0) {
            player.setMoved(true);
            // No need to bet anything (already matched or over bet)
            return;
        }

        if(player.getBet() == 0 && table.getCurrentBet() == 0){
            player.setBet(0);
            player.setMoved(true);
            return;
        }

        if (player.getMoney() >= amountToBet) {
            // Player can call the bet
            player.setMoney(player.getMoney() - amountToBet); // Deduct from player's money
            player.setBet(player.getBet() + amountToBet); // Update player's total bet
            player.setMoved(true);
        } else {
            // Player goes all-in
            amountToBet = player.getMoney(); // They can only bet what they have
            player.setBet(player.getBet() + amountToBet); // Update player's bet
            player.setMoney(0); // Player is now out of money
            player.setMoved(true);
        }

        // Update the prize money with the amount bet
        table.setPrizeMoney(table.getPrizeMoney() + amountToBet);
        if (player.getMoney() <= 0) player.setFolded(true);
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
        if (player.getMoney() <= 0) player.setFolded(true);
        player.setMoved(true);
    }

    // Helper function to handle all-in action
    private static void allIn(Table table, Player player, Game game) {
        table.setCurrentBet(player.getBet() + player.getMoney());
        table.setPrizeMoney(table.getPrizeMoney() + player.getMoney());
        player.setBet(player.getBet() + player.getMoney());
        player.setMoney(0);
        game.setFlop(false);
        game.setTurn(false);
        game.setRiver(false);
        if (player.getMoney() <= 0) player.setFolded(true);
        player.setMoved(true);
    }

    private static void shiftBlinds(List<Player> players) {
        int numPlayers = players.size();

        // Find the current small and big blinds
        int smallBlindIndex = -1;
        int bigBlindIndex = -1;
        for (int i = 0; i < numPlayers; i++) {
            if (players.get(i).isSmallBlind()) {
                smallBlindIndex = i;
            }
            if (players.get(i).isBigBlind()) {
                bigBlindIndex = i;
            }
        }

        // Clear current blinds
        if (smallBlindIndex != -1) {
            players.get(smallBlindIndex).setSmallBlind(false);
        }
        if (bigBlindIndex != -1) {
            players.get(bigBlindIndex).setBigBlind(false);
        }

        // Set new blinds, rotating to the next players in the list
        players.get((smallBlindIndex + 1) % numPlayers).setSmallBlind(true);
        players.get((bigBlindIndex + 1) % numPlayers).setBigBlind(true);
    }

    private static int getIndexOfNextPlayerAfterBigBlind(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isBigBlind()) {
                return (i + 1) % players.size();
            }
        }
        return 0;
    }

    private static Player determineWinner(List<Player> players) {
        return players.stream()
                .filter(player -> !player.isFolded())
                .max(Comparator.comparingInt(Player::getScore))
                .orElse(null);
    }

    // Award the prize money to the winning player
    private static void awardPrizeMoney(Player winner, Table table) {
        if (winner != null) {
            winner.setMoney(winner.getMoney() + table.getPrizeMoney());
            System.out.println(STR."\{winner.getName()} wins the round with $\{table.getPrizeMoney()}");
        }
        table.setPrizeMoney(0); // Reset the prize money for the next round
    }

    private static boolean onlyOnePlayerRemaining(List<Player> players) {
        return players.stream().filter(player -> !player.isFolded()).count() == 1;
    }

    private static boolean countPlayersWithMoney(List<Player> players) {
        int count = 0;
        for(Player player: players){
            if(player.getMoney() == 0) count++;
        }

        return count == 3;
    }
}
