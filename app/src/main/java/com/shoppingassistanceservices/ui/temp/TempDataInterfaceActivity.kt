package com.shoppingassistanceservices.ui.temp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.shoppingassistanceservices.databinding.ActivityTempDataInterfaceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TempDataInterfaceActivity : AppCompatActivity() {
  lateinit var viewBinding: ActivityTempDataInterfaceBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewBinding = ActivityTempDataInterfaceBinding.inflate(layoutInflater)

    setContentView(viewBinding.root)

    viewBinding.tempDataInterfaceViewPager.adapter = ViewPagerAdapter(this)
    viewBinding.tempDataInterfaceViewPagerTabLayout.addOnTabSelectedListener(
      object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
          tab?.position?.apply {
            viewBinding.tempDataInterfaceViewPager.currentItem = this
          }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }
      }
    )

    viewBinding.tempDataInterfaceViewPager.registerOnPageChangeCallback(
      object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
          position: Int,
          positionOffset: Float,
          positionOffsetPixels: Int
        ) {
          viewBinding.tempDataInterfaceViewPagerTabLayout.setScrollPosition(position, 0f, true)
        }
      }
    )
  }

  class ViewPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = if (position == 0) TempDataFragment() else TempDataFragmentTwo()
  }
}