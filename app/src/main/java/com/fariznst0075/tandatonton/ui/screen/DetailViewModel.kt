package com.fariznst0075.tandatonton.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fariznst0075.tandatonton.database.FilmDao
import com.fariznst0075.tandatonton.model.Film
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: FilmDao) : ViewModel() {

    fun insert(judul: String, jenis: String, status: String, tanggal: Long) {
        val film = Film(
            judul = judul,
            jenis = jenis,
            status = status,
            tanggal = tanggal
        )
        viewModelScope.launch {
            dao.insert(film)
        }
    }

    fun update(id: Long, judul: String, jenis: String, status: String, tanggal: Long) {
        val film = Film(id, judul, jenis, status, tanggal)
        viewModelScope.launch {
            dao.update(film)
        }
    }

    suspend fun getFilm(id: Long): Film? {
        return dao.getFilmById(id)
    }

    fun delete(film: Film) {
        viewModelScope.launch {
            dao.delete(film)
        }
    }



}