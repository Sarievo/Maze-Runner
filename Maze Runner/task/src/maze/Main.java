package maze;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);

        String menu1 = "=== Menu ===\n" +
                "1. Generate a new maze\n" +
                "2. Load a maze\n" +
                "0. Exit";
        String menu2 = "=== Menu ===\n" +
                "1. Generate a new maze\n" +
                "2. Load a maze\n" +
                "3. Save the maze\n" +
                "4. Display the maze\n" +
                "5. Find the escape\n" +
                "0. Exit";

        boolean isMazePresent = false;
        boolean isEnd = false;
        int[][] maze = new int[0][0];

        while (!isEnd) {
            System.out.println(!isMazePresent ? menu1 : menu2);
            int choice = -1;
            try {
                choice = Integer.parseInt(s.nextLine().strip());
            } catch (Exception ignored) {
            }

            if (choice < 3)
                switch (choice) {
                    case 1:
                        maze = genMaze(s);
                        printMaze(maze, false);
                        isMazePresent = true;
                        break;

                    case 2:
                        File save = new File(s.nextLine().strip());
                        if (!save.exists()) {
                            System.out.printf("The file %s does not exist\n", save);
                            break;
                        }

                        try {
                            Scanner fileScanner = new Scanner(save);
                            List<int[]> loadedMaze = new ArrayList<>();

                            while (fileScanner.hasNext()) {
                                int[] seq = Arrays.stream(fileScanner.nextLine().strip().split("\\s"))
                                        .mapToInt(Integer::parseInt)
                                        .toArray();
                                loadedMaze.add(seq);
                            }

                            for (int i = 0; i < loadedMaze.size() - 1; i++) {
                                if (loadedMaze.get(i).length != loadedMaze.get(i + 1).length) {
                                    throw new Exception();
                                }
                            }

                            maze = new int[loadedMaze.size()][loadedMaze.get(0).length];
                            for (int i = 0; i < loadedMaze.size(); i++) {
                                maze[i] = loadedMaze.get(i);
                            }
                            isMazePresent = true;
                        } catch (Exception e) {
                            System.out.println("Cannot load the maze. It has an invalid format");
                        }
                        break;

                    case 0:
                        isEnd = true;
                        System.out.println("Bye!");
                        break;

                    default:
                        error();
                }
            else if (isMazePresent) {
                switch (choice) {
                    case 3:
                        File save = new File(s.nextLine().strip());
                        FileWriter writer = new FileWriter(save, false);

                        for (int[] ints : maze) {
                            for (int i : ints) writer.write(i + " ");
                            writer.write("\n");
                        }
                        writer.close();
                        break;

                    case 4:
                        printMaze(maze, false);
                        break;

                    case 5:
                        PathFinder.main(maze);
                        break;

                    default:
                        error();
                }
            } else {
                error();
            }
        }

    }

    public static void error() {
        System.out.println("Incorrect option. Please try again");
    }

    public static int[][] genMaze(Scanner s) {
        System.out.println("Please, enter the size of a maze");

        int row;
        int col;
        while (true) {
            /* asymmetric maze
            String[] str = s.nextLine().strip().split("\\s");

            row = Integer.parseInt(str[0]);
            col = Integer.parseInt(str[1]);
            if (row < 5 || col < 5) {
                System.out.println("Size too small. Try set a larger maze.");
            } else break;
             */
            int side = Integer.parseInt(s.nextLine().strip());
            if (side < 5) {
                System.out.println("Size too small. Try set a larger maze.");
            } else {
                row = col = side;
                break;
            }
        }

        int[][] maze = INIT.genMaze((row - 1) / 2, (col - 1) / 2);

        maze = buildWall(maze);
        createEntrance(maze);

        if (row % 2 == 0) {
            int[][] extMaze = new int[maze.length + 1][maze.length];
            extMaze[0] = maze[0];  // extend Y row

            System.arraycopy(maze, 0, extMaze, 1, maze.length);
            maze = extMaze;  // copy matrix
        }

        if (col % 2 == 0) {
            int[][] extMaze = new int[maze.length][maze[0].length + 1];
            for (int i = 0; i < maze.length; i++) extMaze[i][0] = maze[i][0];  // extend X row

            for (int i = 0; i < maze.length; i++) {
                if (maze[0].length >= 1) System.arraycopy(maze[i], 0, extMaze[i], 1, maze[0].length);
            }
            maze = extMaze;  // copy matrix
        }

        return maze;
    }

    public static int[][] buildWall(int[][] maze) {
        int row = maze.length + 2;
        int col = maze[0].length + 2;
        int[][] walled = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i == 0 || i == row - 1 || j == 0 || j == col - 1) {
                    walled[i][j] = 1;
                } else {
                    walled[i][j] = maze[i - 1][j - 1];
                }
            }
        }
        return walled;
    }

    public static void createEntrance(int[][] maze) {
        Random rand = new Random();
        boolean isVerticalPath = rand.nextBoolean();

        int counter = 2;
        int side1 = 1;
        int side2 = 0;
        Set<Integer> loggedLocation = new HashSet<>();

        while (counter > 0) {
            if (counter == 1) {
                if (isVerticalPath) {
                    side1 = maze[0].length - 2;
                    side2 = maze[0].length - 1;
                } else {
                    side1 = maze.length - 2;
                    side2 = maze.length - 1;
                }
            }

            boolean isCreated = false;
            while (!isCreated) {
                int location = isVerticalPath ? rand.nextInt(maze.length - 2) + 1
                        : rand.nextInt(maze[0].length - 2) + 1;
                while (loggedLocation.contains(location))  // fail check
                    location = isVerticalPath ? rand.nextInt(maze.length - 2) + 1
                            : rand.nextInt(maze[0].length - 2) + 1;

                if (isVerticalPath) {
                    if (maze[location][side1] == 0) {
                        maze[location][side2] = 0;
                        isCreated = true;
                    } else loggedLocation.add(location);
                } else {
                    if (maze[side1][location] == 0) {
                        maze[side2][location] = 0;
                        isCreated = true;
                    } else loggedLocation.add(location);
                }
            }
            counter--;
        }
    }

    public static void printMaze(int[][] maze, boolean isAbstract) {
        if (!isAbstract) {
            // print graphical matrix
            for (int[] ints : maze) {
                for (int anInt : ints) {
                    if (anInt == 1) {
                        System.out.print("\u2588\u2588");
                    } else if (anInt == 2) {
                        System.out.print("//");
                    } else {
                        System.out.print("  ");
                    }
                }
                System.out.println();
            }
        } else {
            // print numeral matrix
            for (int[] ints : maze) {
                for (int anInt : ints) {
                    System.out.print(anInt + " ");
                }
                System.out.println();
            }
        }
    }
}