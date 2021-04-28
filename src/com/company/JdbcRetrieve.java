package com.company;
import org.json.JSONArray;
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
    String insert = "INSERT INTO `product sales` (`Date`,`Region`, `Product`, `Qty`, `Cost`, `Amt`, `Taxe`,`total`) VALUES(?,?,?,?,?,?,?,?)";
    String delete = "DELETE from `product sales` where ID= (SELECT ID FROM (SELECT * FROM `product sales` LIMIT ?,1) as t)";
    String update = "update `product sales` set Date=? , Region=?, Product=?, Qty=?, Cost=?, Amt=?, Taxe=? , Total=?   WHERE ID=(SELECT ID FROM (SELECT * FROM `product sales` LIMIT ?,1) as t)";
    Sender me = new Sender();
    ArrayList<Object[]> arraybo= new ArrayList<Object[]>();
    ArrayList<String> ids= new ArrayList<String>();
    private ArrayList<JSONObject> toSend = new ArrayList<>();

    public JdbcRetrieve(String name){
        this.database=name;
        this.url = "jdbc:mysql://localhost:3306/"+database+"?useSSL=false";
    }

    public void connection() {
        try (Connection con= DriverManager.getConnection(url , user, password);
            PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()){
            ids.add(database);
            while (rs.next()){
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("ID", rs.getString(1));
                ids.add(rs.getString(1));
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
                data1[0]=rs.getString(2);
                data1[1]=rs.getString(3);
                data1[2]=rs.getString(4);
                data1[3]=rs.getString(5);
                data1[4]=rs.getString(6);
                data1[5]=rs.getString(7);
                data1[6]=rs.getString(8);
                data1[7]=rs.getString(9);

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

    public void insertIntoDataBase(Object[] obj){
        try (Connection con= DriverManager.getConnection(url , user, password);
             PreparedStatement pst = con.prepareStatement(insert);
             ){

                pst.setDate(1, java.sql.Date.valueOf((String) obj[0]));
                pst.setString(2, (String) obj[1]);
                pst.setString(3, (String) obj[2]);
                pst.setInt(4, Integer.parseInt((String) obj[3]));
                pst.setDouble(5, Double.parseDouble((String) obj[4]));
                pst.setDouble(6, Double.parseDouble((String) obj[5]));
                pst.setDouble(7, Double.parseDouble((String) obj[6]));
                pst.setDouble(8, Double.parseDouble((String) obj[7]));
                pst.executeUpdate();


        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteFromDataBase(int i){
        try (Connection con= DriverManager.getConnection(url , user, password);
             PreparedStatement pst = con.prepareStatement(delete);
        ){
            pst.setInt(1,i);
            pst.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateDataBase(Object[] obj , int i){
        try (Connection con= DriverManager.getConnection(url , user, password);
             PreparedStatement pst = con.prepareStatement(update);
        ){
            pst.setDate(1, java.sql.Date.valueOf((String) obj[0]));
            pst.setString(2, (String) obj[1]);
            pst.setString(3, (String) obj[2]);
            pst.setInt(4, Integer.parseInt((String) obj[3]));
            pst.setDouble(5, Double.parseDouble((String) obj[4]));
            pst.setDouble(6, Double.parseDouble((String) obj[5]));
            pst.setDouble(7, Double.parseDouble((String) obj[6]));
            pst.setDouble(8, Double.parseDouble((String) obj[7]));
            pst.setInt(9,i);
            pst.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void sendData () throws Exception {
        for (JSONObject data:toSend) {
            me.send(data,true);
        }
        JSONArray arrayofids = new JSONArray(ids);
        JSONObject id = new JSONObject();
        me.send(id.put("ids",arrayofids),false);
    }

    public Object[][] getArray(){
        //Object[][] obj = arraybo.toArray(new Object[arraybo.size()][]);
        Object[][] obj = arraybo.toArray(Object[][]::new);
        return  obj;
    }

}
