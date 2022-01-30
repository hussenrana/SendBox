
package dbconnector;

import java.sql.Connection;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

import main.Maze;
import util.Cell;
import util.History;
import util.Mygenerate;
import util.User;
public class dbConnection {

	public static final String url = "jdbc:mysql://brvx91lpqq1rdxt8bpus-mysql.services.clever-cloud.com:3306/brvx91lpqq1rdxt8bpus";
    public static final String user = "uuk83mzpnciukyzt";
    public static final String pass = "nF9NFGHXUDzBh8U5etPD";

    private static Connection conn;

    /*
    This Class is Responsible for DB Functions
     */
    public static Connection getConnection() {
        if(conn==null)
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(url +  "?user="+user+"&password="+pass+"&characterEncoding=utf8&useSSL=false&useUnicode=true");
            } catch ( Exception throwables) {
                throwables.printStackTrace();
            }
        return conn;
    }
    //In this schema we check if the username is exist and then we add the username to the system
    public static String sign_up(String username, String password, String firstname, String lastname, String email){
    	  String result = "Database Connection Successful\n";
    	  Statement st = null;
    	  String res;
          try {
              st = getConnection().createStatement();
              ResultSet rs = st.executeQuery("select distinct * from users WHERE UserName='"+username+"'");
              ResultSetMetaData rsmd = rs.getMetaData();
          if (rs.next()) {
              res = "Account is Already in our system";
          }
          else{
              st.executeUpdate("INSERT INTO users (UserName, FirstName, LastName, Email, Password,numofgenerate)  VALUES('"+username+"', '"+firstname+"','"+lastname+"','"+email+"','"+password+"','"+0+"')");

              res = "Account Registred Successfully, You can login now...";
          }
          st.close();

          return res;
          } catch (Exception throwables) {
              throwables.printStackTrace();
              return "something wrong";
          }
    }
//check if the details we entered is suitable to the saved details 
    public static boolean validatePassword(String username, String password){
        try {

            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery("select distinct * from users WHERE UserName='"+username+"' AND Password='"+password+"'");
      
            if (rs.next()) {
                    User usr = new User(rs.getString(1), rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5),rs.getInt(6));
                    User.currentUser = usr;
                  
                    return true;
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
  //save the details of the generated maze in the generate table
    public static boolean savegenerate(List<Cell> grid,int width,int height,String type, User currentuser,int StartX,int StartY,int EndX,int EndY){
    	  
    	  Statement st = null;
    	  String maze="";
			try {
              st = getConnection().createStatement();
              st.executeUpdate("UPDATE users SET numofgenerate ='"+ currentuser.getnumofgenerate()+ "' WHERE UserName='"+currentuser.getUsername()+"'");
              int[] walls=new int[width*height*4];
              for(int i=0;i<grid.size();i++) {
            	  walls[4*i]=((grid.get(i).getWalls()[0]==true) ? 1 : 0);
            	  walls[4*i+1]=((grid.get(i).getWalls()[1]==true) ? 1 : 0);
            	  walls[4*i+2]=((grid.get(i).getWalls()[2]==true) ? 1 : 0);
            	  walls[4*i+3]=((grid.get(i).getWalls()[3]==true) ? 1 : 0);
              }
              for(int i=0;i<walls.length;i++) 
            	  maze+=Integer.toString(walls[i]);
              

              st.executeUpdate("INSERT INTO generate (ID, UserName, AlgType, Width, High ,Maze,StartX,StartY,EndX,EndY)  VALUES('"+User.currentUser.getnumofgenerate()+"','"+User.currentUser.getUsername()+"','"+type+"','"+width+"','"+height+"','"+maze+"','"+StartX+"','"+StartY+"','"+EndX+"','"+EndY+"')");

        
              st.close();
              return true;
        
          } catch (Exception throwables) {
              throwables.printStackTrace();
              return false ;
          }
    	//return false;
    }
  //save the details of the solve algorithm in the solve table
    public static boolean savesolve(User currentuser,int IDGen,String type,float solvetime,LocalDateTime date,int speed) {

    	  Statement st = null;
    	 
    	 
  	  try {
            st = getConnection().createStatement();
           

          
            st.executeUpdate("INSERT INTO solve (UserName,IDGen, Type,Time, Date, Speed)   VALUES('"+User.currentUser.getUsername()+"', '"+IDGen+"','"+type+"','"+solvetime+"','"+date+"','"+speed+"')");
            
            st.close();
            return true;
      
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return false ;
        }
      }
    //when the user click reset button ,this schema deleted the solve algorithm
    public static boolean DeleteSolve(User currentuser,int IDGen,String type) {

  	  Statement st = null;
  	 
  	 
	  try {
          st = getConnection().createStatement();
          

          st.executeUpdate("DELETE FROM solve WHERE UserName='"+currentuser.getUsername()+"' AND IDGen='"+IDGen+"' AND Type='"+type+"'");
          
          st.close();
          return true;
    
      } catch (Exception throwables) {
          throwables.printStackTrace();
          return false ;
      }
    }
    //get the details from the solve table to show it in the compare page
    public static ArrayList<String> GetSolve(User currentuser) {
    	ArrayList<String> arr=new ArrayList<>();
        try {

            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery("select * from solve WHERE IDGen='"+User.currentUser.getnumofgenerate()+"'");
            
            while (rs.next()) {                                      
                    arr.add("<html>"+"Type: "+rs.getString(3)+"<br/>"+"Time: "+rs.getString(4)+" Second"+"<br/>"+"Date: "+rs.getString(5)+"</html>");                    
            
            }
            st.close();
            return arr;            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //save the details of the generated maze in the history table
    public static boolean savehistory(User currentuser,String type,LocalDateTime date,float gentime,int speed) {

  	  Statement st = null;
  	 
  	 
	  try {
          st = getConnection().createStatement();
         

        
          st.executeUpdate("INSERT INTO history (ID,UserName, Type, Date, Time, Speed)   VALUES('"+User.currentUser.getnumofgenerate()+"', '"+User.currentUser.getUsername()+"','"+type+"','"+date+"','"+gentime+"','"+speed+"')");
          
          st.close();
          return true;
    
      } catch (Exception throwables) {
          throwables.printStackTrace();
          return false ;
      }
    }
    //fill the history table with the mazes that has been generated
    public static ArrayList<History> TakeHistory(){
    	ArrayList<History> arr=new ArrayList<>();
        try {

            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery("select distinct * from history WHERE UserName='"+User.currentUser.getUsername()+"'");
      
            while (rs.next()) {
                    History s= new History(rs.getString(1), rs.getString(2),rs.getString(3), rs.getDate(4),rs.getTime(4), rs.getFloat(5),rs.getInt(6));
                    arr.add(s);               
            }
            st.close();
            return arr;            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }	
  //get the data of the generate maze to show it in the history page 
    public static  ArrayList<Mygenerate> getdataofgenerate(User current, History data){
    	ArrayList<Mygenerate> arr=new ArrayList<>();
        try {

            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery("select distinct * from generate WHERE UserName='"+User.currentUser.getUsername()+"' AND ID='"+Integer.parseInt(data.getID())+"'");
            if (rs.next()) {
            	String maze=rs.getString(6);
        		int row=-1;
        		int col=0;
            	for(int i=0;i<maze.length();i+=4) {
        
                    boolean[] walls=new boolean[4];
                    walls[0]=((maze.charAt(i)=='1') ? true : false);
                    walls[1]=((maze.charAt(i+1)=='1') ? true : false);
                    walls[2]=((maze.charAt(i+2)=='1') ? true : false);
                    walls[3]=((maze.charAt(i+3)=='1') ? true : false);
                   
                    if(row==rs.getInt(5)-1) {
                    	row=0;
                    	col=col+1;
                    }
                    else {
                    	row=row+1;                  	
                    }
                   
                    Cell cell=new Cell(col,row);
                    cell.setWalls(walls);
                    Mygenerate s= new Mygenerate(rs.getInt(1), rs.getString(3),rs.getString(2), rs.getInt(4),rs.getInt(5), cell,rs.getInt(7),rs.getInt(8),rs.getInt(9),rs.getInt(10));
                    arr.add(s); 
            	}
            }
            st.close();
            return arr;            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    	
    }
    //get the data of the generate maze to show it in the compare page 
    public static  ArrayList<Mygenerate> getdataofgenerateforcompare(User current, String ID){
    	ArrayList<Mygenerate> arr=new ArrayList<>();
        try {

            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery("select distinct * from generate WHERE UserName='"+User.currentUser.getUsername()+"' AND ID='"+ID+"'");
            if (rs.next()) {
            	String maze=rs.getString(6);
        		int row=-1;
        		int col=0;
            	for(int i=0;i<maze.length();i+=4) {
        
                    boolean[] walls=new boolean[4];
                    walls[0]=((maze.charAt(i)=='1') ? true : false);
                    walls[1]=((maze.charAt(i+1)=='1') ? true : false);
                    walls[2]=((maze.charAt(i+2)=='1') ? true : false);
                    walls[3]=((maze.charAt(i+3)=='1') ? true : false);
                   
                    if(row==rs.getInt(5)-1) {
                    	row=0;
                    	col=col+1;
                    }
                    else {
                    	row=row+1;                  	
                    }
                   
                    Cell cell=new Cell(col,row);
                    cell.setWalls(walls);
                    Mygenerate s= new Mygenerate(rs.getInt(1), rs.getString(3),rs.getString(2), rs.getInt(4),rs.getInt(5), cell,rs.getInt(7),rs.getInt(8),rs.getInt(9),rs.getInt(10));
                    arr.add(s); 
            	}
            }
            st.close();
            return arr;            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    	
    }
}