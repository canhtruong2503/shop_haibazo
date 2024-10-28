package com.example.nest_java.service;

import com.example.nest_java.dto.BaseDTO;
import com.example.nest_java.dto.FilterQueryDTO;
import com.example.nest_java.mapper.BaseMapper;
import com.example.nest_java.model.BaseEntity;
import com.example.nest_java.repository.BaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.MappedSuperclass;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@MappedSuperclass
@RequiredArgsConstructor

public class BaseService<E extends BaseEntity,
        DTO extends BaseDTO<ID>,
        ID extends UUID> {
    private final BaseRepository<E, ID> baseRepository;

    private final BaseMapper<E, DTO> baseMapper;

    private final JpaSpecificationExecutor<E> jpaSpecificationExecutor;


    public Page<DTO> filter(FilterQueryDTO filterQueryDTO, Pageable pageable) {

        BaseSpecification<E> specification = new BaseSpecification<>(filterQueryDTO);
        return baseMapper.convertListToDTO(jpaSpecificationExecutor.findAll(specification, pageable));
    }

    public Page<DTO> findAll(Pageable pageable) {

        return baseMapper.convertListToDTO(baseRepository.findByDeletedAtAndDisableAt(null, null, pageable));
    }

    public Optional<DTO> findById(ID id) {

        E e = baseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + id));

        return baseMapper.convertOptional(Optional.of(e));
    }

    public DTO create(DTO dto) {

        E e = baseMapper.convertToEntity(dto);

        baseRepository.save(e);
        return baseMapper.convertToDTO(e);
    }

    @Transactional
    public DTO update(ID id, DTO dto) {

        E entityRepo = baseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + id));

        E updated = baseMapper.updateEntity(dto, entityRepo);

        updated.setId(id);

        baseRepository.save(updated);

        return baseMapper.convertToDTO(updated);
    }

    @Transactional
    public void softDeleteById(ID id) {

        E entityRepo = baseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + id));

        entityRepo.setDeletedAt(Instant.now());

        baseRepository.save(entityRepo);
    }

    @Transactional
    public void disableById(ID id) {

        E entityRepo = baseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + id));

        entityRepo.setDisableAt(Instant.now());

        baseRepository.save(entityRepo);
    }

}
