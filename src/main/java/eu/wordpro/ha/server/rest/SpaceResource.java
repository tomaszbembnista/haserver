package eu.wordpro.ha.server.rest;

import eu.wordpro.ha.server.service.DeviceService;
import eu.wordpro.ha.server.service.SignalProcessorService;
import eu.wordpro.ha.server.service.SpaceService;
import eu.wordpro.ha.server.service.dto.DeviceDTO;
import eu.wordpro.ha.server.service.dto.SignalProcessorDTO;
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
@CrossOrigin("*")
public class SpaceResource {


    @Autowired
    SpaceService spaceService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    SignalProcessorService signalProcessorService;

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

    @GetMapping("/spaces/{id}/spaces")
    public List<SpaceDTO> getSpacesBelongingToSpace(@Valid @PathVariable Long id) throws URISyntaxException {
        System.out.println("Getting spaces");
        if (id == null || id.equals(new Long(-1))){
            System.out.println("Getting spaces with null");
            return spaceService.findSpacesIn(null);
        }
        return spaceService.findSpacesIn(id);
    }

    @GetMapping("/spaces/{id}/devices")
    public List<DeviceDTO> getDevicesBelongingToSpace(@Valid @PathVariable Long id) throws URISyntaxException {
        System.out.println("Getting devices");
        if (id == null || id.equals(new Long(-1))){
            System.out.println("Getting spaces with null");
            return deviceService.findDevicesInSpace(null);
        }
        return deviceService.findDevicesInSpace(id);
    }

    @GetMapping("/spaces/{id}/signalProcessors")
    public List<SignalProcessorDTO> getSignalProcessorsBelongingToSpace(@Valid @PathVariable Long id) throws  URISyntaxException {
        System.out.println("Getting signal processors");
        if (id == null || id.equals(new Long(-1))) {
            System.out.println("Getting signal processors with null");
            return signalProcessorService.findSignalProcessorsInSpace(null);
        }
        return signalProcessorService.findSignalProcessorsInSpace(id);
    }

    @GetMapping("/spaces/{id}")
    public ResponseEntity<SpaceDTO> getSpace(@Valid @PathVariable Long id) throws URISyntaxException {
        Optional<SpaceDTO> result = spaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(result);
    }

    /**
     * DELETE  /bots/:id : delete the "id" bot.
     *
     * @param id the id of the botDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/spaces/{id}")
    public ResponseEntity<Void> deleteSpace(@PathVariable Long id) {
        spaceService.delete(id);
        return ResponseEntity.ok().build();
    }

}
