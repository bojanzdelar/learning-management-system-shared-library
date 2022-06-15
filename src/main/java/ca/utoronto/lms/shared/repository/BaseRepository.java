package ca.utoronto.lms.shared.repository;

import ca.utoronto.lms.shared.model.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface BaseRepository<Model extends BaseEntity<ID>, ID> extends JpaRepository<Model, ID> {
    @Query(value = "select x from #{#entityName} x where cast(x.id as string) like :search")
    Page<Model> findContaining(Pageable pageable, @Param("search") String search);
}
