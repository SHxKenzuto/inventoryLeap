package it.kenzuto.inventoryleap;

import org.bukkit.plugin.java.JavaPlugin;


import java.io.IOException;
import java.util.Objects;

public class Main extends JavaPlugin {
    private LeapMachine l=new LeapMachine();
    private StackSerialize st = LeapMachine.get_serializer();

    public void onEnable(){
        Objects.requireNonNull(this.getCommand("timeleap")).setExecutor(l);
        Objects.requireNonNull(this.getCommand("leapmachine")).setExecutor(l);
        Objects.requireNonNull(this.getCommand("listinventory")).setExecutor(l);
        getServer().getPluginManager().registerEvents(l, this);
        try {
            st.DeserializeStack();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Plugin made by Kenzuto.");
    }

    public void onDisable(){
        try {
            st.SerializeStack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
