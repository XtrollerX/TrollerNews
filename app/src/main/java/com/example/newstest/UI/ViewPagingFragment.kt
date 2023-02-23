package com.example.newstest.UI

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.newstest.Adapter.FragmentAdapter
import com.example.newstest.R
import com.example.newstest.databinding.ViewpagingfragmentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagingFragment:Fragment(R.layout.viewpagingfragment) {

    lateinit var binding: ViewpagingfragmentBinding
    lateinit var PagerAdapter: FragmentAdapter
    lateinit var viewPagerView:ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewpagingfragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val Categories = arrayListOf<String>("BreakingNews","SavedNews")
        viewpager(Categories)

        viewPagerView = view.findViewById(R.id.view_pager)
        viewPagerView.offscreenPageLimit = 2

        var MainToolbarSaved = requireActivity().findViewById<Toolbar>(R.id.MenuToolBar)
        var SecondaryToolBarSaved = requireActivity().findViewById<Toolbar>(R.id.topAppBarthesecond)

        MainToolbarSaved.visibility = View.VISIBLE
        SecondaryToolBarSaved.visibility = View.GONE
    }


    fun viewpager(FragmentList:ArrayList<String>){
        val tabLayout = binding.tabLayout
        PagerAdapter = FragmentAdapter(childFragmentManager,lifecycle)
        binding.viewPager.adapter = PagerAdapter

        TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
            tab.text = FragmentList[position]
        }.attach()
    }
}