package com.yuyakaido.android.gaia.user.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yuyakaido.android.gaia.core.domain.entity.User
import com.yuyakaido.android.gaia.core.domain.extension.dpToPx
import com.yuyakaido.android.gaia.core.presentation.AppNavigatorType
import com.yuyakaido.android.gaia.core.presentation.BaseFragmentWithoutHilt
import com.yuyakaido.android.gaia.user.R
import com.yuyakaido.android.gaia.user.databinding.FragmentUserDetailBinding
import javax.inject.Inject

class UserDetailFragment : BaseFragmentWithoutHilt<UserDetailViewModel>() {

  companion object {
    fun newInstance(args: UserDetailFragmentArgs): Fragment {
      return UserDetailFragment()
        .apply {
          arguments = args.toBundle()
        }
    }
  }

  @Inject
  internal lateinit var appNavigator: AppNavigatorType

  override val viewModel: UserDetailViewModel by viewModels { factory }
  internal val args: UserDetailFragmentArgs by navArgs()
  private lateinit var binding: FragmentUserDetailBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentUserDetailBinding.inflate(inflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupProfile()
  }

  private fun setupProfile() {
    binding.retryButton.setOnClickListener {
      viewModel.onRetry()
    }

    viewModel.state
      .observe(viewLifecycleOwner) { state ->
        binding.contentContainer.visibility = state.contentVisibility
        binding.progressBar.visibility = state.progressVisibility
        binding.retryButton.visibility = state.retryVisibility
        when (state) {
          is UserDetailViewModel.State.Initial,
          is UserDetailViewModel.State.Loading,
          is UserDetailViewModel.State.Error -> Unit
          is UserDetailViewModel.State.Ideal -> {
            val user = state.user
            Glide.with(requireContext())
              .load(user.icon)
              .transform(RoundedCorners(16.dpToPx(requireContext())))
              .into(binding.icon)
            binding.identity.text = getString(R.string.user_detail_identity, user.name)
            binding.birthday.text = user.birthday.toString()
            binding.karma.text = getString(R.string.user_detail_karma, user.karma)
            setupViewPager(user)
          }
        }
      }
  }

  private fun setupViewPager(user: User.Detail) {
    val adapter = UserDetailFragmentPagerAdapter(
      manager = childFragmentManager,
      context = requireContext(),
      page = args.source.page(),
      navigator = appNavigator,
      user = user
    )
    binding.viewPager.adapter = adapter
    binding.tabLayout.setupWithViewPager(binding.viewPager)
  }

}