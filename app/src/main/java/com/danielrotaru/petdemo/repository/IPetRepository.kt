package com.danielrotaru.petdemo.repository

import com.danielrotaru.petdemo.model.AnimalObject
import com.danielrotaru.petdemo.model.Animals
import com.danielrotaru.petdemo.model.Token

interface IPetRepository {
    suspend fun getAnimals(page:Int): Animals
    suspend fun getAnimal(id:Int): AnimalObject
    suspend fun getToken(apiKey:String, apiSecret: String): Token
}