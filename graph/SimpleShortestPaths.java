package graph;


import static java.lang.Double.POSITIVE_INFINITY;

/** A partial implementation of ShortestPaths that contains the weights of
 *  the vertices and the predecessor edges.   The client needs to
 *  supply only the two-argument getWeight method.
 *  @author Roland Li
 */
public abstract class SimpleShortestPaths extends ShortestPaths {

    /** Collection of weights. */
    private double[] weights;

    /** Collection of predecessors. */
    private int[] predecessors;

    /** The shortest paths in G from SOURCE. */
    public SimpleShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public SimpleShortestPaths(Graph G, int source, int dest) {
        super(G, source, dest);
        weights = new double[_G.verticesSet().size()];
        predecessors = new int[_G.verticesSet().size()];

    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    @Override
    protected abstract double getWeight(int u, int v);

    @Override
    public double getWeight(int v) {
        if (!_G.verticesSet().contains(v)) {
            return POSITIVE_INFINITY;
        }
        int a = _G.verticesSet().indexOf(v);
        return weights[a];
    }

    @Override
    protected void setWeight(int v, double w) {
        int a = _G.verticesSet().indexOf(v);
        weights[a] = w;
    }

    @Override
    public int getPredecessor(int v) {
        if (_G.verticesSet().contains(v)) {
            int a = _G.verticesSet().indexOf(v);
            return predecessors[a];
        }
        return 0;
    }

    @Override
    protected void setPredecessor(int v, int u) {
        int a = _G.verticesSet().indexOf(v);
        predecessors[a] = u;
    }



}
