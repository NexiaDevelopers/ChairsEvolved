package net.nexia.chairsEvolved.utils;

import net.nexia.chairsEvolved.annotations.ResourceKey;
import net.nexia.chairsEvolved.annotations.YamlIgnore;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class CustomSerializer implements ConfigurationSerializable {

    final JavaPlugin _plugin;
    final String _path;

    public CustomSerializer(JavaPlugin plugin, String path) {
        this._plugin = plugin;
        this._path = path;
    }

    protected File loadConfigFile() {
        return loadConfigFile(_path);
    }

    protected File loadConfigFile(String path) {
        try {
            File file = new File(_plugin.getDataFolder(), path);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> obj = new HashMap<>();

        for (Field field : this.getClass().getDeclaredFields()) {

            if (!Modifier.isPublic(field.getModifiers())) {
                continue;
            }

            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (field.getAnnotation(YamlIgnore.class) != null) {
                continue;
            }

            ResourceKey resourceKey = field.getAnnotation(ResourceKey.class);
            String key = (resourceKey != null) ? resourceKey.value() : field.getName();

            try {
                obj.put(key, field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return obj;
    }

    public static <T extends CustomSerializer> T deserialize(Class<T> clazz, JavaPlugin plugin, String path) {
        try {
            T obj = clazz.getDeclaredConstructor(JavaPlugin.class, String.class).newInstance(plugin, path);
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(obj.loadConfigFile());

            for (Field field : clazz.getDeclaredFields()) {

                if (!Modifier.isPublic(field.getModifiers())) {
                    continue;
                }

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.getAnnotation(YamlIgnore.class) != null) {
                    continue;
                }

                ResourceKey resourceKey = field.getAnnotation(ResourceKey.class);
                String key = (resourceKey != null) ? resourceKey.value() : field.getName();

                Object value = yaml.get(key);
                if (value != null) {
                    field.set(obj, value);
                }
            }
            return obj;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        save(_path);
    }

    public void save(String path) {
        try {
            File file = loadConfigFile(path);
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            for (Map.Entry<String, Object> entry : serialize().entrySet()) {
                yaml.set(entry.getKey(), entry.getValue());
            }
            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
