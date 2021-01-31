package eu.wordpro.ha.server.rest;

import eu.wordpro.ha.server.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class SystemResource {

    @Autowired
    SystemService systemService;

    @PostMapping("/restart")
    public ResponseEntity<Void> restart() throws URISyntaxException {
        systemService.restart();
        return ResponseEntity.ok().build();
    }

}
