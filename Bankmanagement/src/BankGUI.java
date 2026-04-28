import service.BankService;
import model.Account;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BankGUI extends JFrame {
    private BankService bankService;
    private JTable accountsTable;
    private DefaultTableModel tableModel;
    private JPanel dashboardPanel, createPanel, depositPanel, withdrawPanel, transferPanel, balancePanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public BankGUI() {
        bankService = new BankService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Bank Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(1000, 80));
        JLabel titleLabel = new JLabel("🏦 BANK MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Sidebar with buttons
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Main content area with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create different panels
        dashboardPanel = createDashboardPanel();
        createPanel = createAccountPanel();
        depositPanel = createDepositPanel();
        withdrawPanel = createWithdrawPanel();
        transferPanel = createTransferPanel();
        balancePanel = createBalancePanel();

        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(createPanel, "CreateAccount");
        mainPanel.add(depositPanel, "Deposit");
        mainPanel.add(withdrawPanel, "Withdraw");
        mainPanel.add(transferPanel, "Transfer");
        mainPanel.add(balancePanel, "Balance");

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
        refreshDashboard();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(8, 1, 5, 10));
        sidebar.setBackground(new Color(52, 73, 94));
        sidebar.setPreferredSize(new Dimension(200, 600));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String[] buttonLabels = {"📊 Dashboard", "➕ Create Account", "💰 Deposit", 
                                "💸 Withdraw", "🔄 Transfer", "📋 Check Balance", "🔄 Refresh"};
        String[] cardNames = {"Dashboard", "CreateAccount", "Deposit", "Withdraw", "Transfer", "Balance", "Refresh"};

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton btn = createStyledButton(buttonLabels[i]);
            final String cardName = cardNames[i];
            btn.addActionListener(e -> {
                if (cardName.equals("Refresh")) {
                    refreshDashboard();
                } else {
                    cardLayout.show(mainPanel, cardName);
                }
            });
            sidebar.add(btn);
        }

        return sidebar;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 152, 219));
            }
        });
        
        return btn;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("All Accounts");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Account No", "Name", "Balance (₹)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        accountsTable = new JTable(tableModel);
        accountsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        accountsTable.setRowHeight(30);
        accountsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        accountsTable.getTableHeader().setBackground(new Color(52, 152, 219));
        accountsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(accountsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Customer Name:"), gbc);
        
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Initial Deposit (₹):"), gbc);
        
        JTextField depositField = new JTextField(20);
        depositField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(depositField, gbc);

        JButton createBtn = new JButton("Create Account");
        createBtn.setFont(new Font("Arial", Font.BOLD, 16));
        createBtn.setBackground(new Color(46, 204, 113));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(createBtn, gbc);

        createBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                double deposit = Double.parseDouble(depositField.getText().trim());
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter customer name!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String result = bankService.createAccountGUI(name, deposit);
                JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                nameField.setText("");
                depositField.setText("");
                refreshDashboard();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createDepositPanel() {
        return createTransactionPanel("Deposit Money", "💰 Deposit", new Color(46, 204, 113), true);
    }

    private JPanel createWithdrawPanel() {
        return createTransactionPanel("Withdraw Money", "💸 Withdraw", new Color(231, 76, 60), false);
    }

    private JPanel createTransactionPanel(String title, String buttonText, Color buttonColor, boolean isDeposit) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Account Number:"), gbc);
        
        JTextField accField = new JTextField(20);
        accField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(accField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Amount (₹):"), gbc);
        
        JTextField amountField = new JTextField(20);
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(amountField, gbc);

        JButton actionBtn = new JButton(buttonText);
        actionBtn.setFont(new Font("Arial", Font.BOLD, 16));
        actionBtn.setBackground(buttonColor);
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(actionBtn, gbc);

        actionBtn.addActionListener(e -> {
            try {
                int accNo = Integer.parseInt(accField.getText().trim());
                double amount = Double.parseDouble(amountField.getText().trim());
                
                String result;
                if (isDeposit) {
                    result = bankService.depositGUI(accNo, amount);
                } else {
                    result = bankService.withdrawGUI(accNo, amount);
                }
                
                JOptionPane.showMessageDialog(this, result, "Transaction Status", 
                    result.contains("✅") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                accField.setText("");
                amountField.setText("");
                refreshDashboard();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createTransferPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Transfer Money");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("From Account:"), gbc);
        JTextField fromField = new JTextField(20);
        fromField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(fromField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("To Account:"), gbc);
        JTextField toField = new JTextField(20);
        toField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(toField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Amount (₹):"), gbc);
        JTextField amountField = new JTextField(20);
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(amountField, gbc);

        JButton transferBtn = new JButton("🔄 Transfer");
        transferBtn.setFont(new Font("Arial", Font.BOLD, 16));
        transferBtn.setBackground(new Color(52, 152, 219));
        transferBtn.setForeground(Color.WHITE);
        transferBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(transferBtn, gbc);

        transferBtn.addActionListener(e -> {
            try {
                int from = Integer.parseInt(fromField.getText().trim());
                int to = Integer.parseInt(toField.getText().trim());
                double amount = Double.parseDouble(amountField.getText().trim());
                
                String result = bankService.transferGUI(from, to, amount);
                JOptionPane.showMessageDialog(this, result, "Transfer Status", 
                    result.contains("✅") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                fromField.setText("");
                toField.setText("");
                amountField.setText("");
                refreshDashboard();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createBalancePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Check Balance");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Account Number:"), gbc);
        
        JTextField accField = new JTextField(20);
        accField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(accField, gbc);

        JTextArea resultArea = new JTextArea(5, 30);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(new JScrollPane(resultArea), gbc);

        JButton checkBtn = new JButton("📋 Check Balance");
        checkBtn.setFont(new Font("Arial", Font.BOLD, 16));
        checkBtn.setBackground(new Color(155, 89, 182));
        checkBtn.setForeground(Color.WHITE);
        checkBtn.setFocusPainted(false);
        gbc.gridy = 3;
        panel.add(checkBtn, gbc);

        checkBtn.addActionListener(e -> {
            try {
                int accNo = Integer.parseInt(accField.getText().trim());
                String result = bankService.checkBalanceGUI(accNo);
                resultArea.setText(result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid account number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private void refreshDashboard() {
        tableModel.setRowCount(0);
        List<Account> accounts = bankService.getAllAccounts();
        for (Account acc : accounts) {
            tableModel.addRow(new Object[]{acc.getAccountNo(), acc.getName(), String.format("%.2f", acc.getBalance())});
        }
        cardLayout.show(mainPanel, "Dashboard");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankGUI());
    }
}
