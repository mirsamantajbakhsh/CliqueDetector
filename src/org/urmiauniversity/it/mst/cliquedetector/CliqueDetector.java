/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.urmiauniversity.it.mst.cliquedetector;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.ProgressTicket;

/**
 *
 * @author Account
 */
public class CliqueDetector implements org.gephi.statistics.spi.Statistics, LongTask {

    private String report = "";
    private boolean cancel = false;
    private ProgressTicket progressTicket;
    private int k = 0;
    private int CliqueID = 0;
    private Set<Set<Node>> Cliques = new HashSet<Set<Node>>();

    GenQueue<TreeSet<Node>> Bk = new GenQueue<TreeSet<Node>>();

    public class SortByID implements Comparator<Node> {

        public int compare(Node n1, Node n2) {
            if (n1.getId() > n2.getId()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Queue Implementation">
    public Object getLastElement(final Collection c) {
        /*
         final Iterator itr = c.iterator();
         Object lastElement = itr.next();
         while (itr.hasNext()) {
         lastElement = itr.next();
         }
         return lastElement;
         */
        return null;
    }

    class GenQueue<E> {

        private LinkedList<E> list = new LinkedList<E>();

        public void enqueue(E item) {
            list.addLast(item);
        }

        public E dequeue() {
            return list.pollFirst();
        }

        public boolean hasItems() {
            return !list.isEmpty();
        }

        public int size() {
            return list.size();
        }

        public void addItems(GenQueue<? extends E> q) {
            while (q.hasItems()) {
                list.addLast(q.dequeue());
            }
        }
    }
    //</editor-fold>

    private Vector<Node> getLargerIndexNodes(Graph g, Node vi) {
        Vector<Node> output = new Vector<Node>();
        for (Node n : g.getNodes()) {
            if (n.getId() > vi.getId() && g.getEdge(n, vi) != null) {
                //TODO check degree of n and vi
                output.addElement(n);
            }
        }

        return output;
    }

    private boolean checkBk1IsClique(Graph g, TreeSet<Node> Bk1) {
        for (Node firstNode : Bk1) {
            for (Node secondNode : Bk1) {
                if (firstNode == secondNode) {
                    continue;
                }

                if (g.getEdge(firstNode, secondNode) == null) { //One edge is missing in the Bk+1 clique
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void execute(GraphModel gm, AttributeModel am) {
        Graph g = gm.getGraphVisible();

        g.readLock();

        //Firstly add each node as an item in Bk
        TreeSet<Node> tmp;
        for (Node n : g.getNodes()) {
            //Trick: if the node's degree is less than k-1, it can not involve in k-clique
            if (g.getDegree(n) >= k - 1) {
                tmp = new TreeSet<Node>(new SortByID());
                tmp.add(n);
                Bk.enqueue(tmp); //Add the B1 (node itself) to the queue
            }
        }

        //Now start the iterative process for finding cliques
        tmp = Bk.dequeue();

        while (tmp != null) {

            if (cancel) {
                break;
            }

            //Search for Bk+1
            Node vi = tmp.last(); //(Node) getLastElement(tmp);
            Vector<Node> largerIndexes = getLargerIndexNodes(g, vi);

            for (Node vj : largerIndexes) {
                TreeSet<Node> Bk1 = new TreeSet<Node>(new SortByID());
                Bk1.addAll(tmp); //Clone current Bk into Bk+1
                Bk1.add(vj);
                if (Bk1.size() <= getK() && checkBk1IsClique(g, Bk1)) {

                    if (Bk1.size() == getK()) { //A clique of size k found. Finish expanding this Bk+1 here.
                        Cliques.add(Bk1);
                    } else if (Bk1.size() < getK()) {
                        Bk.enqueue(Bk1); //k should be checked for finding cliques of size k.
                    } else { //Clique with larger size will be omitted.
                        report += "<br>Larger Clique Found. It should not be here<br>";
                    }
                }
            }

            tmp = Bk.dequeue(); //Check next item
        }
        g.readUnlock();
        
        //Algorithm finished.
        //Write the output
        report += "Clique Detection started. Nodes with <b>" + (k - 1) + "</b> edges will not be included.";
        report += "<br><br>";
        report += "Found Cliques of size " + getK() + ":<br>";

        for (Set<Node> output : Cliques) {
            String CliqueMembers = "";
            for (Node n : output) {
                CliqueMembers += n.getNodeData().getLabel() + ", ";
            }
            CliqueMembers = CliqueMembers.substring(0, CliqueMembers.length() - 2);
            report += CliqueMembers + "<br>";
        }
    }

    @Override
    public String getReport() {
        return report;
    }

    @Override
    public boolean cancel() {
        cancel = true;
        return true;
    }

    @Override
    public void setProgressTicket(ProgressTicket pt) {
        this.progressTicket = pt;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }
}
