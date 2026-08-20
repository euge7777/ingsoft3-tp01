package com.agustin.server.services;

import com.agustin.server.dtos.requests.CategoryRequest;
import com.agustin.server.dtos.responses.CategoryDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    // list user & default categories, group them and return to the client
    List<CategoryDTO> listCategories();

    // create user categories
    CategoryDTO createCategory(CategoryRequest request);

    // get a single category, useful to get more details
    CategoryDTO getCategoryById(UUID id);

    // update a user category
    CategoryDTO updateCategory(UUID id, CategoryRequest request);

    // delete a single user category
    String deleteCategory(UUID id);
}
