package com.example.day3.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.day3.models.Abilities
import com.example.day3.models.Ability
import com.example.day3.models.AbilityResponse
import com.example.day3.models.Pokemon
import com.example.day3.models.PokemonResponse
import com.example.day3.services.PokeApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokeApiService: PokeApiService
): ViewModel() {

    private val _liveDataPokemonList = MutableLiveData<List<Pokemon>>()
    val liveDataPokemonList: LiveData<List<Pokemon>> = _liveDataPokemonList

    private val _liveDataAbilitiesList = MutableLiveData<List<Abilities>>()
    val liveDataAbilitiesList: LiveData<List<Abilities>> = _liveDataAbilitiesList

    private val _liveDataAbilityList = MutableLiveData<List<Ability>>()
    val liveDataAbilityList: LiveData<List<Ability>> = _liveDataAbilityList

    private val _liveDataOffset = MutableLiveData<Int>()
    val liveDataOffset: LiveData<Int> = _liveDataOffset

    init {

        callPokemonInfo()
    }

     fun callPokemonAbilities(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            val abilityResponse: AbilityResponse?=try {
                pokeApiService.getPokemonAbilities(id)
            }catch (exception: Exception){
                null
            }

            val abilityList=abilityResponse?.abilities ?: emptyList()
            println("ABILITYNAME"+abilityList[1].ability?.name)
            _liveDataAbilitiesList.postValue(abilityList)
        }
    }

    private fun callPokemonInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            val pokemonResponse: PokemonResponse?=try {
                pokeApiService.getPokemonInfo()
            }catch (exception: Exception){
                null

            }
            println()
            val pokemonList= pokemonResponse?.results ?: emptyList()
            println(pokemonList[1].name)
            _liveDataPokemonList.postValue(pokemonList)

        }
    }

    fun getNextPokemonList() {
        val currentOffset = liveDataOffset.value ?: 0
        if (currentOffset<40){
            viewModelScope.launch(Dispatchers.IO) {
                val newOffset = currentOffset + 20
                val pokemonResponse = try {
                    pokeApiService.getPokemonInfo(offset = newOffset)
                } catch (exception: Exception) {
                    null
                }
                val pokemonList = pokemonResponse?.results ?: emptyList()
                _liveDataPokemonList.postValue(pokemonList)
                _liveDataOffset.postValue(newOffset)
            }
        }

    }

    fun getPreviousPokemonList() {
        val currentOffset = liveDataOffset.value ?: 0
        if (currentOffset >= 20) {
            viewModelScope.launch(Dispatchers.IO) {
                val newOffset = currentOffset - 20
                val pokemonResponse = try {
                    pokeApiService.getPokemonInfo(offset = newOffset)
                } catch (exception: Exception) {
                    null
                }
                val pokemonList = pokemonResponse?.results ?: emptyList()
                _liveDataPokemonList.postValue(pokemonList)
                _liveDataOffset.postValue(newOffset)
            }
        }
    }

}