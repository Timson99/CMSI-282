//package pathfinder.informed;

import java.util.ArrayList;
import java.util.*;



/**
 * Maze Pathfinding algorithm that implements a basic, uninformed, breadth-first tree search.
 */
public class Pathfinder {
    
    private static HashSet<MazeState> graveyard = new HashSet<>();
    
    
    /**
     * Given a MazeProblem, which specifies the actions and transitions available in the
     * search, returns a solution to the problem as a sequence of actions that leads from
     * the initial to a goal state.
     * 
     * @param problem A MazeProblem that specifies the maze, actions, transitions.
     * @return An ArrayList of Strings representing actions that lead from the initial to
     * the goal state, of the format: ["R", "R", "L", ...]
     */
    public static ArrayList<String> solve (MazeProblem problem) {
        // TODO: Initialize frontier -- what data structure should you use here for
        // breadth-first search? Recall: The frontier holds SearchTreeNodes!
        HashSet<MazeState> graveyard = new HashSet<>();
        PriorityQueue<SearchTreeNode> currentFrontier = new PriorityQueue<SearchTreeNode>(50, new Comparator<SearchTreeNode>() 
        {
            public int compare(SearchTreeNode node1, SearchTreeNode node2) {
                Integer one = new Integer(node1.aStarCost);
                Integer two = new Integer(node2.aStarCost);
                return one.compareTo(two); 
            }
        });
        
        
        // TODO: Add new SearchTreeNode representing the problem's initial state to the
        // frontier. Since this is the initial state, the node's action and parent will
        // be null
        boolean keyObtained = false;
        
        currentFrontier.add(new SearchTreeNode(problem.INITIAL_STATE, null, null, 0, manhattanH(keyObtained, problem.GOAL_STATE, problem.KEY_STATE, problem.INITIAL_STATE)));
        graveyard.add(problem.INITIAL_STATE);
        
        
        // TODO: Loop: as long as the frontier is not empty...
        while(currentFrontier.size() > 0) {
            PriorityQueue<SearchTreeNode> copy = new PriorityQueue<>(currentFrontier);
            System.out.println("\nFront of Queue:  ");
            while(copy.peek() != null) {
                SearchTreeNode value = copy.remove();
                System.out.print( "(" + value.aStarCost + ", (" + value.state.row + "," + value.state.col + ")) ");
            }
             System.out.println();
            
            // TODO: Get the next node to expand by the ordering of breadth-first search
            SearchTreeNode temp = currentFrontier.remove();
            
            if(!keyObtained)  {
                if(canCollectKey(temp.state, problem.KEY_STATE)) {
                    graveyard.clear();
                    currentFrontier.clear();
                    keyObtained = true;
                    temp.aStarCost = goalManhattanH(problem.GOAL_STATE, temp.state);
                    temp.history = 0; 
                }
            }
            
           
            
            
            
            // TODO: If that node's state is the goal (see problem's isGoal method),
            // you're done! Return the solution
            // [Hint] Use a helper method to collect the solution from the current node!
            if(problem.isGoal(temp.state) && keyObtained) {
                return getSolution(temp, problem);
            }
            // TODO: Otherwise, must generate children to keep searching. So, use the
            // problem's getTransitions method from the currently expanded node's state...
            Map<String,MazeState> transitions = problem.getTransitions(temp.state);
            // TODO: ...and *for each* of those transition states...
            // [Hint] Look up how to iterate through <key, value> pairs in a Map -- an
            // example of this is already done in the MazeProblem's getTransitions method
            System.out.println("\n   Expanding (" + temp.state.row + "," + temp.state.col +")\n");
            for(Map.Entry<String, MazeState> action : transitions.entrySet()) {
        
                // TODO: ...add a new SearchTreeNode to the frontier with the appropriate
                // action, state, and parent
                
                SearchTreeNode generatedChild = new SearchTreeNode
                    (action.getValue(),action.getKey(),temp, temp.history + problem.getCost(action.getValue()) , manhattanH(keyObtained, problem.GOAL_STATE, problem.KEY_STATE, action.getValue()));
                    
                System.out.println("Test " + action.getKey() + " " + action.getValue().row + " " + action.getValue().col + " " + generatedChild.history + " " + generatedChild.aStarCost + " ");
                if(!graveyard.contains(generatedChild.state)) {
                    currentFrontier.add(generatedChild);
                    graveyard.add(generatedChild.state);
                }
            }
            
        }
        // Should never get here, but just return null to make the compiler happy
        return null;
        
    }
    
    //manhattanH(keyObtained, problem.GOAL_STATE, problem.KEY_STATE, )
    public static int manhattanH(boolean keyObtained, ArrayList<MazeState> goalStates, MazeState keyState, MazeState state) {
        return (keyObtained ? goalManhattanH(goalStates, state) :  keyManhattanH(keyState, state));
    }
    
    public static int goalManhattanH(ArrayList<MazeState> goalStates, MazeState state) { 
        PriorityQueue<Integer> sorter = new PriorityQueue<Integer>();
        for(int i = 0; i < goalStates.size(); i++) {
            sorter.add(Math.abs(goalStates.get(i).row - state.row) + Math.abs(goalStates.get(i).col - state.col));
        }
        return sorter.peek(); 
    }
    public static int keyManhattanH(MazeState keyState, MazeState state) { 
        
        return (Math.abs(keyState.row - state.row) + Math.abs(keyState.col - state.col));
    }
    
    public static boolean canCollectKey(MazeState state, MazeState keyState) {
        if(state.col == keyState.col && state.row == keyState.row) {
            return true;
        }
        return false;
    }
    
    public static ArrayList<String> getSolution(SearchTreeNode goal, MazeProblem problem) {
        ArrayList<String> solution = new ArrayList<String>();
        SearchTreeNode currentNode = goal;
        while(!currentNode.state.equals(problem.INITIAL_STATE)) {
            solution.add(0, currentNode.action);
            currentNode = currentNode.parent;
        }
        //DEBUG
        System.out.println();
        for(int i = 0; i < solution.size(); i++) {
                System.out.print(" " + solution.get(i));
        }
        //DEBUG
        return solution;
    }
}

/**
 * SearchTreeNode that is used in the Search algorithm to construct the Search
 * tree.
 */
class SearchTreeNode {
    
    int aStarCost;
    int history;
    MazeState state;
    String action;
    SearchTreeNode parent;
    
    /**
     * Constructs a new SearchTreeNode to be used in the Search Tree.
     * 
     * @param state The MazeState (col, row) that this node represents.
     * @param action The action that *led to* this state / node.
     * @param parent Reference to parent SearchTreeNode in the Search Tree.
     */
    SearchTreeNode (MazeState state, String action, SearchTreeNode parent, int history, int manhattan) {
        this.state = state;
        this.action = action;
        this.parent = parent;
        this.history = history;
        this.aStarCost = manhattan + history;
    }   
}