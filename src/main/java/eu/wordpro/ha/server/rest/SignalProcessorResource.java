package eu.wordpro.ha.server.rest;

import eu.wordpro.ha.api.model.ProcessorOperationArgument;
import eu.wordpro.ha.api.model.ProcessorOperationDesc;
import eu.wordpro.ha.server.service.SignalProcessorService;
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
public class SignalProcessorResource {


    @Autowired
    SignalProcessorService signalProcessorService;

    @PostMapping("/signal-processors")
    public ResponseEntity<SignalProcessorDTO> createSignalProcessor(@Valid @RequestBody SignalProcessorDTO signalProcessor) throws URISyntaxException {
        if (signalProcessor.getId() != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "A new space cannot already have an ID");
        }
        SignalProcessorDTO savedSpace = signalProcessorService.save(signalProcessor);
        return ResponseEntity.created(new URI("/api/signal-processors/" + savedSpace.getId()))
                .body(savedSpace);
    }

    @PutMapping("/signal-processors")
    public ResponseEntity<SignalProcessorDTO> updateSignalProcessor(@Valid @RequestBody SignalProcessorDTO space) {
        SignalProcessorDTO savedSpace = signalProcessorService.save(space);
        return ResponseEntity.ok(savedSpace);
    }

    @GetMapping("/signal-processors")
    public List<SignalProcessorDTO> getSignalProcessors() {
        return signalProcessorService.findAll();
    }

    @GetMapping("/signal-processors/{id}")
    public ResponseEntity<SignalProcessorDTO> getSignalProcessor(@Valid @PathVariable Long id) {
        Optional<SignalProcessorDTO> result = signalProcessorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(result);
    }

    @GetMapping("/signal-processors/{id}/operations")
    public List<ProcessorOperationDesc> getSignalProcessorOperations(@Valid @PathVariable Long id) {
        return signalProcessorService.getPossibleOperations(id);
    }

    @PutMapping("/signal-processors/{id}/operations/{name}")
    public ResponseEntity<String> executeSignalProcessorOperations(@Valid @PathVariable Long id, @Valid @PathVariable String name, @Valid @RequestBody List<ProcessorOperationArgument> operationArguments) {
        String result = signalProcessorService.executeOperation(id, name, operationArguments);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/signal-processors/{id}")
    public void deleteSignalProcessor(@Valid @PathVariable Long id) {
        signalProcessorService.delete(id);
    }

}
