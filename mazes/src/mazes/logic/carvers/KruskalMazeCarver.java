package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        Set<EdgeWithData<Room, Wall>> edgeSet = new HashSet<>();

        for (Wall wall : walls) {
            double weight = rand.nextDouble();
            edgeSet.add(new EdgeWithData<>(wall.getRoom1(), wall.getRoom2(), weight, wall));
        }
        var mst = this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edgeSet));

        Set<Wall> toRemove = new HashSet<>();
        List<EdgeWithData<Room, Wall>> removeEdges = (List<EdgeWithData<Room, Wall>>) mst.edges();
        for (EdgeWithData<Room, Wall> e : removeEdges) {
            toRemove.add(e.data());
        }
        return toRemove;
    }
}
