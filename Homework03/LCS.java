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
    
    // [!] TODO: Add your shared helper methods here!
    
    public static void printTable(int[][] DP_Table) {
        
        System.out.println();
        for(int r = 0; r < DP_Table.length; r++) {
            for(int c = 0; c < DP_Table[r].length; c++) {
                System.out.print(DP_Table[r][c] + ",");
            }
            System.out.println();
        }
        System.out.println();
        
    }
    
    
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
            
            Set<String> finalTempResult = new HashSet<String>();
            Set<String> tempResult1;
            Set<String> tempResult2;
            
            if( memoCheck[currentR-1][currentC] >=  memoCheck[currentR][currentC-1] ) {
                
                tempResult1 = collectSolution(rStr,cStr, result, currentR - 1, currentC);
                finalTempResult = tempResult1;
            
            }
            if( memoCheck[currentR][currentC-1] >=  memoCheck[currentR-1][currentC] ) {
                
                tempResult2 = collectSolution(rStr, cStr, result, currentR, currentC - 1);
                if(finalTempResult != null)
                    finalTempResult.addAll(tempResult2);
                else
                    finalTempResult = tempResult2;
                    
                
            }
            
            return finalTempResult; 
            
        }
    }
    
    /*
        Adds the same 
      */
    public static Set<String> addStrToAll(Set<String> original, String subStr) {
        
        Set<String> tempSet = new HashSet<String>();
        for(String str : original) {
            tempSet.add(subStr + str);
        }
        return tempSet;
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
            tempDP[currentR][currentC] = tempDP[currentR - 1][currentC];
            tempDP = topDownTableFill(rStr,cStr, tempDP, currentR, currentC - 1);
            
            if(tempDP[currentR][currentC] > tempDP[currentR][currentC - 1]) {
                tempDP[currentR][currentC] = tempDP[currentR][currentC - 1];
            }
            
            return tempDP;
            
        }
    }
    
    
}
