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

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.io.*;


public class LeapMachine implements CommandExecutor, Listener {
    private static boolean flag=true;
    private static StackSerialize dict= new StackSerialize();
    @Override
    public boolean onCommand(CommandSender sender, Command com, String s, String[] strings) {
        if(sender instanceof ConsoleCommandSender || (sender instanceof Player && sender.isOp())){
            if(com.getName().equalsIgnoreCase("timeleap") && strings.length==1){
                for(Player p: Bukkit.getOnlinePlayers()){
                    if(strings[0].equals(p.getName()) && dict.getStack().containsKey(p.getName())){
                        p.getInventory().setContents(dict.getStack().get(p.getName()));
                        return true;
                    }else{
                        sender.sendMessage("Player not found or not dead yet.");
                        return true;
                    }
                }
            }
            if(com.getName().equalsIgnoreCase("leapmachine") && strings.length==1){
                if(strings[0].equalsIgnoreCase("ON")){
                    flag=true;
                    return true;
                }else if(strings[0].equalsIgnoreCase("OFF")){
                    flag=false;
                    return true;
                }else{
                    return false;
                }
            }
            if(com.getName().equalsIgnoreCase("listinventory") && strings.length==1){
                for(Player p: Bukkit.getOnlinePlayers()){
                    if(strings[0].equals(p.getName()) && dict.getStack().containsKey(p.getName())){
                        sender.sendMessage("Inventory: ");
                        ItemStack[] itms=dict.getStack().get(p.getName()).clone();
                        for(ItemStack i : itms){
                            if(i != null){
                                sender.sendMessage(i.getType().toString() + " x" + i.getAmount());
                            }
                        }
                        return true;
                    }else{
                        sender.sendMessage("Player not found or not dead yet.");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static StackSerialize get_serializer(){
        return dict;
    }

    @EventHandler
    public void death_mark(PlayerDeathEvent e) throws IOException {
        if(flag) {
            String p=e.getEntity().getName();
            ItemStack[] items = e.getEntity().getInventory().getContents();
            dict.getStack().put(p, items);
            try {
                FileWriter file = new FileWriter("logs/inventoryof" + p + ".txt", false);
                for(ItemStack i : items){
                    if(i != null) {
                        file.write(i.getType().toString()+" x"+i.getAmount()+"\n");
                    }
                }
                file.close();
            } catch (IOException g) {
                g.printStackTrace();
            }
        }
    }
}
