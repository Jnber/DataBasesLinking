package com.company;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FrameHo extends JFrame implements ActionListener{
    private JdbcPreparedTesting HO;
    private String[] titles = {"ID","Branch Office","Date","Region", "Product", "Qty", "Cost", "Amt", "Taxe", "total"};
    private JTable table;
    private Object[][] data;
    private JScrollPane scrollpane;
    private JPanel panel;
    private DefaultTableModel model;
    private JButton button ;

    public FrameHo (JdbcPreparedTesting b) throws Exception {

        this.HO= b;
        this.HO.getFromDataBase();
        this.data= HO.getArray();

        this.setBounds(100,100,600,437);
        this.setTitle("HO");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(null);

        this.panel= new JPanel();
        this.panel.setBackground(new Color(176,196,222));
        this.panel.setBounds(0,0,600,410);
        this.getContentPane().add(panel);
        this.panel.setLayout(null);

        scrollpane = new JScrollPane();
        scrollpane.setBounds(20,15,550,339);
        panel.add(scrollpane);


        table= new JTable();
        model=new DefaultTableModel();
        model.setColumnIdentifiers(titles);
        for (Object[] obj:data) {
            model.addRow(obj);
        }

        table.setModel(model);
        scrollpane.setViewportView(table);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    public void addRowToTable(JSONObject ob) throws IOException {
        Object[] row = this.JSON_decode(ob);
        model.addRow(row);
    }

    public void updateTable(JSONObject ob) throws IOException {

        Object[] row = this.JSON_decode(ob);
        for (int i = 0; i<model.getRowCount(); i++){
            if(model.getValueAt(i,0).equals(row[0]) && model.getValueAt(i,1).equals(row[1]) ){
                for(int j=2; j<10; j++){ model.setValueAt(row[j],i,j); }
            }
        }
    }

    public void deleteFromTable(JSONObject ob) throws IOException {
        HashMap<String, ArrayList<String>> list= new ObjectMapper().readValue(String.valueOf(ob), new TypeReference<HashMap<String,ArrayList<String>>>(){});

        String database = list.get("ids").get(0);
        ArrayList<String> ids = new ArrayList<String>();

        for (int i =1; i<list.get("ids").size(); i++){
            ids.add(list.get("ids").get(i));
        }
        System.out.println("deleeting");
        for (int i = 0; i<model.getRowCount(); i++){
            System.out.println("rooww number"+i);
            if(model.getValueAt(i,1).equals(database) && notIn((String) model.getValueAt(i,0), ids)){
                model.removeRow(i);
            }
        }

    }

    public boolean notIn(String value, ArrayList<String> arr){
        for (String str:arr) {
            if(str.equals(value)){
                return false;
            }
        }
        return true;
    }
    public Object[] JSON_decode(JSONObject obj) throws IOException {
        HashMap<String,String> map = new ObjectMapper().readValue(String.valueOf(obj), new TypeReference<HashMap<String,String>>(){});
        Object[] row = new Object[10];
        row[0] = map.get("ID");
        row[1] = map.get("BranchOffice");
        row[2] = map.get("Date");
        row[3] = map.get("Region");
        row[4] = map.get("Product");
        row[5] = map.get("Qty");
        row[6] = map.get("Cost");
        row[7] = map.get("Amt");
        row[8] = map.get("Taxe");
        row[9] = map.get("Total");
        return row;
    }

}
