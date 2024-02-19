package com.imjustdoom.bettermessages.config;

public interface ConfigLoader {

    void load();

    void save();

    void setHeader(String header);

    String getString(String path, String defaultString);

    boolean getBoolean(String path, boolean defaultBoolean);

    int getInt(String path, int defaultInt);

    void generatePath(String path);

    void generatePath(String... path);
}
