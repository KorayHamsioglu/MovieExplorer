package com.example.day3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.day3.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.FieldPosition

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel by viewModels<PokemonViewModel>()


    private lateinit var textAbility1: TextView
    private lateinit var textAbility2: TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args=DetailsFragmentArgs.fromBundle(requireArguments())
        val position=args.position

        initView(view,position)
    }

    private fun initView(view: View,position: Int){
        view.apply {
           textAbility1=findViewById(R.id.textAbility1)

            textAbility2=findViewById(R.id.textAbility2)

            val id: String=position.toString()

            viewModel.callPokemonAbilities(id)



            viewModel.liveDataAbilitiesList.observe(viewLifecycleOwner) { abilities ->
                println("ABİLİTY FRAGMENT: " + abilities?.get(0)?.ability?.name)
                textAbility1.text = abilities?.get(0)?.ability?.name
                textAbility2.text = abilities?.get(1)?.ability?.name
            }


        }
    }


}