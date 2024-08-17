package com.imjustdoom.bettermessages.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker {

    public static String checkUpdates(String version) throws IOException, URISyntaxException {
        JsonElement jsonElement = getJsonFromUrl("https://api.imjustdoom.com/projects/better-messages");

        if (jsonElement.isJsonNull() || !jsonElement.getAsJsonObject().get("error").isJsonNull()) {
            throw new IOException("Failed to check for updates");
        }

        JsonElement latestVersion = getJsonFromUrl("https://api.imjustdoom.com/projects/" + jsonElement.getAsJsonObject().get("id").getAsString() + "/latest");

        if (latestVersion.isJsonNull() || !jsonElement.getAsJsonObject().get("error").isJsonNull()) {
            throw new IOException("Failed to check for updates");
        }

        if (latestVersion.getAsJsonObject().get("version").getAsString().equals(version)) {
            return "You are running the latest version of BetterMessages!";
        } else {
            return "There is a new version of BetterMessages available! You are running version " + version + " and the latest version is " + latestVersion.getAsJsonObject().get("version").getAsString() + ". Download it at https://imjustdoom.com/projects/better-messages";
        }
    }

    private static JsonElement getJsonFromUrl(String url) throws IOException, URISyntaxException {
        URL uri = new URI(url).toURL();
        URLConnection con = uri.openConnection();
        con.setRequestProperty("User-Agent", "BetterMessages");
        con.setReadTimeout(5000);
        con.setConnectTimeout(5000);
        con.setUseCaches(false);

        InputStream inputStream = con.getInputStream();

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        reader.setStrictness(Strictness.LENIENT);

        return JsonParser.parseReader(reader).getAsJsonObject();
    }
}
