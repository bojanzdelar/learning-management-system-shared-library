package ca.utoronto.lms.shared.service;

import ca.utoronto.lms.shared.dto.BaseDTO;
import ca.utoronto.lms.shared.mapper.BaseMapper;
import ca.utoronto.lms.shared.model.BaseEntity;
import ca.utoronto.lms.shared.repository.BaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public abstract class BaseService<Model extends BaseEntity<ID>, DTO extends BaseDTO<ID>, ID> {
    private final BaseRepository<Model, ID> repository;
    private final BaseMapper<Model, DTO> mapper;

    public List<DTO> findAll() {
        return mapper.toDTO(repository.findAll());
    }

    public Page<DTO> findAll(Pageable pageable, String search) {
        return repository.findContaining(pageable, "%" + search + "%").map(mapper::toDTO);
    }

    public List<DTO> findById(Set<ID> id) {
        return mapper.toDTO(repository.findAllById(id));
    }

    public DTO save(DTO DTO) {
        Model model = mapper.toModel(DTO);
        return mapper.toDTO(repository.save(model));
    }

    public void delete(Set<ID> id) {
        repository.deleteAllByIdInBatch(id);
    }
}
