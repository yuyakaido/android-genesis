package com.yuyakaido.android.gaia.article.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yuyakaido.android.gaia.core.domain.entity.Article
import com.yuyakaido.android.gaia.core.domain.repository.ArticleRepositoryType
import com.yuyakaido.android.gaia.core.domain.value.EntityPaginationItem
import com.yuyakaido.android.gaia.core.domain.value.VoteTarget
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ArticleListViewModel @Inject constructor(
  application: Application,
  source: ArticleListSource,
  private val repository: ArticleRepositoryType
) : AndroidViewModel(application) {

  private val source = MutableLiveData<ArticleListSource>(source)
  val items = MutableLiveData<List<EntityPaginationItem<Article>>>()

  private var after: String? = null
  private var isLoading: Boolean = false

  fun onBind() {
    Timber.d("repository = ${repository.hashCode()}")
    if (items.value == null) {
      onPaginate()
    }
  }

  fun onRefresh(source: ArticleListSource) {
    this.source.value = source
    this.items.value = emptyList()
    this.after = null
    onPaginate()
  }

  fun onPaginate() {
    if (isLoading) {
      return
    }
    source.value?.let { s ->
      viewModelScope.launch {
        isLoading = true
        val result = s.articles(repository = repository, after = after)
        val oldItems = items.value ?: emptyList()
        val newItems = oldItems.plus(result)
        items.value = newItems
        after = result.after
        isLoading = false
      }
    }
  }

  fun onUpvote(article: Article) {
    vote(target = VoteTarget.forUpvote(entity = article))
  }

  fun onDownvote(article: Article) {
    vote(target = VoteTarget.forDownvote(entity = article))
  }

  private fun vote(target: VoteTarget) {
    viewModelScope.launch {
      repository.vote(target = target)
      refresh(target)
    }
  }

  private fun refresh(target: VoteTarget) {
    val newItems = items
      .value
      ?.map { item ->
        item.copy(
          entities = item
            .entities
            .map { entity ->
              if (entity.name == target.entity.name) {
                entity.copy(likes = target.likes)
              } else {
                entity
              }
            }
        )
      }
    items.value = newItems
  }

}