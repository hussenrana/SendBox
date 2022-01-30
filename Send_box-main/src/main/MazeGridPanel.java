package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import generator.*;
import solver.BFSSolve;
import solver.DijkstraSolve;
//import solver.*;
import util.Cell;
import util.Mygenerate;

public class MazeGridPanel extends JPanel {

    private static final long serialVersionUID = 7237062514425122227L;
	private final List<Cell> grid = new ArrayList<Cell>();
	private List<Cell> currentCells = new ArrayList<Cell>();
	public static int StartX,StartY,EndX,EndY;
	public static Cell StartCell,EndCell;
	public static Cell StartCellRetry,EndCellRetry;
	public MazeGridPanel(int rows, int cols,ArrayList<Mygenerate> arr) {
		for(int i=0;i<arr.size();i++) {
				grid.add(arr.get(i).getCell());	
		}
	}
	public MazeGridPanel(int rows, int cols) {
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				grid.add(new Cell(x, y));
			}
		}
	}
	//Set start points and end points for the maze
	public void setPoints(int StartX,int StartY,int EndX,int EndY) {
		this.StartX=StartX;
		this.StartY=StartY;
		this.EndX=EndX;
		this.EndY=EndY;

	}
	@Override
	public Dimension getPreferredSize() {
		// +1 pixel on width and height so bottom and right borders can be drawn.
		return new Dimension(600 + 1, Maze.HEIGHT + 1);
	}
	public void generateHistory(String type,ArrayList<Mygenerate> arr) {
		if (type.equals("Kruskal")) {
	        
			new KruskalsGen(grid, this,arr);
		}
	}
	
	public void generate(int index) {
		// switch statement for gen method read from combobox in Maze.java
		switch (index) {

		case 0:
			new KruskalsGen(grid, this);
			break;
		case 1:
			new WilsonsGen(grid, this);
			break;
		case -1:
			new KruskalsGen(grid,this,-1);
			break;		
		
		}
	}
	
	//Start the solve algorithm
	public void solve(int index) {
		switch (index) {	
		case 0:
			new BFSSolve(grid, this);
			Maze.type="BFS";
			break;
		
		case 1:
			new DijkstraSolve(grid, this);
			Maze.type="Dijkstra";
			break;
		default:
			new DijkstraSolve(grid, this);
			Maze.type="Dijkstra";
			break;
		}
	}
	//Back to the first maze page
	public void resetSolution() {
		for (Cell c : grid) {
			c.setDeadEnd(false);
			c.setPath(false);
			c.setDistance(-1);
			c.setParent(null);
		}
		repaint();
	}
	
	public void setCurrent(Cell current) {
		if(currentCells.size() == 0) {
			currentCells.add(current);
		} else {
			currentCells.set(0, current);			
		}
	}
	
	public void setCurrentCells(List<Cell> currentCells) {
		this.currentCells = currentCells;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (Cell c : grid) {
			c.draw(g);
		}
		for (Cell c : currentCells) {
			if(c != null) c.displayAsColor(g, Color.ORANGE);
		}
		for(Cell c : grid) {
			if(c.getX()==StartX && c.getY()==StartY)
				StartCell=c;
			if(c.getX()==EndX && c.getY()==EndY)
				EndCell=c;
		}
		StartCell.displayAsColor(g, Color.GREEN); // start cell
		EndCell.displayAsColor(g, Color.YELLOW); // end or goal cell
	}
}