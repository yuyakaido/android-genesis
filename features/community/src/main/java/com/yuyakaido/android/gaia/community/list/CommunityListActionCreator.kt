package com.yuyakaido.android.gaia.community.list

import com.yuyakaido.android.gaia.core.AppAction
import com.yuyakaido.android.gaia.core.AppState
import com.yuyakaido.android.gaia.core.CommunityAction
import com.yuyakaido.android.gaia.core.CompletableAction
import com.yuyakaido.android.gaia.core.domain.repository.CommunityRepositoryType
import com.yuyakaido.android.reduxkit.DispatcherType
import com.yuyakaido.android.reduxkit.SelectorType
import io.reactivex.Completable
import kotlinx.coroutines.rx2.rxCompletable
import javax.inject.Inject

class CommunityListActionCreator @Inject constructor(
  private val repository: CommunityRepositoryType
) {

  fun refresh(): AppAction {
    return CommunityAction.ToInitial
  }

  fun paginate(): CompletableAction {
    return object : CompletableAction {
      override fun execute(
        selector: SelectorType<AppState>,
        dispatcher: DispatcherType<AppState>
      ): Completable {
        return if (selector.select().signedIn.community.canPaginate()) {
          val state = selector.select().signedIn.community
          dispatcher.dispatch(
            CommunityAction.ToLoading(
              communities = state.communities
            )
          )
          rxCompletable {
            val item = repository.mine(after = state.after)
            dispatcher.dispatch(
              CommunityAction.ToIdeal(
                communities = item.entities,
                after = item.after
              )
            )
          }
        } else {
          Completable.complete()
        }
      }
    }
  }

}