package gui;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import dbconnector.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Maze;
import util.History;
import util.Mygenerate;
import util.User;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class HistoryController implements Initializable{
	@FXML
	private TableView<History> historytable;
	@FXML private TableColumn<History, String> ID;
    @FXML private TableColumn<History, String> type;
    @FXML private TableColumn<History, Date> date;
    @FXML private TableColumn<History, Float> time;
    @FXML private TableColumn<History, Integer> speed;
	public static ArrayList<History> arr;
	//get the data of the generate maze to show it in the history page
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ID.setCellValueFactory(new PropertyValueFactory<History, String>("ID"));
    	type.setCellValueFactory(new PropertyValueFactory<History, String>("type"));
    	date.setCellValueFactory(new PropertyValueFactory<History, Date>("date"));
    	time.setCellValueFactory(new PropertyValueFactory<History, Float>("time"));
    	speed.setCellValueFactory(new PropertyValueFactory<History, Integer>("speed"));
    	historytable.getItems().setAll(arr);
    	

    	historytable.setRowFactory( tv -> {
    	    TableRow<History> row = new TableRow<>();
    	    row.setOnMouseClicked(event -> {
    	        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
    	            History rowData = row.getItem();
    	            ArrayList<Mygenerate> arr=dbConnection.getdataofgenerate(User.currentUser,rowData);
    	            Maze maze=new Maze();
    	            Maze.currentID=rowData.getID();
    	    		maze.setRowCol(arr.get(0).getWidth(), arr.get(0).getHeight());
    	    		maze.setPoints(arr.get(0).getStartX(), arr.get(0).getStartY(), arr.get(0).getEndX(), arr.get(0).getEndY());
    	    		maze.MyHistroySwing(arr);
    	   
    	        }
    	    });
    	    return row ;
    	});
    }
    	
	//move to login page
	public void MoveToLogin(ActionEvent event) throws Exception {
		NavigateController signout=new NavigateController();
		signout.openScene(event,"/gui/login.fxml","Send-Box");
	}
	//move to home page
	 public void MoveToHome(ActionEvent event) throws Exception {
			NavigateController back=new NavigateController();
			back.openScene(event,"/gui/Home.fxml","Send-Box --> Home");
		}
	
}
