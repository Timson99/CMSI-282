//package lcs;

import java.util.Set;
import java.util.HashSet;

public class LCS {
    
    /**
     * memoCheck is used to verify the state of your tabulation after
     * performing bottom-up and top-down DP. Make sure to set it after
     * calling either one of topDownLCS or bottomUpLCS to pass the tests!
     */
    public static int[][] memoCheck;
    
    // -----------------------------------------------
    // Shared Helper Methods
    // -----------------------------------------------
     
    /**
        Helper for collectSolution()
        Takes a set and returns a copy of the set with a substring added to the left side of all elements
        @param original       Holds the Set of Strings that will be copied and modified
        @param subStr         String to be concatenated to the end of every string in original 
        @return               Set of Strings that is a copy of original with the subStr attached to all elements
      */
    public static Set<String> addStrToAll(Set<String> original, String subStr) {
        
        Set<String> tempSet = new HashSet<String>();
        for(String str : original) {
            tempSet.add(str + subStr);
        }
        return tempSet;
    }
    
    /**
     * Collect Solution returns a Set of solutions that corresponds to a given fill dynamic programming problem table
     * Collects solution from memoCheck class field
     * Uses Recursion
     * @param rStr          String associated with table row
     * @param cStr          String associated with table colLength
     * @param result        Initialized but empty set of strings to be modified
     * @param currentR      Keeps track of current row index in table during recursion
     * @param currentC      Keeps track of current col index in table during recursion
     * @return              Returns table filled using the top down approach
     */
    public static Set<String> collectSolution( String rStr, String cStr, Set<String> result, int currentR, int currentC ) {

        if(currentR <= 0 || currentC <= 0) {
            
            return result;  
        }
        else if( rStr.charAt( currentR - 1 ) == cStr.charAt( currentC - 1) ) {
            
            Set<String> tempResult = collectSolution(rStr,cStr, result, currentR - 1, currentC - 1);
            
            Set<String> copyResult = addStrToAll( tempResult, rStr.substring( currentR - 1, currentR ) );
            tempResult.clear();
            tempResult.addAll(copyResult);
            
            return tempResult;  
        }
        else {
            
            Set<String> tempResult1, tempResult2;
            Set<String> finalTempResult = new HashSet<String>();
            Set<String> resultCopy = new HashSet<String>(result);
            
            if( memoCheck[currentR-1][currentC] >=  memoCheck[currentR][currentC-1] ) {
                
                tempResult1 = collectSolution(rStr,cStr, result, currentR - 1, currentC);
                finalTempResult = tempResult1;
            }
            if( memoCheck[currentR][currentC-1] >=  memoCheck[currentR-1][currentC] ) {
                
                tempResult2 = collectSolution(rStr, cStr, resultCopy, currentR, currentC - 1);
                
                if(finalTempResult != null)
                    finalTempResult.addAll(tempResult2);
                else
                    finalTempResult = tempResult2;
            }
            return finalTempResult; 
        }
    }

    // -----------------------------------------------
    // Bottom-Up LCS
    // -----------------------------------------------
    
    /**
     * Bottom-up dynamic programming approach to the LCS problem, which
     * solves larger and larger subproblems iterative using a tabular
     * memoization structure.
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr +
     *         [Side Effect] sets memoCheck to refer to table
     */
    public static Set<String> bottomUpLCS (String rStr, String cStr) {
        
        Set<String> solutions = new HashSet<String>();
        solutions.add("");
        
        int rowLength = rStr.length() + 1;
        int colLength = cStr.length() + 1;
        int[][] DP_Table = new int[rowLength][colLength];
        
        for(int r = 1; r < rowLength; r++) {
            for(int c = 1; c < colLength; c++) {

                if( rStr.charAt(r-1) == cStr.charAt(c-1) ) {
                    
                   DP_Table[r][c] = 1 + DP_Table[r-1][c-1];  
                }
                else {
                    
                   DP_Table[r][c]= Math.max( DP_Table[r][c-1] , DP_Table[r-1][c] ); 
                }
            }
        }
        memoCheck = DP_Table;
        return collectSolution(rStr, cStr, solutions, rowLength - 1, colLength - 1);
    }
     
    // -----------------------------------------------
    // Top-Down LCS
    // -----------------------------------------------
    
    /**
     * Top-down dynamic programming approach to the LCS problem, which
     * solves smaller and smaller subproblems recursively using a tabular
     * memoization structure.
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr +
     *         [Side Effect] sets memoCheck to refer to table  
     */
    public static Set<String> topDownLCS (String rStr, String cStr) {
        
        Set<String> solutions = new HashSet<String>();
        solutions.add("");
        
        int rowLength = rStr.length() + 1;
        int colLength = cStr.length() + 1;
        int[][] DP_Table = new int[rowLength][colLength];
        
        DP_Table = topDownTableFill(rStr,cStr, DP_Table, rowLength - 1, colLength - 1);
        memoCheck = DP_Table;
        return collectSolution(rStr, cStr, solutions, rowLength - 1, colLength - 1);
    }
    
    /**
     * Helper method for topDownLCS() using recursion
     * Fills a dynamic programming subproblem table according to the topDown methodology
     * @param rStr          String associated with table row
     * @param cStr          String associated with table colLength
     * @param DP_Table      Initialized but empty table 2D int array to be modified
     * @param currentR      Keeps track of current row index in table during recursion
     * @param currentC      Keeps track of current col index in table during recursion
     * @return              Returns table filled using the top down approach
     */
    public static int[][] topDownTableFill(String rStr, String cStr, int[][] DP_Table, int currentR, int currentC) {
        
        if(currentR <= 0 || currentC <= 0) {
            
            return DP_Table;  
        }
        else if( rStr.charAt( currentR - 1 ) == cStr.charAt( currentC - 1) ) {
            
            int[][] tempDP = topDownTableFill(rStr,cStr, DP_Table, currentR - 1, currentC - 1);
            tempDP[currentR][currentC] = tempDP[currentR - 1][currentC - 1] + 1;
            return tempDP;
        }
        else {
            int[][] tempDP = topDownTableFill(rStr,cStr, DP_Table, currentR - 1, currentC);
            tempDP = topDownTableFill(rStr,cStr, tempDP, currentR, currentC - 1);
            
            if(tempDP[currentR - 1][currentC] < tempDP[currentR][currentC - 1]) {
                tempDP[currentR][currentC] = tempDP[currentR][currentC - 1];
            }
            else {
                tempDP[currentR][currentC] = tempDP[currentR - 1][currentC];
            }
            
            return tempDP; 
        }
    } 
}
