package ca.utoronto.lms.shared.repository;

import ca.utoronto.lms.shared.model.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@NoRepositoryBean
@Transactional
public interface BaseRepository<Model extends BaseEntity<ID>, ID>
        extends CrudRepository<Model, ID> {
    @Override
    @Query("select x from #{#entityName} x where x.deleted = false")
    Iterable<Model> findAll();

    @Query("select x from #{#entityName} x where x.deleted = true")
    Iterable<Model> findAllDeleted();

    @Query("select x from #{#entityName} x")
    Iterable<Model> findAllWithSoftDeleted();

    @Query("update #{#entityName} x set x.deleted = true where x.id = :id")
    @Modifying
    void softDeleteById(ID id);

    @Query("update #{#entityName} x set x.deleted = true where x.id in :ids")
    @Modifying
    void softDeleteByIds(Set<ID> ids);

    @Query("update #{#entityName} x set x.deleted = false where x.id in :id")
    @Modifying
    void restoreById(ID id);

    @Query("update #{#entityName} x set x.deleted = false where x.id in :ids")
    @Modifying
    void restoreByIds(Set<ID> ids);

    @Query(
            value =
                    "select x from #{#entityName} x where x.deleted = false and cast(x.id as string) like :search")
    Page<Model> findContaining(Pageable pageable, @Param("search") String search);
}
