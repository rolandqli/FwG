package graph;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests for the Graph class.
 *  @author Roland Li
 */
public class GraphTest {


    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

    @Test
    public void directedTest() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add(1, 2);
        assertEquals(g.outDegree(1), 1);
        g.add();
        g.add();
        g.add();
        g.add(1, 5);
        g.add(1, 3);
        assertEquals(g.outDegree(1), 3);
        assertFalse(g.contains(6));
        assertTrue(g.contains(1));
        assertTrue(g.contains(1, 5));
        assertFalse(g.contains(2, 5));
        g.remove(1);
        assertFalse(g.contains(1));
        assertFalse(g.contains(1, 3));
        assertFalse(g.contains(1, 5));
        g.add();
        g.add(1, 3);
        g.add(1, 4);
        g.remove(1, 3);
        assertTrue(g.contains(1));
        assertTrue(g.contains(1, 4));
        assertFalse(g.contains(1, 3));
        g.remove(5);
    }

    @Test
    public void undirectedTest() {
        UndirectedGraph g = new UndirectedGraph();
        g.add();
        g.add();
        g.add(1, 2);
        assertEquals(g.outDegree(1), 1);
        g.add();
        g.add();
        g.add();
        g.add(1, 5);
        g.add(1, 3);
        assertEquals(g.outDegree(1), 3);
        assertFalse(g.contains(6));
        assertTrue(g.contains(1));
        assertTrue(g.contains(1, 5));
        assertFalse(g.contains(2, 5));
        g.remove(1);
        assertFalse(g.contains(1));
        assertFalse(g.contains(1, 3));
        assertFalse(g.contains(1, 5));
        g.add();
        g.add(3, 1);
        g.add(4, 1);
        g.remove(1, 3);
        assertTrue(g.contains(1));
        assertTrue(g.contains(1, 4));
        assertFalse(g.contains(1, 3));
        g.remove(5);
    }

    @Test
    public void labeledTest() {
        UndirectedGraph g = new UndirectedGraph();
        g.add();
        g.add();
        g.add(1, 2);
        assertEquals(g.outDegree(1), 1);
        g.add();
        g.add();
        g.add();
        g.add(1, 5);
        g.add(1, 3);
        assertEquals(g.outDegree(1), 3);
        assertFalse(g.contains(6));
        assertTrue(g.contains(1));
        assertTrue(g.contains(1, 5));
        assertFalse(g.contains(2, 5));
        LabeledGraph graph = new LabeledGraph(g);
    }

    @Test
    public void bfsTest() {
        Graph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(2, 4);
        g.add(2, 5);
        g.add(3, 6);
        BreadthFirstTraversal bFS = new BreadthFirstTraversal(g);
        bFS.traverse(1);
    }

    @Test
    public void dfsTest() {
        Graph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(2, 4);
        g.add(2, 5);
        g.add(3, 6);
        DepthFirstTraversal dFS = new DepthFirstTraversal(g);
        dFS.traverse(1);
    }

    @Test
    public void outDegreeTest() {
        Graph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(2, 1);
        g.add(3, 2);
        g.add(3, 5);
        g.add(4, 3);
        g.add(4, 4);
        g.add(4, 5);
        assertEquals(g.outDegree(1), 2);
        assertEquals(g.outDegree(2), 1);
        assertEquals(g.outDegree(3), 2);
        assertEquals(g.outDegree(4), 3);
        assertEquals(g.outDegree(5), 0);
    }

    @Test
    public void containsTest() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add(1, 2);
        g.add();
        g.add();
        g.add();
        g.add(1, 5);
        g.add(1, 3);
        assertFalse(g.contains(6));
        assertTrue(g.contains(1));
        assertTrue(g.contains(1, 5));
        assertFalse(g.contains(2, 5));
        g.remove(1);
        assertFalse(g.contains(1));
        assertFalse(g.contains(1, 3));
        assertFalse(g.contains(1, 5));
        g.add();
        g.add(1, 3);
        g.add(1, 4);
        g.remove(1, 3);
        assertTrue(g.contains(1));
        assertTrue(g.contains(1, 4));
        assertFalse(g.contains(1, 3));
        g.remove(5);
    }

    @Test
    public void dInDegTest() {
        Graph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(2, 1);
        g.add(3, 2);
        g.add(3, 5);
        g.add(4, 3);
        g.add(4, 4);
        g.add(4, 5);
        assertEquals(g.inDegree(1), 1);
        assertEquals(g.inDegree(2), 2);
        assertEquals(g.inDegree(3), 2);
        assertEquals(g.inDegree(4), 1);
        assertEquals(g.inDegree(5), 2);
        assertEquals(g.inDegree(6), 0);
    }


}
