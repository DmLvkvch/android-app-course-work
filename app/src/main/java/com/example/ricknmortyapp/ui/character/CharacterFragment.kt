package com.example.ricknmortyapp.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ricknmortyapp.BR
import com.example.ricknmortyapp.R


class CharacterFragment(private val characterId: Int) : Fragment() {

    lateinit var characterViewModelFactory: CharacterViewModelFactory

    lateinit var viewModel: CharacterViewModel

    private lateinit var binding: ViewDataBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_character,
            container,
            false
        )
        characterViewModelFactory = CharacterViewModelFactory(characterId)
        viewModel =
            ViewModelProvider(this, characterViewModelFactory).get(CharacterViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.character.observe(viewLifecycleOwner, { item ->
            binding.setVariable(BR.character, item.data)
        })
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}