package com.danielrotaru.service
import com.danielrotaru.petdemo.model.AnimalObjectRemote
import com.danielrotaru.petdemo.model.AnimalsRemote
import com.danielrotaru.petdemo.model.TokenRemote
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AnimalRemoteApi {
    @FormUrlEncoded
    @POST("v2/oauth2/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
    ): TokenRemote

    @GET("v2/animals")
    suspend fun getAnimals(@Query("limit") limit: Int = 10, @Query("page") page: Int = 1): AnimalsRemote

    @GET("v2/animals")
    suspend fun getAnimal(@Query("id") id: Int = 1): AnimalObjectRemote
}