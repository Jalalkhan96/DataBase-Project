import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class CrudOperationPanel {

    private static final Map<String, String[]> TABLE_ATTRIBUTES = new HashMap<>() {{
        put("PoliceStation", new String[]{"StationID", "StationName", "StationAddress", "City", "StationPhone", "StationEmail"});
        put("PoliceIncharge", new String[]{"InchargeID", "InchargeName", "InchargeContact", "StationID"});
        put("Officer", new String[]{"OfficerID", "OfficerName", "OfficerRank", "OfficerContact", "DateOfJoining", "OfficerAddress", "OfficerCity"});
        put("OfficerAssignment", new String[]{"OfficerID", "StationID", "AssignedDate"});
        put("Criminal", new String[]{"CriminalID", "CriminalName", "CriminalDOB"});
        put("Crime", new String[]{"CrimeID", "CrimeDetails", "CrimeDate", "StationID", "ArrestingOfficerID", "CriminalID"});
        put("Cases", new String[]{"CaseID", "CaseTitle", "CaseType", "CaseDetails", "CaseStatus", "DateReported"});
        put("CaseAssignment", new String[]{"CaseID", "InvestigatingOfficerID"});
        put("FIR", new String[]{"FIRID", "ComplainantID", "FIRDetails", "FIRDate", "StationID"});
        put("Complainant", new String[]{"ComplainantID", "ComplainantName", "ComplainantContact"});
        put("Complaint", new String[]{"ComplaintID", "ComplaintTitle", "ComplaintDetails", "ComplainantID", "StationID"});
        put("Evidence", new String[]{"EvidenceID", "EvidenceType", "EvidenceDetails", "CaseID", "OfficerID"});
        put("StationOfficer", new String[]{"StationID", "OfficerID"});
        put("ArrestRecord", new String[]{"ArrestID", "CriminalID", "ArrestingOfficerID", "CrimeID", "ArrestDate"});
        put("CriminalCaseMapping", new String[]{"CaseID", "CriminalID"});
        put("CaseStationMapping", new String[]{"CaseID", "StationID"});
    }};


    public static void show(JPanel mainPanel, Connection connection, String operation) {
        mainPanel.removeAll();

        JPanel operationPanel = new JPanel(new BorderLayout());

        JPanel tableSelectorPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(20);
        JComboBox<String> tableSelector = new JComboBox<>();
        String[] tableNames = {"PoliceStation", "PoliceIncharge", "Officer", "OfficerAssignment", "Criminal", "Crime",
                "Cases", "CaseAssignment", "FIR", "Complainant", "Complaint", "Evidence",
                "StationOfficer", "ArrestRecord", "CriminalCaseMapping", "CaseStationMapping"};

        for (String tableName : tableNames) {
            tableSelector.addItem(tableName);
        }
        tableSelector.setSelectedItem("PoliceStation");

        tableSelectorPanel.add(searchField, BorderLayout.NORTH);
        tableSelectorPanel.add(tableSelector, BorderLayout.CENTER);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = searchField.getText().toLowerCase();
                tableSelector.removeAllItems();
                for (String tableName : tableNames) {
                    if (tableName.toLowerCase().contains(searchText)) {
                        tableSelector.addItem(tableName);
                    }
                }
            }
        });

        operationPanel.add(tableSelectorPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        operationPanel.add(contentPanel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel();

        if (!"view".equals(operation)) {
            formPanel.setLayout(new GridLayout(0, 2, 5, 5));
            updateFormFields("PoliceStation", formPanel);
            contentPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        }

        tableSelector.addActionListener(e -> {
            if (!"view".equals(operation)) {
                updateFormFields((String) tableSelector.getSelectedItem(), formPanel);
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(4, 5, 10, 10));

        JButton addButton = createActionButton("Add", "Add new record", connection, formPanel, tableSelector, operation);
        JButton resetButton = createActionButton("Reset", "Reset form fields", connection, formPanel, tableSelector, operation);
        JButton updateButton = new JButton("Update");
        updateButton.setBackground(Color.YELLOW);
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.RED);

        if ("view".equalsIgnoreCase(operation)) {
            contentPanel.removeAll();

            JPanel tablePanel = new JPanel(new BorderLayout());
            contentPanel.add(tablePanel, BorderLayout.NORTH);

            showTableData(tablePanel, connection, (String) tableSelector.getSelectedItem());

            buttonPanel.add(createQueryButton("Highest Rank", connection, "Highest Rank Query", "Get the highest rank officer"));
            buttonPanel.add(createQueryButton("Cases Solved by Officer", connection, "Cases Solved Query", "Get cases solved by an officer"));
            buttonPanel.add(createQueryButton("Officers Assigned to Station 1", connection, "Officers Assigned Query", "Show officers assigned to a station"));
            buttonPanel.add(createQueryButton("Officers with Rank inspector", connection, "Officers Rank Query", "Show officers with a specific rank"));
            buttonPanel.add(createQueryButton("Each Criminal Arrests count", connection, "Criminal Arrests Query", "Get arrest details for criminals"));
            buttonPanel.add(createQueryButton("Cases in specific Date Range", connection, "Date Range Case Query", "Show cases in a date range"));
            buttonPanel.add(createQueryButton("Officers assign to Multi Stations", connection, "Multiple Stations Query", "Find officers assigned to multiple stations"));
            buttonPanel.add(createQueryButton("Officer with Longest Service", connection, "Longest Service Query", "Find officer with longest service"));
            buttonPanel.add(createQueryButton("Crimes per Station", connection, "Crimes per Station Query", "Show crime count per station"));
            buttonPanel.add(createQueryButton("Cases Assigned to Officer", connection, "Cases Assigned to Officer", "Get cases assigned to a specific officer"));
            buttonPanel.add(createQueryButton("Union of Officers and Criminals", connection, "Officer and Criminal Union", "Union of officers and criminals"));
            buttonPanel.add(createQueryButton("Multiple Arrests by Officer", connection, "Multiple Arrests Query", "Find officers who made multiple arrests"));
            buttonPanel.add(createQueryButton("Solved Cases Details", connection, "Solved Case Query", "Get solved cases"));
            buttonPanel.add(createQueryButton("Top 5 Officers by Arrests", connection, "Top 5 Officers Query", "Top 5 officers by arrests"));
            buttonPanel.add(createQueryButton("Total Crimes and Cases per Station", connection, "Total Crimes and Cases Query", "Total crimes and cases per station"));
            buttonPanel.add(createQueryButton("Officers Assigned Multiple Times", connection, "Assigned Multiple Times Query", "Find officers assigned multiple times to different stations"));
            buttonPanel.add(createQueryButton("Complaints per Station", connection, "Complaints per Station Query", "Total complaints per station"));
            buttonPanel.add(createQueryButton("Criminals in Multiple Crimes", connection, "Multiple Crimes Query", "Criminals involved in multiple crimes"));
            buttonPanel.add(createQueryButton("Officers with No Assignments", connection, "No Assignment Query", "Find officers with no assignments"));
            buttonPanel.add(createQueryButton("Cases in Last 30 Days", connection, "Cases in Last 30 Days", "Get cases reported in the last 30 days"));

            tableSelector.addActionListener(e -> {
                showTableData(tablePanel, connection, (String) tableSelector.getSelectedItem());
            });

            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
            contentPanel.revalidate();
            contentPanel.repaint();

        }

        if ("update".equalsIgnoreCase(operation)) {
            buttonPanel.add(updateButton);
            buttonPanel.add(resetButton);
        } else if ("delete".equalsIgnoreCase(operation)) {
            buttonPanel.add(deleteButton);
            buttonPanel.add(resetButton);
        } else if ("add".equalsIgnoreCase(operation)) {
            buttonPanel.add(addButton);
            buttonPanel.add(resetButton);
        }

        if (!"view".equals(operation)) {
            operationPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        mainPanel.add(operationPanel);
        mainPanel.revalidate();
        mainPanel.repaint();

        updateButton.addActionListener(e -> {
            String selectedTable = (String) tableSelector.getSelectedItem();
            String primaryKeyColumn = TABLE_ATTRIBUTES.get(selectedTable)[0];

            Map<String, String> formData = new HashMap<>();
            for (int i = 0; i < formPanel.getComponentCount(); i += 2) {
                JLabel label = (JLabel) formPanel.getComponent(i);
                JTextField textField = (JTextField) formPanel.getComponent(i + 1);
                formData.put(label.getText().replace(": ", ""), textField.getText());
            }

            StringBuilder updateQuery = new StringBuilder("UPDATE ");
            updateQuery.append(selectedTable).append(" SET ");

            for (String attribute : TABLE_ATTRIBUTES.get(selectedTable)) {
                if (!attribute.equals(primaryKeyColumn)) {
                    updateQuery.append(attribute).append(" = ?, ");
                }
            }
            updateQuery.delete(updateQuery.length() - 2, updateQuery.length());
            updateQuery.append(" WHERE ").append(primaryKeyColumn).append(" = ?");

            try (PreparedStatement pstmt = connection.prepareStatement(updateQuery.toString())) {
                int index = 1;

                for (String attribute : TABLE_ATTRIBUTES.get(selectedTable)) {
                    if (!attribute.equals(primaryKeyColumn)) {
                        pstmt.setString(index++, formData.get(attribute));
                    }
                }

                pstmt.setString(index, formData.get(primaryKeyColumn));

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Record updated successfully.");

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error updating record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            String selectedTable = (String) tableSelector.getSelectedItem();
            String primaryKeyColumn = TABLE_ATTRIBUTES.get(selectedTable)[0];

            String primaryKeyValue = "";
            for (int i = 0; i < formPanel.getComponentCount(); i += 2) {
                JLabel label = (JLabel) formPanel.getComponent(i);
                if (label.getText().replace(": ", "").equals(primaryKeyColumn)) {
                    JTextField textField = (JTextField) formPanel.getComponent(i + 1);
                    primaryKeyValue = textField.getText();
                    break;
                }
            }

            if (primaryKeyValue.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Could not determine the primary key value for deletion.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            String deleteQuery = "DELETE FROM " + selectedTable + " WHERE " + primaryKeyColumn + " = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
                pstmt.setString(1, primaryKeyValue);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Record deleted successfully.");
                    resetFormFields(formPanel);
                } else {
                    JOptionPane.showMessageDialog(null, "No record found with the specified ID.", "Delete Failed", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error deleting record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static JButton createQueryButton(String text, Connection connection, String queryName, String tooltip) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);

        button.addActionListener(e -> executeQuery(connection, queryName));
        return button;
    }

    private static JButton createActionButton(String text, String tooltip, Connection connection, JPanel formPanel, JComboBox<String> tableSelector, String operationType) {
        JButton button = new JButton(text);
        button.setBackground(new Color(46, 204, 113));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);

        button.addActionListener(e -> {
            String tableName = (String) tableSelector.getSelectedItem();
            String[] attributes = TABLE_ATTRIBUTES.get(tableName);
            Map<String, String> formData = new HashMap<>();

            Component[] components = formPanel.getComponents();
            for (int i = 0; i < components.length; i++) {
                if (components[i] instanceof JLabel) {
                    JLabel label = (JLabel) components[i];
                    if (i + 1 < components.length && components[i + 1] instanceof JTextField) {
                        JTextField textField = (JTextField) components[i + 1];
                        formData.put(label.getText().replace(": ", ""), textField.getText());
                        i++;
                    }
                }
            }

            switch (text) {
                case "Add":
                    addRecord(connection, tableName, attributes, formData, formPanel);
                    break;
                case "Reset":
                    resetFormFields(formPanel);
                    break;
            }
        });
        return button;
    }

    private static void addRecord(Connection connection, String tableName, String[] attributes, Map<String, String> formData, JPanel formPanel) {
        StringBuilder query = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        StringBuilder values = new StringBuilder("VALUES (");

        for (String attribute : attributes) {
            query.append(attribute).append(", ");
            values.append("?, ");
        }

        query.setLength(query.length() - 2);
        values.setLength(values.length() - 2);

        query.append(") ").append(values).append(")");

        try (PreparedStatement pstmt = connection.prepareStatement(query.toString())) {
            int index = 1;
            for (String attribute : attributes) {
                pstmt.setString(index++, formData.get(attribute));
            }
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Record added successfully.");
            resetFormFields(formPanel);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding record: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void resetFormFields(JPanel formPanel) {
        Component[] components = formPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            }
        }
    }

    private static void executeQuery(Connection connection, String queryName) {
        String query = "";
        switch (queryName) {
            case "Highest Rank Query":
                query = "SELECT OfficerName, OfficerRank FROM Officer WHERE OfficerRank = (SELECT MAX(OfficerRank) FROM Officer)";
                break;
            case "Cases Solved Query":
                query = "SELECT OfficerName, COUNT(Crime.CrimeID) AS CasesSolved FROM Officer LEFT JOIN Crime ON Officer.OfficerID = Crime.ArrestingOfficerID GROUP BY OfficerName";
                break;
            case "Officers Assigned Query":
                query = "SELECT OfficerName FROM Officer JOIN OfficerAssignment ON Officer.OfficerID = OfficerAssignment.OfficerID WHERE OfficerAssignment.StationID = 1";
                break;
            case "Officers Rank Query":
                query = "SELECT OfficerName FROM Officer WHERE OfficerRank LIKE 'inspector'";
                break;
            case "Criminal Arrests Query":
                query = "SELECT CriminalName, COUNT(ArrestRecord.ArrestID) AS ArrestCount FROM Criminal LEFT JOIN ArrestRecord ON Criminal.CriminalID = ArrestRecord.CriminalID GROUP BY CriminalName";
                break;
            case "Date Range Case Query":
                query = "SELECT CaseTitle, CaseStatus FROM Cases WHERE DateReported BETWEEN '2023-01-01' AND '2023-12-31'";
                break;
            case "Multiple Stations Query":
                query = "SELECT OfficerName, COUNT(DISTINCT StationID) AS StationsAssigned FROM Officer JOIN StationOfficer ON Officer.OfficerID = StationOfficer.OfficerID GROUP BY OfficerName HAVING COUNT(DISTINCT StationID) > 1";
                break;
            case "Longest Service Query":
                query = "SELECT TOP 1 OfficerName, DateOfJoining FROM Officer ORDER BY DateOfJoining ASC";
                break;
            case "Crimes per Station Query":
                query = "SELECT StationName, COUNT(CrimeID) AS TotalCrimes FROM PoliceStation LEFT JOIN Crime ON PoliceStation.StationID = Crime.StationID GROUP BY StationName";
                break;
            case "Cases Assigned to Officer":
                query = "SELECT c.CaseTitle, c.CaseType FROM Cases c JOIN CaseAssignment ca ON c.CaseID = ca.CaseID WHERE ca.InvestigatingOfficerID = 2;";
                break;
            case "Officer and Criminal Union":
                query = "SELECT OfficerName, 'Officer' AS Source FROM Officer UNION SELECT CriminalName, 'Criminal' AS Source FROM Criminal";
                break;
            case "Multiple Arrests Query":
                query = "SELECT o.OfficerName, COUNT(a.ArrestID) AS ArrestCount FROM Officer o JOIN ArrestRecord a ON o.OfficerID = a.ArrestingOfficerID GROUP BY o.OfficerName HAVING COUNT(a.ArrestID) > 1";
                break;
            case "Solved Case Query":
                query = "SELECT c.CaseTitle, c.CaseStatus, o.OfficerName AS InvestigatingOfficer, cr.CriminalName AS InvolvedCriminal FROM Cases c JOIN CaseAssignment ca ON c.CaseID = ca.CaseID JOIN Officer o ON ca.InvestigatingOfficerID = o.OfficerID JOIN CriminalCaseMapping ccm ON c.CaseID = ccm.CaseID JOIN Criminal cr ON ccm.CriminalID = cr.CriminalID  WHERE c.CaseStatus = 'Solved' AND c.DateReported > '2012-01-01'";
                break;
            case "Top 5 Officers Query":
                query = "SELECT TOP 5 OfficerName, COUNT(ArrestRecord.ArrestID) AS ArrestCount FROM Officer LEFT JOIN ArrestRecord ON Officer.OfficerID = ArrestRecord.ArrestingOfficerID GROUP BY OfficerName ORDER BY ArrestCount DESC";
                break;

            case "Total Crimes and Cases Query":
                query = "SELECT ps.StationName, COUNT(DISTINCT c.CrimeID) AS TotalCrimes, COUNT(DISTINCT cs.CaseID) AS TotalCases FROM PoliceStation ps LEFT JOIN Crime c ON ps.StationID = c.StationID LEFT JOIN CriminalCaseMapping ccm ON c.CriminalID = ccm.CriminalID LEFT JOIN Cases cs ON ccm.CaseID = cs.CaseID GROUP BY ps.StationName;";
                break;
            case "Assigned Multiple Times Query":
                query = "SELECT OfficerName, StationID FROM Officer JOIN OfficerAssignment ON Officer.OfficerID = OfficerAssignment.OfficerID GROUP BY OfficerName, StationID HAVING COUNT(StationID) > 1";
                break;
            case "Complaints per Station Query":
                query = "SELECT PoliceStation.StationName, COUNT(Complaint.ComplaintID) AS TotalComplaints FROM PoliceStation LEFT JOIN Complaint ON PoliceStation.StationID = Complaint.StationID GROUP BY PoliceStation.StationName";
                break;
            case "Multiple Crimes Query":
                query = "SELECT CriminalName, COUNT(DISTINCT CrimeID) AS CrimesInvolved FROM Criminal LEFT JOIN Crime ON Criminal.CriminalID = Crime.CriminalID GROUP BY CriminalName HAVING COUNT(DISTINCT CrimeID) > 1";
                break;
            case "No Assignment Query":
                query = "SELECT OfficerName FROM Officer LEFT JOIN OfficerAssignment ON Officer.OfficerID = OfficerAssignment.OfficerID WHERE OfficerAssignment.StationID IS NULL";
                break;
            case "Cases in Last 30 Days":
                query = "SELECT CaseTitle, DateReported FROM Cases WHERE DateReported >= DATEADD(day, -30, GETDATE())";
                break;
            default:
                JOptionPane.showMessageDialog(null, "No such query found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (!query.isEmpty()) {
            executeSQLQuery(query, connection);
        }
    }

    private static void executeSQLQuery(String query, Connection connection) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                model.addRow(row);
            }

            JTable table = new JTable(model);
            JOptionPane.showMessageDialog(null, new JScrollPane(table), "Query Results", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error executing query: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void updateFormFields(String tableName, JPanel formPanel) {
        formPanel.removeAll();
        String[] attributes = TABLE_ATTRIBUTES.getOrDefault(tableName, new String[]{});

        formPanel.setLayout(new GridLayout(attributes.length, 2, 5, 5));

        for (String attribute : attributes) {
            JLabel label = new JLabel(attribute + ": ");
            JTextField textField = new JTextField();
            formPanel.add(label);
            formPanel.add(textField);
        }
        formPanel.revalidate();
        formPanel.repaint();
    }

    private static void showTableData(JPanel tablePanel, Connection connection, String tableName) {
        try {
            String query = "SELECT * FROM " + tableName;
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet resultSet = pstmt.executeQuery();

            DefaultTableModel tableModel = new DefaultTableModel();
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(row);
            }

            JTable table = new JTable(tableModel);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(table);

            tablePanel.removeAll();
            tablePanel.add(scrollPane, BorderLayout.CENTER);
            tablePanel.revalidate();
            tablePanel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}