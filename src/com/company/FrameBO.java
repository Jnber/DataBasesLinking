package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameBO extends JFrame implements ActionListener {

    private JdbcRetrieve BO;
    private String[] titles = {"Date","Region", "Product", "Qty", "Cost", "Amt", "Taxe", "total"};
    private JLabel label;
    private JTable table;
    private JPanel panelButt ;
    private JButton button;
    private Object[][] data;

    public FrameBO (JdbcRetrieve b) throws Exception {

        this.BO= b;
        this.data= b.getArray();
        this.button= new JButton();
        this.table= new JTable(data, titles);

        this.button.setText("Synchronize");
        this.button.setBackground(Color.DARK_GRAY);
        this.button.setForeground(Color.lightGray);
        this.button.setVisible(true);
        this.button.addActionListener(this);

        this.panelButt = new JPanel();
        panelButt.setLayout(new GridLayout(1, 3,200,10));
        panelButt.add(button);


        this.setSize(500, 300);
        this.setTitle("Sender");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().add(table.getTableHeader(),BorderLayout.NORTH);
        this.getContentPane().add(table);
        this.add(panelButt, BorderLayout.SOUTH);
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==button) {
            try {
                BO.sendData();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

    }
}
