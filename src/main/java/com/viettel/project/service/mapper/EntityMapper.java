package com.viettel.project.service.mapper;

import java.util.List;

public interface EntityMapper<D, T> {
    D toDTO(T t);

    T toEntity(D d);

    List<D> toDTOS(List<T> ts);

    List<T> toEntities(List<D> ds);
}
