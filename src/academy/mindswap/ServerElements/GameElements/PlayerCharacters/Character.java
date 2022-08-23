package academy.mindswap.ServerElements.GameElements.PlayerCharacters;

import academy.mindswap.ServerElements.GameElements.GameInterfaces.Attackable;

import java.util.Random;

/**
 * This class represents the common attributes and actions for all player's character in the game
 */

public abstract class Character implements Attackable {

    private int health;

    private int damage;

    private int dodgeUsage = 0;
    private boolean chooseDodge = false;
    private boolean chooseDefend = false;


    /**
     * Class constructor with the given health and damage parameters
     * @param health amount of health
     * @param damage amount of damage (attack value)
     */
    public Character(int health, int damage) {
        this.health = health;
        this.damage = damage;
    }

    /**
     *  Method to get the health of the character
     * @return the amount of health character has
     */
    public int getHealth() {
        return health;
    }

    /**
     * Method to get the damage level of the character
     * @return
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Method to set the defend action when the character is attacked
     * @param damage the amount of decrease health when the character is defending from the attack
     *
     */

    public void defend(int damage){
        decreaseHealth(damage / 2);
    }

    /**
     * Method to check if the player choose to dodge from the attack
     */

    public void chooseDodge(){
        chooseDodge = true;
    }

    /**
     * Method to check if the player choose to defend from the attack
     */

    public void chooseDefend(){
        chooseDefend = true;
    }

    /**
     * Method to increase the amount of health of the character
     * @param health amount of health to increase
     */
    @Override
    public void increaseHealth(int health) {
        this.health += health;
    }

    /**
     * Method to decrease the amount of health of the character
     * @param health amount of health to decrease
     */

    @Override
    public void decreaseHealth(int health) {
        this.health -= health;

    }

    /**
     * Attack method for the character
     * @return 0 damage if the player choose to dodge or defend
     */

    public int attack() {
        if (chooseDodge || chooseDefend){
            chooseDodge = false;
            chooseDefend = false;

            return 0;
        }
        return damage;
    }


    /**
     * Method to determine whether the characters suffer an attack when the player choose to dodge and defend
     * @param damage amount of damage suffered by the attack
     *
     */
    public void sufferAttack(int damage){
        if (chooseDodge){
            tryToDodge(damage);
            return;
        }
        if (chooseDefend){
            defend(damage);
            return;
        }
        decreaseHealth(damage);

    }

    /**
     * This method is called when the player try to dodge multiple times
     * @param damage the amount of damage suffered after first usage of dodge
     */

    public void tryToDodge(int damage){
        int probability = new Random().nextInt(10);
        chooseDodge = false;
        if(dodgeUsage == 0){ //Number os usage (0 = first usage)
            dodgeUsage++;
            return;
        }

        if(dodgeUsage == 1){
            if(probability > 4){
                decreaseHealth(damage);
                dodgeUsage = 0;
                return;
            }
            dodgeUsage++;
            return;
        }
        if (dodgeUsage > 1){
            if(probability > 7){
                decreaseHealth(damage);
                dodgeUsage = 0;
                return;
            }
            dodgeUsage++;
            return;
        }



    }

    /**
     * Method to determine if the character is dead
     * @return true if the character is dead based on the character stats
     */

    @Override
    public boolean isDead(){
        return health <=0;
    }

    /**
     * Method to stop the character's actions when dead
     */

    @Override
    public void die() {
        if (isDead()){
            health = 0;
            damage = 0;
        }
    }
}