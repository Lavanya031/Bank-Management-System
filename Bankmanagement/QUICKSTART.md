# 🚀 Quick Start Guide

## Step 1: Setup MySQL Database

Open MySQL command line and run:
```sql
CREATE DATABASE bankdb;
USE bankdb;

CREATE TABLE accounts (
    account_no INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    balance DOUBLE NOT NULL
);
```

Or use the provided script:
```bash
mysql -u root -p < setup.sql
```

## Step 2: Configure Database Connection

Edit `src/db/DatabaseConnection.java` if your MySQL password is not "1234":
```java
private static final String PASSWORD = "your_password_here";
```

## Step 3: Run the Application

### GUI Mode (Best Experience) 🎨
```bash
run-gui.bat
```

### Console Mode 💻
```bash
run.bat
```

## GUI Features Overview

### 📊 Dashboard
- View all accounts in a real-time table
- Shows Account Number, Name, and Balance
- Auto-refreshes after each transaction

### ➕ Create Account
- Enter customer name (supports spaces)
- Enter initial deposit amount
- Get account number instantly

### 💰 Deposit
- Enter account number
- Enter deposit amount
- See new balance immediately

### 💸 Withdraw
- Enter account number
- Enter withdrawal amount
- Validates sufficient balance

### 🔄 Transfer
- Enter source account
- Enter destination account
- Enter transfer amount
- Both accounts validated before transfer

### 📋 Check Balance
- Enter account number
- View complete account details

### 🔄 Refresh
- Manually refresh the dashboard
- Updates account list from database

## Tips

✅ **Always note the Account Number** when creating an account - you'll need it for all transactions

✅ **Use the Dashboard** to see all account numbers and balances at a glance

✅ **Click Refresh** if you want to verify the latest data from the database

✅ **GUI validates all inputs** - you'll get clear error messages if something is wrong

## Keyboard Shortcuts in GUI

- Click sidebar buttons to navigate between screens
- Tab to move between input fields
- Enter to submit forms (when button is focused)

## Common Operations

### Check if account exists
1. Go to Dashboard (or click Refresh)
2. Look for the account number in the table

### Transfer money between accounts
1. Note both account numbers from Dashboard
2. Click "🔄 Transfer" in sidebar
3. Enter from/to accounts and amount
4. Dashboard auto-updates after successful transfer

### View transaction summary
After each operation, a popup shows:
- Success/error status
- Amount transacted
- New balance (where applicable)

## Troubleshooting

**GUI doesn't open?**
- Make sure you compiled with: `javac -cp "lib/mysql-connector-j-9.4.0.jar" -d out src/BankGUI.java ...`
- Check Java version: `java -version` (need JDK 8+)

**Database connection failed?**
- Ensure MySQL is running
- Verify password in `src/db/DatabaseConnection.java`
- Check if `bankdb` database exists

**"Account not found" error?**
- Check the account number in the Dashboard
- Account numbers are auto-generated starting from 1

Enjoy using the Bank Management System! 🏦
