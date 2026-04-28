package service;
import db.DatabaseConnection;
import model.Account;
import java.sql.*;

public class BankService {

    // Create new account
    public void createAccount(String name, double initialDeposit) {
        String query = "INSERT INTO accounts(name, balance) VALUES(?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setDouble(2, initialDeposit);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int accountNo = rs.getInt(1);
                System.out.println("✅ Account created successfully!");
                System.out.println("📋 Account Number: " + accountNo);
                System.out.println("👤 Name: " + name);
                System.out.println("💰 Initial Balance: ₹" + initialDeposit);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error creating account!");
            e.printStackTrace();
        }
    }

    // Deposit
    public void deposit(int accNo, double amount) {
        if (!accountExists(accNo)) {
            System.out.println("❌ Account not found!");
            return;
        }
        if (amount <= 0) {
            System.out.println("❌ Amount must be positive!");
            return;
        }
        updateBalance(accNo, amount, true);
    }

    // Withdraw
    public void withdraw(int accNo, double amount) {
        if (!accountExists(accNo)) {
            System.out.println("❌ Account not found!");
            return;
        }
        if (amount <= 0) {
            System.out.println("❌ Amount must be positive!");
            return;
        }
        updateBalance(accNo, amount, false);
    }

    // Transfer
    public void transfer(int fromAcc, int toAcc, double amount) {
        if (!accountExists(fromAcc)) {
            System.out.println("❌ Source account not found!");
            return;
        }
        if (!accountExists(toAcc)) {
            System.out.println("❌ Destination account not found!");
            return;
        }
        if (amount <= 0) {
            System.out.println("❌ Amount must be positive!");
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            if (getBalance(fromAcc) >= amount) {
                updateBalance(fromAcc, amount, false, conn);
                updateBalance(toAcc, amount, true, conn);
                conn.commit();
                System.out.println("✅ Transfer successful!");
            } else {
                System.out.println("❌ Insufficient balance!");
                conn.rollback();
            }

        } catch (SQLException e) {
            System.out.println("❌ Transfer failed!");
            e.printStackTrace();
        }
    }

    // Check Balance
    public void checkBalance(int accNo) {
        String query = "SELECT * FROM accounts WHERE account_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, accNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Account acc = new Account(rs.getInt("account_no"), rs.getString("name"), rs.getDouble("balance"));
                System.out.println(acc);
            } else {
                System.out.println("❌ Account not found!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper Methods
    private void updateBalance(int accNo, double amount, boolean isDeposit) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            updateBalance(accNo, amount, isDeposit, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBalance(int accNo, double amount, boolean isDeposit, Connection conn) throws SQLException {
        double current = getBalance(accNo, conn);
        double newBalance = isDeposit ? current + amount : current - amount;

        if (newBalance < 0) {
            System.out.println("❌ Insufficient balance!");
            return;
        }

        String query = "UPDATE accounts SET balance = ? WHERE account_no = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, accNo);
            ps.executeUpdate();
            System.out.println(isDeposit ? "💰 Deposit successful!" : "💸 Withdrawal successful!");
        }
    }

    private double getBalance(int accNo) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return getBalance(accNo, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private double getBalance(int accNo, Connection conn) throws SQLException {
        String query = "SELECT balance FROM accounts WHERE account_no = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, accNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        }
        return 0.0;
    }
    
    // Check if account exists
    private boolean accountExists(int accNo) {
        String query = "SELECT account_no FROM accounts WHERE account_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, accNo);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // View all accounts
    public void viewAllAccounts() {
        String query = "SELECT * FROM accounts ORDER BY account_no";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n========== ALL ACCOUNTS ==========");
            boolean found = false;
            while (rs.next()) {
                found = true;
                Account acc = new Account(rs.getInt("account_no"), rs.getString("name"), rs.getDouble("balance"));
                System.out.println(acc);
            }
            if (!found) {
                System.out.println("No accounts found!");
            }
            System.out.println("==================================");

        } catch (SQLException e) {
            System.out.println("❌ Error fetching accounts!");
            e.printStackTrace();
        }
    }
    
    // ==================== GUI-SPECIFIC METHODS ====================
    
    // Get all accounts for GUI table
    public java.util.List<Account> getAllAccounts() {
        java.util.List<Account> accounts = new java.util.ArrayList<>();
        String query = "SELECT * FROM accounts ORDER BY account_no";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Account acc = new Account(rs.getInt("account_no"), 
                                         rs.getString("name"), 
                                         rs.getDouble("balance"));
                accounts.add(acc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
    
    // Create account for GUI - returns message
    public String createAccountGUI(String name, double initialDeposit) {
        if (initialDeposit < 0) {
            return "❌ Initial deposit cannot be negative!";
        }
        
        String query = "INSERT INTO accounts(name, balance) VALUES(?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setDouble(2, initialDeposit);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int accountNo = rs.getInt(1);
                return "✅ Account created successfully!\n\n" +
                       "📋 Account Number: " + accountNo + "\n" +
                       "👤 Name: " + name + "\n" +
                       "💰 Initial Balance: ₹" + String.format("%.2f", initialDeposit);
            }
        } catch (SQLException e) {
            return "❌ Error creating account: " + e.getMessage();
        }
        return "❌ Failed to create account!";
    }
    
    // Deposit for GUI - returns message
    public String depositGUI(int accNo, double amount) {
        if (!accountExists(accNo)) {
            return "❌ Account not found!";
        }
        if (amount <= 0) {
            return "❌ Amount must be positive!";
        }
        
        try {
            double currentBalance = getBalance(accNo);
            double newBalance = currentBalance + amount;
            
            String query = "UPDATE accounts SET balance = ? WHERE account_no = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setDouble(1, newBalance);
                ps.setInt(2, accNo);
                ps.executeUpdate();
                return "✅ Deposit successful!\n\n" +
                       "💰 Amount Deposited: ₹" + String.format("%.2f", amount) + "\n" +
                       "📊 New Balance: ₹" + String.format("%.2f", newBalance);
            }
        } catch (Exception e) {
            return "❌ Deposit failed: " + e.getMessage();
        }
    }
    
    // Withdraw for GUI - returns message
    public String withdrawGUI(int accNo, double amount) {
        if (!accountExists(accNo)) {
            return "❌ Account not found!";
        }
        if (amount <= 0) {
            return "❌ Amount must be positive!";
        }
        
        try {
            double currentBalance = getBalance(accNo);
            if (currentBalance < amount) {
                return "❌ Insufficient balance!\n\n" +
                       "📊 Current Balance: ₹" + String.format("%.2f", currentBalance) + "\n" +
                       "💸 Requested Amount: ₹" + String.format("%.2f", amount);
            }
            
            double newBalance = currentBalance - amount;
            String query = "UPDATE accounts SET balance = ? WHERE account_no = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setDouble(1, newBalance);
                ps.setInt(2, accNo);
                ps.executeUpdate();
                return "✅ Withdrawal successful!\n\n" +
                       "💸 Amount Withdrawn: ₹" + String.format("%.2f", amount) + "\n" +
                       "📊 New Balance: ₹" + String.format("%.2f", newBalance);
            }
        } catch (Exception e) {
            return "❌ Withdrawal failed: " + e.getMessage();
        }
    }
    
    // Transfer for GUI - returns message
    public String transferGUI(int fromAcc, int toAcc, double amount) {
        if (!accountExists(fromAcc)) {
            return "❌ Source account not found!";
        }
        if (!accountExists(toAcc)) {
            return "❌ Destination account not found!";
        }
        if (amount <= 0) {
            return "❌ Amount must be positive!";
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            double fromBalance = getBalance(fromAcc, conn);
            if (fromBalance < amount) {
                conn.rollback();
                return "❌ Insufficient balance!\n\n" +
                       "📊 Current Balance: ₹" + String.format("%.2f", fromBalance) + "\n" +
                       "💸 Requested Amount: ₹" + String.format("%.2f", amount);
            }
            
            updateBalance(fromAcc, amount, false, conn);
            updateBalance(toAcc, amount, true, conn);
            conn.commit();
            
            return "✅ Transfer successful!\n\n" +
                   "🔄 From Account: " + fromAcc + "\n" +
                   "➡️ To Account: " + toAcc + "\n" +
                   "💰 Amount Transferred: ₹" + String.format("%.2f", amount);
        } catch (SQLException e) {
            return "❌ Transfer failed: " + e.getMessage();
        }
    }
    
    // Check balance for GUI - returns formatted string
    public String checkBalanceGUI(int accNo) {
        String query = "SELECT * FROM accounts WHERE account_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, accNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return "📋 Account Number: " + rs.getInt("account_no") + "\n" +
                       "👤 Name: " + rs.getString("name") + "\n" +
                       "💰 Balance: ₹" + String.format("%.2f", rs.getDouble("balance"));
            } else {
                return "❌ Account not found!";
            }
        } catch (SQLException e) {
            return "❌ Error: " + e.getMessage();
        }
    }
}
