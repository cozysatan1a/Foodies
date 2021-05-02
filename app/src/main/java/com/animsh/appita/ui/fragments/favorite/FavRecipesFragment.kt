package com.animsh.appita.ui.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.animsh.appita.adapters.FavRecipeAdapter
import com.animsh.appita.databinding.FragmentFavRecipesBinding
import com.animsh.appita.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class FavRecipesFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private val fAdapter: FavRecipeAdapter by lazy {
        FavRecipeAdapter(
            requireActivity(),
            mainViewModel
        )
    }

    private var _binding: FragmentFavRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavRecipesBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@FavRecipesFragment
            viewModel = mainViewModel
            adapter = fAdapter
            favRecipesRecyclerView.adapter = fAdapter
            favRecipesRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


            mainViewModel.readFavRecipe.observe(viewLifecycleOwner, { favEntity ->
                fAdapter.setData(favEntity)
            })

            activity?.deleteBtn?.setOnClickListener {
                mainViewModel.deleteAllFavRecipe()
                Snackbar.make(
                    appBar,
                    "All Recipes Deleted!",
                    Snackbar.LENGTH_SHORT
                ).setAction("Okay") {}
                    .show()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            fAdapter.removeContextualActionMode()
            activity?.deleteBtn?.visibility = View.GONE
            activity?.searchBtn?.visibility = View.VISIBLE
        } else {
            activity?.deleteBtn?.visibility = View.VISIBLE
            activity?.searchBtn?.visibility = View.GONE
        }
        super.onHiddenChanged(hidden)
    }
}