package online.yudream.yudreamskin.service;

import java.io.File;
import java.util.Map;
import java.util.Set;

public interface PluginLoaderService {

    void loadPlugin(File pluginJar);

    Set<Map.Entry<String, String>> getTemplatesBasePath();

    ClassLoader getPluginClassLoader(String pluginId);

    void unloadPlugin(String pluginId);
}
