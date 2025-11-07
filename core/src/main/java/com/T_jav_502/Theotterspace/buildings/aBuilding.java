package com.T_jav_502.Theotterspace.buildings;
import com.T_jav_502.Theotterspace.teams.*;
import com.T_jav_502.Theotterspace.tiles.*;
import com.T_jav_502.Theotterspace.units.aUnit;

/**
 * This abstract class defines the parameters and methods of all buildings.
 */
public abstract class aBuilding {
    protected aTile tile ;
    protected Team owner ;
    protected int hp ;
    protected int maxHp ;

    public aBuilding (aTile tile , Team team , int hp) {
        this.tile = tile;
        this.owner = team;
        this.hp = hp;
        this.maxHp = hp;
    }
    /**
     * @return tile
     */
    public aTile getTile() {
        return tile;
    }
    /**
     * @return owner
     */
    public Team getTeam() {
        return owner;
    }
    /**
     * @return hp
     */
    public int getHp() {
        return hp;
    }

    /**
     *Change the building's HP depending on whether the tile is occupied by an enemy team or not.
     *If the tile is not occupied, the building heal 5 hp.
     *If the space is occupied, we check if the unit is allied.
     *If allied the building Heal 10hp.
     *Else the building loos 10hp and if the hp fall to 0 or less the building change owner and get 50hp.
     *the building don't have more than 50 hp.
     */
    public void updateBuilding(aUnit unit) {
        if (unit != null){
            if(unit.getTeam() != owner){
                if(hp - 10 <= 0){
                    owner = unit.getTeam() ;
                    hp = maxHp ;
                }
                else {
                    hp -= 10;
                }
            }
            else {
                if(hp + 10 >= maxHp){
                    hp = maxHp;
                }
                else {
                    hp += 10;
                }
            }
        }
        else {
            if(hp + 5 >= maxHp){
                hp = maxHp;
            }
            else {
                hp += 5;
            }
        }
    }

}
