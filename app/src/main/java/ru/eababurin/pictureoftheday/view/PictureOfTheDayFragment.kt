package ru.eababurin.pictureoftheday.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.eababurin.pictureoftheday.R
import ru.eababurin.pictureoftheday.databinding.FragmentPictureBinding
import ru.eababurin.pictureoftheday.viewmodel.AppState
import ru.eababurin.pictureoftheday.viewmodel.PictureOfTheDayViewModel
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this)[PictureOfTheDayViewModel::class.java]
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPictureBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))

        viewModel.getLiveData().observe(viewLifecycleOwner) { appState -> renderData(appState) }
        viewModel.sendRequest()

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> { /*TODO("Not yet implemented")*/
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> { /*TODO("Not yet implemented")*/
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> { /*TODO("Not yet implemented")*/
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> { /*TODO("Not yet implemented")*/
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> { /*TODO("Not yet implemented")*/
                    }
                    BottomSheetBehavior.STATE_SETTLING -> { /*TODO("Not yet implemented")*/
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                /*TODO("Not yet implemented")*/
            }
        })

        binding.chipToday.setOnClickListener {
            viewModel.sendRequest()
        }

        binding.chipYesterday.setOnClickListener {
            viewModel.sendRequestByDate(requireDate((-1)))
        }

        binding.chipBeforeYesterday.setOnClickListener {
            viewModel.sendRequestByDate(requireDate((-2)))
        }

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.input.text.toString()}")
            })
        }
    }

    private fun requireDate(offset: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, offset)
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time).toString()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.progressBar.visibility = View.INVISIBLE
                binding.imageView.visibility = View.VISIBLE
                binding.imageView.setImageResource(R.drawable.ic_no_image)
                Toast.makeText(
                    requireActivity(),
                    appState.error.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
            is AppState.Success -> {
                binding.progressBar.visibility = View.INVISIBLE
                binding.imageView.visibility = View.VISIBLE
                binding.imageView.load(appState.pictureOfTheDayResponseData.url)
                requireView().findViewById<TextView>(R.id.bottom_sheet_description_header).text =
                    appState.pictureOfTheDayResponseData.title
                requireView().findViewById<TextView>(R.id.bottom_sheet_description).text =
                    appState.pictureOfTheDayResponseData.explanation
            }
            AppState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.imageView.visibility = View.INVISIBLE
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }
}