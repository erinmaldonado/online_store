package com.example.login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnterCategories {
    private JTextField category1TextField;
    JPanel searchUsersPanel;
    private JTextField category2TextField;
    private JButton searchUsersButton;

    public EnterCategories(){

        searchUsersButton.addActionListener(e->{
            JFrame frame = new JFrame("Enter categories");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);
            frame.setLocationRelativeTo(null);


            UserDatabase userDatabase = new UserDatabase();
            List<UserItems> userItemsList;
            String categoryX = category1TextField.getText();
            String categoryY = category2TextField.getText();

            userItemsList = userDatabase.getUsersByTwoCategoriesSameDay(categoryX, categoryY);

            if (!userItemsList.isEmpty()) {
                String[] columnNames = {"Username", "Category", "Item", "Date"};
                Object[][] data = new Object[userItemsList.size()][4];

                for (int i = 0; i < userItemsList.size(); i++) {
                    UserItems useritems = userItemsList.get(i);
                    data[i][0] = useritems.getUsername();
                    data[i][1] = useritems.getCategoryName();
                    data[i][2] = useritems.getItemName();
                    data[i][3] = useritems.getDate();
                }

                DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // Make all cells non-editable
                    }
                };

                JTable table = new JTable(tableModel);
                JScrollPane scrollPane = new JScrollPane(table);
                frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
            } else {
                JOptionPane.showMessageDialog(null, "No users found in the categories.", "No users.", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.setVisible(true);
        });

    }
}
