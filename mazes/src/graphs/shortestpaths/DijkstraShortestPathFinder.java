package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see ShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    implements ShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
        You'll also need to change the part of the class declaration that says
        `ArrayHeapMinPQ<T extends Comparable<T>>` to `ArrayHeapMinPQ<T>`.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public ShortestPath<V, E> findShortestPath(G graph, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        Map<V, E> edgeToV = new HashMap<>();
        Map<V, Double> distToV = new HashMap<>();
        ExtrinsicMinPQ<V> orderedPerimeter = createMinPQ();

        for (E edge : graph.outgoingEdgesFrom(start)) {
            distToV.put(edge.to(), Double.POSITIVE_INFINITY);
        }
        orderedPerimeter.add(start, 0);
        distToV.put(start, 0.0);
        while (!orderedPerimeter.isEmpty()) {
            V from = orderedPerimeter.removeMin();
            if (from.equals(end)) {
                break;
            }
            for (E edge : graph.outgoingEdgesFrom(from)) {
                V to = edge.to();
                if (!distToV.containsKey(to)) {
                    distToV.put(to, Double.POSITIVE_INFINITY);
                }
                double oldDist = distToV.get(to);
                double newDist = distToV.get(from) + edge.weight();
                if (newDist < oldDist) {
                    edgeToV.put(to, edge);
                    distToV.put(to, newDist);
                    if (orderedPerimeter.contains(to)) {
                        orderedPerimeter.changePriority(to, newDist);
                    } else {
                        orderedPerimeter.add(to, newDist);
                    }
                }
            }
        }

        if (edgeToV.containsKey(end)) {
            List<E> list = new ArrayList<>();
            E edge = edgeToV.get(end);
            V to;
            list.add(edge);
            while (!edge.from().equals(start)) {
                to = edge.from();
                edge = edgeToV.get(to);
                list.add(edge);
            }
            Collections.reverse(list);
            return new ShortestPath.Success<>(list);
        }
        return new ShortestPath.Failure<>();
    }
}
