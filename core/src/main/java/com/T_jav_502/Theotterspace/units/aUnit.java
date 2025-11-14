package com.T_jav_502.Theotterspace.units;
import com.T_jav_502.Theotterspace.teams.*;
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

    /**
     * @return team
     */
    public Team getTeam() {
        return team;
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
    public void moveTo(Array<Vector2> coordinates, OrthogonalTiledMapRenderer renderer) {
        for (Vector2 coordinate : coordinates) {
            while ((int)this.coordinates.x*10 != (int)coordinate.x*10 && (int)this.coordinates.y*10 != (int)coordinate.y*10) {
                this.coordinates.x = this.coordinates.x + ((coordinate.x - this.coordinates.x) / 10);
                this.coordinates.y = this.coordinates.y + ((coordinate.y - this.coordinates.y) / 10);

                setX(this.coordinates.x);
                setY(this.coordinates.y);

                renderer.getBatch().begin();
                this.draw(renderer.getBatch());
                renderer.getBatch().end();

            }
        }
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
        // TODO: The GameScreen/UI system should detect that an attack happened
        // and trigger the display of the Fight scene here.
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
     * return a boolean if a unit is in range
     * @param opponent
     * @return true if the opponent is within the unit's attack range (Manhattan distance)
     */
    protected boolean isAtRange(aUnit opponent){
        // Calculate Manhattan distance (distance en nombre de cases)
        int dx = (int) Math.abs(coordinates.x - opponent.coordinates.x);
        int dy = (int) Math.abs(coordinates.y - opponent.coordinates.y);

        return (dx + dy) <= range;
    }

    /**
     * Returns all tile coordinates within the unit's attack range for visualization.
     * This is used to display the translucent red tiles.
     * @param mapWidth The width of the map (in tiles).
     * @param mapHeight The height of the map (in tiles).
     * @return Array of Vector2 (tile coordinates) that are in range.
     */
    public Array<Vector2> getTilesInRange(int mapWidth, int mapHeight) {
        Array<Vector2> rangeTiles = new Array<>();
        int currentX = (int) coordinates.x;
        int currentY = (int) coordinates.y;

        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                // Use Manhattan distance check
                if (Math.abs(i) + Math.abs(j) <= range) {
                    int checkX = currentX + i;
                    int checkY = currentY + j;

                    // Check bounds
                    if (checkX >= 0 && checkX < mapWidth && checkY >= 0 && checkY < mapHeight) {
                        // We must add a new Vector2 instance
                        rangeTiles.add(new Vector2(checkX, checkY));
                    }
                }
            }
        }
        return rangeTiles;
    }
}
