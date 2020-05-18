package com.karzek.exercises.data.category

import android.content.SharedPreferences
import com.karzek.exercises.data.category.contract.ICategoryLocalDataSource
import com.karzek.exercises.database.category.CategoryDao
import com.karzek.exercises.database.category.CategoryEntity
import com.karzek.exercises.database.category.toModels
import com.karzek.exercises.domain.category.model.Category
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val categoryDao: CategoryDao
) : ICategoryLocalDataSource {

    override fun getAllCategories(): Maybe<List<Category>> {
        return Single.just(isCacheValid())
            .flatMap {
                if (it) {
                    categoryDao.getAll()
                } else {
                    categoryDao.deleteAll()
                        .andThen(Single.just(emptyList()))
                }
            }.flatMapMaybe {
                if (it.isEmpty()) {
                    Maybe.empty()
                } else {
                    Maybe.just(it.toModels())
                }
            }
    }

    override fun setAllCategories(categories: List<Category>): Completable {
        return Completable.fromCallable {
            val entities = categories.map {
                CategoryEntity(it.id, it.name)
            }
            categoryDao.insertAll(entities)
        }
    }

    private fun isCacheValid(): Boolean {
        val currentTime = Date().time
        val timestamp = sharedPreferences.getLong(CATEGORY_CACHE_TIME_STAMP, currentTime)
        return (timestamp + ONE_DAY) >= currentTime
    }

    companion object {
        private const val CATEGORY_CACHE_TIME_STAMP = "CATEGORY_CACHE_TIME_STAMP"
        private const val ONE_DAY = 24 * 60 * 60 * 1000L
    }

}
