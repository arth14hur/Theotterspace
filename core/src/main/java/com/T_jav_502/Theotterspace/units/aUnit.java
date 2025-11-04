package com.T_jav_502.Theotterspace.units;
import com.T_jav_502.Theotterspace.teams.*;
import static java.lang.Math.abs;

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

    //Getters
    public int getHp() {
        return hp;
    }
    public int getAttack() {
        return attack;
    }
    public int getDefense() {
        return defense;
    }
    public int getMovement() {
        return movement;
    }
    public int getRange() {
        return range;
    }
    public boolean isMoved() {
        return moved;
    }

    // Methods

    public boolean attack(aUnit opponent){
        //TODO: Make this method display a Fight scene.
        if (isAtRange(opponent)){
            opponent.receiveDamage(attack);
            return true;
        }
        return false;
    }

    public void receiveDamage(int damage){
        if (damage <= defense && damage >0 ){
            hp -= 1;
        }else if  (damage > 0){
            hp -= (damage - defense);
        }
    }

    public Team.Species getTeam() {
        return team;
    }

    protected boolean isAtRange(aUnit opponent){
        //TODO: Fix once tiles are implemented.
        return true;
    }







}
