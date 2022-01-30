package solver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.Timer;

import dbconnector.dbConnection;
import main.*;
import util.Cell;
import util.User;
//all distances from the start are filled in at the same time, except here each pixel remembers how far it is from the beginning.
//Once the end is reached, do another breadth first search starting from the end, however only allow pixels to be included which are one distance unit less than the current pixel. 
//The included pixels precisely mark all the shortest solutions, as blind alleys and non-shortest paths will jump in pixel distances or have them increase.
public class BFSSolve {
	
	private final Queue<Cell> queue = new LinkedList<Cell>();
	private Cell current;
	private List<Cell> grid;
	private long starttime;
	private long endTime;
	public BFSSolve(List<Cell> grid, MazeGridPanel panel) {
		starttime=System.currentTimeMillis();
		
		this.grid = grid;
		current = MazeGridPanel.StartCell;
		current.setDistance(0);
		queue.offer(current);
		final Timer timer = new Timer(100, null);
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!current.equals(MazeGridPanel.EndCell)) {
					flood();
				} else {
					drawPath();
					endTime=System.currentTimeMillis();
					System.out.println("That took " + (endTime -starttime)/1000.00 + " seconds");
					Maze.solved = true;
					dbConnection.savesolve(User.currentUser,User.currentUser.getnumofgenerate(),"BFS", (float)((endTime -starttime)/1000.00),LocalDateTime.now(),100);
					Maze.RetrySolved=true;
					timer.stop();
					
				}
				panel.setCurrent(current);
				panel.repaint();
				timer.setDelay(100);
				}
			
		});
		timer.start();
	}
	
	private void flood() {
		current.setDeadEnd(true);
		current = queue.poll();
		List<Cell> adjacentCells = current.getValidMoveNeighbours(this.grid);
		for (Cell c : adjacentCells) {
			if (c.getDistance() == -1) {
				c.setDistance(current.getDistance() + 1);
				c.setParent(current);
				queue.offer(c);
			}
		}
	}
	//this method fill the cell that considered the shortest path in the maze
	private void drawPath() {
		
		while (current != MazeGridPanel.StartCell && current!=null) {
			
			current.setPath(true);
			current = current.getParent();
		}
		
	}
}