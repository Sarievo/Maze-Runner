import java.util.*;

public class INIT {
    public static int[][] genMaze(int row, int col) {
        int vertexNum = row * col;
        int[][] aMatrix = genAdjacencyMatrix(vertexNum, col);
        return aMatrix2Maze(aMatrix, row, col);
    }

    public static int[][] genAdjacencyMatrix(int vertexNum, int rowNum) {
        int[][] aMatrix = new int[vertexNum][vertexNum];

        Random random = new Random();

        for (int i = 0; i < vertexNum; i++) {
            for (int j = i; j < vertexNum; j++) {
                int value = 0;
                if (i / rowNum == j / rowNum && j == i + 1
                        || j == i + rowNum || i == j + rowNum) {
                    value = random.nextInt(99);
                } else if (i != j) value = 99;
                aMatrix[i][j] = value;
            }

            for (int j = 0; j < i; j++) {
                aMatrix[i][j] = aMatrix[j][i];
            }
        }
        return aMatrix;
    }

    public static Set<List<Integer>> MSTSets2RawSets(Set<Set<Integer>> MSTSets, int col) {
        Set<List<Integer>> rawSet = new LinkedHashSet<>();
        for (Set<Integer> set : MSTSets) {
            int y = 0, x = 0;

            for (Integer i : set) {
                y += i / col;
                x += i % col;
            }
            rawSet.add(List.of(y, x));
        }
        return rawSet;
    }

    public static int[][] aMatrix2Maze(int[][] aMatrix, int row, int col) {

        Set<Set<Integer>> MSTSets = MST.primMST(aMatrix);
//        System.out.println(MSTSets);
        Set<List<Integer>> rawSet = MSTSets2RawSets(MSTSets, col);
//        System.out.println(rawSet);

        int extendRow = row * 2 - 1;
        int extendCol = col * 2 - 1;

//        System.out.println(extendRow + " : " + extendCol);
        int[][] rawMatrix = new int[extendRow][extendCol];
        for (int i = 0; i < extendRow; i++) {
            for (int j = 0; j < extendCol; j++) {
                rawMatrix[i][j] = i % 2 == 0 && j % 2 == 0 ? 0 : 1;
            }
        }

        for (List<Integer> coList : rawSet) {
            rawMatrix[coList.get(0)][coList.get(1)] = 0;
        }

        return rawMatrix;
    }
}
