package generator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.swing.Timer;

import dbconnector.dbConnection;
import gui.LogController;
import gui.SizeOfMazeController;
import main.Maze;
import main.MazeGridPanel;
import util.Cell;
import util.DisjointSets;
import util.Mygenerate;
import util.User;


// Slightly different as it loops through cells randomly and then each wall of the cell. Not through each wall randomly.
//Rather than keep a list of edges (walls), I've opted to make the algorithm node (vertices/cell/passage) based. 
//This means, rather than pick a random wall from a list, I pick a random cell and loop through each wall.
public class KruskalsGen {

	private final Stack<Cell> stack = new Stack<Cell>();
	private final DisjointSets disjointSet = new DisjointSets();
	private final List<Cell> grid;
	private Cell current;
	private ArrayList<Mygenerate> arr;
	public static ArrayList<Cell> log=new ArrayList<>();
	public static String currentlog="";
	private int j=0;
	public KruskalsGen(List<Cell> grid, MazeGridPanel panel,ArrayList<Mygenerate> arr) {	
		this.grid = grid;
		for(int i=0;i<arr.size();i++){
			
			panel.setCurrent(arr.get(i).getCell());
			panel.repaint();
		}
		//Maze.generated.setValue(1);
		Maze.generated = true;

	}
	public KruskalsGen(List<Cell> grid, MazeGridPanel panel,int flag) {	
		
		this.grid = grid;
		
		current = log.get(0);
		for (int i = 0; i < grid.size(); i++) {
			grid.get(i).setId(i);
			disjointSet.create_set(grid.get(i).getId());
		}
		
		stack.addAll(grid);
		final Timer timer = new Timer(Maze.speed, null);
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				for (Cell n : grid) {
					if(n!=null &&current!=null) {
				if (n.getX()==current.getX() && n.getY()==current.getY()) {
					
					n.setWalls(current.getWalls());
					j++;
					if(j==log.size()) {
					current = null;
					Maze.generated = true;
				//	Maze.generated.setValue(1);
					long endTime=System.currentTimeMillis();
					timer.stop();
					Maze.RetryGenerated=true;
					}
					else {
					current=log.get(j);
					}
				}
				}
				}
				panel.setCurrent(current);
				panel.repaint();
				timer.setDelay(Maze.speed);
			}
		});
		timer.start();
		
		Maze.generated = true;

	}
	public KruskalsGen(List<Cell> grid, MazeGridPanel panel) {
		this.grid = grid;
		current = grid.get(0);
		
		for (int i = 0; i < grid.size(); i++) {
			grid.get(i).setId(i);
			disjointSet.create_set(grid.get(i).getId());
		}
		
		stack.addAll(grid);
		
		final Timer timer = new Timer(Maze.speed, null);
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!grid.parallelStream().allMatch(c -> c.isVisited())) {
					carve();
				} else {
					current = null;
					Maze.generated = true;
					long endTime=System.currentTimeMillis();
					System.out.println("That took " + (endTime - Maze.startTime)/1000.00 + " seconds");
					//grid
					User.currentUser.setnumofgenerate(User.currentUser.getnumofgenerate()+1);
					Maze.currentID=String.valueOf(User.currentUser.getnumofgenerate());
					dbConnection.savegenerate(grid,SizeOfMazeController.wid,SizeOfMazeController.heig,"Kruskal",User.currentUser,MazeGridPanel.StartX,MazeGridPanel.StartY,MazeGridPanel.EndX,MazeGridPanel.EndY);
					dbConnection.savehistory(User.currentUser,"Kruskal",LocalDateTime.now(),(float)((endTime - Maze.startTime)/1000.00),1);
					timer.stop();
					
				}
				/*
				for(int i=0;i<n;i++){
					Cell row=new Cell(x,y);
					row.setWalls(0,1,0,1);
					panel.setCurrent(current);
					panel.repaint();
				}*/
				panel.setCurrent(current);
				panel.repaint();
				timer.setDelay(Maze.speed);
			}
		});
		timer.start();
	}
//this function checking if two cells is in different areas then remove the wall between them
	private void carve() {
		
		current = stack.pop();
		
		current.setVisited(true);
		
		List<Cell> neighs = current.getAllNeighbours(grid);
		log.add(current);	
		for (Cell n : neighs) {
			if (disjointSet.find_set(current.getId()) != disjointSet.find_set(n.getId())) {
				currentlog+="We are on Cell "+current.getX()+","+current.getY()+"and his neigberhood cell "+n.getX()+","+n.getY()+" ";
				Maze.log.append("We are on Cell "+current.getX()+","+current.getY()+"and his neigberhood cell "+n.getX()+","+n.getY()+"\n");
				current.removeWalls(n);
				log.add(n);
				disjointSet.union(current.getId(), n.getId());
			}
			currentlog+="\n";
		}
		
		Collections.shuffle(stack);
	}
}