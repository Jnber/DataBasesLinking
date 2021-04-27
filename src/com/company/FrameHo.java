package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class FrameHo extends JFrame {
    private JdbcPreparedTesting HO;
    private String[] titles = {"ID","Branch Office","Date","Region", "Product", "Qty", "Cost", "Amt", "Taxe", "total"};
    private JTable table;
    private Object[][] data;
    private DefaultTableModel model;

    public FrameHo (JdbcPreparedTesting b) throws Exception {

        this.HO= b;
        this.HO.getFromDataBase();
        this.data= HO.getArray();
        this.model= new DefaultTableModel(data, titles);
        this.table= new JTable(model);


        this.setSize(500, 300);
        this.setTitle("Receiver");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().add(table.getTableHeader(),BorderLayout.NORTH);
        this.getContentPane().add(table);
        this.setVisible(true);
    }

    public void UpdateTable(){
        this.HO.getFromDataBase();
        this.data = HO.getArray();
        model= new DefaultTableModel(data, titles);
        table.setModel(model);
        model.fireTableChanged(null);


    }

}
