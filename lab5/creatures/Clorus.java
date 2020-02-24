package creatures;

import java.util.Map;
import java.awt.Color;
import java.util.Deque;
import java.util.ArrayDeque;
import huglife.HugLifeUtils;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;

public class Clorus extends Creature{
    private int r;
    private int g;
    private int b;


    public Clorus(double e){
        super("clorus");
        energy = e;
        r = 34;
        g = 0;
        b = 231;
    }

    public Clorus(){
        this(1);
    }

    public Color color(){
        return color(34, 0, 231);
    }
    @Override
    public void move(){
        energy -= 0.03;
    }

    @Override
    public void attack(Creature c){
        energy += c.energy();
    }

    @Override
    public void stay(){
        energy -= 0.01;
    }

    @Override
    public Clorus replicate(){
        energy *= 0.5;
        return new Clorus(energy);
    }

    @Override

    public Action chooseAction(Map<Direction, Occupant> neighbors) {

        Deque<Direction> emptyNeighbors = new ArrayDeque<>();

        Deque<Direction> plipNeighbors = new ArrayDeque<>();

        // Rule 1: If there are no empty squares, the Clorus will STAY

        // (even if there are Plips nearby they could attack since plip

        // squares do not count as empty squares).

        for (Direction key : neighbors.keySet()) {

            if (neighbors.get(key).name().equals("empty")) {

                emptyNeighbors.add(key);

            } else if (neighbors.get(key).name().equals("plip")) {

                plipNeighbors.add(key);

            }

        }

        if (emptyNeighbors.size() == 0) {

            return new Action(Action.ActionType.STAY);

        } else if (plipNeighbors.size() > 0) {

            // Rule 2: Otherwise, if any Plips are seen, the Clorus will ATTACK

            // one of them randomly.

            return new Action(Action.ActionType.ATTACK, HugLifeUtils.randomEntry(plipNeighbors));

        } else if (energy >= 1.0) {

            // Rule 3: Otherwise, if the Clorus has energy greater than or equal to one,

            // it will REPLICATE to a random empty square.

            return new Action(Action.ActionType.REPLICATE, HugLifeUtils.randomEntry(emptyNeighbors));

        } else {

            // Rule 4: Otherwise, the Clorus will MOVE to a random empty square.

            return new Action(Action.ActionType.MOVE, HugLifeUtils.randomEntry(emptyNeighbors));

        }



    }
}


