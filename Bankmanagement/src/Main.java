 import service.BankService;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        BankService bank = new BankService();
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        do {
            try {
                System.out.println("\n===== BANK MANAGEMENT SYSTEM =====");
                System.out.println("1. Create Account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Transfer");
                System.out.println("5. Check Balance");
                System.out.println("6. View All Accounts");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");
                choice = sc.nextInt();
                sc.nextLine(); // consume leftover newline

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter initial deposit: ");
                        double deposit = sc.nextDouble();
                        bank.createAccount(name, deposit);
                    }
                    case 2 -> {
                        System.out.print("Enter account no: ");
                        int acc = sc.nextInt();
                        System.out.print("Enter amount: ");
                        double amt = sc.nextDouble();
                        bank.deposit(acc, amt);
                    }
                    case 3 -> {
                        System.out.print("Enter account no: ");
                        int acc = sc.nextInt();
                        System.out.print("Enter amount: ");
                        double amt = sc.nextDouble();
                        bank.withdraw(acc, amt);
                    }
                    case 4 -> {
                        System.out.print("Enter From Account: ");
                        int from = sc.nextInt();
                        System.out.print("Enter To Account: ");
                        int to = sc.nextInt();
                        System.out.print("Enter Amount: ");
                        double amt = sc.nextDouble();
                        bank.transfer(from, to, amt);
                    }
                    case 5 -> {
                        System.out.print("Enter account no: ");
                        int acc = sc.nextInt();
                        bank.checkBalance(acc);
                    }
                    case 6 -> bank.viewAllAccounts();
                    case 0 -> System.out.println("👋 Exiting...");
                    default -> System.out.println("❌ Invalid choice!");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input! Please enter valid data.");
                sc.nextLine(); // clear the invalid input
                choice = -1; // prevent exit
            } catch (Exception e) {
                System.out.println("❌ An error occurred: " + e.getMessage());
                sc.nextLine();
                choice = -1;
            }

        } while (choice != 0);
        sc.close();
    }
}
