import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.util.PriorityQueue;
import java.awt.*;
import javax.swing.*;

public class Solver {
	private boolean possible;
	private SearchNode l;
	private int moves;
	private class SearchNode implements Comparable<SearchNode>{//implements comparable to compare nodes to find the best path 
		private Board board;
		private int moves;
		private int priority;
		private SearchNode prev;
		public SearchNode(Board board, int moves, SearchNode prev){//use nodes to create a linked list with links to previous node
			this.board = board;
			this.moves = moves;
			this.priority = board.manhattan() + moves;
			this.prev = prev;
		}
		public int compareTo(SearchNode that){//compare two nodes with priority, which is compromised of the manhattan moves of the board in that node + the amount of moves it already took
			if(this.priority>that.priority)
				return 1;
			if(this.priority<that.priority)
				return -1;
			return 0;
		}
		
	}
	
	  private void in(SearchNode node, PriorityQueue<SearchNode> pq) {//put on all the nodes that are neighbors to the initial board while not being the same as the orginal board
	        for (Board nextBoard: node.board.neighbors()) {//using iterator that was created in board 
	            if ((node.prev == null) || (!nextBoard.equals(node.prev.board))) {
	                SearchNode n1 = new SearchNode(nextBoard, node.moves + 1,node);
	                pq.add(n1);
	            }
	        }
	    }
    public Solver(Board initial){
    	if(initial ==null)
    		throw new java.lang.NullPointerException();
    	PriorityQueue<SearchNode> nodes = new PriorityQueue<SearchNode>();//Priority queue to fin the best path
    	PriorityQueue<SearchNode> nodest = new PriorityQueue<SearchNode>();
    	SearchNode start = new SearchNode(initial,0,null);
    	SearchNode startT = new SearchNode(initial.twin(),0, null);//if a board's twin finishes first, then the board is impossible 
    	nodes.add(start);
    	nodest.add(startT);
    	while(true){
    		SearchNode n1 = nodes.remove();//remove of priority queue removes the minimum value, which is determined by the compareTo in searchnode
    		SearchNode n2 = nodest.remove();
    		if(n1.board.isGoal()){//check if twin or board is goal
    			l = n1;
    			moves = l.moves;
    			possible = true;
    			break;
    		}
    		if(n2.board.isGoal()){
    			possible = false;
    			moves = -1;
    			break;
    		}
    		in(n1, nodes);//keep adding the neighbors 
    		in(n2,nodest);
    	}
    	
    }
    public boolean isSolvable(){
    	return possible;// is the initial board solvable?
    	
    }
    public int moves(){
    	return moves;
    }
    // min number of moves to solve initial board; -1 if unsolvable
    public Iterable<Board> solution(){
    	Stack<Board> stack = new Stack<Board>();
    	if(!possible){
    		 stack = null;;
    	}
    	else{
    		SearchNode f = l;
    		while(f!=null){
    			stack.push(f.board);//use the prev linking of the serachnode class
    			f = f.prev;
    		}
    	}
    	return stack;// sequence of boards in a shortest solution; null if unsolvable
    }
    public static void main(String[] args) {

        // create initial board from file
        try {
		  		JFrame frame = new JFrame("My Drawing");//making the pop-up happen
				ImageCanvas canvas = new ImageCanvas();           
				frame.add(canvas);
				canvas.setSize(600,400);
				frame.pack();
				frame.setVisible(true);
            System.out.print("Enter the file name with extension : ");//file should be entered same as Image in ImageCanvas, use text files with 8puzzles
            Scanner in = new Scanner(System.in);
            File file = new File(in.nextLine());
            in = new Scanner(file);//scanning file ints into array of a board
            int n = in.nextInt();
            int[][] blocks = new int[n][n];
            while(in.hasNextInt()){
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    blocks[i][j] = in.nextInt();
            }
            Board initial = new Board(blocks);

            // solve the puzzle
            Solver solver = new Solver(initial);

            // print solution in a user freindly format
            if (!solver.isSolvable())
                System.out.println("No solution possible");
            else {
                System.out.println("Minimum number of moves = " + solver.moves());
                for (Board board : solver.solution())
                    System.out.println(board);
            }
        }

       catch (Exception ex) {//exception catching 
            ex.printStackTrace();
        }
    }
}