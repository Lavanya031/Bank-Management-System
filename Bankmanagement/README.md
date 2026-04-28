# Bank Management System

A Java-based bank account management system with **GUI and Console interfaces**, powered by MySQL database.

## 🎨 Two Interface Modes

### 1. **GUI Mode (Recommended)** 
Modern graphical interface with:
- 📊 Real-time dashboard showing all accounts in a table
- 🎯 Easy-to-use forms for all operations
- 🖱️ Click-based navigation
- ✅ Visual feedback with dialog messages
- 🔄 Auto-refresh after transactions

### 2. **Console Mode**
Traditional command-line interface for terminal users.

## Features

- ✅ Create new bank accounts
- 💰 Deposit money
- 💸 Withdraw money
- 🔄 Transfer funds between accounts
- 📊 Check account balance
- 👥 View all accounts in a table (GUI) or list (Console)
- ✨ Input validation and error handling
- 🎨 Modern, user-friendly GUI

## Prerequisites

1. **Java JDK 8 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/

2. **MySQL Server**
   - Download from: https://dev.mysql.com/downloads/mysql/

3. **MySQL JDBC Driver** (Already included in `lib/mysql-connector-j-9.4.0.jar`)

## Database Setup

### Option 1: Using the SQL Script
1. Start MySQL Server
2. Run the setup script:
   ```bash
   mysql -u root -p < setup.sql
   ```

### Option 2: Manual Setup
1. Open MySQL command line or MySQL Workbench
2. Execute the following commands:
   ```sql
   CREATE DATABASE bankdb;
   USE bankdb;
   
   CREATE TABLE accounts (
       account_no INT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(100) NOT NULL,
       balance DOUBLE NOT NULL
   );
   ```

## Configuration

Update database credentials in `src/db/DatabaseConnection.java` if needed:
```java
private static final String USER = "root";     // Your MySQL username
private static final String PASSWORD = "Arun@2004";  // Your MySQL password
```

## How to Run

### 🎨 GUI Mode (Recommended)

#### Quick Start (Windows)
```bash
run-gui.bat
```

#### Manual Commands
```bash
# Compile
javac -cp "lib/mysql-connector-j-9.4.0.jar" -d out src/BankGUI.java src/Main.java src/db/DatabaseConnection.java src/model/Account.java src/service/BankService.java

# Run
java -cp "out;lib/mysql-connector-j-9.4.0.jar" BankGUI
```

### 💻 Console Mode

#### Quick Start (Windows)
```bash
run.bat
```

#### Manual Commands
```bash
# Compile
javac -cp "lib/mysql-connector-j-9.4.0.jar" -d out src/Main.java src/db/DatabaseConnection.java src/model/Account.java src/service/BankService.java

# Run
java -cp "out;lib/mysql-connector-j-9.4.0.jar" Main
```

**Note:** On Linux/Mac, use `:` instead of `;` in classpath:
```bash
java -cp "out;lib/mysql-connector-j-9.4.0.jar" BankGUI
```

## Usage

### Creating an Account
1. Select option `1`
2. Enter customer name (can include spaces)
3. Enter initial deposit amount
4. Note the generated **Account Number** for future transactions

### Deposit
1. Select option `2`
2. Enter account number
3. Enter deposit amount

### Withdraw
1. Select option `3`
2. Enter account number
3. Enter withdrawal amount

### Transfer
1. Select option `4`
2. Enter source account number
3. Enter destination account number
4. Enter transfer amount

### Check Balance
1. Select option `5`
2. Enter account number

### View All Accounts
1. Select option `6`
2. See list of all accounts with balances

## Project Structure

```
Bankmanagement/
├── lib/
│   └── mysql-connector-j-9.4.0.jar  # MySQL JDBC Driver
├── src/
│   ├── BankGUI.java                  # GUI application entry point (NEW!)
│   ├── Main.java                     # Console application entry point
│   ├── db/
│   │   └── DatabaseConnection.java   # Database connection handler
│   ├── model/
│   │   └── Account.java              # Account data model
│   └── service/
│       └── BankService.java          # Business logic layer
├── setup.sql                         # Database setup script
├── run-gui.bat                       # Quick launcher for GUI mode
├── run.bat                           # Quick launcher for Console mode
└── README.md                         # This file
```

## Error Handling

- ✅ Invalid input validation
- ✅ Account existence checks
- ✅ Insufficient balance detection
- ✅ Negative amount prevention
- ✅ Database connection error handling

## Troubleshooting

### Database Connection Failed
- Ensure MySQL server is running
- Verify database credentials in `DatabaseConnection.java`
- Check if `bankdb` database exists

### ClassNotFoundException
- Ensure `mysql-connector-j-9.4.0.jar` is in the `lib/` folder
- Verify the classpath includes the JDBC driver

### InputMismatchException
- Enter numbers for account numbers and amounts
- Enter text for names
- The application now handles this gracefully

## Author

Wipro Bank Management System   


javac -cp "lib/mysql-connector-j-9.4.0.jar" -d out src/BankGUI.java src/Main.java src/db/DatabaseConnection.java src/model/Account.java src/service/BankService.java

java -cp "out;lib/mysql-connector-j-9.4.0.jar" Main

java -cp "out;lib/mysql-connector-j-9.4.0.jar" BankGUI