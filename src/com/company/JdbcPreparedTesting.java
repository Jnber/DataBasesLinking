package com.company;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
public class JdbcPreparedTesting {
    String cs = "jdbc:mysql://localhost:3306/ho?useSSL=false";
    String user = "root";
    String password = "";
    String markers= "";
    String insert = "INSERT INTO `product sales` VALUES(?,?,?,?,?,?,?,?,?,?)";
    String query = "SELECT * FROM `product sales`";
    //String delete = "DELETE FROM `product sales` WHERE id NOT IN ("+markers+") and BranchOffice=?";
    String update = "update `product sales` set Date=? , Region=?, Product=?, Qty=?, Cost=?, Amt=?, Taxe=? , Total=?   WHERE ID=? and BranchOffice=?";
    ArrayList<Object[]> arraybo= new ArrayList<Object[]>();

    public int insertIntoDataBase (JSONObject obj) {
        try (Connection con= DriverManager.getConnection(cs , user, password);
             PreparedStatement pst = con.prepareStatement(insert);
             PreparedStatement pst1 = con.prepareStatement(update);
        ){
            HashMap<String,String> map = new ObjectMapper().readValue(String.valueOf(obj), new TypeReference<HashMap<String,String>>(){});
            if (!exists(Integer.parseInt(map.get("ID")),map.get("BranchOffice"), con)){
                System.out.println("adding a new row");
                pst.setInt(1,Integer.parseInt(map.get("ID")));
                pst.setString(2,map.get("BranchOffice"));
                pst.setDate(3, java.sql.Date.valueOf(map.get("Date")));
                pst.setString(4, map.get("Region"));
                pst.setString(5, map.get("Product"));
                pst.setInt(6, Integer.parseInt(map.get("Qty")));
                pst.setDouble(7, Double.parseDouble(map.get("Cost")));
                pst.setDouble(8, Double.parseDouble(map.get("Amt")));
                pst.setDouble(9, Double.parseDouble(map.get("Taxe")));
                pst.setDouble(10, Double.parseDouble(map.get("Total")));
                pst.executeUpdate();
                return 1;
            }
            else {
                if (!same(map, con)){
                    System.out.println("updating a row");
                    pst1.setDate(1, java.sql.Date.valueOf(map.get("Date")));
                    pst1.setString(2, map.get("Region"));
                    pst1.setString(3, map.get("Product"));
                    pst1.setInt(4, Integer.parseInt(map.get("Qty")));
                    pst1.setDouble(5, Double.parseDouble(map.get("Cost")));
                    pst1.setDouble(6, Double.parseDouble(map.get("Amt")));
                    pst1.setDouble(7, Double.parseDouble(map.get("Taxe")));
                    pst1.setDouble(8, Double.parseDouble(map.get("Total")));
                    pst1.setInt(9,Integer.parseInt(map.get("ID")));
                    pst1.setString(10,map.get("BranchOffice"));
                    pst1.executeUpdate();
                    return 2;
                }
            }

        }
        catch (SQLException  ex){
            Logger lgr= Logger.getLogger(JdbcPreparedTesting.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteFromDataBase (JSONObject ids) throws IOException {

        HashMap<String, ArrayList<String>> list= new ObjectMapper().readValue(String.valueOf(ids), new TypeReference<HashMap<String,ArrayList<String>>>(){});
        System.out.println(list);

        for (int i =1; i<list.get("ids").size(); i++){
            this.markers= this.markers +  "?,";
        }
        markers= markers.substring(0,markers.length()-1);
        String delete = "DELETE FROM `product sales` WHERE id NOT IN ("+markers+") and BranchOffice=?";

        try (Connection con= DriverManager.getConnection(cs , user, password);
             PreparedStatement pst = con.prepareStatement(delete);
        ){
            for (int i =1; i<list.get("ids").size(); i++){
                pst.setInt(i,Integer.parseInt(list.get("ids").get(i)));
            }
            pst.setString(list.get("ids").size(),list.get("ids").get(0));
            pst.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void getFromDataBase () {
        try (Connection con= DriverManager.getConnection(cs , user, password);
             PreparedStatement pstt = con.prepareStatement(query);
             ResultSet rs = pstt.executeQuery();
        ){
            while (rs.next()){
                Object[] data1= new Object[10];
                data1[0]=rs.getString(1);
                data1[1]=rs.getString(2);
                data1[2]=rs.getString(3);
                data1[3]=rs.getString(4);
                data1[4]=rs.getString(5);
                data1[5]=rs.getString(6);
                data1[6]=rs.getString(7);
                data1[7]=rs.getString(8);
                data1[8]=rs.getString(9);
                data1[9]=rs.getString(10);
                arraybo.add(data1);
            }

        }
        catch (SQLException  ex) {
            Logger lgr = Logger.getLogger(JdbcPreparedTesting.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public boolean exists(int id, String bo, Connection con) throws SQLException {
        String query = "select * from `product sales` where id=? and BranchOffice=?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, id);
        pst.setString(2,bo);
        ResultSet rs = pst.executeQuery();
        return rs.next();

    }
    public boolean same(HashMap<String,String> map, Connection con) throws SQLException {
        String query = "select * from `product sales` where id=? and BranchOffice=? and Date=? and Region=? and Product=? and Qty=? and Cost=? and Amt=? and Taxe=? and Total=?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,Integer.parseInt(map.get("ID")));
        pst.setString(2,map.get("BranchOffice"));
        pst.setDate(3, java.sql.Date.valueOf(map.get("Date")));
        pst.setString(4, map.get("Region"));
        pst.setString(5, map.get("Product"));
        pst.setInt(6, Integer.parseInt(map.get("Qty")));
        pst.setDouble(7, Double.parseDouble(map.get("Cost")));
        pst.setDouble(8, Double.parseDouble(map.get("Amt")));
        pst.setDouble(9, Double.parseDouble(map.get("Taxe")));
        pst.setDouble(10, Double.parseDouble(map.get("Total")));
        ResultSet rs= pst.executeQuery();
        return rs.next();
    }


    public Object[][] getArray(){
        Object[][] obj = arraybo.toArray(Object[][]::new);
        return  obj;
    }
}
