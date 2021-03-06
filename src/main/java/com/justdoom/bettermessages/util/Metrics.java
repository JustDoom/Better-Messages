package com.justdoom.bettermessages.util;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class Metrics {

    public static final int B_STATS_VERSION = 1;

    private static final String URL = "https://bStats.org/submitData/bukkit";

    private boolean enabled;

    private static boolean logFailedRequests;

    private static boolean logSentData;

    private static boolean logResponseStatusText;

    private static String serverUUID;

    private final Plugin plugin;

    private final int pluginId;

    static {
        if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
            String defaultPackage = new String(new byte[] {
                    111, 114, 103, 46, 98, 115, 116, 97, 116, 115,
                    46, 98, 117, 107, 107, 105, 116 });
            String examplePackage = new String(new byte[] {
                    121, 111, 117, 114, 46, 112, 97, 99, 107, 97,
                    103, 101 });
            if (Metrics.class.getPackage().getName().equals(defaultPackage) || Metrics.class.getPackage().getName().equals(examplePackage))
                throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
        }
    }

    private final List<CustomChart> charts = new ArrayList<>();

    public Metrics(Plugin plugin, int pluginId) {
        if (plugin == null)
            throw new IllegalArgumentException("Plugin cannot be null!");
        this.plugin = plugin;
        this.pluginId = pluginId;
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File configFile = new File(bStatsFolder, "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("serverUuid")) {
            config.addDefault("enabled", Boolean.valueOf(true));
            config.addDefault("serverUuid", UUID.randomUUID().toString());
            config.addDefault("logFailedRequests", Boolean.valueOf(false));
            config.addDefault("logSentData", Boolean.valueOf(false));
            config.addDefault("logResponseStatusText", Boolean.valueOf(false));
            config.options().header("bStats collects some data for plugin authors like how many servers are using their plugins.\nTo honor their work, you should not disable it.\nThis has nearly no effect on the server performance!\nCheck out https://bStats.org/ to learn more :)")

                    .copyDefaults(true);
            try {
                config.save(configFile);
            } catch (IOException iOException) {}
        }
        this.enabled = config.getBoolean("enabled", true);
        serverUUID = config.getString("serverUuid");
        logFailedRequests = config.getBoolean("logFailedRequests", false);
        logSentData = config.getBoolean("logSentData", false);
        logResponseStatusText = config.getBoolean("logResponseStatusText", false);
        if (this.enabled) {
            boolean found = false;
            for (Class<?> service : (Iterable<Class<?>>)Bukkit.getServicesManager().getKnownServices()) {
                try {
                    service.getField("B_STATS_VERSION");
                    found = true;
                    break;
                } catch (NoSuchFieldException noSuchFieldException) {}
            }
            Bukkit.getServicesManager().register(Metrics.class, this, plugin, ServicePriority.Normal);
            if (!found)
                startSubmitting();
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void addCustomChart(CustomChart chart) {
        if (chart == null)
            throw new IllegalArgumentException("Chart cannot be null!");
        this.charts.add(chart);
    }

    private void startSubmitting() {
        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (!Metrics.this.plugin.isEnabled()) {
                    timer.cancel();
                    return;
                }
                Bukkit.getScheduler().runTask(Metrics.this.plugin, () -> Metrics.this.submitData());
            }
        }300000L, 1800000L);
    }

    public JsonObject getPluginData() {
        JsonObject data = new JsonObject();
        String pluginName = this.plugin.getDescription().getName();
        String pluginVersion = this.plugin.getDescription().getVersion();
        data.addProperty("pluginName", pluginName);
        data.addProperty("id", Integer.valueOf(this.pluginId));
        data.addProperty("pluginVersion", pluginVersion);
        JsonArray customCharts = new JsonArray();
        for (CustomChart customChart : this.charts) {
            JsonObject chart = customChart.getRequestJsonObject();
            if (chart == null)
                continue;
            customCharts.add((JsonElement)chart);
        }
        data.add("customCharts", (JsonElement)customCharts);
        return data;
    }

    private JsonObject getServerData() {
        int playerAmount;
        try {
            Method onlinePlayersMethod = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers", new Class[0]);
            playerAmount = onlinePlayersMethod.getReturnType().equals(Collection.class) ? ((Collection)onlinePlayersMethod.invoke(Bukkit.getServer(), new Object[0])).size() : ((Player[])onlinePlayersMethod.invoke(Bukkit.getServer(), new Object[0])).length;
        } catch (Exception e) {
            playerAmount = Bukkit.getOnlinePlayers().size();
        }
        int onlineMode = Bukkit.getOnlineMode() ? 1 : 0;
        String bukkitVersion = Bukkit.getVersion();
        String bukkitName = Bukkit.getName();
        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        int coreCount = Runtime.getRuntime().availableProcessors();
        JsonObject data = new JsonObject();
        data.addProperty("serverUUID", serverUUID);
        data.addProperty("playerAmount", Integer.valueOf(playerAmount));
        data.addProperty("onlineMode", Integer.valueOf(onlineMode));
        data.addProperty("bukkitVersion", bukkitVersion);
        data.addProperty("bukkitName", bukkitName);
        data.addProperty("javaVersion", javaVersion);
        data.addProperty("osName", osName);
        data.addProperty("osArch", osArch);
        data.addProperty("osVersion", osVersion);
        data.addProperty("coreCount", Integer.valueOf(coreCount));
        return data;
    }

    private void submitData() {
        JsonObject data = getServerData();
        JsonArray pluginData = new JsonArray();
        for (Class<?> service : (Iterable<Class<?>>)Bukkit.getServicesManager().getKnownServices()) {
            try {
                service.getField("B_STATS_VERSION");
                for (RegisteredServiceProvider<?> provider : (Iterable<RegisteredServiceProvider<?>>)Bukkit.getServicesManager().getRegistrations(service)) {
                    try {
                        Object plugin = provider.getService().getMethod("getPluginData", new Class[0]).invoke(provider.getProvider(), new Object[0]);
                        if (plugin instanceof JsonObject) {
                            pluginData.add((JsonElement)plugin);
                            continue;
                        }
                        try {
                            Class<?> jsonObjectJsonSimple = Class.forName("org.json.simple.JSONObject");
                            if (plugin.getClass().isAssignableFrom(jsonObjectJsonSimple)) {
                                Method jsonStringGetter = jsonObjectJsonSimple.getDeclaredMethod("toJSONString", new Class[0]);
                                jsonStringGetter.setAccessible(true);
                                String jsonString = (String)jsonStringGetter.invoke(plugin, new Object[0]);
                                JsonObject object = (new JsonParser()).parse(jsonString).getAsJsonObject();
                                pluginData.add((JsonElement)object);
                            }
                        } catch (ClassNotFoundException e) {
                            if (logFailedRequests)
                                this.plugin.getLogger().log(Level.SEVERE, "Encountered unexpected exception", e);
                        }
                    } catch (NullPointerException|NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException nullPointerException) {}
                }
            } catch (NoSuchFieldException noSuchFieldException) {}
        }
        data.add("plugins", (JsonElement)pluginData);
        (new Thread(() -> {
            try {
                sendData(this.plugin, data);
            } catch (Exception e) {
                if (logFailedRequests)
                    this.plugin.getLogger().log(Level.WARNING, "Could not submit plugin stats of " + this.plugin.getName(), e);
            }
        })).start();
    }

    private static void sendData(Plugin plugin, JsonObject data) throws Exception {
        if (data == null)
            throw new IllegalArgumentException("Data cannot be null!");
        if (Bukkit.isPrimaryThread())
            throw new IllegalAccessException("This method must not be called from the main thread!");
        if (logSentData)
            plugin.getLogger().info("Sending data to bStats: " + data);
        HttpsURLConnection connection = (HttpsURLConnection)(new URL("https://bStats.org/submitData/bukkit")).openConnection();
        byte[] compressedData = compress(data.toString());
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip");
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "MC-Server/1");
        connection.setDoOutput(true);
        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.write(compressedData);
        }
        StringBuilder builder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null)
                builder.append(line);
        }
        if (logResponseStatusText)
            plugin.getLogger().info("Sent data to bStats and received response: " + builder);
    }

    private static byte[] compress(String str) throws IOException {
        if (str == null)
            return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
        }
        return outputStream.toByteArray();
    }

    public static abstract class CustomChart {
        final String chartId;

        CustomChart(String chartId) {
            if (chartId == null || chartId.isEmpty())
                throw new IllegalArgumentException("ChartId cannot be null or empty!");
            this.chartId = chartId;
        }

        private JsonObject getRequestJsonObject() {
            JsonObject chart = new JsonObject();
            chart.addProperty("chartId", this.chartId);
            try {
                JsonObject data = getChartData();
                if (data == null)
                    return null;
                chart.add("data", (JsonElement)data);
            } catch (Throwable t) {
                if (Metrics.logFailedRequests)
                    Bukkit.getLogger().log(Level.WARNING, "Failed to get data for custom chart with id " + this.chartId, t);
                return null;
            }
            return chart;
        }

        protected abstract JsonObject getChartData() throws Exception;
    }

    public static class SimplePie extends CustomChart {
        private final Callable<String> callable;

        public SimplePie(String chartId, Callable<String> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            String value = this.callable.call();
            if (value == null || value.isEmpty())
                return null;
            data.addProperty("value", value);
            return data;
        }
    }

    public static class AdvancedPie extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty())
                return null;
            boolean allSkipped = true;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (((Integer)entry.getValue()).intValue() == 0)
                    continue;
                allSkipped = false;
                values.addProperty(entry.getKey(), entry.getValue());
            }
            if (allSkipped)
                return null;
            data.add("values", (JsonElement)values);
            return data;
        }
    }

    public static class DrilldownPie extends CustomChart {
        private final Callable<Map<String, Map<String, Integer>>> callable;

        public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
            super(chartId);
            this.callable = callable;
        }

        public JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Map<String, Integer>> map = this.callable.call();
            if (map == null || map.isEmpty())
                return null;
            boolean reallyAllSkipped = true;
            for (Map.Entry<String, Map<String, Integer>> entryValues : map.entrySet()) {
                JsonObject value = new JsonObject();
                boolean allSkipped = true;
                for (Map.Entry<String, Integer> valueEntry : (Iterable<Map.Entry<String, Integer>>)((Map)map.get(entryValues.getKey())).entrySet()) {
                    value.addProperty(valueEntry.getKey(), valueEntry.getValue());
                    allSkipped = false;
                }
                if (!allSkipped) {
                    reallyAllSkipped = false;
                    values.add(entryValues.getKey(), (JsonElement)value);
                }
            }
            if (reallyAllSkipped)
                return null;
            data.add("values", (JsonElement)values);
            return data;
        }
    }

    public static class SingleLineChart extends CustomChart {
        private final Callable<Integer> callable;

        public SingleLineChart(String chartId, Callable<Integer> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            int value = ((Integer)this.callable.call()).intValue();
            if (value == 0)
                return null;
            data.addProperty("value", Integer.valueOf(value));
            return data;
        }
    }

    public static class MultiLineChart extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public MultiLineChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty())
                return null;
            boolean allSkipped = true;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (((Integer)entry.getValue()).intValue() == 0)
                    continue;
                allSkipped = false;
                values.addProperty(entry.getKey(), entry.getValue());
            }
            if (allSkipped)
                return null;
            data.add("values", (JsonElement)values);
            return data;
        }
    }

    public static class SimpleBarChart extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty())
                return null;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                JsonArray categoryValues = new JsonArray();
                categoryValues.add((JsonElement)new JsonPrimitive(entry.getValue()));
                values.add(entry.getKey(), (JsonElement)categoryValues);
            }
            data.add("values", (JsonElement)values);
            return data;
        }
    }

    public static class AdvancedBarChart extends CustomChart {
        private final Callable<Map<String, int[]>> callable;

        public AdvancedBarChart(String chartId, Callable<Map<String, int[]>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, int[]> map = this.callable.call();
            if (map == null || map.isEmpty())
                return null;
            boolean allSkipped = true;
            for (Map.Entry<String, int[]> entry : map.entrySet()) {
                if (((int[])entry.getValue()).length == 0)
                    continue;
                allSkipped = false;
                JsonArray categoryValues = new JsonArray();
                for (int categoryValue : (int[])entry.getValue())
                    categoryValues.add((JsonElement)new JsonPrimitive(Integer.valueOf(categoryValue)));
                values.add(entry.getKey(), (JsonElement)categoryValues);
            }
            if (allSkipped)
                return null;
            data.add("values", (JsonElement)values);
            return data;
        }
    }
}
