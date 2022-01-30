package gui;

import java.io.IOException;
import java.util.ArrayList;

import dbconnector.dbConnection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.History;
import javafx.scene.Node;

public class HomeController {
	
	private AnchorPane lowerAnchorPane;
	private SplitPane splitpane;
	public HomeController() {
		
	}

	//move to login page
	public void MoveToLogin(ActionEvent event) throws Exception {
		NavigateController signout=new NavigateController();
		signout.openScene(event,"/gui/login.fxml","Send-Box");
	}
	//move to generate page
	public void Generate(ActionEvent event) throws Exception {
		NavigateController MvGenerate=new NavigateController();
		MvGenerate.openScene(event,"/gui/SizeOfMaze.fxml","Send-Box --> Home --> SizeOfMaze");
	}
	//move to history page
	public void MoveToHistory(ActionEvent event) throws Exception {
		HistoryController.arr=dbConnection.TakeHistory();
		HistoryController s=new HistoryController();
        
		NavigateController signout=new NavigateController();
		signout.openScene(event,"/gui/History.fxml","Send-Box --> Home --> History");
		
	}
	
}