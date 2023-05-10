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

            ItemsDatabase itemsDatabase = new ItemsDatabase();
            String categoryX = category1TextField.getText();
            String categoryY = category2TextField.getText();

            List<Item> items = itemsDatabase.getUsersByTwoCategoriesSameDay(categoryX, categoryY);

            JFrame searchResultFrame = new JFrame("Users who posted at least two items on the same day");
            searchResultFrame.setSize(800, 500);
            searchResultFrame.setLocationRelativeTo(null);

            String[] columnNames = {"Category", "ID", "Name", "Description", "Username", "Price", "Date Posted"};
            Object[][] data = new Object[items.size()][7];
            if (!items.isEmpty()) {
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);

                    data[i][0] = item.getCategoryName();
                    data[i][1] = item.getItemId();
                    data[i][2] = item.getItemName();
                    data[i][3] = item.getItemDescription();
                    data[i][4] = item.getUsername();
                    data[i][5] = "$" + item.getItemPrice();
                    data[i][6] = item.getDatePosted();
                }
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            searchResultFrame.getContentPane().add(scrollPane);
            searchResultFrame.setVisible(true);
        });

    }
}
