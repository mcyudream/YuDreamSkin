package online.yudream.yudreamskin.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import online.yudream.yudreamskin.service.PluginLoaderService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class PluginLoaderServiceImpl implements PluginLoaderService {

    @Resource
    private ApplicationContext applicationContext;

    private final Map<String, URLClassLoader> pluginClassLoaders = new ConcurrentHashMap<>();
    private final Map<String, String> pluginTemplates = new ConcurrentHashMap<>();

    @Override
    public void loadPlugin(File pluginJar) {
        try {
            URLClassLoader classLoader = new URLClassLoader(
                    new URL[]{pluginJar.toURI().toURL()},
                    Thread.currentThread().getContextClassLoader()
            );

            //todo 从 plugin 获取基本信息
            String pluginId = "test";
            String templatePathBase = "plugin-templates";

            log.info("Loading plugin " + pluginId + " from " + pluginJar);
            pluginTemplates.put(pluginId, templatePathBase);
            pluginClassLoaders.put(pluginId, classLoader);
            refreshTemplateEngine();
        } catch (MalformedURLException ignored) {

        }

    }

    @Override
    public Set<Map.Entry<String, String>> getTemplatesBasePath(){
        return pluginTemplates.entrySet();
    }

    @Override
    public ClassLoader getPluginClassLoader(String pluginId){
        return pluginClassLoaders.get(pluginId);
    }

    @Override
    public void unloadPlugin(String pluginId) {
        URLClassLoader classLoader = pluginClassLoaders.remove(pluginId);
        if (classLoader != null) {
            pluginTemplates.remove(pluginId);
            try {
                classLoader.close();
            } catch (IOException e) {
                // 日志记录
            }
            refreshTemplateEngine();
        }
    }

    private void refreshTemplateEngine() {
        SpringTemplateEngine engine = applicationContext.getBean(SpringTemplateEngine.class);
        engine.clearTemplateCache();
    }
}