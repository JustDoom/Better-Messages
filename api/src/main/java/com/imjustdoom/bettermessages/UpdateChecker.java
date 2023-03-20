package com.imjustdoom.bettermessages;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker {

    public static String checkUpdates(String version) throws IOException {
        JsonElement jsonElement = getJsonFromUrl("https://api.imjustdoom.com/projects/better-messages");

        // Check if it got the json
        if (jsonElement == null) {
            throw new IOException("Failed to check for updates");
        }

        // Get the latest version (better way is coming soon)
        JsonElement latestVersion = getJsonFromUrl("https://api.imjustdoom.com/projects/" + jsonElement.getAsJsonObject().get("id").getAsString() + "/latest");

        // Check if it got the json for latest version
        if (latestVersion == null) {
            throw new IOException("Failed to check for updates");
        }

        // Get the latest version and check the current version
        if (latestVersion.getAsJsonObject().get("version").getAsString().equals(version)) {
            return "You are running the latest version of BetterMessages!";
        } else {
            return "There is a new version of BetterMessages available! You are running version " + version + " and the latest version is " + latestVersion.getAsJsonObject().get("version").getAsString() + ". Download it at https://imjustdoom.com/projects/better-messages";
        }
    }

    private static JsonElement getJsonFromUrl(String url) throws IOException {
        URL uri = new URL(url);
        URLConnection con = uri.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla");
        con.setReadTimeout(5000);
        con.setConnectTimeout(5000);
        con.setUseCaches(false);

        InputStream inputStream = con.getInputStream();

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        reader.setLenient(true);

        return new JsonParser().parse(reader).getAsJsonObject();
    }
}
