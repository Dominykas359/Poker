import misc.Game;
import misc.Misc;

import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final Game game;
    String decision = "";

    public Menu(Game game){
        this.game = game;
    }

    public void menu(){

        while (!game.isPlaying()){
            Misc.cleanScreen();
            System.out.println("Play - p");
            System.out.println("Exit - x");
            System.out.print("Your choice: ");
            decision = scanner.nextLine();

            switch (decision){

                case "p":
                    game.setPlaying(true);
                    Misc.cleanScreen();
                    break;
                case "x":
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
