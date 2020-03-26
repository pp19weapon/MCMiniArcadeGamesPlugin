package me.vargaszabolcs.gamemodes.fortdefense;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class Wave {
    public ArrayList<EntityType> getEntitiesToSpawn() {
        return entitiesToSpawn;
    }

    public ArrayList<Integer> getNumbersToSpawn() {
        return numbersToSpawn;
    }

    private ArrayList<EntityType> entitiesToSpawn;
    private ArrayList<Integer> numbersToSpawn; //Each must correspond with an entity in the above list

    public int getTotalEntitiesToSpawn() {
        int total = 0;
        for (int i : numbersToSpawn){
            total += i;
        }
        return total;
    }


    Wave (ArrayList<EntityType> ent, ArrayList<Integer> num){
        entitiesToSpawn = ent;
        numbersToSpawn = num;
    }


}
