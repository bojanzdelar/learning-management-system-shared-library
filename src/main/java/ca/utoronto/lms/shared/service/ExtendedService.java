package ca.utoronto.lms.shared.service;

import ca.utoronto.lms.shared.dto.BaseDTO;
import ca.utoronto.lms.shared.mapper.BaseMapper;
import ca.utoronto.lms.shared.model.BaseEntity;
import ca.utoronto.lms.shared.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ExtendedService<Model extends BaseEntity<ID>, DTO extends BaseDTO<ID>, ID>
        extends BaseService<Model, DTO, ID> {
    private final BaseRepository<Model, ID> repository;
    private final BaseMapper<Model, DTO, ID> mapper;

    public ExtendedService(
            BaseRepository<Model, ID> repository, BaseMapper<Model, DTO, ID> mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<DTO> findAll() {
        List<DTO> DTO = super.findAll();
        return DTO.isEmpty() ? DTO : mapMissingValues(DTO);
    }

    @Override
    public Page<DTO> findAll(Pageable pageable, String search) {
        Page<DTO> DTO = super.findAll(pageable, search);
        return DTO.getContent().isEmpty()
                ? DTO
                : new PageImpl<>(
                        mapMissingValues(DTO.getContent()), pageable, DTO.getTotalElements());
    }

    @Override
    public List<DTO> findById(Set<ID> id) {
        List<DTO> DTO = super.findById(id);
        return mapMissingValues(DTO);
    }

    protected Set<ID> getID(List<DTO> list, Getter<DTO, ID> getter) {
        return list.stream()
                .filter((el) -> getter.get(el) != null)
                .map((el) -> getter.get(el).getId())
                .collect(Collectors.toSet());
    }

    protected <MissingDTO extends BaseDTO<ID>> void replaceID(
            List<DTO> list,
            List<MissingDTO> missingList,
            Getter<DTO, ID> getter,
            Setter<DTO, MissingDTO> setter) {
        list.forEach(
                el ->
                        setter.set(
                                el,
                                missingList.stream()
                                        .filter(
                                                (missing) ->
                                                        getter.get(el)
                                                                .getId()
                                                                .equals(missing.getId()))
                                        .findFirst()
                                        .orElse(null)));
    }

    protected <MissingDTO extends BaseDTO<ID>> void map(
            List<DTO> list,
            Getter<DTO, ID> getter,
            Setter<DTO, MissingDTO> setter,
            FeignClient<MissingDTO, ID> client) {
        Set<ID> ID = getID(list, getter);
        List<MissingDTO> missingList = client.call(ID);
        if (!missingList.isEmpty()) {
            replaceID(list, missingList, getter, setter);
        }
    }

    protected abstract List<DTO> mapMissingValues(List<DTO> DTO);

    @FunctionalInterface
    protected interface FeignClient<DTO, ID> {
        List<DTO> call(Set<ID> ids);
    }

    @FunctionalInterface
    protected interface Getter<DTO, ID> {
        BaseDTO<ID> get(DTO DTO);
    }

    @FunctionalInterface
    protected interface Setter<DTO, OtherDTO> {
        void set(DTO DTO, OtherDTO val);
    }
}
