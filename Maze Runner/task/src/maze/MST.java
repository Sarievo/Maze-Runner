package maze;

import java.util.Set;

public class MST {

    static int V;
    static int INT_MAX = Integer.MAX_VALUE;

    static boolean isValidEdge(int u, int v, boolean[] inMST) {
        if (u == v) return false;
        if (!inMST[u] && !inMST[v]) return false;
        return !inMST[u] || !inMST[v];
    }

    static Set<Set<Integer>> primMST(int[][] cost) {
        V = cost.length;
        boolean[] inMST = new boolean[V];
        Set<Set<Integer>> MSTSets = new java.util.HashSet<>(Set.of());

        inMST[0] = true;
        int edgeCount = 0;  //, minCost = 0;
        while (edgeCount < V - 1) {

            int min = INT_MAX, a = -1, b = -1;
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (cost[i][j] < min) {
                        if (isValidEdge(i, j, inMST)) {
                            min = cost[i][j];
                            a = i;
                            b = j;
                        }
                    }
                }
            }

            // MST presentation
            if (a != -1 && b != -1) {
//                System.out.printf("Edge %d:(%d, %d) cost: %d \n", edgeCount++, a, b, min);
//                minCost = minCost + min;
                edgeCount++;
                inMST[b] = inMST[a] = true;
                MSTSets.add(Set.of(a, b));
            }
        }

//        System.out.printf("\nMinimum cost = %d \n", minCost);
//        System.out.println("Edges: " + edgeCount);
        return MSTSets;
    }
}
