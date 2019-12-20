package com.yuyakaido.android.gaia.core.infrastructure

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yuyakaido.android.gaia.core.domain.app.AuthTokenServiceType
import com.yuyakaido.android.gaia.core.domain.app.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {

  @AppScope
  @Provides
  fun provideOkHttpClient(
    service: AuthTokenServiceType
  ): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
      .apply { level = HttpLoggingInterceptor.Level.BASIC }
    return OkHttpClient
      .Builder()
      .addNetworkInterceptor(StethoInterceptor())
      .addInterceptor(httpLoggingInterceptor)
      .addInterceptor(AuthInterceptor(service = service))
      .build()
  }

  @AppScope
  @Provides
  fun provideMoshi(): Moshi {
    return Moshi
      .Builder()
      .add(
        PolymorphicJsonAdapterFactory
          .of(ListingDataResponse.Children.Child::class.java, "kind")
          .withSubtype(
            ListingDataResponse.Children.Child.Comment::class.java,
            ListingDataResponse.Children.Child.Kind.Comment.id
          )
          .withSubtype(
            ListingDataResponse.Children.Child.Article::class.java,
            ListingDataResponse.Children.Child.Kind.Article.id
          )
          .withSubtype(
            ListingDataResponse.Children.Child.More::class.java,
            ListingDataResponse.Children.Child.Kind.More.id
          )
      )
      .add(KotlinJsonAdapterFactory())
      .build()
  }

  @AppScope
  @Provides
  fun provideRedditPublicApi(
    moshi: Moshi,
    client: OkHttpClient
  ): RedditPublicApi {
    val retrofit = Retrofit
      .Builder()
      .client(client)
      .baseUrl("https://reddit.com")
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
    return retrofit.create(RedditPublicApi::class.java)
  }

  @AppScope
  @Provides
  fun provideRedditAuthApi(
    moshi: Moshi,
    client: OkHttpClient
  ): RedditAuthApi {
    val retrofit = Retrofit
      .Builder()
      .client(client)
      .baseUrl("https://oauth.reddit.com")
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
    return retrofit.create(RedditAuthApi::class.java)
  }

  @AppScope
  @Provides
  fun provideRedditWwwApi(
    moshi: Moshi,
    client: OkHttpClient
  ): RedditWwwApi {
    val retrofit = Retrofit
      .Builder()
      .client(client)
      .baseUrl("https://www.reddit.com/")
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
    return retrofit.create(RedditWwwApi::class.java)
  }

}