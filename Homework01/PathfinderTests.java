
// Group: Tim Herrmann & Joe Maiocco 

//package pathfinder.informed;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

/**
 * Unit tests for Maze Pathfinder. Tests include completeness and
 * optimality.
 */
public class PathfinderTests {
    
    @Test
    public void testPathfinder_t0() {
        String[] maze = {
            "XXXXXXX",
            "XI...KX",
            "X.....X",
            "X.X.XGX",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        // result will be a 2-tuple (isSolution, cost) where
        // - isSolution = 0 if it is not, 1 if it is
        // - cost = numerical cost of proposed solution
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]); // Test that result is a solution
        assertEquals(6, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t1() {
        String[] maze = {
            "XXXXXXX",
            "XI....X",
            "X.MMM.X",
            "X.XKXGX",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(14, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t2() {
        String[] maze = {
            "XXXXXXX",
            "XI.G..X",
            "X.MMMGX",
            "X.XKX.X",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(10, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t3() {
        String[] maze = {
            "XXXXXXX",
            "XI.G..X",
            "X.MXMGX",
            "X.XKX.X",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        assertNull(solution); // Ensure that Pathfinder knows when there's no solution
    }
    
    @Test
    public void testPathfinder_t4() {
        String[] maze = {
            "XXXXXXX",
            "XI...KX",
            "XGMM..X",
            "XGGM..X",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(9, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t5() {
        String[] maze = {
            "XXXXXXX",
            "XGXMMKX",
            "XGXMM.X",
            "XGXIM.X",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        assertNull(solution);
    }
    
    @Test
    public void testPathfinder_t6() {
        String[] maze = {
            "XXXXXXX",
            "XGXMMKX",
            "XGMMM.X",
            "XG.IM.X",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(14, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t7() {
        String[] maze = {
            "XXXXXXXXX",
            "X.......X",
            "XIMMKMMGX",
            "X.......X",
            "XXXXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(10, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t8() {
        String[] maze = {
            "XXXXXXXXX",
            "X.....MMX",
            "XIMMKMMGX",
            "X......MX",
            "XXXXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(12, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t9() {
        String[] maze = {
            "XXXXXXXXXX",
            "XG.MMIM.GX",
            "XG..XXX.GX",
            "XG.M.KM.GX",
            "XGM..M.MGX",
            "XXXXXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(15, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t10() {
        String[] maze = {
            "XXXXXXXXXX",
            "XG.MMIMMGX",
            "XG..XXX.GX",
            "XG.MMKMMGX",
            "XGMM.M..GX",
            "XXXXXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(20, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t11() {
        String[] maze = {
            "XXXXXXXXXX",
            "XI..MGM..X",
            "XGM..M...X",
            "XXXX.....X",
            "X.K.XM...X",
            "X........X",
            "XXXXXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(26, result[1]); // Ensure that the solution is optimal
    }
    @Test
    public void testPathfinder_t12() {
        String[] maze = {
            "XXXXXXXXXXXX",
            "XM.......MGX",
            "XGM....MMMMX",
            "X......M...X",
            "XM....KM.IXX",
            "X........XGX",
            "XXXXXXXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(12, result[1]); // Ensure that the solution is optimal
    }
    @Test
    public void testPathfinder_t13() {
        String[] maze = {
            "XXXXXXXXXXXX",
            "XGM.......GX",
            "X.MMMMMMM.MM",
            "X..........I",
            "XXX.XXXXMXXX",
            "X....K.....X",
            "XXXXXXXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(18, result[1]); // Ensure that the solution is optimal
    }
    @Test
    public void testPathfinder_t14() {
        String[] maze = {
            "XXXXXXXXXXXX",
            "XGM.......GX",
            "X.MMMMMMM.MM",
            "X..........I",
            "XXX.XXXXMXXX",
            "X...XK..X..X",
            "XXXXXXXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        assertNull(solution);
    }
}