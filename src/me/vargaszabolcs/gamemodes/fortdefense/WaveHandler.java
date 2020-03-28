package me.vargaszabolcs.gamemodes.fortdefense;

import net.minecraft.server.v1_15_R1.Entity;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;


import java.util.ArrayList;

public class WaveHandler {
    WaveHandler(){}

    int waveSpawnerTaskID;
    boolean isStarted = false;
    MonsterHandler monsterHandler;

    public void startWaves(FortDefenseHandler p_fortDefenseHandler){
        if (!isStarted) {
            monsterHandler = new MonsterHandler(p_fortDefenseHandler, 7, 7);
            monsterHandler.startCheckingMonstersInArea(p_fortDefenseHandler.plugin);

            WaveSpawner waveSpawner = new WaveSpawner(p_fortDefenseHandler.currentWorld, monsterHandler);
            waveSpawnerTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(p_fortDefenseHandler.plugin, waveSpawner, 20, 400);


            isStarted = true;
        }
    }

    public void stopWaves(){
        if (isStarted){
            Bukkit.getScheduler().cancelTask(waveSpawnerTaskID);
            monsterHandler.stopCheckingMonstersInArea();

            isStarted = false;
        }
    }
}

class WaveSpawner implements Runnable{
    World currentWorld;
    MonsterHandler monsterHandler;
    boolean alreadySpawned = false;
    int currentWave = 1;

    WaveSpawner(World p_world, MonsterHandler p_monsterHandler){
        currentWorld = p_world;
        monsterHandler = p_monsterHandler;
    }

    boolean isNighttime(){
        if ((currentWorld.getTime() < 12300) || (currentWorld.getTime() > 23850)){
            return false;
        }
        return true;
    }

    Wave wave;
    ArrayList<EntityType> entities = new ArrayList<EntityType>();
    ArrayList<Integer> count = new ArrayList<Integer>();

    @Override
    public void run() {
        if (isNighttime()) {
            if (alreadySpawned == false) {
                Bukkit.getServer().broadcastMessage("Wave " + currentWave + "!");
                switch (currentWave) {
                    case 1:
                        entities.add(EntityType.ZOMBIE);
                        count.add(10);
                        entities.add(EntityType.SKELETON);
                        count.add(10);
                        break;
                    case 3:
                        entities.add(EntityType.SPIDER);
                        count.add(10);
                        break;
                    case 6:
                        entities.add(EntityType.CREEPER);
                        count.add(2);
                        break;
                    case 10:
                        entities.add(EntityType.BLAZE);
                        count.add(1);
                        break;
                    case 15:
                        entities.add(EntityType.ENDERMAN);
                        count.add(1);
                        entities.add(EntityType.PHANTOM);
                        count.add(1);
                        break;
                    case 20:
                        entities.add(EntityType.WITCH);
                        count.add(1);
                        entities.add(EntityType.VEX);
                        count.add(1);
                        break;
                    case 25:
                        entities.add(EntityType.ENDER_DRAGON);
                        count.add(1);
                        break;
                }

                wave = new Wave(entities, count);
                monsterHandler.spawnWave(wave);
                alreadySpawned = true;
                currentWave++;

                // Multiplicators
                for (int i = 0; i < count.size(); i++){
                    count.set(i, count.get(i) + currentWave * 2);
                }
            }
        } else if (!isNighttime()) {
            if (alreadySpawned == true) {
                alreadySpawned = false;
            }
        }

    }
}
