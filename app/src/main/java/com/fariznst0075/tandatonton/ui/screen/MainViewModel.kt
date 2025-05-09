package com.fariznst0075.tandatonton.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fariznst0075.tandatonton.database.FilmDao
import com.fariznst0075.tandatonton.model.Film
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(dao: FilmDao) : ViewModel() {

    val data: StateFlow<List<Film>> = dao.getFilm().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun getFilm(id: Long): Film? {
        return data.value.find { it.id == id }
    }
}