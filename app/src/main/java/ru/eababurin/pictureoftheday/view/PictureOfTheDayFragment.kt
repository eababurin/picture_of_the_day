package ru.eababurin.pictureoftheday.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import ru.eababurin.pictureoftheday.databinding.FragmentPictureBinding
import ru.eababurin.pictureoftheday.viewmodel.AppState
import ru.eababurin.pictureoftheday.viewmodel.PictureOfTheDayViewModel

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureBinding? = null
    private val binding get() = _binding!!
    val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPictureBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) { appState -> renderData(appState) }
        viewModel.sendRequest()

        binding.chipToday.setOnClickListener {
            Toast.makeText(requireContext(), "chipToday", Toast.LENGTH_SHORT).show()
        }

        binding.chipYesterday.setOnClickListener {
            Toast.makeText(requireContext(), "chipYesterday", Toast.LENGTH_SHORT).show()
        }

        binding.chipBeforeYesterday.setOnClickListener {
            Toast.makeText(requireContext(), "chipBeforeYesterday", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> { /*TODO HW*/
            }
            is AppState.Success -> {
                binding.imageView.load(appState.pictureOfTheDayResponseData.url) {
                    // TODO HW настроить загрузку изображения placeholder() error()
                }
            }
            AppState.Loading -> { /*TODO HW*/
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }
}