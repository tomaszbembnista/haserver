package eu.wordpro.ha.server.service;

import eu.wordpro.ha.server.service.dto.PluginDTO;

import java.util.List;

public interface PluginService {
    List<PluginDTO> findAll();
    PluginDTO findByClassName(String className);
    String getDocumentationByClassName(String className);
    void clearCache();
}
