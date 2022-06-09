package creatures;

import huglife.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Clorus extends Creature{
    private int r,g,b;

    public Clorus(double e){
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    public Clorus(){
        this(1);
    }
    public void move(){
        energy -= 0.03;
        if(energy < 0) energy = 0;
    }
    public void attack(Creature c){
        if(c == null) return;
        energy += c.energy();
    }
    public Clorus replicate(){
        if(energy < 1) return null;
        Clorus r = new Clorus(energy/2);
        energy /= 2;
        return r;
    }
    public void stay(){
        energy -= 0.01;
        if(energy < 0) energy = 0;
    }
    public Action chooseAction(Map<Direction, Occupant> neighbors){
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        if(empties.size() == 0) return new Action(Action.ActionType.STAY);
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if(plips.size() != 0){
            Direction d = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, d);
        }
        Direction d = HugLifeUtils.randomEntry(empties);
        if(energy >= 1.0){
            return new Action(Action.ActionType.REPLICATE, d);
        }
        return new Action(Action.ActionType.MOVE, d);
    }

    public Color color() {
        return color(r, g, b);
    }
}
