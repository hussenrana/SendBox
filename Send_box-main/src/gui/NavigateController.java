package gui;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class NavigateController {
	private AnchorPane lowerAnchorPane;
	private SplitPane splitpane;
	public static Stage primaryStage;
	public NavigateController() {
		
	}
	//this function change the path of shown pages
	public NavigateController(String path) {
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	primaryStage = new Stage();
        		Parent root;
        		try {
        			
        			root = FXMLLoader.load(getClass().getResource(path));
        			Scene scene = new Scene(root);
        			primaryStage.setScene(scene);
        			primaryStage.resizableProperty().setValue(false);
        			primaryStage.show();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		
            }
       });
		
			
	}
	//In this function display the Home page
	public void start(SplitPane splitpane) throws Exception {
		// TODO Auto-generated method stub
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Home.fxml"));
			lowerAnchorPane = loader.load();
			splitpane.getItems().set(0, lowerAnchorPane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  void openScene(ActionEvent event, String path,String title) throws IOException {
		
		if(path.equals("/gui/log.fxml")) {
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window

			LogController s=new LogController();
			try {
				s.start(primaryStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}else {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		primaryStage = new Stage();
		primaryStage.setTitle(title);
		Parent root;
		FXMLLoader loader1 = new FXMLLoader(getClass().getResource(path));
		root = FXMLLoader.load(getClass().getResource(path));		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest((WindowEvent we) -> {
			System.exit(1);
		});
		primaryStage.resizableProperty().setValue(false);
		primaryStage.show();
		}
		
		}
	
	public  Stage openScene(String path) throws IOException {
		primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(path));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.resizableProperty().setValue(false);
		return primaryStage;
		}

}
