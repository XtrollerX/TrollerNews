package com.example.newstest.UI

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappenqueue.Adapters.NewsAdapter
import com.example.newstest.Architecture.NewsViewModel
import com.example.newstest.MainActivity
import com.example.newstest.R
import com.google.android.material.snackbar.Snackbar


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SavedFragment : Fragment(R.layout.fragment_saved) {

    lateinit var MainToolbar:Toolbar
    lateinit var SecondaryToolBar:Toolbar
    lateinit var secondaryToolbarImageView:ImageButton
    lateinit var RecyclerView:RecyclerView
    lateinit var newsAdapter: NewsAdapter
    lateinit var viewModel: NewsViewModel
    lateinit var emptyImage:ImageView
    lateinit var emptyText:TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainToolbar = requireActivity().findViewById<Toolbar>(R.id.MenuToolBar)

        SecondaryToolBar = requireActivity().findViewById<Toolbar>(R.id.topAppBarthesecond)

        RecyclerView = view.findViewById(R.id.SavedNewsRecyclerView)

        newsAdapter = NewsAdapter()

        viewModel = (activity as MainActivity).viewModel

        SettingUpDialog_andRecyclerView()

        viewModel.getNewsFromDB()?.observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })


        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteNews(article)

                Snackbar.make(view, "Successfully Deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.insertNews(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(RecyclerView)
        }
        RecyclerView_OnClickListener()
    }


    fun SettingUpDialog_andRecyclerView(){
        RecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        newsAdapter.setOnDialogListener{Article,UI ->
            var Dialog = UI.Dialog
            Dialog.visibility = View.GONE
        }
    }
    fun RecyclerView_OnClickListener(){
        newsAdapter.setOnItemClickListener {NewsModel->
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            Toast.makeText(context,"Directing you to browser", Toast.LENGTH_SHORT).show()
            context?.let { customTabsIntent.launchUrl(it, Uri.parse(NewsModel.url)) }
        }
    }
}