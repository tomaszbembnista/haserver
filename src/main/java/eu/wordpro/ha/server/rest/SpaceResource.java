package eu.wordpro.ha.server.rest;

import eu.wordpro.ha.server.service.SpaceService;
import eu.wordpro.ha.server.service.dto.SpaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SpaceResource {


    @Autowired
    SpaceService spaceService;

    @PostMapping("/spaces")
    public ResponseEntity<SpaceDTO> createSpace(@Valid @RequestBody SpaceDTO space) throws URISyntaxException {
        if (space.getId() != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "A new space cannot already have an ID");
        }
        SpaceDTO savedSpace = spaceService.save(space);
        return ResponseEntity.created(new URI("/api/spaces/" + savedSpace.getId()))
                .body(savedSpace);
    }

    @PutMapping("/spaces")
    public ResponseEntity<SpaceDTO> updateSpace(@Valid @RequestBody SpaceDTO space) throws URISyntaxException {
        SpaceDTO savedSpace = spaceService.save(space);
        return ResponseEntity.created(new URI("/api/spaces/" + savedSpace.getId()))
                .body(savedSpace);
    }

    @GetMapping("/spaces")
    public List<SpaceDTO> getSpaces() throws URISyntaxException {
        return spaceService.findAll();
    }

    @GetMapping("/spaces/{id}")
    public ResponseEntity<SpaceDTO> getSpace(@Valid @PathVariable Long id) throws URISyntaxException {
        Optional<SpaceDTO> result = spaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(result);
    }

}
