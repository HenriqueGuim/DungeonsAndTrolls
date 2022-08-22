package academy.mindswap.ServerElements.GameElements.PlayerCharacters;

import academy.mindswap.ServerElements.GameElements.GameInterfaces.Attackable;

import java.util.Random;

public abstract class Character implements Attackable {

    private int health;

    private int damage;

    private int dodgeUsage = 0;
    private boolean chooseDodge = false;
    private boolean chooseDefend = false;



    public Character(int health, int damage) {
        this.health = health;
        this.damage = damage;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public void defend(int damage){
        decreaseHealth(damage / 2);
        chooseDefend = false;
    }

    public void chooseDodge(){
        chooseDodge = true;
    }

    public void chooseDefend(){
        chooseDodge = true;
    }

        @Override
    public void increaseHealth(int health) {
        this.health += health;
    }

        @Override
        public void decreaseHealth(int health) {
        this.health -= health;

    }

    public int attack() {
        if (chooseDodge || chooseDefend){
            return 0;

        }
        return damage;
    }




    public void sufferAttack(int damage){
        if (chooseDodge){ // if we choose to dodge, it will call de tryToDodge method;
            tryToDodge(damage);
            return;
        }
        if (chooseDefend){ //if we choose to dodge, it will call de decreaseHealth method;
            defend(damage);
            return;
        }
        decreaseHealth(damage);

    }


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

    @Override
    public boolean isDead(){
        return health <=0;
    }

    @Override
    public void die() {
        if (isDead()){
            health = 0;
        }
    }
}
