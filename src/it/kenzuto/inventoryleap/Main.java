/* Copyright (C) 2020 SHxKenzuto
    This file is part of inventoryLeap.
    inventoryLeap is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    inventoryLeap is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with inventoryLeap.  If not, see <http://www.gnu.org/licenses/>.
*/

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
