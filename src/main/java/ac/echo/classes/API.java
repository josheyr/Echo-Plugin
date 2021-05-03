package ac.echo.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.md_5.bungee.api.ChatColor;

public class API {

    public API() {

    }

    public String getRequest(String url_string) {
        URL url;
        try {
            url = new URL(url_string);

            HttpURLConnection con;
            try {
                con = (HttpURLConnection) url.openConnection();

                try {
                    con.setRequestMethod("GET");

                    con.connect();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    return content.toString();

                } catch (ProtocolException e) {
                    Bukkit.getLogger().warning(e.getMessage());
                    e.printStackTrace();
                    return "";
                }
            } catch (IOException e) {
                Bukkit.getLogger().warning(e.getMessage());
                e.printStackTrace();
                return "";
            }
        } catch (MalformedURLException e) {
            Bukkit.getLogger().warning(e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    public ArrayList<String> getAlts(String key, String username, Player p){

        String meResponse = getRequest("https://api.echo.ac/query/player?key=" + key + "&player=" + username);


        if(meResponse == ""){
            p.sendMessage(ChatColor.RED + "Couldn't reach Echo servers (API DOWN?).");
            return null;
        }

        try {
            JSONParser parser = new JSONParser();
            Object response = parser.parse(meResponse);

            JSONObject json_response = (JSONObject)response;
            
            if((Boolean)json_response.get("success") == true){
                return (JSONArray)((JSONObject)json_response.get("result")).get("alts");
            }else{
                p.sendMessage(ChatColor.RED + "Error from Echo servers: " + (String)json_response.get("message") + ".");
                return null;
            }

        } catch (ParseException e) {
            Bukkit.getLogger().warning(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Boolean isValidKey(String key, Player p) {
        p.sendMessage("Checking your key before adding to register...");

        String meResponse = getRequest("https://api.echo.ac/query/me?key=" + key);

        if(meResponse == ""){
            p.sendMessage(ChatColor.RED + "Couldn't reach Echo servers (API DOWN?).");
            return false;
        }
        try {
            JSONParser parser = new JSONParser();
            Object response = parser.parse(meResponse);

            JSONObject json_response = (JSONObject)response;
            
            if((Boolean)json_response.get("success") == true){
                p.sendMessage(ChatColor.WHITE + "Hello " + ChatColor.AQUA + (String)((JSONObject)json_response.get("result")).get("username") + ChatColor.WHITE + ", your license has been found and key is now registered. You may now use Echo ingame!");
                return true;
            }else{
                p.sendMessage(ChatColor.RED + "Error from Echo servers: " + (String)json_response.get("message") + ".");
                return false;
            }

        } catch (ParseException e) {
            Bukkit.getLogger().warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
