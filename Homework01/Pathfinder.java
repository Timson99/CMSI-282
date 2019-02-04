

// Group: Tim Herrmann & Joe Maiocco 

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
        
        HashSet<MazeState> graveyard = new HashSet<>();
        PriorityQueue<SearchTreeNode> currentFrontier = new PriorityQueue<SearchTreeNode>(50, new Comparator<SearchTreeNode>() 
        {
            public int compare(SearchTreeNode node1, SearchTreeNode node2) {
                Integer one = new Integer(node1.aStarCost);
                Integer two = new Integer(node2.aStarCost);
                return one.compareTo(two); 
            }
        });
        
        boolean keyObtained = false;
        currentFrontier.add(new SearchTreeNode(problem.INITIAL_STATE, null, null, 0, manhattanH(keyObtained, problem.GOAL_STATE, problem.KEY_STATE, problem.INITIAL_STATE)));
        graveyard.add(problem.INITIAL_STATE);
        
        
        while(currentFrontier.size() > 0) {
        /* DEBUG
            PriorityQueue<SearchTreeNode> copy = new PriorityQueue<>(currentFrontier);
            System.out.println("\nFront of Queue:  ");
            while(copy.peek() != null) {
                SearchTreeNode value = copy.remove();
                System.out.print( "(" + value.aStarCost + ", (" + value.state.row + "," + value.state.col + ")) ");
            }
             System.out.println(); 
          DEBUG */

            SearchTreeNode temp = currentFrontier.remove();
            graveyard.add(temp.state);
            
            if(!keyObtained)  {
                if(canCollectKey(temp.state, problem.KEY_STATE)) {
                    graveyard.clear();
                    currentFrontier.clear();
                    keyObtained = true;
                }
            }
            
            if(problem.isGoal(temp.state) && keyObtained) {
                return getSolution(temp, problem);
            }
            
            Map<String,MazeState> transitions = problem.getTransitions(temp.state);
            //DEBUG
            //System.out.println("\n   Expanding (" + temp.state.row + "," + temp.state.col +")\n");
            //DEBUG
            for(Map.Entry<String, MazeState> action : transitions.entrySet()) {
       
                SearchTreeNode generatedChild = new SearchTreeNode
                    (action.getValue(),action.getKey(),temp, temp.history + problem.getCost(action.getValue()) , 
                     manhattanH(keyObtained, problem.GOAL_STATE, problem.KEY_STATE, action.getValue()));
                    
                if(!graveyard.contains(generatedChild.state)) {
                    //DEBUG
                    //System.out.println("Test " + action.getKey() + " " + action.getValue().row + " " + action.getValue().col + " " + generatedChild.history + " " + generatedChild.aStarCost + " ");
                    //DEBUG
                    currentFrontier.add(generatedChild);
                }
            }
        }
        // Should never get here, but just return null to make the compiler happy
        return null; 
    }
    
    /**
     *  Returns the number of tiles between a target using the Manhattan Heuristic
     *  Goal can be either a key or a Goal State, depending on keyObtained parameter.
     * @param boolean keyObtained                Tells method if the key tile has been passed over.
     * @param ArrayList<MazeState> goalStates    Provides all the potential nearest goal states 
     * @param MazeState keyState                 Provides state of the key
     * @param MazeState state                    Provides current state
     * @return int The number of uniform cost movements away from the nearest target, 
     * which is the key state if the keyObtained is false, or the nearest goal state if keyObtained is true
    */
    public static int manhattanH(boolean keyObtained, ArrayList<MazeState> goalStates, MazeState keyState, MazeState state) {
        return (keyObtained ? goalManhattanH(goalStates, state) :  keyManhattanH(keyState, state));
    }
    
    /**
     *  Returns the number of tiles between the nearest goal state using the Manhattan Heuristic
     * @param ArrayList<MazeState> goalStates    Provides all the potential nearest goal states 
     * @param MazeState state                    Provides current state
     * @return int The number of uniform cost movements away from the nearest goal state.
    */
    public static int goalManhattanH(ArrayList<MazeState> goalStates, MazeState state) { 
        PriorityQueue<Integer> sorter = new PriorityQueue<Integer>();
        for(int i = 0; i < goalStates.size(); i++) {
            sorter.add(Math.abs(goalStates.get(i).row - state.row) + Math.abs(goalStates.get(i).col - state.col));
        }
        return sorter.peek(); 
    }
    
    /**
     *  Returns the number of tiles between the nearest key state using the Manhattan Heuristic
     * @param MazeState keyState                 Provides state of the key
     * @param MazeState state                    Provides current state
     * @return int The number of uniform cost movements away from the nearest key state.
    */
    public static int keyManhattanH(MazeState keyState, MazeState state) { 
        
        return (Math.abs(keyState.row - state.row) + Math.abs(keyState.col - state.col));
    }
    
    /**
     *  Provides a boolean value to represent whether or not the current state matches the key state 
     * @param MazeState keyState                 Provides state of the key
     * @param MazeState state                    Provides current state
     * @return boolean  Returns true if current state is key state, false if otherwise.
    */
    public static boolean canCollectKey(MazeState state, MazeState keyState) {
        if(state.col == keyState.col && state.row == keyState.row) {
            return true;
        }
        return false;
    }
    
    /**
     * Gets the path taken to reach the solution, from earliest to most recent
     * @param MazeState keyState                 Provides state of the key
     * @param MazeState state                    Provides current state
     * @return ArrayList<String> The list of string actions taken to reach the solution
    */
    public static ArrayList<String> getSolution(SearchTreeNode goal, MazeProblem problem) {
        ArrayList<String> solution = new ArrayList<String>();
        SearchTreeNode currentNode = goal;
        while(currentNode.parent != null) {
            solution.add(0, currentNode.action);
            currentNode = currentNode.parent;
        }
       /*
        //DEBUG 
        System.out.println();
        for(int i = 0; i < solution.size(); i++) {
                System.out.print(" " + solution.get(i));
        }
        //DEBUG
        */ 
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
     * @param history The int representing spent cost in nodes history
     * @param manhattan The int of  the current calculated manhattan cost
     */
    SearchTreeNode (MazeState state, String action, SearchTreeNode parent, int history, int manhattan) {
        this.state = state;
        this.action = action;
        this.parent = parent;
        this.history = history;
        this.aStarCost = manhattan + history;
    }   
}