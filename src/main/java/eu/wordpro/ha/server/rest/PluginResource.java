package eu.wordpro.ha.server.rest;

import eu.wordpro.ha.server.service.PluginService;
import eu.wordpro.ha.server.service.dto.PluginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PluginResource {

    @Autowired
    PluginService pluginService;

    @GetMapping("/plugins")
    public List<PluginDTO> getPlugins() throws URISyntaxException {
        return pluginService.findAll();
    }

}
