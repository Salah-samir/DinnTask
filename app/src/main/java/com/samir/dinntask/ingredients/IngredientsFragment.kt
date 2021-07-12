package com.samir.dinntask.ingredients

import OrdersViewState.ingredients.viewmodel.IngredientsActions
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.samir.dinntask.R
import com.samir.dinntask.databinding.IngredientsFragmentView
import com.samir.dinntask.ingredients.page.IngredientsPageFragment
import com.samir.dinntask.model.IngredientsCategoryItem
import com.samir.dinntask.shared.domain.ingredients.IngredientsListPartialState
import com.samir.dinntask.shared.domain.ingredients.IngredientsViewState
import com.google.android.material.tabs.TabLayoutMediator
import com.samir.dinntask.ingredients.viewmodel.IngredientsViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber

@AndroidEntryPoint
class IngredientsFragment : Fragment() {

    private lateinit var binding: IngredientsFragmentView
    private val ingredientsViewModel: IngredientsViewModel by viewModels()
    private lateinit var pagerAdapter: SectionPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        with(IngredientsFragmentView.inflate(layoutInflater, container, false)) {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            pagerAdapter = SectionPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
            adapter = pagerAdapter
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeSearchViewStyle()
        ingredientsViewModel.partialStatPublisher
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) {
            }

        ingredientsViewModel.states()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) { Timber.e(it) }

        intents().apply(ingredientsViewModel::processIntents)
    }

    private fun render(partialState: IngredientsListPartialState) {
    }

    private fun render(vs: IngredientsViewState) {
        binding.viewState = vs
        configureTabs(vs.ingredientsCategory)
    }

    private fun configureTabs(ingredientsCategory: List<IngredientsCategoryItem>) {
        if (ingredientsCategory.isNotEmpty()) {
            for (item in ingredientsCategory) {
                pagerAdapter.addFragment(IngredientsPageFragment.newInstance(item.id))
            }

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
                tab.text = ingredientsCategory[pos].title
            }.attach()
        }
    }

    private fun changeSearchViewStyle() {
        binding.toolbar.run {
            inflateMenu(R.menu.ingredients_menu)
            setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.search) {
                    openSearch()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun openSearch() {
        findNavController().navigate(R.id.action_ingredientsFragment_to_searchFragment)
    }

    private fun intents(): Observable<IngredientsActions> =
        Observable.just(IngredientsActions.GetIngredientsCategory)
}
