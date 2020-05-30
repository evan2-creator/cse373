package seamcarving;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.DijkstraShortestPathFinder;
import graphs.shortestpaths.ShortestPathFinder;

import java.util.ArrayList;
import java.util.List;

public class DijkstraSeamFinder implements SeamFinder {
    // TO-DO: replace all 4 references to "Object" on the line below with whatever vertex type
    //  you choose for your graph
    private final ShortestPathFinder<Graph<Object, Edge<Object>>, Object, Edge<Object>> pathFinder;

    public DijkstraSeamFinder() {
        this.pathFinder = createPathFinder();
    }

    protected <G extends Graph<V, Edge<V>>, V> ShortestPathFinder<G, V, Edge<V>> createPathFinder() {
        /*
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
        */
        return new DijkstraShortestPathFinder<>();
    }

    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        int width = energies.length;
        int height = energies[0].length;

        int[] seam = new int[width];
        int[][] nodeTo = new int[width][height];
        double[][] distTo = new double[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == 0) {
                    distTo[i][j] = energies[i][j];
                } else {
                    distTo[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }

        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height; j++) {
                if (j > 0) {
                    if (distTo[i + 1][j - 1] > distTo[i][j] + energies[i + 1][j - 1]) {
                        distTo[i + 1][j - 1] = distTo[i][j] + energies[i + 1][j - 1];
                        nodeTo[i + 1][j - 1] = j;
                    }
                }
                if (distTo[i + 1][j] > distTo[i][j] + energies[i + 1][j]) {
                    distTo[i + 1][j] = distTo[i][j] + energies[i + 1][j];
                    nodeTo[i + 1][j] = j;
                }
                if (j + 1 < height) {
                    if (distTo[i + 1][j + 1] > distTo[i][j] + energies[i + 1][j + 1]) {
                        distTo[i + 1][j + 1] = distTo[i][j] + energies[i + 1][j + 1];
                        nodeTo[i + 1][j + 1] = j;
                    }
                }
            }
        }

        int index = 0;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < height; i++) {
            if (min > distTo[width - 1][i]) {
                min = distTo[width - 1][i];
                index = i;
            }
        }

        int lastCol = width - 1;
        while (lastCol >= 0) {
            seam[lastCol] = index;
            index = nodeTo[lastCol--][index];
        }

        List<Integer> output = new ArrayList<>(seam.length);
        for (int i : seam) {
            output.add(i);
        }
        return output;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        int width = energies.length;
        int height = energies[0].length;

        int[] seam = new int[height];
        int[][] nodeTo = new int[width][height];
        double[][] distTo = new double[width][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0) {
                    distTo[j][i] = energies[j][i];
                } else {
                    distTo[j][i] = Double.POSITIVE_INFINITY;
                }
            }
        }

        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {
                if (j > 0) {
                    if (distTo[j - 1][i + 1] > distTo[j][i] + energies[j - 1][i + 1]) {
                        distTo[j - 1][i + 1] = distTo[j][i] + energies[j - 1][i + 1];
                        nodeTo[j - 1][i + 1] = j;
                    }
                }
                if (distTo[j][i + 1] > distTo[j][i] + energies[j][i + 1]) {
                    distTo[j][i + 1] = distTo[j][i] + energies[j][i + 1];
                    nodeTo[j][i + 1] = j;
                }
                if (j + 1 < width) {
                    if (distTo[j + 1][i + 1] > distTo[j][i] + energies[j + 1][i + 1]) {
                        distTo[j + 1][i + 1] = distTo[j][i] + energies[j + 1][i + 1];
                        nodeTo[j + 1][i + 1] = j;
                    }
                }
            }
        }

        int index = 0;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < width; i++) {
            if (min > distTo[i][height - 1]) {
                min = distTo[i][height - 1];
                index = i;
            }
        }

        int lastRow = height - 1;
        while (lastRow >= 0) {
            seam[lastRow] = index;
            index = nodeTo[index][lastRow--];
        }

        List<Integer> output = new ArrayList<>(seam.length);
        for (int i : seam) {
            output.add(i);
        }
        return output;
    }
}
