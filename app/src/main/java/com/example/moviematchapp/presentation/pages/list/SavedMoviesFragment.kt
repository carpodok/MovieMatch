package com.example.moviematchapp.presentation.pages.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviematchapp.R
import com.example.moviematchapp.databinding.FragmentSavedMoviesBinding
import com.example.moviematchapp.domain.models.TrendingMovie
import com.example.moviematchapp.presentation.adapters.TrendingMoviesAdapter
import com.example.moviematchapp.presentation.pages.detail.MovieDetailFragment
import com.example.moviematchapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedMoviesFragment : Fragment() {

    private lateinit var binding: FragmentSavedMoviesBinding

    private val viewModel: MovieListViewModel by viewModels()

    private lateinit var adapter: TrendingMoviesAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedMoviesBinding.inflate(inflater, container, false)

        binding.apply {
            savedMoviesRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getAllSavedMovies()

        observeData()


    }

    private fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.savedMovieListState.collect { savedMoviesViewState ->


                when (savedMoviesViewState.status) {

                    Status.LOADING -> {
                        binding.savedMoviesProgressBar.visibility = View.VISIBLE
                        binding.savedMoviesRecyclerView.visibility = View.GONE

                        Log.i("SavedMovieListFragment", "Loading...")
                    }

                    Status.SUCCESS -> {
                        binding.savedMoviesProgressBar.visibility = View.GONE
                        binding.savedMoviesRecyclerView.visibility = View.VISIBLE


                        savedMoviesViewState.data.let { savedMovieList ->

                            adapter = TrendingMoviesAdapter(
                                movies = savedMovieList!!,
                                onItemClickListener = {
                                    onItemClicked(trendingMovie = it)
                                },
                                onSaveBtnClickedListener = {
                                    onSaveBtnClicked(trendingMovie = it)
                                }
                            )


                            binding.savedMoviesRecyclerView.adapter = adapter


                        }


                    }

                    Status.ERROR -> {

                    }

                }


            }
        }


    }

    private fun onSaveBtnClicked(trendingMovie: TrendingMovie) {

        viewModel.unsaveMovie(trendingMovie)
        Toast.makeText(
            requireContext(),
            "${trendingMovie.title} removed from the favorites!",
            Toast.LENGTH_SHORT
        ).show()

        val newSaveStatus = !trendingMovie.isSaved
        adapter.updateMovieSavedStatus(trendingMovie.id, newSaveStatus)
        viewModel.getAllSavedMovies()


    }

    private fun onItemClicked(trendingMovie: TrendingMovie) {

        val bundle = Bundle().apply {
            putInt(
                MovieDetailFragment.BUNDLE_KEY_MOVIE_ID,
                trendingMovie.id
            )
        }

        val fragment = MovieDetailFragment().apply {
            arguments = bundle
        }


        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.navHost, fragment)
            .addToBackStack(null)
            .commit()
    }
}