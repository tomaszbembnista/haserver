package eu.wordpro.ha.server.rest;

import eu.wordpro.ha.server.service.ProcessingChainService;
import eu.wordpro.ha.server.service.dto.ProcessingChainDTO;
import eu.wordpro.ha.server.service.dto.SignalProcessorDTO;
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
public class ProcessingChainResource {


    @Autowired
    ProcessingChainService processingChainService;

    @PostMapping("/processing-chains")
    public ResponseEntity<ProcessingChainDTO> createProcessingChain(@Valid @RequestBody ProcessingChainDTO processingChainDTO) throws URISyntaxException {
        if (processingChainDTO.getId() != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "A new processing chain cannot already have an ID");
        }
        ProcessingChainDTO savedSpace = processingChainService.save(processingChainDTO);
        return ResponseEntity.created(new URI("/api/processing-chains/" + savedSpace.getId()))
                .body(savedSpace);
    }

    @PutMapping("/processing-chains")
    public ResponseEntity<ProcessingChainDTO> updateProcessingChain(@Valid @RequestBody ProcessingChainDTO processingChainDTO) throws URISyntaxException {
        ProcessingChainDTO savedElement = processingChainService.save(processingChainDTO);
        return ResponseEntity.created(new URI("/api/processing-chains/" + savedElement.getId()))
                .body(savedElement);
    }

    @GetMapping("/processing-chains")
    public List<ProcessingChainDTO> getProcessingChains() throws URISyntaxException {
        return processingChainService.findAll();
    }

    @GetMapping("/processing-chains/{id}")
    public ResponseEntity<ProcessingChainDTO> getProcessingChain(@Valid @PathVariable Long id) throws URISyntaxException {
        Optional<ProcessingChainDTO> result = processingChainService.findOne(id);
        return ResponseUtil.wrapOrNotFound(result);
    }

    @GetMapping("/processing-chains/{id}/steps")
    public List<ProcessingChainDTO> getProcessingChainSteps(@Valid @PathVariable Long id) throws URISyntaxException {
        return processingChainService.findAllStepsOfProcessingChain(id);
    }

    @DeleteMapping("/processing-chains/{id}")
    public void deleteProcessingChainStep(@Valid @PathVariable Long id) throws URISyntaxException {
        processingChainService.delete(id);
    }

}
