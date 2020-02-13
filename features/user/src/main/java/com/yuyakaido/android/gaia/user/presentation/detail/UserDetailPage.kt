package com.yuyakaido.android.gaia.user.presentation.detail

import androidx.fragment.app.Fragment
import com.yuyakaido.android.gaia.core.domain.app.AppNavigatorType
import com.yuyakaido.android.gaia.core.domain.entity.User
import com.yuyakaido.android.gaia.user.R

enum class UserDetailPage {

  Me {
    override val containers = listOf(
      UserDetailPageContainer(
        title = R.string.user_detail_submitted_articles,
        fragment = fun (navigator: AppNavigatorType, user: User.Detail): Fragment {
          return navigator.newSubmittedArticleListFragment(user = user)
        }
      ),
      UserDetailPageContainer(
        title = R.string.user_detail_comments,
        fragment = fun (navigator: AppNavigatorType, user: User.Detail): Fragment {
          return navigator.newCommentListFragment(user = user)
        }
      ),
      UserDetailPageContainer(
        title = R.string.user_detail_upvoted_articles,
        fragment = fun (navigator: AppNavigatorType, user: User.Detail): Fragment {
          return navigator.newUpvotedArticleListFragment(user = user)
        }
      ),
      UserDetailPageContainer(
        title = R.string.user_detail_downvoted_articles,
        fragment = fun (navigator: AppNavigatorType, user: User.Detail): Fragment {
          return navigator.newDownvotedArticleListFragment(user = user)
        }
      )
    )
  },
  Other {
    override val containers = listOf(
      UserDetailPageContainer(
        title = R.string.user_detail_submitted_articles,
        fragment = fun (navigator: AppNavigatorType, user: User.Detail): Fragment {
          return navigator.newSubmittedArticleListFragment(user = user)
        }
      ),
      UserDetailPageContainer(
        title = R.string.user_detail_comments,
        fragment = fun (navigator: AppNavigatorType, user: User.Detail): Fragment {
          return navigator.newCommentListFragment(user = user)
        }
      )
    )
  };

  abstract val containers: List<UserDetailPageContainer>

  fun size(): Int {
    return containers.size
  }
  fun title(index: Int): Int {
    return containers[index].title
  }
  fun fragment(navigator: AppNavigatorType, user: User.Detail, index: Int): Fragment {
    return containers[index].fragment.invoke(navigator, user)
  }

}