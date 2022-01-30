package gui;

import java.io.IOException;
import java.net.SocketException;
import javafx.scene.Node;
import dbconnector.dbConnection;
import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import main.Maze;

public class LoginController extends Application{
@FXML
private TextField Username;
@FXML
private TextField Password;

public static boolean flag = false;
private AnchorPane lowerAnchorPane;
public static Stage primaryStage;
/**
 *
 * @param primaryStage
 * @param cc
 */
//In this function  display the sign in  page
public void start(Stage primaryStage) throws Exception {

    
	this.primaryStage=primaryStage;
	try {
		// TODO Auto-generated method stubtry
		Parent root = FXMLLoader.load(getClass().getResource("/gui/login.fxml"));
		Scene scene = new Scene(root);
		this.primaryStage.setScene(scene);
		this.primaryStage.setResizable(false);
		this.primaryStage.setTitle("Send-Box");
		this.primaryStage.show();

	} catch (SocketException e1) {
		System.out.println("Client Stopped!!");
	} catch (RuntimeException e2) {
		System.out.println("Client Stopped!!");
	}
}
//In this function we Enter the system and display the Home page
public void MoveToHome(ActionEvent event) throws Exception {
	  Alert a = new Alert(AlertType.NONE);
	String username = Username.getText();
    String password = Password.getText();
    boolean result=dbConnection.validatePassword(username, password);
      if(result==true) {
      NavigateController Navigate= new NavigateController();

	  Navigate.openScene(event,"/gui/Home.fxml","Send-Box --> Home");
      }
      else {
     
    	  	  a.setAlertType(AlertType.WARNING);
    	  	  a.setContentText("Incorrect username or password");
    	        // show the dialog
    	        a.show();
    	    
      }
}
//In this function display the sign up page
public void MoveToSignUp(ActionEvent event) throws Exception {
	
	NavigateController Navigate= new NavigateController();
	
	Navigate.openScene(event,"/gui/Sign-up.fxml","Send-Box --> Sign-Up");
}



}

