import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SimpleBankingSystem extends Application {
    private double balance = 1000;  // Initial balance in PHP
    private double dailyWithdrawalLimit = 5000; // Daily withdrawal limit in PHP
    private double dailyWithdrawalTotal = 0;  // Total withdrawal today in PHP
    private String pinCode = "1234";  // Hardcoded PIN code for simplicity
    private boolean isAuthenticated = false;
    private StringBuilder transactionHistory = new StringBuilder();  // For storing transactions

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ATM Banking System");

        // UI Components
        Label balanceLabel = new Label("Balance: ");
        Label balanceValue = new Label("₱" + balance);
        TextField amountField = new TextField();
        Button depositButton = new Button("Deposit");
        Button withdrawButton = new Button("Withdraw");
        Button checkBalanceButton = new Button("Check Balance");
        Button transactionHistoryButton = new Button("Transaction History");
        Button exitButton = new Button("Exit");
        Label messageLabel = new Label();

        // Authentication
        Label pinLabel = new Label("Enter PIN:");
        PasswordField pinField = new PasswordField();
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> authenticate(pinField, messageLabel, balanceValue));

        // Set up the GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Add components to the grid
        grid.add(pinLabel, 0, 0);
        grid.add(pinField, 1, 0);
        grid.add(loginButton, 0, 1, 2, 1);
        grid.add(balanceLabel, 0, 2);
        grid.add(balanceValue, 1, 2);
        grid.add(new Label("Amount:"), 0, 3);
        grid.add(amountField, 1, 3);
        grid.add(depositButton, 0, 4);
        grid.add(withdrawButton, 1, 4);
        grid.add(checkBalanceButton, 0, 5);
        grid.add(transactionHistoryButton, 1, 5);
        grid.add(messageLabel, 0, 6, 2, 1);
        grid.add(exitButton, 0, 7, 2, 1);

        // Event Handlers
        depositButton.setOnAction(e -> deposit(amountField, balanceValue, messageLabel));
        withdrawButton.setOnAction(e -> withdraw(amountField, balanceValue, messageLabel));
        checkBalanceButton.setOnAction(e -> checkBalance(balanceValue, messageLabel));
        transactionHistoryButton.setOnAction(e -> showTransactionHistory(messageLabel));
        exitButton.setOnAction(e -> System.exit(0));

        // Set the scene and display
        Scene scene = new Scene(grid, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void authenticate(PasswordField pinField, Label messageLabel, Label balanceValue) {
        String enteredPin = pinField.getText();
        if (enteredPin.equals(pinCode)) {
            isAuthenticated = true;
            messageLabel.setText("Login successful!");
            balanceValue.setText("₱" + balance);
        } else {
            messageLabel.setText("Incorrect PIN. Try again.");
        }
    }

    private void deposit(TextField amountField, Label balanceValue, Label messageLabel) {
        if (!isAuthenticated) {
            messageLabel.setText("Please login first.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount > 0) {
                balance += amount;
                transactionHistory.append("Deposited: ₱").append(amount).append("\n");
                balanceValue.setText("₱" + balance);
                messageLabel.setText("Successfully deposited ₱" + amount);
            } else {
                messageLabel.setText("Please enter a positive amount.");
            }
        } catch (NumberFormatException ex) {
            messageLabel.setText("Invalid amount entered.");
        }
    }

    private void withdraw(TextField amountField, Label balanceValue, Label messageLabel) {
        if (!isAuthenticated) {
            messageLabel.setText("Please login first.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount > 0 && amount <= balance) {
                if (amount + dailyWithdrawalTotal > dailyWithdrawalLimit) {
                    messageLabel.setText("Daily withdrawal limit exceeded.");
                } else {
                    balance -= amount;
                    dailyWithdrawalTotal += amount;
                    transactionHistory.append("Withdrew: ₱").append(amount).append("\n");
                    balanceValue.setText("₱" + balance);
                    messageLabel.setText("Successfully withdrew ₱" + amount);
                }
            } else if (amount > balance) {
                messageLabel.setText("Insufficient funds.");
            } else {
                messageLabel.setText("Please enter a positive amount.");
            }
        } catch (NumberFormatException ex) {
            messageLabel.setText("Invalid amount entered.");
        }
    }

    private void checkBalance(Label balanceValue, Label messageLabel) {
        if (!isAuthenticated) {
            messageLabel.setText("Please login first.");
            return;
        }
        messageLabel.setText("Your balance is: ₱" + balance);
    }

    private void showTransactionHistory(Label messageLabel) {
        if (!isAuthenticated) {
            messageLabel.setText("Please login first.");
            return;
        }
        if (transactionHistory.length() == 0) {
            messageLabel.setText("No transactions yet.");
        } else {
            messageLabel.setText(transactionHistory.toString());
        }
    }
}
