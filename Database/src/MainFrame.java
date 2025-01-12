import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

class MainFrame extends JFrame {

    private static final int FRAME_WIDTH = 900;
    private static final int FRAME_HEIGHT = 700;
    private Connection connection;

    public MainFrame(Connection connection) {
        this.connection = connection;
s
        if (!showLoginDialog()) {
            JOptionPane.showMessageDialog(this, "Invalid login credentials. Exiting application.");
            closeConnection();
            System.exit(0);
        }

        setTitle("Police Station Management System");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Are you sure you want to exit?", "Exit Application", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    closeConnection();
                    System.exit(0);
                }
            }
        });

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel sidebar = SidebarPanel.create(connection);
        add(sidebar, BorderLayout.PAGE_END);

        JPanel mainPanel = new JPanel(new CardLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        SidebarPanel.setMainPanel(mainPanel);
        SidebarPanel.setConnection(connection);

        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    private boolean showLoginDialog() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2, 10, 10));
        loginPanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        loginPanel.add(usernameField);

        loginPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(this, loginPanel, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            return username.equals("jalal") && password.equals("123");
        }
        return false;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));

        JLabel titleLabel = new JLabel("Police Station Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(MainFrame.this,
                    "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Logged out successfully.");
                closeConnection();
                System.exit(0);
            }
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.setBackground(new Color(44, 62, 80));
        JLabel statusLabel = new JLabel("Connected to database", JLabel.LEFT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE);
        statusBar.add(statusLabel, BorderLayout.WEST);

        return statusBar;
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to close database connection.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.connect();
            System.out.println("Database connected successfully!");
            SwingUtilities.invokeLater(() -> new MainFrame(connection));
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection failed: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}