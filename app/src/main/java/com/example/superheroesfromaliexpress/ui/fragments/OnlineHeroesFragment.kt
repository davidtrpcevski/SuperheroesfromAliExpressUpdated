package com.example.superheroesfromaliexpress.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.crazylegend.kotlinextensions.activity.launchActivity
import com.crazylegend.kotlinextensions.core.exhaustive
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.retrofit.NoConnectionException
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.example.superheroesfromaliexpress.R
import com.example.superheroesfromaliexpress.adapeters.SuperHeroAdapter
import com.example.superheroesfromaliexpress.core.INTENT_TAG_DETAILED_HERO
import com.example.superheroesfromaliexpress.listeners.onClickListeners
import com.example.superheroesfromaliexpress.model.SuperHeroModel
import com.example.superheroesfromaliexpress.ui.activities.DetailedSuperHeroActivity
import com.example.superheroesfromaliexpress.viewmodels.OnlineHeroesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.online_heroes_fragment.view.*
import java.util.concurrent.TimeUnit

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

class OnlineHeroesFragment : Fragment() {

    private lateinit var viewModel: OnlineHeroesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var superHeroAdapter: SuperHeroAdapter
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var mFloatingActionButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.online_heroes_fragment, container, false)
        initUI(view)
        return view
    }

    private fun initUI(view: View) {
        mFloatingActionButton = view.scroll_to_top_button
        compositeDisposable = CompositeDisposable()
        setHasOptionsMenu(true)
        swipeRefreshLayout = view.foh_swipeToRefresh
        recyclerView = view.foh_recycler
        recyclerView.setHasFixedSize(false)
        linearLayoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = linearLayoutManager
        superHeroAdapter = SuperHeroAdapter(requireActivity())
        recyclerView.adapter = superHeroAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && mFloatingActionButton.visibility == View.VISIBLE) {
                    mFloatingActionButton.hide()
                } else if (dy < 0 && mFloatingActionButton.visibility != View.VISIBLE) {
                    mFloatingActionButton.show()
                }
            }
        })
    }

    private var searchView: SearchView? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.online_menu, menu)

        val searchItem = menu.findItem(R.id.searchSuperHeroes)

        searchItem?.apply {
            searchView = this.actionView as SearchView?
        }

        val observable = searchView?.getTextView?.let {
            RxTextView.textChanges(it).debounce(200, TimeUnit.MILLISECONDS)
                .skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    it.toString()
                }
        }

        viewModel.filterData().observe(viewLifecycleOwner, Observer {
            it?.apply {
                superHeroAdapter.submitList(it)
            }
        })

        observable?.subscribe {
            viewModel.setSearchQuery(it.toString())
        }?.addTo(compositeDisposable)

        super.onCreateOptionsMenu(menu, inflater)
    }

    val SearchView?.getTextView get() = this?.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OnlineHeroesViewModel::class.java)
        viewModel.list.observe(viewLifecycleOwner, Observer {
            it?.apply {
                when (this) {
                    is RetrofitResult.Success -> {
                        handleSuccess(value)
                    }
                    RetrofitResult.Loading -> {
                        handleLoading()
                    }
                    RetrofitResult.NoData -> {
                    }
                    RetrofitResult.EmptyData -> {

                    }
                    is RetrofitResult.Error -> {
                        handlError(this)
                    }
                    is RetrofitResult.ApiError -> {

                    }
                }.exhaustive
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

        mFloatingActionButton.setOnClickListenerCooldown {
            recyclerView.smoothSnapToPosition(0)
        }

        superHeroAdapter.onClickListeners = object : onClickListeners<SuperHeroModel> {
            override fun clickedItem(item: SuperHeroModel) {
                requireActivity().launchActivity<DetailedSuperHeroActivity> {
                    putExtra(INTENT_TAG_DETAILED_HERO, item.id)
                }
            }

        }

    }

    private fun handlError(error: RetrofitResult.Error) {
        if (error.throwable is NoConnectionException) {
            shortToast("No connection")
        }
    }

    private fun handleLoading() {
        swipeRefreshLayout.isEnabled = false
        swipeRefreshLayout.isRefreshing = true
    }

    private fun handleSuccess(list: List<SuperHeroModel>) {
        swipeRefreshLayout.isEnabled = true
        superHeroAdapter.submitList(list)
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
        val smoothScroller = object: LinearSmoothScroller(this.context) {
            override fun getVerticalSnapPreference(): Int {
                return snapMode
            }

            override fun getHorizontalSnapPreference(): Int {
                return snapMode
            }
        }
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }

}