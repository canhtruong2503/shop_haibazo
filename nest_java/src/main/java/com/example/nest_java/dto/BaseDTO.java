package com.example.nest_java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseDTO<ID extends UUID> {
    private ID id;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private Instant disableAt;
}
