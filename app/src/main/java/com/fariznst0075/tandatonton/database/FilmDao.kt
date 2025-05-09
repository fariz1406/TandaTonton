package com.fariznst0075.tandatonton.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fariznst0075.tandatonton.model.Film
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {

    @Insert
    suspend fun insert(film: Film)

    @Update
    suspend fun update(film: Film)

    @Query("SELECT * FROM film ORDER BY tanggal DESC")
    fun getFilm(): Flow<List<Film>>

    @Query("SELECT * FROM film WHERE id = :id")
    suspend fun getFilmById(id: Long): Film?

    @Delete
    suspend fun delete(film: Film)


}