package com.yuyakaido.android.gaia.core.infrastructure.remote.api

import com.yuyakaido.android.gaia.core.infrastructure.remote.response.*
import retrofit2.http.*

interface PrivateApi {

  @GET("{sort}")
  suspend fun articlesOfSort(
    @Path("sort") sort: String,
    @Query("after") after: String?
  ): ListingDataResponse

  @GET("r/{community}")
  suspend fun articlesOfCommunity(
    @Path("community") community: String,
    @Query("after") after: String?
  ): ListingDataResponse

  @FormUrlEncoded
  @POST("api/vote")
  suspend fun vote(
    @Field("id") id: String,
    @Field("dir") dir: Int
  )

  @GET("r/{community}/about/moderators")
  suspend fun moderators(
    @Path("community") community: String
  ): UserListResponse

  @GET("r/{community}/about/contributors")
  suspend fun contributors(
    @Path("community") community: String
  ): UserListResponse

  @GET("api/v1/me")
  suspend fun me(): MeResponse

  @GET("user/{user}/about")
  suspend fun user(
    @Path("user") user: String
  ): UserResponse

  @GET("user/{user}/submitted")
  suspend fun articlesOfUser(
    @Path("user") user: String,
    @Query("after") after: String?
  ): ListingDataResponse

  @GET("user/{user}/{type}")
  suspend fun voted(
    @Path("user") user: String,
    @Path("type") type: String,
    @Query("after") after: String?
  ): ListingDataResponse

}
