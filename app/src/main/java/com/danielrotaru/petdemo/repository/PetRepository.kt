package com.danielrotaru.petdemo.repository

import com.danielrotaru.petdemo.model.AnimalObject
import com.danielrotaru.petdemo.model.Animals
import com.danielrotaru.petdemo.model.Token
import com.danielrotaru.petdemo.model.toDomain
import com.danielrotaru.service.AnimalRemoteApi
import javax.inject.Inject

class PetRepository @Inject constructor(
    private val remoteApi: AnimalRemoteApi
) : IPetRepository {

    override suspend fun getAnimals(page: Int): Animals = remoteApi.getAnimals(page = page).toDomain()
    override suspend fun getAnimal(id: Int): AnimalObject = remoteApi.getAnimal(id).toDomain()
    override suspend fun getToken(apiKey: String, apiSecret: String): Token
            = remoteApi.getAccessToken("client_credentials", apiKey, apiSecret).toDomain()

}