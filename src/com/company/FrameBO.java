package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FrameBO extends JFrame implements ActionListener {

    private JdbcRetrieve BO;
    private String[] titles = {"Date","Region", "Product", "Qty", "Cost", "Amt", "Taxe", "total"};
    private JLabel labelRegion , labelProduct , labelQTY, labelAMT, labelCost, labelDate, labelTaxe, labelTotal;
    private JTable table;
    private JPanel panel ;
    private Object[][] data;
    private ArrayList<JTextField> textfields;
    private JScrollPane scrollpane;
    private JButton add, update, delete, synchronize;
    private DefaultTableModel model;


    public FrameBO (JdbcRetrieve b, int nb) throws Exception {

        this.BO= b;
        this.data= b.getArray();

        this.setBounds(100,100,900,437);
        this.setTitle("BO"+nb);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(null);

        this.panel= new JPanel();
        this.panel.setBackground(new Color(176,196,222));
        this.panel.setBounds(0,0,900,410);
        this.getContentPane().add(panel);
        this.panel.setLayout(null);




        this.labelDate= new JLabel("Date :");
        labelDate.setBounds(21,103,70,14);
        panel.add(labelDate);

        this.labelRegion= new JLabel("Region :");
        labelRegion.setBounds(21,123,70,14);
        panel.add(labelRegion);

        this.labelProduct= new JLabel("Product :");
        labelProduct.setBounds(21,143,70,14);
        panel.add(labelProduct);

        this.labelQTY= new JLabel("QTY :");
        labelQTY.setBounds(21,163,70,14);
        panel.add(labelQTY);

        this.labelCost= new JLabel("Cost :");
        labelCost.setBounds(21,183,70,14);
        panel.add(labelCost);

        this.labelAMT= new JLabel("AMT :");
        labelAMT.setBounds(21,203,70,14);
        panel.add(labelAMT);

        this.labelTaxe= new JLabel("Taxe :");
        labelTaxe.setBounds(21,223,70,14);
        panel.add(labelTaxe);

        this.labelTotal= new JLabel("Total :");
        labelTotal.setBounds(21,243,70,14);
        panel.add(labelTotal);

        this.textfields = new ArrayList<JTextField>();
        for (int i=0; i<8;i++){
            JTextField field= new JTextField();
            field.setBounds(100,i*20+101,132,16);
            field.setColumns(10);
            textfields.add(field);
            panel.add(field);
        }

        scrollpane = new JScrollPane();
        scrollpane.setBounds(280,48,600,339);
        panel.add(scrollpane);


        table= new JTable();
        //table.setBackground(C);
        model=new DefaultTableModel();
        model.setColumnIdentifiers(titles);
        for (Object[] obj:data
             ) {
            model.addRow(obj);
        }

        table.setModel(model);

        scrollpane.setViewportView(table);

        add= new JButton("Add");
        add.setBounds(21,280,90,23);
        add.addActionListener(this);
        panel.add(add);

        delete= new JButton("Delete");
        delete.setBounds(21,324,90,23);
        delete.addActionListener(this);
        panel.add(delete);

        update= new JButton("Update");
        update.setBounds(133,280,90,23);
        update.addActionListener(this);
        panel.add(update);

        synchronize= new JButton("Synch");
        synchronize.setBounds(133,324,90,23);
        synchronize.addActionListener(this);
        panel.add(synchronize);


        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==synchronize) {
            System.out.println("synchronizing");
            try {
                BO.sendData();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        else if (e.getSource()==add){
            Object[] row = new Object[8];
            int i =0;
            boolean empty= false;
            for (JTextField f:textfields) {
                if (f.getText().equals("")){
                    empty=true;
                }
                row[i]= f.getText();
                f.setText("");
                i++;
            }
            if (!empty) {
                model.addRow(row);
                BO.insertIntoDataBase(row);
            }
            else {
                JOptionPane.showMessageDialog(null, "Please fill all the fields");
            }
        }
        else if (e.getSource()==update){
            Object[] row = new Object[8];
            int i = table.getSelectedRow();
            int j=0;
            if (i>=0){
                for (JTextField f:textfields) {
                    if (!f.getText().equals("")){
                        model.setValueAt(f.getText(),i,j);
                        f.setText("");
                    }
                    System.out.println(model.getValueAt(i,j));
                    row[j]= model.getValueAt(i,j);
                    j++;

                }

                JOptionPane.showMessageDialog(null,"Updated Successfully");
                System.out.println(row[0]);
                BO.updateDataBase(row,i);
            }
            else {
                JOptionPane.showMessageDialog(null, "Please select a row to update");
            }
        }
        else if (e.getSource()==delete){
            int i = table.getSelectedRow();
            if (i>=0){
                model.removeRow(i);
                JOptionPane.showMessageDialog(null,"Deleted Successfully");
                BO.deleteFromDataBase(i);
            }
            else {
                JOptionPane.showMessageDialog(null, "Please select a row to delete");
            }
        }

    }
}
