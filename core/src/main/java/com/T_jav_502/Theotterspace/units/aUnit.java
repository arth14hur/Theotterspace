package com.T_jav_502.Theotterspace.units;
import com.T_jav_502.Theotterspace.teams.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * This abstract class defines the parameters and methods of all Units.
 */
public abstract class aUnit extends Sprite {
    //attributes
    protected int hp;
    protected int movement;
    protected int defense;
    protected int attack;
    protected int range = 1;
    protected boolean moved = false;
    protected Vector2 coordinates;
    protected Team team;
    //Constructor
    protected aUnit(int hp, int movement, int defense, int attack, Team team, int posX, int posY, String unitType) {
        super(new Sprite(new Texture("../assets/units/"+unitType+ (team.getCurentSpecies() == Team.Species.OTTER ? "L" : "W") +".png")));

        this.hp = hp;
        this.movement = movement;
        this.defense = defense;
        this.attack = attack;
        this.team = team;
        this.coordinates = new Vector2(posX, posY);
        setX(posX);
        setY(posY);

        this.setSize(48,48);
        this.translateX(-9);
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
    public boolean hasMoved() {
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
     * moves the unit and displays the movement of the unit
     * @param coordinates
     */
    public void moveTo(Vector2 coordinates) {
        System.out.println(coordinates);

        this.coordinates = coordinates;
        setX(coordinates.x);
        setY(coordinates.y);

    }

    @Override
    public void setX(float x){
        super.setX(x * 32);
    }
    @Override
    public void setY(float y){
        super.setY(y * 32);
    }

    /**
     * attack an enemy unit
     * @param opponent
     * @return if unit has attacked
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





}
