package com.yuyakaido.android.gaia.storybook.article.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yuyakaido.android.gaia.article.list.ArticleListFragment
import com.yuyakaido.android.gaia.core.value.ArticleListPage

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    supportFragmentManager
      .beginTransaction()
      .replace(
        R.id.fragment_container,
        ArticleListFragment.newInstance(page = ArticleListPage.Popular)
      )
      .commitNow()
  }

}
