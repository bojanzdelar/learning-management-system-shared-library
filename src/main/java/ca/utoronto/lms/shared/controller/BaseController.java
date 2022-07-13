package ca.utoronto.lms.shared.controller;

import ca.utoronto.lms.shared.dto.BaseDTO;
import ca.utoronto.lms.shared.model.BaseEntity;
import ca.utoronto.lms.shared.service.BaseService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public abstract class BaseController<Model extends BaseEntity<ID>, DTO extends BaseDTO<ID>, ID> {
    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    private final BaseService<Model, DTO, ID> service;

    @GetMapping("/all")
    public ResponseEntity<List<DTO>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<DTO>> getAll(
            Pageable pageable, @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(service.findAll(pageable, search), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DTO>> get(@PathVariable Set<ID> id) {
        List<DTO> DTO;
        try {
            DTO = service.findById(id);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (DTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(DTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DTO> create(@RequestBody DTO DTO) {
        try {
            return new ResponseEntity<>(service.save(DTO), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DTO> update(@PathVariable ID id, @RequestBody DTO DTO) {
        DTO.setId(id);
        Set<ID> ids = new HashSet<>(Arrays.asList(id));
        try {
            if (service.findById(ids).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(service.save(DTO), HttpStatus.OK);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Set<ID> id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
