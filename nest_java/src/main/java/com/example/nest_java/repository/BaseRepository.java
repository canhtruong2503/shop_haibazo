package com.example.nest_java.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<E,Id extends UUID> extends JpaRepository<E,Id>, JpaSpecificationExecutor<E> {
    Page<E> findByDeletedAtAndDisableAt(Instant deletedAt,Instant disableAt, Pageable pageable);
}
