package com.example.ricknmortyapp.ui.character

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ricknmortyapp.model.Resource
import com.example.ricknmortyapp.model.entity.character.Character
import com.example.ricknmortyapp.model.repository.remote.APIService
import com.example.ricknmortyapp.ui.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


class CharacterViewModelFactory(private val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
            return CharacterViewModel(id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CharacterViewModel(val id: Int) : BaseViewModel() {
    var character: MutableLiveData<Resource<Character>> = MutableLiveData()

    @Inject
    lateinit var api: APIService

    init {
        getCharacter(id)
    }

    fun getCharacter(id: Int) = viewModelScope.launch {
        fetchCharacter(id)
    }


    private suspend fun fetchCharacter(id: Int) {
        character.postValue(Resource.Loading())
        try {
            val response = api.getCharacterDetails(id)
            character.postValue(handleCharacterResponse(response))
        } catch (t: Throwable) {
            character.postValue(
                t.message?.let {
                    Resource.Error(
                        it
                    )
                }
            )
        }
    }
}

private fun handleCharacterResponse(response: Response<Character>): Resource<Character> {
    if (response.isSuccessful) {
        response.body()?.let { resultResponse ->
            return Resource.Success(resultResponse)
        }
    }
    return Resource.Error(response.message())
}

