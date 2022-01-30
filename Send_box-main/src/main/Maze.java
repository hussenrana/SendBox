package main;

import java.awt.CardLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import dbconnector.dbConnection;
import generator.KruskalsGen;
import gui.LoginController;
import gui.NavigateController;
import gui.SizeOfMazeController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import util.Mygenerate;
import util.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Maze extends Application {

	public static int WIDTH = 800;
	public static int HEIGHT = 550; // best to keep these the same. variable is only created for readability.
	public static final int W = 13;
	public static int speed = 1;
	public static boolean  generated, solved,RetryGenerated,RetrySolved;
	public static long startTime;
	public static int flag = 0;
	private static final String[] GENERATION_METHODS = { "Kruskal's", "Wilson's" };
	private static final String[] SOLVING_METHODS = { "BFS", "Dijkstra's" };
	private Stage test;
	private int cols, rows;
	public static String[] args;
	public static String currentID;
	public static String type;
	public static JTextArea log;
	private int currentsolveindex=0;
	private NavigateController logpage;
	public static int StartX,StartY,EndX,EndY;
	public static int RetryAlgoIndex=-1;
	public static void main(String[] args) {
		Maze.args = args;
		LoginController login = new LoginController();
		launch(args);
	}
	//Set start points and end points for the maze
	public void setPoints(int StartX,int StartY,int EndX,int EndY) {
		this.StartX=StartX;
		this.StartY=StartY;
		this.EndX=EndX;
		this.EndY=EndY;

	}
	//Set number of rows and cols for the Maze
	public void setRowCol(int row, int col) {
		rows = row;
		cols = col;
	}
	//Start thread for first generate maze gui
	public void MySwing() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}
				createAndShowGUI();
			}
		});
	}
	//Create new thread for generate maze by gui
	public void MySwing2() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}
				createAndShowGUI2();
			}
		});
	}	
	//Create new thread for history gui
	public void MyHistroySwing(ArrayList<Mygenerate> arr) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}
				creatMyHistoryGUI(arr);
			}
		});
	}
	//create new thread for compare gui
	public void CompareySwing(ArrayList<Mygenerate> arr) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}
				createCompareGUI(arr);
			}
		});
	}
	//Create the History maze gui
	private void creatMyHistoryGUI(ArrayList<Mygenerate> arr) {
		JFrame frame = new JFrame("Home --> History --> Generate --> "+arr.get(0).getType());
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		frame.setContentPane(container);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MazeGridPanel grid = new MazeGridPanel(rows, cols, arr);
		grid.setLayout(new GridBagLayout());
		grid.setBackground(Color.BLACK);
		grid.setPoints(StartX, StartY, EndX, EndY);
		JPanel mazeBorder = new JPanel();
		final int BORDER_SIZE = 20;
		mazeBorder.setBounds(0, 0, 600 + BORDER_SIZE, HEIGHT + BORDER_SIZE);
		mazeBorder.setBackground(Color.BLACK);
		mazeBorder.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));	
		mazeBorder.add(grid);
		container.add(mazeBorder);
		CardLayout cardLayout = new CardLayout();
		JButton backbutton = new JButton("Back");
		JButton runButton = new JButton("Run");
		JButton solveButton = new JButton("Solve");
		JButton resetButton = new JButton("Reset");
		JButton solveAgainButton = new JButton("Solve Again");
		JButton SolveSecondAlg = new JButton("Solve Second Alg");
		JComboBox<String> genMethodsComboBox = new JComboBox<>(GENERATION_METHODS);
		JComboBox<String> solveMethodsComboBox = new JComboBox<>(SOLVING_METHODS);
		// may need to comment these out if running on small resolution!!!
		genMethodsComboBox.setMaximumRowCount(genMethodsComboBox.getModel().getSize());
		solveMethodsComboBox.setMaximumRowCount(solveMethodsComboBox.getModel().getSize());
		Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
		labels.put(1, new JLabel("Fast"));
		labels.put(40, new JLabel("Slow"));
		// Create the card panels.
		JPanel card1 = new JPanel();
		JPanel card2 = new JPanel();
		card1.setLayout(new GridBagLayout());
		card2.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		;
		c.insets = new Insets(5, 0, 5, 18);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.7;
		c.gridx = 0;
		c.gridy = 0;
		card1.add(genMethodsComboBox, c);
		card2.add(solveMethodsComboBox, c);
		c.gridheight = 2;
		c.weightx = 0.3;
		c.gridx = 1;
		c.gridy = 0;
		card1.add(runButton, c);
		card2.add(solveButton, c);
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;	
		JPanel card3 = new JPanel();
		card3.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		card3.add(solveAgainButton, c);
		c.gridx = 0;
		c.gridy = 1;
		card3.add(SolveSecondAlg, c);
		// Create the panel that contains the cards.
		JPanel cards = new JPanel(cardLayout);
		cards.setBorder(new EmptyBorder(0, 20, 0, 0));
		cards.setOpaque(false);
		cards.add(card1, "gen");
		cards.add(card2, "solve");
		cards.add(card3, "reset");
		container.add(cards);
		this.generated=true;
		solved = false;
		grid.generateHistory(arr.get(0).getType(), arr);
		cardLayout.next(cards);
		solveButton.addActionListener(event -> {
			if(generated ) {
				frame.setTitle(frame.getTitle()+" --> "+solveMethodsComboBox.getSelectedItem().toString());
				grid.solve(solveMethodsComboBox.getSelectedIndex());
				cardLayout.last(cards);
			} else {
				JOptionPane.showMessageDialog(frame, "Please wait until the maze has been generated.");
			}
		});
		
		solveAgainButton.addActionListener(event -> {
			if (solved ) {
				frame.setTitle("Home --> History --> Generate --> "+arr.get(0).getType());
				grid.resetSolution();
				cardLayout.show(cards, "solve");
			} else {
				JOptionPane.showMessageDialog(frame, "Please wait until the maze has been solved.");
			}
		});
		SolveSecondAlg.addActionListener(event -> {
			if (solved ) {
				if(SolveSecondAlg.getText().equals("Compare") && Maze.solved ) {
					ArrayList<Mygenerate> arr2=dbConnection.getdataofgenerateforcompare(User.currentUser,currentID);
					frame.hide();
    	            Maze maze=new Maze();
    	    		maze.setRowCol(arr2.get(0).getWidth(), arr2.get(0).getHeight());
    	    		maze.createCompareGUI(arr2);			
				}
				else {		
					if(Maze.type.equals("BFS")) {
						frame.setTitle(frame.getTitle()+" --> Dijkstra");
						Maze.solved=false;
						grid.resetSolution();
						cardLayout.show(cards, "solve");
						grid.solve(1);
					cardLayout.last(cards);
					solveButton.setVisible(false);
					resetButton.setVisible(false);
					solveAgainButton.setVisible(false);
					}
					else {
						frame.setTitle(frame.getTitle()+" --> BFS");
						Maze.solved=false;
						grid.resetSolution();
						cardLayout.show(cards, "solve");
						grid.solve(0);
						cardLayout.last(cards);
						solveButton.setVisible(false);
						resetButton.setVisible(false);
						solveAgainButton.setVisible(false);
					}
					SolveSecondAlg.setText("Compare");
				}
			} else {
				JOptionPane.showMessageDialog(frame, "Please wait until the maze has been solved and generated.");
			}
		});
		resetButton.addActionListener(event -> createAndShowGUI());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	//Create GUI for generated maze by log
	private void createAndShowGUI2() {
		JFrame frame = new JFrame("Log");
		JPanel mazeandlog = new JPanel();
		JPanel loglayout = new JPanel();
		loglayout.setLayout(new BoxLayout(loglayout, BoxLayout.Y_AXIS));
		mazeandlog.setLayout(new GridBagLayout());
		mazeandlog.setBackground(Color.BLACK);
		JPanel container = new JPanel();	    
		DefaultCaret caret = (DefaultCaret)log.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		frame.setContentPane(container);
		MazeGridPanel grid = new MazeGridPanel(rows, cols);
		grid.setBackground(Color.black);
		grid.setSize(200, 200);
		JPanel mazeBorder = new JPanel();
		final int BORDER_SIZE = 20;
		mazeBorder.setBackground(Color.BLACK);
		mazeBorder.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            "Are you sure you want to close this window?", "Close Window?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		            RetryGenerated=true;
		    		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        }else {
		        	frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
		        }
		    }
		});
		mazeBorder.add(grid);
		mazeandlog.add(mazeBorder);		
		container.add(mazeandlog);		
		grid.generate(-1);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	//Create GUI for first maze to generate
	private void createAndShowGUI() {
		JFrame frame = new JFrame("Home --> Size Of Maze --> Generate");
		frame.setResizable(false);
		JPanel mazeandlog = new JPanel();
		JPanel loglayout = new JPanel();
		loglayout.setLayout(new BoxLayout(loglayout, BoxLayout.Y_AXIS));
		mazeandlog.setLayout(new GridBagLayout());
		mazeandlog.setBackground(Color.BLACK);
		mazeandlog.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JPanel container = new JPanel();
		log=new JTextArea(20,30);
		log.setLineWrap(true);
		        JScrollPane scrollableTextArea = new JScrollPane(log);  
		        log.setText("");
		        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
		        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		Font font = new Font("Courier", Font.BOLD,13);
		log.setFont(font);
		log.setForeground(Color.WHITE);
		log.setBackground(Color.BLACK);
		log.setOpaque(true);
		DefaultCaret caret = (DefaultCaret)log.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		frame.setContentPane(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final int BORDER_SIZE = 20;
		MazeGridPanel grid = new MazeGridPanel(rows, cols);
		grid.setBackground(Color.BLACK);
		grid.setPoints(StartX, StartY, EndX, EndY);
		JSlider initialSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1, 1000, 1);
		Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
		labels.put(1, new JLabel("Fast"));
		labels.put(1000, new JLabel("Slow"));
		initialSpeedSlider.setLabelTable(labels);
		initialSpeedSlider.setInverted(true);
		initialSpeedSlider.setPaintLabels(true);
		JButton RetryBtn = new JButton("Retry");
		RetryBtn.setPreferredSize(new Dimension(100, 50));
		RetryBtn.setVisible(false);
		loglayout.add(scrollableTextArea);
		loglayout.add(initialSpeedSlider);
		loglayout.add(RetryBtn);
		mazeandlog.add(grid);
		mazeandlog.add(loglayout);
		container.add(mazeandlog);
		CardLayout cardLayout = new CardLayout();
		JButton runbutton = new JButton("Run");
		JButton backbutton = new JButton("Quit");
		JButton solveButton = new JButton("Solve");
		JButton resetButton = new JButton("Reset");
		JButton solveAgainButton = new JButton("Solve Again");
		JButton SolveSecondAlg = new JButton("Solve Second Alg");
		JComboBox<String> genMethodsComboBox = new JComboBox<>(GENERATION_METHODS);
		JComboBox<String> solveMethodsComboBox = new JComboBox<>(SOLVING_METHODS);
		initialSpeedSlider.setVisible(false);
		// may need to comment these out if running on small resolution!!!
		genMethodsComboBox.setMaximumRowCount(genMethodsComboBox.getModel().getSize());
		solveMethodsComboBox.setMaximumRowCount(solveMethodsComboBox.getModel().getSize());
		// Create the card panels.
		RetryGenerated=true;
		JPanel card1 = new JPanel();
		JPanel card2 = new JPanel();
		card1.setLayout(new GridBagLayout());
		card2.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		;
		c.insets = new Insets(5, 0, 5, 18);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.weightx = 0.3;
		c.gridx = 0;
		c.gridy = 0;
		card1.add(backbutton, c);

		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 0;
		card1.add(genMethodsComboBox, c);
		card2.add(solveMethodsComboBox, c);

		c.gridheight = 1;
		c.weightx = 0.3;
		c.gridx = 2;
		c.gridy = 0;
		card1.add(runbutton, c);
		card2.add(solveButton, c);

		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;

		JPanel card3 = new JPanel();
		card3.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		card3.add(solveAgainButton, c);
		c.gridx = 0;
		c.gridy = 1;
		card3.add(resetButton, c);
		c.gridx = 0;
		c.gridy = 2;
		card3.add(SolveSecondAlg, c);
		// Create the panel that contains the cards.
		JPanel cards = new JPanel(cardLayout);
		cards.setBorder(new EmptyBorder(0, 20, 0, 0));
		cards.setOpaque(false);
		cards.add(card1, "gen");
		cards.add(card2, "solve");
		cards.add(card3, "reset");
		container.add(cards);
		solveAgainButton.setText("Restart");
		RetrySolved=true;
		backbutton.addActionListener(event -> {
		frame.hide();
		if(flag==1) {
		Maze maze=new Maze();
		maze.setRowCol(SizeOfMazeController.wid, SizeOfMazeController.heig);
		maze.MySwing();
		flag=0;
		}
		});
		RetryBtn.addActionListener(event -> {
		if(RetrySolved) {
		RetryGenerated=false;
		Maze maze=new Maze();
		maze.setRowCol(this.rows, this.cols);
		maze.speed=initialSpeedSlider.getValue();
		maze.MySwing2();
		}
		else {
		JOptionPane.showMessageDialog(frame, "Please wait until the maze has been solved.");
		}
		});
		runbutton.addActionListener(event -> {
		KruskalsGen.log=new ArrayList<>();
		RetryBtn.setVisible(true);
		initialSpeedSlider.setVisible(true);
		flag=1;
		speed = initialSpeedSlider.getValue();
		generated = false;
		solved = false;
		grid.generate(genMethodsComboBox.getSelectedIndex());
		frame.setTitle("Home --> Size Of Maze --> Generate --> "+genMethodsComboBox.getSelectedItem().toString());
		c.gridheight = 2;
		c.weightx = 0.3;
		c.gridx = 0;
		c.gridy = 0;
		card2.add(backbutton, c);
		cardLayout.next(cards);
		startTime = System.currentTimeMillis();
		backbutton.setText("Back");
		});
		solveButton.addActionListener(event -> {
		if (generated && RetryGenerated) {
		frame.setTitle(frame.getTitle()+" --> "+ solveMethodsComboBox.getSelectedItem().toString());
		RetrySolved=false;
		grid.solve(solveMethodsComboBox.getSelectedIndex());
		cardLayout.last(cards);
		} else {
		JOptionPane.showMessageDialog(frame, "Please wait until the maze has been generated.");
		}
		});
		solveAgainButton.addActionListener(event -> {
		if (solved ) {
		dbConnection.DeleteSolve(User.currentUser,User.currentUser.getnumofgenerate(),Maze.type);
		frame.setTitle("Home --> Size Of Maze --> Generate --> "+genMethodsComboBox.getSelectedItem().toString());
		grid.resetSolution();
		cardLayout.show(cards, "solve");
		} else {
		JOptionPane.showMessageDialog(frame, "Please wait until the maze has been solved and generated.");
		}
		});
		SolveSecondAlg.addActionListener(event -> {
		if (solved) {
		if(SolveSecondAlg.getText().equals("Compare")) {
		ArrayList<Mygenerate> arr=dbConnection.getdataofgenerateforcompare(User.currentUser,currentID);
		frame.hide();
		Maze maze=new Maze();
		maze.setRowCol(arr.get(0).getWidth(), arr.get(0).getHeight());
		maze.createCompareGUI(arr);
		}
		else{
		RetrySolved=false;
		if(RetryGenerated) {
		if(Maze.type.equals("BFS")) {
		frame.setTitle(frame.getTitle()+" --> Dijkstra");
		Maze.solved=false;
		grid.resetSolution();
		cardLayout.show(cards, "solve");
		grid.solve(1);
		cardLayout.last(cards);
		solveButton.setVisible(false);
		resetButton.setVisible(false);
		solveAgainButton.setVisible(false);
		}
		else {
		frame.setTitle(frame.getTitle()+" --> BFS");
		Maze.solved=false;
		grid.resetSolution();
		cardLayout.show(cards, "solve");
		grid.solve(0);
		cardLayout.last(cards);
		solveButton.setVisible(false);
		resetButton.setVisible(false);
		solveAgainButton.setVisible(false);
		}
		SolveSecondAlg.setText("Compare");
		}else {
		JOptionPane.showMessageDialog(frame, "Please wait until the maze has been solved and generated.");
		}
		}
		} else {
		JOptionPane.showMessageDialog(frame, "Please wait until the maze has been solved and generated.");
		}
		});
		resetButton.addActionListener(event -> {
		frame.hide();
		flag=0;
		Maze.generated=false;
		createAndShowGUI();
		});
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	//Start the Log in page
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("m");
		if (flag == 1) {
			try {
				// TODO Auto-generated method stubtry
				Parent root = FXMLLoader.load(getClass().getResource("/gui/SizeOfMaze.fxml"));
				Scene scene = new Scene(root);
				NavigateController.primaryStage.setScene(scene);
				NavigateController.primaryStage.setResizable(false);
				NavigateController.primaryStage.setTitle("Send-Box");
				NavigateController.primaryStage.show();
			} catch (SocketException e1) {
				System.out.println("Client Stopped!!");
			} catch (RuntimeException e2) {
				System.out.println("Client Stopped!!");
			}
		} else {
			try {
				this.test = primaryStage;
				// TODO Auto-generated method stubtry
				Parent root = FXMLLoader.load(getClass().getResource("/gui/login.fxml"));
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.setTitle("Send-Box");
				primaryStage.show();
			} catch (SocketException e1) {
				System.out.println("Client Stopped!!");
			} catch (RuntimeException e2) {
				System.out.println("Client Stopped!!");
			}
		}
	}
	//Create the compare gui
	private void createCompareGUI(ArrayList<Mygenerate> arr) {
		JFrame frame = new JFrame("Summary Table");
		JButton backbutton = new JButton("Lets Play Again");
		backbutton.setForeground(Color.decode("#2276D0"));
		backbutton.setBackground(Color.blue);
		backbutton.setOpaque(true);
		Font font3 = new Font("Lets Play Again", Font.BOLD,20);		
		backbutton.setFont(font3);
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		frame.setContentPane(container);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MazeGridPanel grid = new MazeGridPanel(rows, cols, arr);
		final int BORDER_SIZE = 20;

		grid.setBackground(Color.decode("#2276D0"));
		grid.setBounds(0, 0, 600 + BORDER_SIZE, HEIGHT + BORDER_SIZE);

		grid.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel mazeBorder = new JPanel();
		mazeBorder.setBounds(0, 0, 600 + BORDER_SIZE, HEIGHT + BORDER_SIZE);
		mazeBorder.setBackground(Color.decode("#2276D0"));
		mazeBorder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		//mazeBorder.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
		mazeBorder.add(grid);
		container.add(mazeBorder);

		CardLayout cardLayout = new CardLayout();	
		// Create the card panels.
		JPanel card1 = new JPanel();
		JPanel card2 = new JPanel();
		card1.setLayout(new GridBagLayout());
		card2.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();	
		c.insets = new Insets(5, 0, 5, 18);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.7;
		JLabel firstAlg=new JLabel();	
		JLabel SecondAlg=new JLabel();		
		ArrayList<String> arr2=dbConnection.GetSolve(User.currentUser);
		firstAlg.setText(arr2.get((arr2.size()-2)));
		SecondAlg.setText(arr2.get((arr2.size()-1)));	
		firstAlg.setHorizontalAlignment(SwingConstants.CENTER);
		firstAlg.setVerticalAlignment(SwingConstants.CENTER);
		Font font1 = new Font(arr2.get(0), Font.BOLD,20);
		firstAlg.setFont(font1);
		SecondAlg.setHorizontalAlignment(SwingConstants.CENTER);
		SecondAlg.setVerticalAlignment(SwingConstants.CENTER);
		Font font2 = new Font(arr2.get(1), Font.BOLD,20);
		SecondAlg.setFont(font2);
		JPanel card3 = new JPanel();
		card3.setLayout(new GridBagLayout());
		c.gridx = 1;
		c.gridy = 0;
		card3.add(backbutton, c);
		c.gridx = 0;
		c.gridy = 0;
		card3.add(firstAlg, c);
		firstAlg.setForeground(Color.decode("#2276D0"));
		c.gridx = 2;
		c.gridy = 0;
		card3.add(SecondAlg, c);
	    SecondAlg.setForeground(Color.decode("#2276D0"));
		// Create the panel that contains the cards.
		JPanel cards = new JPanel(cardLayout);
		cards.setBorder(new EmptyBorder(0, 20, 0, 0));
		cards.setOpaque(false);
		cards.add(card3, "reset");
		container.add(cards);	
		cards.setBackground(Color.decode("#2276D0"));
		generated = false;
		solved = false;
		grid.generateHistory(arr.get(0).getType(), arr);
		cardLayout.next(cards);
		backbutton.addActionListener(event -> {
			frame.hide();					
		});
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}