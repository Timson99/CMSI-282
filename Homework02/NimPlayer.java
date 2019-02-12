//package nim;

import java.util.ArrayList;
import java.util.Map;
import java.util.*;

/**
 * Artificial Intelligence responsible for playing the game of Nim!
 * Implements the alpha-beta-pruning mini-max search algorithm
 */
public class NimPlayer {
    
    private final int MAX_REMOVAL;
    
    NimPlayer (int MAX_REMOVAL) {
        this.MAX_REMOVAL = MAX_REMOVAL;
    }
    
    /**
     * 
     * @param   remaining   Integer representing the amount of stones left in the pile
     * @return  An int action representing the number of stones to remove in the range
     *          of [1, MAX_REMOVAL]
     */
    public int choose (int remaining) {
        
        Map<GameTreeNode, Integer> visited = new HashMap<GameTreeNode, Integer>(); //GameTreeNode, Utility Score
        GameTreeNode root = new GameTreeNode(remaining, 0, true);
        //Generate scores for every node
        alphaBetaMinimax(root, Integer.MIN_VALUE, Integer.MAX_VALUE, true, visited);
        
        
        //DEBUG
        //System.out.println("END: ");
        /*for(int i = 0; i < root.children.size(); i++) {
            System.out.print("Child " + (i + 1) + " : " + root.children.get(i).score + "\n");
        }*/
        
        //DEBUG
        for(int i = 0; i < root.children.size(); i++) {
            if(root.children.get(i).score == 1) {
                //
                //System.out.println("Returning: " + root.children.get(i).action);
                //
                return root.children.get(i).action;
            }
        }
        //System.out.println("Return Losing Default of 1");
        return 1;
    }
    
    /**
     * Constructs the minimax game tree by the tenets of alpha-beta pruning with
     * memoization for repeated states.
     * @param   node    The root of the current game sub-tree
     * @param   alpha   Smallest minimax score possible
     * @param   beta    Largest minimax score possible
     * @param   isMax   Boolean representing whether the given node is a max (true) or min (false) node
     * @param   visited Map of GameTreeNodes to their minimax scores to avoid repeating large subtrees
     * @return  Minimax score of the given node + [Side effect] constructs the game tree originating
     *          from the given node
     */
    private int alphaBetaMinimax (GameTreeNode node, int alpha, int beta, boolean isMax, Map<GameTreeNode, Integer> visited) {

        if(node.remaining == 0) {
            //System.out.println("Terminal State: Up : Score " + node.score);
            return node.score;
        }
        if(visited.containsKey(node)) {
            //System.out.println("Memoized State: Up : Score " + visited.get(node));
            return visited.get(node);
        }
       
        
        if(isMax) {
            int minimaxScore = Integer.MIN_VALUE;
            for(int i = 0; i < MAX_REMOVAL; i++) {
                
                GameTreeNode expandingChild = node.generateChild(1 + i);
                
                if(expandingChild == null) {
                    break;
                }
                
                //System.out.println("Remaining " + node.remaining + " Down");
                //
                minimaxScore = Math.max(minimaxScore, alphaBetaMinimax(expandingChild, alpha, beta, false, visited));
                alpha = Math.max(alpha, minimaxScore);
                //
                //System.out.println("Remaining " + node.remaining + " Up  :  Score: " + minimaxScore);
                //
                if(beta <= alpha) {
                    //System.out.println("Pruning: " + node.remaining + " child index " + i+1);
                    break;
                }
            }
            visited.put(node, minimaxScore);
            node.score = minimaxScore;
            return minimaxScore;
        }
        else {
            int minimaxScore = Integer.MAX_VALUE;
            for(int i = 0; i < MAX_REMOVAL; i++) {
                
                GameTreeNode expandingChild = node.generateChild(1 + i);
                if(expandingChild == null) {
                    break;
                }
                //
                
                //
                //System.out.println("Remaining " + node.remaining + " Down");
                //
                minimaxScore = Math.min(minimaxScore, alphaBetaMinimax(expandingChild, alpha, beta, true, visited));
                beta = Math.min(beta, minimaxScore);
                //
                //System.out.println("Remaining " + node.remaining + " Up  :  Score: " + minimaxScore);
                //
                if(beta <= alpha) {
                  //System.out.println("Pruning: " + node.remaining + " child index " + i+1);
                  break;
                }
            }
            visited.put(node, minimaxScore);
            node.score = minimaxScore;
            return minimaxScore;
        }
    }
}
/**
 * GameTreeNode to manage the Nim game tree.
 */
class GameTreeNode {
    
    int remaining, action, score;
    boolean isMax;
    ArrayList<GameTreeNode> children;
    
    /**
     * Constructs a new GameTreeNode with the given number of stones
     * remaining in the pile, and the action that led to it. We also
     * initialize an empty ArrayList of children that can be added-to
     * during search, and a placeholder score of -1 to be updated during
     * search.
     * 
     * @param   remaining   The Nim game state represented by this node: the #
     *          of stones remaining in the pile
     * @param   action  The action (# of stones removed) that led to this node
     * @param   isMax   Boolean as to whether or not this is a maxnode
     */
    GameTreeNode (int remaining, int action, boolean isMax) {
        this.remaining = remaining; //Stones Left
        this.action = action;  //Actions Taken
        this.isMax = isMax;   
        children = new ArrayList<>(); //Children
        score = -1; //Utility Score
        
        if(this.remaining == 0 && isMax) {
            score = 0;
        }
        else {
            score = 1;
        }
    }
    
    public GameTreeNode generateChild(int removeStones) {
        if(removeStones > 3 || removeStones < 1) {
            throw new IllegalArgumentException("Out of Bounds choices : generateChild()");
        }
        if(remaining - removeStones < 0) {
            return null;
        }
        GameTreeNode child = new GameTreeNode(remaining - removeStones, removeStones, !this.isMax);
        children.add(child);
        return child;
    }
    
    
    @Override
    public boolean equals (Object other) {
        return other instanceof GameTreeNode 
            ? remaining == ((GameTreeNode) other).remaining && 
              isMax == ((GameTreeNode) other).isMax 
              && action == ((GameTreeNode) other).action
            : false;
    }
    
    @Override
    public int hashCode () {
        return remaining + ((isMax) ? 1 : 0);
    }
 
}
