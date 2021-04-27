package com.company;
import org.json.JSONObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcRetrieve {
    String url;
    String database;
    String user = "root";
    String password = "";
    String query = "SELECT * FROM `product sales`";
    Sender me = new Sender();
    ArrayList<Object[]> arraybo= new ArrayList<Object[]>();
    private ArrayList<JSONObject> toSend = new ArrayList<>();

    public JdbcRetrieve(String name){
        this.database=name;
        this.url = "jdbc:mysql://localhost:3306/"+database+"?useSSL=false";
    }

    public void connection() {
        try (Connection con= DriverManager.getConnection(url , user, password);
            PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()){
            while (rs.next()){
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("ID", rs.getString(1));
                data.put("BranchOffice",this.database);
                data.put("Date", rs.getString(2));
                data.put("Region", rs.getString(3));
                data.put("Product", rs.getString(4));
                data.put("Qty", rs.getString(5));
                data.put("Cost", rs.getString(6));
                data.put("Amt", rs.getString(7));
                data.put("Taxe", rs.getString(8));
                data.put("Total", rs.getString(9));
                JSONObject obj = new JSONObject(data);
                toSend.add(obj);

                Object[] data1= new Object[9];
                data1[0]=rs.getString(1);
                data1[1]=rs.getString(2);
                data1[2]=rs.getString(3);
                data1[3]=rs.getString(4);
                data1[4]=rs.getString(5);
                data1[5]=rs.getString(6);
                data1[6]=rs.getString(7);
                data1[7]=rs.getString(8);
                data1[8]=rs.getString(9);

                arraybo.add(data1);
            }
        }
        catch (SQLException ex){
            Logger lgr= Logger.getLogger(JdbcRetrieve.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendData () throws Exception {
        for (JSONObject data:toSend
             ) {
            me.send(data);
        }
    }

    public Object[][] getArray(){
        //Object[][] obj = arraybo.toArray(new Object[arraybo.size()][]);
        Object[][] obj = arraybo.toArray(Object[][]::new);
        return  obj;
    }

}
