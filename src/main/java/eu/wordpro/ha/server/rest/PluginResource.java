package eu.wordpro.ha.server.rest;

import eu.wordpro.ha.server.domain.StringWrapper;
import eu.wordpro.ha.server.rest.error.NotFoundException;
import eu.wordpro.ha.server.service.PluginService;
import eu.wordpro.ha.server.service.dto.PluginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PluginResource {

    @Autowired
    PluginService pluginService;

    @GetMapping("/plugins")
    public List<PluginDTO> getPlugins() throws URISyntaxException {
        return pluginService.findAll();
    }

    @GetMapping("/plugins/{className}")
    public PluginDTO getPlugin(@PathVariable String className) throws URISyntaxException {
        PluginDTO result = pluginService.findByClassName(className);
        if (result != null){
            return result;
        }
        throw new NotFoundException();
    }

    @GetMapping(value = "/plugins/{className}/documentation")
    public StringWrapper getPluginDocumentation(@PathVariable String className) throws URISyntaxException {
        String result = pluginService.getDocumentationByClassName(className);
        if (result != null){
            return new StringWrapper(result);
        }
        throw new NotFoundException();
    }

}
