package me.vargaszabolcs.gamemodes.fortdefense;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class Wave {
    public EntityType[] getEntitiesToSpawn() {
        return entitiesToSpawn;
    }

    public int[] getNumbersToSpawn() {
        return numbersToSpawn;
    }

    private EntityType[] entitiesToSpawn;
    private int[] numbersToSpawn; //Each must correspond with an entity in the above list

    public int getTotalEntitiesToSpawn() {
        int total = 0;
        for (int i : numbersToSpawn){
            total += i;
        }
        return total;
    }


    Wave (EntityType[] ent, int[] num){
        entitiesToSpawn = ent;
        numbersToSpawn = num;
    }


}
