package eu.wordpro.ha.server.rest;

import eu.wordpro.ha.server.service.DeviceService;
import eu.wordpro.ha.server.service.SpaceService;
import eu.wordpro.ha.server.service.dto.DeviceDTO;
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
public class DeviceResource {


    @Autowired
    DeviceService deviceService;

    @PostMapping("/devices")
    public ResponseEntity<DeviceDTO> createDevice(@Valid @RequestBody DeviceDTO device) throws URISyntaxException {
        if (device.getId() != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "A new device cannot already have an ID");
        }
        DeviceDTO savedSpace = deviceService.save(device);
        return ResponseEntity.created(new URI("/api/spaces/" + savedSpace.getId()))
                .body(savedSpace);
    }

    @PutMapping("/devices")
    public ResponseEntity<DeviceDTO> updateDevice(@Valid @RequestBody DeviceDTO device) throws URISyntaxException {
        DeviceDTO savedSpace = deviceService.save(device);
        return ResponseEntity.created(new URI("/api/spaces/" + savedSpace.getId()))
                .body(savedSpace);
    }

    @GetMapping("/devices")
    public List<DeviceDTO> getDevices() throws URISyntaxException {
        return deviceService.findAll();
    }

    @GetMapping("/devices/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@Valid @PathVariable Long id) throws URISyntaxException {
        Optional<DeviceDTO> result = deviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(result);
    }

}
