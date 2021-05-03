package ac.echo.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Config {

    String location = "config.json";
    Map<String, String> users;

    public Config() {
        users = new HashMap<String, String>();
    }
    

    public void exportConfig() {
        File file = new File(location);

        try {
            file.createNewFile();

            JSONObject mainObject = new JSONObject();
            mainObject.put("users", new JSONObject(users));

            FileWriter fileWriter = new FileWriter(location);
            fileWriter.write(mainObject.toJSONString());
            fileWriter.close();

        } catch (IOException e) {
            Bukkit.getLogger().warning(e.getMessage());
            e.printStackTrace();
        }
    }

    public void importConfig() {
        JSONParser parser = new JSONParser();

        File file = new File(location);

        try {
            if(file.createNewFile()){
            
            JSONObject mainObject = new JSONObject();
                mainObject.put("users", new JSONObject(users));

                FileWriter fileWriter = new FileWriter(location);
                fileWriter.write(mainObject.toJSONString());
                fileWriter.close();
            }
        } catch (IOException e) {

            Bukkit.getLogger().warning("Error occured while handling config file.");
            e.printStackTrace();
        }

        File newFile = new File(location);

        Scanner fileReader;
        try {
            fileReader = new Scanner(newFile);

            try {
                Object config = parser.parse(fileReader.nextLine());
                JSONObject jConfig = (JSONObject)config;

                users = ((HashMap)jConfig.get("users"));
            
                
            } catch (ParseException e) {
                Bukkit.getLogger().warning(e.getMessage());
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            Bukkit.getLogger().warning(e.getMessage());
            e.printStackTrace();
        }

        
    }

    public String getKey(String uuid){
        if(!users.containsKey(uuid)){
            return null;
        }
        return users.get(uuid);
    }

    public void addUser(String uuid, String key) {
        users.put(uuid, key);

        new Thread(() -> {
            exportConfig();
        }).start();
    }

}
