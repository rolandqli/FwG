package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

import static graph.Iteration.iteration;

/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *  @author Roland Li
 */
abstract class GraphObj extends Graph {

    /** Vertices near my vertex. */
    private ArrayList<ArrayList<Integer>> nextTo;

    /** A new, empty Graph. */
    GraphObj() {
        verticesSet = new ArrayList<Integer>();
        edgesSet = new ArrayList<int[]>();
        nextTo = new ArrayList<>();
    }

    @Override
    public int vertexSize() {
        return vertexSize;
    }

    @Override
    public ArrayList<Integer> verticesSet() {
        return verticesSet;
    }

    @Override
    public int maxVertex() {
        return maxVertex;
    }

    @Override
    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public int outDegree(int v) {
        Iteration<Integer> successors = successors(v);
        int i = 0;
        while (successors.hasNext()) {
            successors.next();
            i++;
        }
        return i;
    }

    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        Iteration<Integer> vertices = vertices();
        while (vertices.hasNext()) {
            int vertex = vertices.next();
            if (vertex == u) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(int u, int v) {
        int[] con = new int[2];
        con[0] = u;
        con[1] = v;
        if (isDirected() && contains(u)) {
            for (int[] edge : edgesSet) {
                if (con[0] == edge[0] && con[1] == edge[1]) {
                    return true;
                }
            }
        } else if (!isDirected() && contains(u) && contains(v)) {
            Iteration<Integer> edgesIterator = successors(u);
            Iteration<Integer> edgesIteratorv = successors(v);
            while (edgesIterator.hasNext()) {
                if (edgesIterator.next() == v) {
                    return true;
                }
            }
            while (edgesIteratorv.hasNext()) {
                if (edgesIteratorv.next() == u) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int add() {
        Iteration<Integer> vertexIterator = vertices();
        int i = 0;
        while (vertexIterator.hasNext()) {
            i++;
            if (vertexIterator.next() != i) {
                vertexSize++;
                verticesSet.add(i);
                sort(verticesSet, verticesSet.size());
                return i;
            }
        }
        if (i == vertexSize) {
            maxVertex = i + 1;
            verticesSet.add(i + 1);
            sort(verticesSet, verticesSet.size());
        }
        vertexSize++;
        nextTo.add(new ArrayList<>());
        return i + 1;
    }

    @Override
    public int add(int u, int v) {
        if (contains(u, v)) {
            return edgeId(u, v);
        }
        if (!isDirected() && contains(v, u)) {
            return edgeId(u, v);
        }
        checkMyVertex(v);
        checkMyVertex(u);
        int[] edgeArray = new int[2];
        edgeArray[0] = u;
        edgeArray[1] = v;
        if (!nextTo.get(u - 1).contains(v)) {
            nextTo.get(u - 1).add(v);
        }
        if (!isDirected() && !nextTo.get(v - 1).contains(u)) {
            nextTo.get(v - 1).add(u);
        }
        edgesSet.add(edgeArray);
        edgeSize++;
        return edgeId(u, v);
    }

    @Override
    public void remove(int v) {
        int a = verticesSet.indexOf(v);
        Iteration<Integer> successors = successors(v);
        nextTo.set(v - 1, new ArrayList<>());
        for (ArrayList<Integer> array : nextTo) {
            if (array.contains(v)) {
                Object b = (int) v;
                array.remove(b);
            }
        }
        verticesSet.remove(a);
        if (v == maxVertex) {
            maxVertex--;
        }
        vertexSize--;
        ArrayList<int[]> newEdges = new ArrayList<int[]>();
        int i = 0;
        for (int[] edge : edgesSet) {
            if (edge[0] != v && edge[1] != v) {
                newEdges.add(edge);
                i++;
            }
        }
        edgeSize = i;
        edgesSet = newEdges;
    }

    @Override
    public void remove(int u, int v) {
        ArrayList<int[]> newEdges = new ArrayList<int[]>();
        for (int[] edge : edgesSet) {
            if (!isDirected()) {
                if (!((edge[0] == v && edge[1] == u)
                        || (edge[0] == u && edge[1] == v))) {
                    newEdges.add(edge);
                    ArrayList<Integer> start = nextTo.get(u - 1);
                    ArrayList<Integer> starts = nextTo.get(v - 1);
                    if (start.contains(v)) {
                        int a = start.indexOf(v);
                        start.remove(a);
                        starts.remove(a);
                    } else {
                        int a = starts.indexOf(u);
                        starts.remove(a);
                        start.remove(a);
                    }
                }
            } else {
                if (!(edge[0] == u && edge[1] == v)) {
                    newEdges.add(edge);
                    Object a = (int) v;
                    nextTo.get(u - 1).remove(a);
                }
            }
        }
        edgeSize--;
        edgesSet = newEdges;
    }

    @Override
    public Iteration<Integer> vertices() {
        sort(verticesSet, verticesSet.size());
        Iteration<Integer> vert = iteration(verticesSet);
        return vert;
    }

    @Override
    public Iteration<Integer> successors(int v) {
        int a = verticesSet.indexOf(v);
        if (a == -1) {
            return iteration(new ArrayList<Integer>());
        }
        ArrayList<Integer> successors = nextTo.get(v - 1);
        Iteration<Integer> successor = iteration(successors);
        return successor;
    }

    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public Iteration<int[]> edges() {
        return iteration(edgesSet);
    }

    @Override
    protected void checkMyVertex(int v) {
        if (!contains(v)) {
            throw new IllegalArgumentException("vertex not in my graph Graph");
        }
    }

    @Override
    protected int edgeId(int u, int v) {
        if (!isDirected() && v > u) {
            return ((v + u) * (u + v + 1) / 2 + u);
        }
        return ((u + v) * (u + v + 1) / 2 + v);
    }

    /** Sort an ARRAY for K integers. */
    public void sort(ArrayList<Integer> array, int k) {
        int i = 1;
        while (i < k) {
            int j = i - 1;
            int value = array.get(i);
            while (j >= 0) {
                if (array.get(j) > value) {
                    array.add(j + 1, array.get(j));
                    array.remove(j + 2);
                    array.add(j, value);
                    array.remove(j + 1);
                }
                j--;
            }
            i++;
        }
    }

    /** The number of vertices in the graph. */
    private int vertexSize;

    /** The highest value vertex's value. */
    private int maxVertex;

    /** The number of edges in the graph. */
    private int edgeSize;

    /** Collection of vertices. */
    private ArrayList<Integer> verticesSet;

    /** Collection of edges. */
    private ArrayList<int[]> edgesSet;






}
