package maze;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {
    private static int[][] solution;
    private static int[][] maze;

    private static int rLimit;
    private static int cLimit;

    public static void main(int[][] maze) {
        PathFinder.solution = maze.clone();
        PathFinder.maze = maze;

        PathFinder.rLimit = maze.length;
        PathFinder.cLimit = maze[0].length;

        List<List<Integer>> entrance = findEntrance(maze);
        if (findPath(entrance.get(0).get(0),
                entrance.get(0).get(1), entrance)) {
            Main.printMaze(solution, false);
        } else
            System.out.println("No solution\n");
    }

    private static boolean findPath(int r, int c, List<List<Integer>> entrance) {
        if (List.of(r, c).equals(entrance.get(1))) {
            solution[r][c] = 2;
            return true;
        }

        if (r >= 0 && c >= 0 && r < rLimit && c < cLimit
                && solution[r][c] == 0 && maze[r][c] == 0) {
            solution[r][c] = 2;

            if (findPath(r + 1, c, entrance)) return true;
            if (findPath(r, c + 1, entrance)) return true;
            if (findPath(r - 1, c, entrance)) return true;
            if (findPath(r, c - 1, entrance)) return true;

            // back tracking
            solution[r][c] = 0;
            return false;
        }
        return false;

    }

    private static List<List<Integer>> findEntrance(int[][] maze) {
        List<Integer> entryFace = new ArrayList<>();
        List<List<Integer>> entrance = new ArrayList<>();

        for (int i : maze[0]) entryFace.add(i);
        if (entryFace.contains(0)) {
            entrance.add(List.of(0, entryFace.indexOf(0)));
            entryFace.clear();

            for (int i : maze[maze.length - 1]) entryFace.add(i);
            entrance.add(List.of(maze.length - 1, entryFace.indexOf(0)));
        } else {
            entryFace.clear();
            for (int[] ints : maze) entryFace.add(ints[0]);

            entrance.add(List.of(entryFace.indexOf(0), 0));
            entryFace.clear();

            for (int[] ints : maze) entryFace.add(ints[maze.length - 1]);
            entrance.add(List.of(entryFace.indexOf(0), maze.length - 1));
        }
//        System.out.println(entrance);
        return entrance;
    }
}
