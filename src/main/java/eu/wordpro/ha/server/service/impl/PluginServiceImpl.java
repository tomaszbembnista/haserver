package eu.wordpro.ha.server.service.impl;

import eu.wordpro.ha.api.SignalProcessor;
import eu.wordpro.ha.server.service.PluginService;
import eu.wordpro.ha.server.service.dto.PluginDTO;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PluginServiceImpl implements PluginService {

    @Override
    public List<PluginDTO> findAll() {
        Reflections reflections = new Reflections("");
        Set<Class<? extends SignalProcessor>> subTypesOf = reflections.getSubTypesOf(SignalProcessor.class);
        List<PluginDTO> pluginDTOS = subTypesOf.stream().map(elem -> newPluginDTO(elem.getCanonicalName())).collect(Collectors.toList());
        return pluginDTOS;
    }

    private PluginDTO newPluginDTO(String className){
        PluginDTO result = new PluginDTO();
        result.setClassName(className);
        return result;
    }
}
