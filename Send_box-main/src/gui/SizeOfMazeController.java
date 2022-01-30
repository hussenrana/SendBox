package gui;


import java.net.SocketException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dbconnector.dbConnection;
import generator.KruskalsGen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Maze;
import util.History;
import util.Mygenerate;
import util.User;

public class SizeOfMazeController implements Runnable, Initializable{
	@FXML
	private Slider Width;	
	@FXML
	private Slider High;
	@FXML
	private Label widthlabel;	
	@FXML
	private Label highlabel;
	@FXML 
	private TextField StartX;
	@FXML 
	private TextField StartY;
	@FXML 
	private TextField EndX;
	@FXML 
	private TextField EndY;
	public static int wid,heig;
	//get the details of size of maze from the user
	  @Override
	    public void initialize(URL location, ResourceBundle resources) {
		  StartX.textProperty().addListener(new ChangeListener<String>() {
		        @Override
		        public void changed(
		                ObservableValue<? extends String> observable,
		                String oldValue, String newValue) {
		            if (!newValue.matches("\\d*")) {
		            	StartX.setText(newValue.replaceAll("[^\\d]", ""));
		            }
		        }
		    });
		  StartY.textProperty().addListener(new ChangeListener<String>() {
		        @Override
		        public void changed(
		                ObservableValue<? extends String> observable,
		                String oldValue, String newValue) {
		            if (!newValue.matches("\\d*")) {
		            	StartY.setText(newValue.replaceAll("[^\\d]", ""));
		            }
		        }
		    });
		  EndX.textProperty().addListener(new ChangeListener<String>() {
		        @Override
		        public void changed(
		                ObservableValue<? extends String> observable,
		                String oldValue, String newValue) {
		            if (!newValue.matches("\\d*")) {
		            	EndX.setText(newValue.replaceAll("[^\\d]", ""));
		            }
		        }
		    });
		  EndY.textProperty().addListener(new ChangeListener<String>() {
		        @Override
		        public void changed(
		                ObservableValue<? extends String> observable,
		                String oldValue, String newValue) {
		            if (!newValue.matches("\\d*")) {
		            	EndY.setText(newValue.replaceAll("[^\\d]", ""));
		            }
		        }
		    });
	    }
	    	
	public void BackToHome(ActionEvent event) throws Exception {	
		NavigateController Backbtn=new NavigateController();
		Backbtn.openScene(event,"/gui/Home.fxml","Send-Box --> Home");
	}
	//convert from string to integer
	public void WidthEvent()  {	
		widthlabel.setText(Integer.toString((int)Width.getValue()));
	}
	public void HighEvent()  {	
		highlabel.setText(Integer.toString((int)High.getValue()));
	}
	
	public void GoToswing(ActionEvent event) throws Exception {
		  Alert a = new Alert(AlertType.NONE);
		this.wid=(int)Width.getValue();
		this.heig=(int)High.getValue();
		if(StartX.getText().equals("")||StartY.getText().equals("")||EndX.getText().equals("")||EndY.getText().equals("")) {
			 a.setAlertType(AlertType.WARNING);
	   	  	  a.setContentText("Points must be 0<X<"+this.wid+" and 0<Y<"+this.heig);
	   	        // show the dialog
	   	        a.show();
		}
		else if(Integer.parseInt(StartX.getText())>=this.wid || Integer.parseInt(StartX.getText())<0|| Integer.parseInt(StartY.getText())>=this.heig||Integer.parseInt(StartY.getText())<0|| Integer.parseInt(EndX.getText())>=this.wid||Integer.parseInt(EndX.getText())<0|| Integer.parseInt(EndY.getText())>=this.heig||Integer.parseInt(EndX.getText())<0){
			 a.setAlertType(AlertType.WARNING);
   	  	  a.setContentText("Points must be 0<X<"+this.wid+" and 0<Y<"+this.heig);
   	        // show the dialog
   	        a.show();
		}
		else {
		Maze maze=new Maze();
		maze.flag=0;
		maze.setRowCol(this.wid, this.heig);
		maze.setPoints(Integer.parseInt(StartX.getText()), Integer.parseInt(StartY.getText()), Integer.parseInt(EndX.getText()), Integer.parseInt(EndY.getText()));
		maze.MySwing();	
		}
		
	}
	@Override
	public void run() {
		Platform.setImplicitExit( true );

		// TODO Auto-generated method stub
		Platform.runLater(new Runnable() {
			
	        @Override
	        public void run() {
	        	
	        	NavigateController.primaryStage.show();

	        }
	        
	   });
	}
	

	
	
}
