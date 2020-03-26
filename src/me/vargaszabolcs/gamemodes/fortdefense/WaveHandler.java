package me.vargaszabolcs.gamemodes.fortdefense;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

public class WaveHandler {
    WaveHandler(){}

    int waveSpawnerTaskID;
    boolean isStarted = false;
    MonsterHandler monsterHandler;
    public void startWaves(FortDefenseHandler p_fortDefenseHandler){
        if (!isStarted) {
            monsterHandler = new MonsterHandler(p_fortDefenseHandler, 2, 5);
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

    Wave testWave = new Wave(new EntityType[]{EntityType.BLAZE, EntityType.SKELETON}, new int[]{20, 20});
    boolean alreadySpawned = false;

    @Override
    public void run() {
        if (isNighttime()) {
            if (alreadySpawned == false) {
                monsterHandler.spawnWave(testWave);
                alreadySpawned = true;
            }
        } else if (!isNighttime()) {
            if (alreadySpawned == true) {
                alreadySpawned = false;
            }
        }
    }
}
