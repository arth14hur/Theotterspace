package com.T_jav_502.Theotterspace.units;
import com.T_jav_502.Theotterspace.teams.*;
import static java.lang.Math.abs;
/**
 * This abstract class defines the parameters and methods of all Unit.
 */
public abstract class aUnit {
    //attributes
    protected int hp;
    protected int movement;
    protected int defense;
    protected int attack;
    protected int range = 1;
    protected boolean moved = false;
    protected Team.Species team;

    //Constructor
    protected aUnit(int hp, int movement, int defense, int attack, Team.Species team) {
        this.hp = hp;
        this.movement = movement;
        this.defense = defense;
        this.attack = attack;
        this.team = team;
    }

    /**
     * @return hp
     */
    public int getHp() {
        return hp;
    }
    /**
     * @return attack
     */
    public int getAttack() {
        return attack;
    }
    /**
     * @return defense
     */
    public int getDefense() {
        return defense;
    }
    /**
     * @return movement
     */
    public int getMovement() {
        return movement;
    }
    /**
     * @return range
     */
    public int getRange() {
        return range;
    }
    /**
     * @return moved
     */
    public boolean isMoved() {
        return moved;
    }

    // Methods

    /**
     * attack an enemy unit
     * @param opponent
     * @return
     */
    public boolean attack(aUnit opponent){
        //TODO: Make this method display a Fight scene.
        if (isAtRange(opponent)){
            opponent.receiveDamage(attack);
            return true;
        }
        return false;
    }

    /**
     * take damage from an enemy
     * @param damage
     */
    public void receiveDamage(int damage){
        if (damage <= defense && damage >0 ){
            hp -= 1;
        }else if  (damage > 0){
            hp -= (damage - defense);
        }
    }

    /**
     * @return team
     */
    public Team.Species getTeam() {
        return team;
    }

    /**
     * return a boolean if a unit is in range
     * @param opponent
     * @return
     */
    protected boolean isAtRange(aUnit opponent){
        //TODO: Fix once tiles are implemented.
        return true;
    }







}
