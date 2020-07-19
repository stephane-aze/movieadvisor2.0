package com.master.movieadvisor.service.mapper



import com.master.movieadvisor.model.Category
import com.master.movieadvisor.model.Movie
import com.master.movieadvisor.service.dto.CategoryDTO


class CategoryMapper {
    fun map(dto: List<CategoryDTO>) = dto.map { mapCategory(it) }

    private fun mapCategory(categoryDTO: CategoryDTO): Category {
        return with(categoryDTO){
            Category(id=id,title = title)
        }
    }

}