package com.T_jav_502.Theotterspace.units;

import com.T_jav_502.Theotterspace.teams.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public abstract class aUnit extends Sprite {
    protected int hp;
    protected int maxHp; // Stocke la vie maximale pour le calcul de la barre de vie
    protected int movement;
    protected int defense;
    protected int attack;
    protected int range = 1;
    protected boolean moved = false;
    protected boolean attacked = false;
    protected Vector2 coordinates;
    protected Team team;

    protected aUnit(int hp, int movement, int defense, int attack, Team team, int posX, int posY, String unitType) {
        super(new Sprite(new Texture("units/"+unitType+ (team.getCurentSpecies() == Team.Species.OTTER ? "L" : "W") +".png")));
        this.hp = hp;
        this.maxHp = hp; // Initialisation des PV max
        this.movement = movement;
        this.defense = defense;
        this.attack = attack;
        this.team = team;
        this.coordinates = new Vector2(posX, posY);
        setX(posX);
        setY(posY);
        this.setSize(48,48);
        this.translateX(-8);
        this.translateY(0);
    }

    // --- Getters & Setters ---
    public int getMovement() { return movement; }
    public int getRange() { return range; }
    public boolean hasMoved() { return moved; }
    public boolean hasAttacked() { return attacked; }
    public Vector2 getCoordinates() { return coordinates; }
    public Team getTeam() { return team; }

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }

    public void setMoved(boolean moved) { this.moved = moved; }
    public void setAttacked(boolean attacked) { this.attacked = attacked; }

    public void resetTurn() {
        this.moved = false;
        this.attacked = false;
        this.setColor(1, 1, 1, 1);
    }

    /**
     * Applique les dégâts à l'unité.
     */
    public void receiveDamage(int damage){
        int actualDamage = Math.max(1, damage - defense);
        hp -= actualDamage;
        System.out.println("Unit took " + actualDamage + " damage. HP left: " + hp);
    }

    /**
     * Gère l'animation de déplacement
     */
    public Array<Vector2> moveTo(Array<Vector2> path, int speed, float delta) {
        if (path.size == 0) return path;

        Vector2 target = path.get(0);
        float targetX = target.x;
        float targetY = target.y;

        if (Math.abs(coordinates.x - targetX) < 0.1f && Math.abs(coordinates.y - targetY) < 0.1f) {
            this.coordinates.set(targetX, targetY);
            setX(targetX);
            setY(targetY);
            path.removeIndex(0);
        } else {
            float moveAmount = speed * delta;
            if (coordinates.x < targetX) coordinates.x += moveAmount;
            else if (coordinates.x > targetX) coordinates.x -= moveAmount;

            if (coordinates.y < targetY) coordinates.y += moveAmount;
            else if (coordinates.y > targetY) coordinates.y -= moveAmount;

            setX(coordinates.x);
            setY(coordinates.y);
        }
        this.translateX(-8);

        this.moved = true;
        return path;
    }

    @Override
    public void setX(float x){ super.setX(x * 32); }
    @Override
    public void setY(float y){ super.setY(y * 32); }
}
