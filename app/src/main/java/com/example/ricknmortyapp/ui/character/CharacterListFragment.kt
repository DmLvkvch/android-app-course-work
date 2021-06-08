package com.example.ricknmortyapp.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.example.ricknmortyapp.BR
import com.example.ricknmortyapp.R
import com.example.ricknmortyapp.model.entity.character.Character
import com.example.ricknmortyapp.ui.adapter.PaginationScrollListener
import com.example.ricknmortyapp.ui.adapter.RecyclerBindingAdapter


class CharacterListFragment : Fragment() {

    val viewModel: CharacterListViewModel by viewModels()

    lateinit var recyclerView: RecyclerView

    private var adapter: RecyclerBindingAdapter<Character> =
        RecyclerBindingAdapter(R.layout.item_character, BR.character_item)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter.onClick = { item, _ ->
            apply {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.nav_host_fragment, CharacterFragment(item.id))
                    addToBackStack(CharacterListFragment::class.java.canonicalName)
                    commit()
                }
            }
        }
        return inflater.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.list_recycler)
        recyclerView.adapter = adapter
        viewModel.characters.observe(viewLifecycleOwner, { item ->
            val items = adapter.items
            item.data?.results?.let { items.addAll(it) }
            adapter.items = items
        })

        adapter.onLoadingData = {
            view.findViewById<ProgressBar>(R.id.recycler_progress).visibility = View.VISIBLE
        }

        adapter.onLoadingDataEnd = {
            view.findViewById<ProgressBar>(R.id.recycler_progress).visibility = View.INVISIBLE
        }

        recyclerView.addOnScrollListener(object :
            PaginationScrollListener(recyclerView.layoutManager as GridLayoutManager) {

            override fun isLastPage(): Boolean {
                return viewModel.isLastPage()
            }

            override fun isLoading(): Boolean {
                return viewModel.isLoading
            }

            override fun loadMoreItems() {
                loadNextPage()
            }
        })
    }

    private fun loadNextPage() {
        viewModel.getCharacters()
    }
}