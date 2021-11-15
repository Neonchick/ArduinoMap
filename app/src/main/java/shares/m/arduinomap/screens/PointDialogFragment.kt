package shares.m.arduinomap.screens

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.point_dialog.*
import shares.m.arduinomap.R
import shares.m.arduinomap.models.Point

class PointDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.point_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val point: Point = arguments?.getParcelable(POINT)!!
        temperatureValue.text = point.sensors.temperature.toString()
        humidityValue.text = point.sensors.humidity.toString()
        pressureValue.text = point.sensors.pressure.toString()
        okButton.setOnClickListener { dialog?.dismiss() }
    }


    companion object {

        const val TAG = "ScenarioDialogFragment"

        const val POINT = "point"

        fun newInstance(point: Point): PointDialogFragment {
            val scenarioDialogFragment = PointDialogFragment()
            scenarioDialogFragment.arguments = Bundle()
            scenarioDialogFragment.arguments?.putParcelable(POINT, point)
            return scenarioDialogFragment
        }

    }

}