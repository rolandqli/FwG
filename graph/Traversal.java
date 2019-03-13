package graph;

/* See restrictions in Graph.java. */


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.Arrays;

/** Implements a generalized traversal of a graph.  At any given time,
 *  there is a particular collection of untraversed vertices---the "fringe."
 *  Traversal consists of repeatedly removing an untraversed vertex
 *  from the fringe, visiting it, and then adding its untraversed
 *  successors to the fringe.
 *
 *  Generally, the client will extend Traversal.  By overriding the visit
 *  method, the client can determine what happens when a node is visited.
 *  By supplying an appropriate type of Queue object to the constructor,
 *  the client can control the behavior of the fringe. By overriding the
 *  shouldPostVisit and postVisit methods, the client can arrange for
 *  post-visits of a node (as in depth-first search).  By overriding
 *  the reverseSuccessors and processSuccessor methods, the client can control
 *  the addition of neighbor vertices to the fringe when a vertex is visited.
 *
 *  Traversals may be interrupted or restarted, remembering the previously
 *  marked vertices.
 *  @author Roland Li
 */
public abstract class Traversal {

    /** Cycle detector. */
    private ArrayList<Integer> cycle;

    /** A Traversal of G, using FRINGE as the fringe. */
    protected Traversal(Graph G, Queue<Integer> fringe) {
        _G = G;
        _fringe = fringe;
        markedstuff = new boolean[_G.vertexSize()];
        cycle = new ArrayList<Integer>();
    }

    /** Returns the MARKEDSTUFF collection. */
    public boolean[] getMarked() {
        return markedstuff;
    }

    /** Unmark all vertices in the graph. */
    public void clear() {
        int i = 0;
        while (i < markedstuff.length) {
            markedstuff[i] = false;
            i++;
        }
    }

    /** Initialize the fringe to V0 and perform a traversal. */
    public void traverse(Collection<Integer> V0) {
        _fringe.clear();
        _fringe.addAll(V0);
        while (!_fringe.isEmpty()) {
            int now = _fringe.remove();
            int a = _G.verticesSet().indexOf(now);
            if (!markedstuff[a]) {
                if (shouldPostVisit(now)) {
                    if (!_G.successors(now).hasNext()) {
                        mark(now);
                        cycle.add(now);
                        if (!postVisit(now)) {
                            return;
                        }
                    } else {
                        Iteration<Integer> successors = _G.successors(now);
                        int i = 0;
                        ArrayList<Integer> adding = new ArrayList<Integer>();
                        for (int successor : successors) {
                            int b = _G.verticesSet().indexOf(successor);
                            if (!markedstuff[b]) {
                                i++;
                                adding.add(successor);
                            }
                        }
                        if (i == 0) {
                            mark(now);
                            if (!postVisit(now)) {
                                return;
                            }
                        } else {
                            _fringe.add(now);
                            if (getClass().equals(DepthFirstTraversal.class)) {
                                Collections.reverse(adding);
                            }
                            _fringe.addAll(adding);
                        }
                    }
                } else {
                    mark(now);
                    if (!visit(now)) {
                        return;
                    }
                    processSuccessors(now);
                }
            }
        }
    }


    /** Initialize the fringe to { V0 } and perform a traversal. */
    public void traverse(int v0) {
        traverse(Arrays.<Integer>asList(v0));
    }

    /** Returns true iff V has been marked. */
    protected boolean marked(int v) {
        int a = _G.verticesSet().indexOf(v);
        if (markedstuff[a]) {
            return true;
        }
        return false;
    }

    /** Mark vertex V. */
    protected void mark(int v) {
        int a = _G.verticesSet().indexOf(v);
        markedstuff[a] = true;

    }

    /** Perform a visit on vertex V.  Returns false iff the traversal is to
     *  terminate immediately. */
    protected boolean visit(int v) {
        return true;
    }

    /** Return true if we should postVisit V after traversing its
     *  successors.  (Post-visiting generally is useful only for depth-first
     *  traversals, although we define it for all traversals.) */
    protected boolean shouldPostVisit(int v) {
        return false;
    }

    /** Revisit vertex V after traversing its successors.  Returns false iff
     *  the traversal is to terminate immediately. */
    protected boolean postVisit(int v) {
        return true;
    }

    /** Process the successors of vertex U.  Assumes U has been visited.  This
     *  default implementation simply processes each successor using
     *  processSuccessor. */
    protected void processSuccessors(int u) {
        ArrayList<Integer> help = new ArrayList<>();
        for (int v : _G.successors(u)) {
            if (processSuccessor(u, v)) {
                help.add(v);
            }
        }
        if (getClass().equals(DepthFirstTraversal.class)) {
            Collections.reverse(help);
        }
        _fringe.addAll(help);
    }

    /** Process successor V to U.  Returns true iff V is then to
     *  be added to the fringe.  By default, returns true iff V is unmarked. */
    protected boolean processSuccessor(int u, int v) {
        return !marked(v);
    }

    /** The graph being traversed. */
    private final Graph _G;
    /** The fringe. */
    protected final Queue<Integer> _fringe;
    /** Collection of marked vertices. */
    private boolean[] markedstuff;
    /** Collection of seen vertices. */
    private ArrayList<Integer> visualized;
}
