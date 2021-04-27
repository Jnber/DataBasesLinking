package com.company;

import javax.swing.*;
import java.awt.*;

public class FrameHo extends JFrame {
    private JdbcPreparedTesting HO;
    private String[] titles = {"Date","Region", "Product", "Qty", "Cost", "Amt", "Taxe", "total"};
    private JTable table;
    private Object[][] data;

    public FrameHo (JdbcPreparedTesting b) throws Exception {

        this.HO= b;
        this.HO.getFromDataBase();
        this.data= b.getArray();
        this.table= new JTable(data, titles);


        this.setSize(500, 300);
        this.setTitle("Sender");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().add(table.getTableHeader(),BorderLayout.NORTH);
        this.getContentPane().add(table);
        this.setVisible(true);
    }

}
