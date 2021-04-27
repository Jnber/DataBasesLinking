package com.company;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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
    String sql = "INSERT INTO `product sales` VALUES(?,?,?,?,?,?,?,?)";
    String query = "SELECT * FROM `product sales`";
    String sqlQuery = "DELETE  FROM `product sales`";
    ArrayList<Object[]> arraybo= new ArrayList<Object[]>();

    public void insertIntoDataBase (JSONObject obj) {
        try (Connection con= DriverManager.getConnection(cs , user, password);
             PreparedStatement pst = con.prepareStatement(sql);
        ){
            HashMap<String,String> map = new ObjectMapper().readValue(String.valueOf(obj), new TypeReference<HashMap<String,String>>(){});
            System.out.println(map);
            pst.setDate(1, java.sql.Date.valueOf(map.get("Date")));
            pst.setString(2, map.get("Region"));
            pst.setString(3, map.get("Product"));
            pst.setInt(4, Integer.parseInt(map.get("Qty")));
            pst.setDouble(5, Double.parseDouble(map.get("Cost")));
            pst.setDouble(6, Double.parseDouble(map.get("Amt")));
            pst.setDouble(7, Double.parseDouble(map.get("Taxe")));
            pst.setDouble(8, Double.parseDouble(map.get("Total")));
            pst.executeUpdate();

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

    }


    public void DeleteFromDataBase () {
        try (Connection con= DriverManager.getConnection(cs , user, password);
             PreparedStatement pst1 = con.prepareStatement(sqlQuery);
        ){
            pst1.execute();
        }
        catch (SQLException  ex){
            Logger lgr= Logger.getLogger(JdbcPreparedTesting.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public void getFromDataBase () {
        try (Connection con= DriverManager.getConnection(cs , user, password);
             PreparedStatement pstt = con.prepareStatement(query);
             ResultSet rs = pstt.executeQuery();
        ){
            while (rs.next()){
                Object[] data1= new Object[8];
                data1[0]=rs.getString(1);
                data1[1]=rs.getString(2);
                data1[2]=rs.getString(3);
                data1[3]=rs.getString(4);
                data1[4]=rs.getString(5);
                data1[5]=rs.getString(6);
                data1[6]=rs.getString(7);
                data1[7]=rs.getString(8);
                arraybo.add(data1);
            }

        }
        catch (SQLException  ex) {
            Logger lgr = Logger.getLogger(JdbcPreparedTesting.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public Object[][] getArray(){
        Object[][] obj = arraybo.toArray(Object[][]::new);
        return  obj;
    }
}
