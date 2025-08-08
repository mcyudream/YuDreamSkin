package online.yudream.yudreamskin.service.impl;

import jakarta.annotation.Resource;
import online.yudream.yudreamskin.service.PluginLoaderService;
import org.springframework.stereotype.Service;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresource.ClassLoaderTemplateResource;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;

@Service
public class PluginTemplateResolverImpl extends AbstractConfigurableTemplateResolver {

    @Resource
    PluginLoaderService pluginLoaderService;

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, String resourceName, String characterEncoding, Map<String, Object> templateResolutionAttributes) {
        for (Map.Entry<String, String> plugin : pluginLoaderService.getTemplatesBasePath()) {
            String fullPath = plugin.getValue() + "/" + resourceName;
            ITemplateResource resource = new ClassLoaderTemplateResource(pluginLoaderService.getPluginClassLoader(plugin.getKey()), fullPath, characterEncoding);
            if (resource.exists()) {
                return resource;
            }
        }
        return new ClassLoaderTemplateResource(null, resourceName, characterEncoding);
    }
}