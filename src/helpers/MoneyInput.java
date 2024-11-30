package helpers;

import java.util.Scanner;

public class MoneyInput {

    public static double inputMoney(Scanner scanner){

        double money;

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

        return money;
    }
}
