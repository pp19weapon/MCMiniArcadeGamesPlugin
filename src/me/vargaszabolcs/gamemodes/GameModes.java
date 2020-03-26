package me.vargaszabolcs.gamemodes;

import me.vargaszabolcs.gamemodes.fortdefense.FortDefenseHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class GameModes extends JavaPlugin {

    public Location centralLocation = null;
    public int fortSize = 25;
    public static int MAX_ALLOWED_MOBS = 20;
    public boolean isGameRunning = false;

    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("GameModes loaded up!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    FortDefenseHandler newFortDefense = null;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("fort")) {
            if (newFortDefense == null) {
                newFortDefense = new FortDefenseHandler(sender, this);
            }

            if (args[0].equalsIgnoreCase("start")) {
                newFortDefense.startGame();
            } else if (args[0].equalsIgnoreCase("stop")) {
                newFortDefense.stopGame();
                newFortDefense = null;
            } else {
                Bukkit.getPlayer(sender.getName()).sendMessage("Please enter start or stop after /fort command!");
            }

        }

        return false;
    }
}
