package graph;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.ArrayDeque;
import java.util.Collection;

/** The shortest paths through an edge-weighted graph.
 *  By overrriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author Roland Li
 */
public abstract class ShortestPaths {
    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
        paths = new ArrayList<ArrayList<Integer>>(_G.verticesSet().size());
        int i = 0;
        while (i < _G.verticesSet().size()) {
            paths.add(i, new ArrayList<>());
            i++;
        }
    }

    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        for (int a : _G.verticesSet()) {
            setWeight(a, Integer.MAX_VALUE);
        }
        setWeight(_source, 0);
        for (int a : _G.verticesSet()) {
            setPredecessor(a, 0);
        }
        Breadth dij = new Breadth(_G);
        dij.traverse(_source);
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        int a = _G.verticesSet().indexOf(v);
        ArrayList<Integer> path = new ArrayList<>();
        while (getPredecessor(v) != 0) {
            path.add(0, v);
            v = getPredecessor(v);
        }
        path.add(0, _source);
        return path;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }


    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;

    /** List of paths. */
    private ArrayList<ArrayList<Integer>> paths;

    /** Helper class for traversing the graph. */
    private class Breadth extends Traversal {
        /** Tree set representation. */
        private TreeSet<Integer> shiftry;
        /** Cycler. */
        private ArrayList<Integer> cycle;
        /** Initializes a helper traversal based off G. */
        protected Breadth(Graph G) {
            super(G, new ArrayDeque<Integer>());
            cycle = new ArrayList<>();
            shiftry = new TreeSet<Integer>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    double a = getWeight(o1)
                            + estimatedDistance(o1);
                    double b = getWeight(o2)
                            + estimatedDistance(o2);
                    if (a > b) {
                        return 1;
                    } else if (a < b) {
                        return -1;
                    } else {
                        return o1.compareTo(o2);
                    }
                }
            });
        }

        @Override
        public void traverse(Collection<Integer> v0) {
            shiftry.clear();
            shiftry.addAll(v0);
            while (!shiftry.isEmpty()) {
                int now = shiftry.pollFirst();
                if (now == _dest) {
                    break;
                }
                processSuccessors(now);
            }
        }

        @Override
        public void processSuccessors(int now) {
            for (int v : _G.successors(now)) {
                if (getWeight(v) > getWeight(now) + getWeight(now, v)) {
                    shiftry.remove(v);
                    setWeight(v, getWeight(now) + getWeight(now, v));
                    setPredecessor(v, now);
                    shiftry.add(v);
                }
            }
        }
    }
}

