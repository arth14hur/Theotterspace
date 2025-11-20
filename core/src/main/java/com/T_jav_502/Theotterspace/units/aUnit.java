package com.T_jav_502.Theotterspace.units;

import com.T_jav_502.Theotterspace.teams.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
        if (hp<=0) hp = 0;
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

    public Array<Vector2> whereCanWalk(TiledMapTileLayer layer, boolean atk,Team enemyTeam) {
        Array<Vector2> output = new Array<>();
        output.add(coordinates);
        Array<Vector2> bufferOutput = new Array<>();
        Array<Vector2> atkOutput = new Array<>();
        Array<Vector2> directions = new Array<>();
        Array<Vector2> enemyPositions = new Array<>();
        Vector2 bufferPosition = new Vector2();

        for (aUnit unit : enemyTeam.getUnits()) {
            enemyPositions.add(unit.getCoordinates());
        }

        directions.add(new Vector2(0,-1));
        directions.add(new Vector2(0,1));
        directions.add(new Vector2(-1, 0));
        directions.add(new Vector2(1, 0));

        int mvt = 0;
        if (!moved) mvt = movement;
        if (atk && !hasAttacked()) mvt += range;

        while (mvt > 0) {
            for (Vector2 position : output) {
                for (Vector2 direction : directions) {
                    bufferPosition.x = direction.x + position.x;
                    bufferPosition.y = direction.y + position.y;

                    if (layer.getCell((int) bufferPosition.x, (int) bufferPosition.y) != null && !output.contains(bufferPosition, false)) {
                        if (layer.getCell((int) bufferPosition.x, (int) bufferPosition.y).getTile().getProperties().get("walkable", Boolean.class)) {
                            if (enemyPositions.contains(bufferPosition, false)) {
                                if (!atkOutput.contains(bufferPosition, false))atkOutput.add(bufferPosition.cpy());
                            }
                            else bufferOutput.add(new Vector2(bufferPosition.cpy()));
                        }
                    }
                }
            }
            for (Vector2 out : bufferOutput) {
                if (!output.contains(out, false)) {
                    output.add(out);
                    if (atk && mvt <= range && !atkOutput.contains(out, false)) atkOutput.add(out);
                }
            }
            bufferOutput.clear();
            mvt --;
        }
        output.removeIndex(0);
        if (atk) return atkOutput;
        else return output;
    }

    @Override
    public void setX(float x){ super.setX(x * 32); }
    @Override
    public void setY(float y){ super.setY(y * 32); }
}
