package com.agustin.server.mappers;

import com.agustin.server.domain.entities.Category;
import com.agustin.server.dtos.responses.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);
    Category toEntity(CategoryDTO categoryDTO);
}
