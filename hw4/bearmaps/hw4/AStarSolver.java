package bearmaps.hw4;

import java.util.*;

import bearmaps.proj2ab.*;
import edu.princeton.cs.algs4.Stopwatch;


public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex>{
    private SolverOutcome outcome;
    private List<Vertex> solution;
    private double solutionWeight;
    private int numStatesExplored = 0;
    private double explorationtime;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout){
        HashSet<Vertex> markedVertices = new HashSet<>();
        Stopwatch sw = new Stopwatch();
        Map<Vertex, Double> distTo = new HashMap<>();
        Map<Vertex, Vertex> edgeTo = new HashMap<>();   // Map<to, from>
        ExtrinsicMinPQ<Vertex> PQ = new DoubleMapPQ<>();
        PQ.add(start, 0 + input.estimatedDistanceToGoal(start, end));
        distTo.put(start, 0.0);
        while( PQ.size() > 0 && !PQ.getSmallest().equals(end)){
            Vertex from = PQ.removeSmallest();
            numStatesExplored++;
            markedVertices.add(from);
            List<WeightedEdge<Vertex>> adj = input.neighbors(from);
            for(WeightedEdge<Vertex> edge : adj){
                // Relaxation
                Vertex to = edge.to();
                if(markedVertices.contains(to)) continue;
                if (PQ.contains(to)){
                    Double a = distTo.get(from);
                    Double b = edge.weight();
                    Double c = distTo.get(to);
                    if (distTo.get(from) + edge.weight() < distTo.get(to)){
                        PQ.changePriority(to, distTo.get(from) + edge.weight() + input.estimatedDistanceToGoal(to, end));
                        distTo.put(from, distTo.get(from) + edge.weight());
                        edgeTo.put(to, from);
                    }
                }
                else {
                    PQ.add(to, distTo.get(from) + edge.weight() + input.estimatedDistanceToGoal(to, end));
                    distTo.put(to, distTo.get(from) + edge.weight());
                    edgeTo.put(to, from);
                }
            }
        }
        explorationtime = sw.elapsedTime();
        if (explorationtime > timeout){
            outcome = SolverOutcome.TIMEOUT;
            solution = new LinkedList<>(); // solution is empty;
            solutionWeight = -1.0;
            numStatesExplored = -1;
        }
        else if (PQ.size() == 0){
            outcome = SolverOutcome.UNSOLVABLE;
            solution = new LinkedList<>(); // solution is empty;
            solutionWeight = -2.0;
            numStatesExplored = -2;
            }
        else{
            solution = constructSolutionList(edgeTo, start, end);
            outcome = SolverOutcome.SOLVED;
            solutionWeight = distTo.get(end) == null ? 0 : distTo.get(end);
        }
    }

    private LinkedList<Vertex> constructSolutionList(Map<Vertex, Vertex> edgeTo, Vertex start, Vertex end){
        LinkedList<Vertex> forReturn = new LinkedList<>();
        Vertex v = end;
        while(!v.equals(start)){
            forReturn.add(v);
            v = edgeTo.get(v);
        }
        forReturn.add(v);
        LinkedList<Vertex> forReturn1 = new LinkedList<>();
        while(!forReturn.isEmpty()){
            forReturn1.addLast(forReturn.removeLast());
        }
        return forReturn1;
    }
    @Override
    public SolverOutcome outcome(){return outcome;}

    @Override
    public List<Vertex> solution(){return solution;}

    @Override
    public double solutionWeight(){return solutionWeight;}

    @Override
    public int numStatesExplored(){return numStatesExplored;}

    @Override
    public double explorationTime(){return explorationtime;}
}
