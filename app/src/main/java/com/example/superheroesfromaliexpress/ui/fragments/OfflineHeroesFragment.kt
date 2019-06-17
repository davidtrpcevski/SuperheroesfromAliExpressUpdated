package com.example.superheroesfromaliexpress.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.context.showConfirmationDialog
import com.crazylegend.kotlinextensions.fragments.launch
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.livedata.fragmentProvider
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.example.superheroesfromaliexpress.R
import com.example.superheroesfromaliexpress.adapeters.myheroadapter.MyHeroesAdapter
import com.example.superheroesfromaliexpress.database.MyHeroModel
import com.example.superheroesfromaliexpress.dialog.AddMyHeroDialog
import com.example.superheroesfromaliexpress.listeners.onClickListeners
import com.example.superheroesfromaliexpress.viewmodels.MyHeroDatabaseVM
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.offline_heroes_fragment.view.*

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

class OfflineHeroesFragment : Fragment() {

    private lateinit var myHeroDatabaseVM: MyHeroDatabaseVM
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var myHeroesAdapter: MyHeroesAdapter
    private lateinit var addMyHero : FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.offline_heroes_fragment, container, false)
        initUI(view)
        return view
    }

    private fun initUI(view: View) {
        addMyHero = view.fof_add
        recyclerView = view.fof_recycler
        recyclerView.setHasFixedSize(false)
        linearLayoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = linearLayoutManager
        myHeroesAdapter = MyHeroesAdapter(requireActivity())
        recyclerView.adapter = myHeroesAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        myHeroDatabaseVM = fragmentProvider()

        myHeroDatabaseVM.heroes.observe(viewLifecycleOwner, Observer {
            it?.apply {
                myHeroesAdapter.submitList(this)
            }
        })

        addMyHero.setOnClickListenerCooldown {
            launch<AddMyHeroDialog>()
        }

        myHeroesAdapter.onClickListeners = object : onClickListeners<MyHeroModel>{
            override fun clickedItem(item: MyHeroModel) {
                requireActivity().showConfirmationDialog("Delete my hero",{
                    if (it){
                        myHeroDatabaseVM.deleteModel(item)
                        shortToast("Deleted")
                    }
                }, "Delete", "Cancel", false)
            }
        }
    }
}
