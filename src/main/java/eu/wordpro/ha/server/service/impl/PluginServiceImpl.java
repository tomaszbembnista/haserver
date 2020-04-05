package eu.wordpro.ha.server.service.impl;

import eu.wordpro.ha.api.DisplayName;
import eu.wordpro.ha.api.Documentation;
import eu.wordpro.ha.api.SignalProcessor;
import eu.wordpro.ha.plugin.circuitSwitch.CircuitSwitch;
import eu.wordpro.ha.server.domain.Device;
import eu.wordpro.ha.server.service.PluginService;
import eu.wordpro.ha.server.service.dto.PluginDTO;
import org.apache.commons.io.IOUtils;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PluginServiceImpl implements PluginService {

    private boolean cacheFilled = false;
    private Map<String, CacheEntry> cache = new HashMap<>();

    @Override
    public List<PluginDTO> findAll() {
        fillCache();
        return cache.values().stream().map(elem -> elem.pluginDTO).collect(Collectors.toList());
    }

    @Override
    public PluginDTO findByClassName(String className) {
        fillCache();
        CacheEntry cacheEntry = cache.get(className);
        if (cacheEntry != null){
            return cacheEntry.pluginDTO;
        }
        return null;
    }

    @Override
    public String getDocumentationByClassName(String className) {
        fillCache();
        CacheEntry cacheEntry = cache.get(className);
        if (cacheEntry != null){
            return cacheEntry.documentation;
        }
        return null;
    }

    @Override
    public synchronized void clearCache() {
        cache.clear();
        cacheFilled = false;
    }

    private synchronized void fillCache() {
        if (cacheFilled){
            return;
        }
        Reflections reflections = new Reflections("eu");
        Set<Class<? extends SignalProcessor>> processorClasses = reflections.getSubTypesOf(SignalProcessor.class);
        for (Class<? extends SignalProcessor> clazz : processorClasses) {
            CacheEntry entry = newCacheEntry(clazz);
            cache.put(entry.pluginDTO.getClassName(), entry);
        }
        cacheFilled = true;
    }

    private CacheEntry newCacheEntry(Class<? extends SignalProcessor> clazz) {
        CacheEntry result = new CacheEntry();
        PluginDTO dto = new PluginDTO();
        dto.setClassName(clazz.getCanonicalName());
        DisplayName displayNameAnnotation = clazz.getAnnotation(DisplayName.class);
        if (displayNameAnnotation != null) {
            dto.setDisplayName(displayNameAnnotation.value());
        }
        result.pluginDTO = dto;
        result.clazz = clazz;
        result.documentation = getDocumentation(clazz);
        return result;
    }

    private String getDocumentation(Class<? extends SignalProcessor> clazz) {
        Documentation documentationAnnotation = clazz.getAnnotation(Documentation.class);
        if (documentationAnnotation == null){
            return "";
        }
        if (documentationAnnotation.pathToFile() != null && documentationAnnotation.pathToFile().trim().length() > 0) {
            try {
                InputStream inputStream = clazz.getResourceAsStream("/" + documentationAnnotation.pathToFile());
                String doc = IOUtils.toString(inputStream, Charset.defaultCharset());
                return doc;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (documentationAnnotation.text() != null && documentationAnnotation.text().trim().length() > 0) {
            return documentationAnnotation.text();
        }
        return "";
    }

    private class CacheEntry {
        public Class<? extends SignalProcessor> clazz;
        public PluginDTO pluginDTO;
        public String documentation;
    }

}
