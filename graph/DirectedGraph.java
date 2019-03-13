package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

import static graph.Iteration.iteration;

/** Represents a general unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author Roland Li
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int inDegree(int v) {
        Iteration<Integer> predecessors = predecessors(v);
        int i = 0;
        while (predecessors.hasNext()) {
            predecessors.next();
            i++;
        }
        return i;
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        ArrayList<Integer> predecessors = new ArrayList<Integer>();
        for (int[] a : edges()) {
            if (a[1] == v) {
                predecessors.add(a[0]);
            }
        }
        Iteration<Integer> predecessor = iteration(predecessors);
        return predecessor;
    }


}
