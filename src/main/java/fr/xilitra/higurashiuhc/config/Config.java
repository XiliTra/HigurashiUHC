package fr.xilitra.higurashiuhc.config;

import java.util.HashMap;

public class Config {

    String configName;

    public static Config generateDefaultConfig(String configName){
        Config config = new Config(configName);
        for(ConfigLocation configLocation : ConfigLocation.values()){
            config.set(configLocation, configLocation.getDefaultValue());
        }
        return config;
    }

    private Config(String configName){
        this.configName = configName;
    }

    public String getConfigName(){
        return configName;
    }

    public void setConfigName(String configName){
        this.configName = configName;
    }

    private final HashMap<String, Object> configMap = new HashMap<>();

    public Object getOrSet(ConfigLocation configLocation){
        if(configMap.containsKey(configLocation.getName()))
            return configMap.get(configLocation.getName());
        configMap.put(configLocation.getName(), configLocation.getDefaultValue());
        return configLocation.getDefaultValue();
    }

    public Object get(ConfigLocation configLocation){
        return configMap.get(configLocation.getName());
    }

    public void set(ConfigLocation configLocation, Object value){
        remove(configLocation);
        configMap.put(configLocation.getName(), value);
    }

    public Object remove(ConfigLocation configLocation){
        return configMap.remove(configLocation.getName());
    }

    public int getInt(ConfigLocation configLocation){
        return (int) get(configLocation);
    }

    public String getString(ConfigLocation configLocation){
        return (String) get(configLocation);
    }

    public Boolean getBoolean(ConfigLocation configLocation){
        return (boolean) get(configLocation);
    }

}
