package com.example.day3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.day3.adapters.RecyclerAdapter
import com.example.day3.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list), RecyclerAdapter.ItemClickListener {

    private val viewModel by viewModels<PokemonViewModel>()

    private val pokemonRecyclerAdapter=RecyclerAdapter()

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnPrev: ImageButton
    private lateinit var btnNext: ImageButton


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        listenViewModel()
    }

    private fun initView(view: View){
        view.apply {
            val layoutManager = LinearLayoutManager(view.context)
            recyclerView=findViewById(R.id.recyclerView)
            recyclerView.layoutManager=layoutManager
            recyclerView.adapter=pokemonRecyclerAdapter

            pokemonRecyclerAdapter.setItemClickListener(this@ListFragment)

            btnNext=findViewById(R.id.imageBtnNext)
            btnPrev=findViewById(R.id.imageBtnPrev)

            btnPrev.setOnClickListener {
                viewModel.getPreviousPokemonList()
            }

            btnNext.setOnClickListener {
                viewModel.getNextPokemonList()
            }


        }
    }

    private fun listenViewModel(){
        viewModel.apply {
            liveDataPokemonList.observe(viewLifecycleOwner){
                pokemonRecyclerAdapter.updateList(it)
            }
        }
    }

    override fun onItemClick(position: Int) {

        val currentOffset = viewModel.liveDataOffset.value ?: 0

        val action = ListFragmentDirections.actionDetail(position+currentOffset)

        findNavController().navigate(action)
    }


}