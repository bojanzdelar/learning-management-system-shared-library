package ca.utoronto.lms.shared.controller;

import ca.utoronto.lms.shared.dto.BaseDTO;
import ca.utoronto.lms.shared.model.BaseEntity;
import ca.utoronto.lms.shared.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public abstract class BaseController<Model extends BaseEntity<ID>, DTO extends BaseDTO<ID>, ID> {
    private final BaseService<Model, DTO, ID> service;

    @GetMapping("/all")
    public ResponseEntity<List<DTO>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<DTO>> getAll(
            @ParameterObject Pageable pageable, @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(service.findAll(pageable, search), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DTO>> get(@PathVariable Set<ID> id) {
        List<DTO> DTO = service.findById(id);
        return new ResponseEntity<>(DTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DTO> create(@RequestBody DTO DTO) {
        return new ResponseEntity<>(service.save(DTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DTO> update(@PathVariable ID id, @RequestBody DTO DTO) {
        DTO.setId(id);
        return new ResponseEntity<>(service.save(DTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Set<ID> id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
