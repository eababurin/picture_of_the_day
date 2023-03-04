package ru.eababurin.pictureoftheday.view.navigation.earth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import ru.eababurin.pictureoftheday.R
import ru.eababurin.pictureoftheday.databinding.FragmentPhotoYesterdayBinding
import ru.eababurin.pictureoftheday.utils.WIKI_SEARCH_URL
import ru.eababurin.pictureoftheday.utils.YESTERDAY
import ru.eababurin.pictureoftheday.utils.requireDate
import ru.eababurin.pictureoftheday.viewmodel.AppState
import ru.eababurin.pictureoftheday.viewmodel.PictureOfTheDayViewModel

class PhotoYesterdayFragment : Fragment() {

    private var _binding: FragmentPhotoYesterdayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this)[PictureOfTheDayViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoYesterdayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(WIKI_SEARCH_URL + binding.input.text.toString())
            })
        }

        viewModel.sendRequest(requireDate(YESTERDAY))
        viewModel.getLiveData().observe(viewLifecycleOwner) { appState -> renderData(appState) }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.progressBar.visibility = View.INVISIBLE
                binding.imageView.visibility = View.VISIBLE
                binding.imageView.setImageResource(R.drawable.ic_no_image)
            }
            is AppState.Success -> {
                binding.progressBar.visibility = View.INVISIBLE
                binding.imageView.visibility = View.VISIBLE
                binding.imageView.load(appState.pictureOfTheDayResponseData.hdurl)
            }
            AppState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.imageView.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}