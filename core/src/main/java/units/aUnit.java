package units;

import static java.lang.Math.abs;

public abstract class aUnit {
    //attributes
    protected int hp;
    protected int movement;
    protected int defense;
    protected int attack;
    protected int range = 1;
    protected int posX;
    protected int posY;
    protected boolean moved = false;

    //Constructor
    protected aUnit(int hp, int movement, int defense, int attack, int posX, int posY) {
        this.hp = hp;
        this.movement = movement;
        this.defense = defense;
        this.attack = attack;
        this.posX = posX;
        this.posY = posY;
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
    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
    public boolean isMoved() {
        return moved;
    }

    // Methods
    public void setPos(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

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

    protected boolean isAtRange(aUnit opponent){
        //TODO: Fix range to check if wall is in the way.
        return abs(posX - opponent.getPosX()) + (abs(posY - opponent.getPosY())) <= range;
    }





}
