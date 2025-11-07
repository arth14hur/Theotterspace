package com.T_jav_502.Theotterspace.units;
import com.T_jav_502.Theotterspace.teams.*;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.graphics.Texture;

import static java.lang.Math.abs;
/**
 * This abstract class defines the parameters and methods of all Units.
 */
public abstract class aUnit {
    //attributes
    protected int hp;
    protected int movement;
    protected int defense;
    protected int attack;
    protected int range = 1;
    protected boolean moved = false;
    protected Vector2 coordinates;
    protected Team team;
    protected Texture texture;
    //Constructor
    protected aUnit(int hp, int movement, int defense, int attack, Team team, int posX, int posY) {
        this.hp = hp;
        this.movement = movement;
        this.defense = defense;
        this.attack = attack;
        this.team = team;
        this.coordinates = new Vector2(posX, posY);
        switch (team.getCurentSpecies()){
            case OTTER:
                texture = new Texture("miniMapL.png");
                break;
            case WOLF:
                texture = new Texture("miniMapW.png");
                break;
            default:
                texture = new Texture("miniMapW.png");
                break;
        }
    }

    //Getters
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

    /**
     * @return coordinates
     */
    public Vector2 getCoordinates() {
        return coordinates;
    }

    //setters

    public void setCoordinates(int posX, int posY) {
        this.coordinates = new Vector2(posX, posY);
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
    public Team getTeam() {
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
    public Texture getTexture() {
        return texture;
    }





}
