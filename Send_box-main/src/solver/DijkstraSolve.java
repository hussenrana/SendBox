package solver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.Timer;

import dbconnector.dbConnection;
import main.*;
import util.Cell;
import util.User;
//We first assign a distance-from-source value to all the nodes. Node s receives a 0 value because it is the source;
//the rest receive values of infinity to start.

public class DijkstraSolve {
	
	private final Queue<Cell> queue;
	private Cell current;
	private final List<Cell> grid;
	private long starttime;
	private long endTime;
	public DijkstraSolve(List<Cell> grid, MazeGridPanel panel) {
		starttime=System.currentTimeMillis();

		this.grid = grid;
		queue = new PriorityQueue<Cell>(new CellDistanceFromGoalComparator());
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
					dbConnection.savesolve(User.currentUser,User.currentUser.getnumofgenerate(),"Dijkstra", (float)((endTime -starttime)/1000.00),LocalDateTime.now(),100);
					Maze.solved = true;
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
		List<Cell> adjacentCells = current.getValidMoveNeighbours(grid);
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
		while (current != MazeGridPanel.StartCell  && current!=null) {
			current.setPath(true);
			current = current.getParent();
		}
	}
	//this method compare between two cells who is shortest path from currwnt cell

	private class CellDistanceFromGoalComparator implements Comparator<Cell> {
		Cell goal = MazeGridPanel.EndCell;
		
		@Override
		public int compare(Cell arg0, Cell arg1) {
			if (getDistanceFromGoal(arg0) > getDistanceFromGoal(arg1)) {
				return 1;
			} else {
				return getDistanceFromGoal(arg0) < getDistanceFromGoal(arg1) ? -1 : 0;
			}
		}
		
		private double getDistanceFromGoal(Cell c) {
			return Math.hypot(c.getX() - goal.getX(), c.getY() - goal.getY());
		}
	}
}