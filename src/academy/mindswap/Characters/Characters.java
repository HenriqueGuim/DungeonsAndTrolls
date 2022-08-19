package academy.mindswap.Characters;

import academy.mindswap.GameInterfaces.Attackable;

import java.util.Random;

public abstract class Characters implements Attackable {

    private int health;

    private int damage;

    private int dodgeUsage = 0;
    private boolean chooseDodge = false;
    private boolean chooseDefend = false;



    public Characters(int health, int damage) {
        this.health = health;
        this.damage = damage;
    }

    public int getInitialHealth() {
        return health;
    }

    public int getInitialDamage() {
        return damage;
    }

    public void defense (int damage){
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

    public int attack(){
        return damage;

    }
    public void sufferAttack(int damage){
        if (chooseDodge){ // if we choose to dodge, it will call de tryToDodge method;
            tryToDodge(damage);
            return;
        }
        if (chooseDefend){ //if we choose to dodge, it will call de decreaseHealth method;
            defense(damage);
            return;
        }

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
    public boolean isdead(){
        return health <=0;
    }

    @Override
    public void die() {
        if (isdead()){
            health = 0;
        }
    }
}
