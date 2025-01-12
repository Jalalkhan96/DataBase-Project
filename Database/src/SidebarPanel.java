import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

class SidebarPanel {
    private static JPanel mainPanel;
    private static Connection connection;

    public static JPanel create(Connection dbConnection) {
        connection = dbConnection;

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1, 10, 10));
        sidebar.setBackground(new Color(34, 45, 50));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 400, 20, 400));

        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("Add", "Add new records to the database");
        buttons.put("Update", "Update existing records in the database");
        buttons.put("Delete", "Delete records from the database");
        buttons.put("View", "View data in the database");

        for (Map.Entry<String, String> entry : buttons.entrySet()) {
            JButton button = createStyledButton(entry.getKey(), entry.getValue());
            button.addActionListener(e -> CrudOperationPanel.show(mainPanel, connection, entry.getKey()));
            sidebar.add(button);
        }

        return sidebar;
    }

    public static void setMainPanel(JPanel panel) {
        mainPanel = panel;
    }

    public static void setConnection(Connection conn) {
        connection = conn;
    }

    private static JButton createStyledButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(new LineBorder(new Color(41, 128, 185), 2, true));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(41, 128, 185));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(52, 152, 219));
            }
        });

        return button;
    }
}