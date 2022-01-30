package gui;

import java.io.IOException;
import java.sql.Statement;

import dbconnector.dbConnection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SignUpController {
	@FXML
	private TextField Username;
	@FXML
	private PasswordField Password;
	@FXML
	private TextField FirstName;
	@FXML
	private TextField LastName;
	@FXML
	private TextField Email;
	
	private AnchorPane lowerAnchorPane;
	private SplitPane splitpane;
	public SignUpController() {
			
	}

	public void start(SplitPane splitpane) throws Exception {
		// TODO Auto-generated method stub
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Sign-up.fxml"));
			lowerAnchorPane = loader.load();
			splitpane.getItems().set(0, lowerAnchorPane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 //Save user registration details at DB
    public void registration(ActionEvent event) throws Exception{
  	  Alert a = new Alert(AlertType.NONE);
       String username = Username.getText();
       String password = Password.getText();
       String firstname = FirstName.getText();
       String lastname = LastName.getText();
       String email = Email.getText();
       if(username.equals("")) {
    	   a.setAlertType(AlertType.WARNING);
  	  	  a.setContentText("Please fill username field");
  	        // show the dialog
  	        a.show();      
  	   }else if(password.equals("")) {
  		 a.setAlertType(AlertType.WARNING);
 	  	  a.setContentText("Please fill password field");
 	        // show the dialog
 	        a.show();        
 	     }
       else {
       String result=dbConnection.sign_up(username,password,firstname,lastname,email);       
       if(result.equals("Account is Already in our system")) {
    	   a.setAlertType(AlertType.WARNING);
 	  	  a.setContentText("Account is Already in our system");
 	        // show the dialog
 	        a.show();
       }else if(result.equals("Account Registred Successfully, You can login now...")){
    	   a.setAlertType(AlertType.INFORMATION);
  	  	  a.setContentText("Account Registred Successfully, You can login now...");
  	        // show the dialog
  	        a.show();
       }          
       }
   }
    public void MoveToLogin(ActionEvent event) throws Exception {
		NavigateController back=new NavigateController();
		back.openScene(event,"/gui/login.fxml","Send-Box");
	}
}
