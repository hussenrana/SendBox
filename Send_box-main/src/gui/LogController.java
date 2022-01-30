package gui;

import java.net.SocketException;

import generator.KruskalsGen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import main.Maze;
import util.History;

public class LogController extends Application {

	@FXML
	private TextArea log;
	@FXML
	private Button ret;

	
	public static LogController controller;
	//open log page
	public void start(Stage primaryStage) throws Exception {
		primaryStage = new Stage();
		Parent root;
		FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/gui/log.fxml"));
		root = FXMLLoader.load(getClass().getResource("/gui/log.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.resizableProperty().setValue(false);
		primaryStage.show();
		
	} 
	//this method set the text of step by step of the maze
	public void setlog(String newlog) {
		controller.log.setText(newlog);
	}
	public LogController getController() {
		return controller;
	}
}
