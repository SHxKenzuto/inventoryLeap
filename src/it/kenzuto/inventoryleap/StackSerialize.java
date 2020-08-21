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

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

public class StackSerialize {
    private HashMap<String, ItemStack[]> stack;

    StackSerialize(){this.stack = new HashMap<>();}
    StackSerialize(HashMap<String,ItemStack[]> s){
        this.stack=s;
    }
    StackSerialize(StackSerialize c){ this.stack = c.stack;}

    HashMap<String,ItemStack[]> getStack(){
        return stack;
    }
    public void setStack(HashMap<String,ItemStack[]> stack){this.stack = stack;}

    private String stack_to_base64(ItemStack[] items) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BukkitObjectOutputStream object_output = new BukkitObjectOutputStream(output);
        object_output.writeInt(items.length);
        for (ItemStack item : items) {
            object_output.writeObject(item);
        }
        object_output.close();
        return Base64.getEncoder().encodeToString(output.toByteArray());
    }

    private ItemStack[] stack_from_base64(String data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream input = new ByteArrayInputStream(Base64.getDecoder().decode(data));
        BukkitObjectInputStream object_input = new BukkitObjectInputStream(input);
        ItemStack[] items = new ItemStack[object_input.readInt()];
        for (int i = 0; i < items.length; i++) {
            items[i] = (ItemStack) object_input.readObject();
        }
        object_input.close();
        return items;
    }

    private void write_to_file(String name, String content, boolean append) throws IOException {
        FileWriter f= new FileWriter(name,append);
        f.write(content);
        f.close();
    }

    @NotNull
    private String read_from_file(String name) throws IOException {
        String stck=new String(Files.readAllBytes(Paths.get(name)));
        return stck;
    }

    private ArrayList<String> read_list(String filename) throws FileNotFoundException {
        File f = new File("logs/PlayersSaved.txt");
        Scanner sc = new Scanner(f);
        ArrayList<String> str_list= new ArrayList<>();
        while(sc.hasNext()){
            str_list.add(sc.next());
        }
        return str_list;
    }

    public void SerializeStack() throws IOException {
        File f = new File("logs/PlayersSaved.txt");
        if(f.exists()){
            f.delete();
        }
        for(String s : stack.keySet()){
            write_to_file("logs/PlayersSaved.txt",s+"\n",true);
            write_to_file("logs/hash"+s+".txt",stack_to_base64(stack.get(s)),false);
        }
        System.out.println("Hashes saved correctly");
    }

    public void DeserializeStack() throws IOException, ClassNotFoundException {
        File f = new File("logs/PlayersSaved.txt");
        if(f.exists()) {
            ArrayList<String> str_list;
            ItemStack[] itms;
            str_list = read_list("logs/PlayersSaved.txt");
            for(String s : str_list){
            itms = stack_from_base64(read_from_file("logs/hash"+s+".txt"));
            this.stack.put(s, itms);
            }
            System.out.println("Hashes loaded correctly.");
        }else{
            System.out.println("Hashes not found.");
        }
    }
}
