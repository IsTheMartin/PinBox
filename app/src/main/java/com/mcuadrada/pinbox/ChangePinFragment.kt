package com.mcuadrada.pinbox

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.mcuadrada.pinbox.Utils.SharedApp
import kotlinx.android.synthetic.main.fragment_change_pin.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ChangePin.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class ChangePinFragment : Fragment(), View.OnClickListener, View.OnLongClickListener {


    private var listener: OnFragmentInteractionListener? = null
    private var currentPin: String = ""
    private var confirmPin: String = ""
    private var stateMachine: Int = 0
    private lateinit var tvInstructions: TextView

    companion object {
        fun newInstance(): ChangePinFragment {
            return ChangePinFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_change_pin, container, false)

        tvInstructions = view.findViewById<TextView>(R.id.tvInstructions)

        initStateMachine()

        val btns0 = view.findViewById<Button>(R.id.btn0)
        btns0.setOnClickListener(this)
        val btns1 = view.findViewById<Button>(R.id.btn1)
        btns1.setOnClickListener(this)
        val btns2 = view.findViewById<Button>(R.id.btn2)
        btns2.setOnClickListener(this)
        val btns3 = view.findViewById<Button>(R.id.btn3)
        btns3.setOnClickListener(this)
        val btns4 = view.findViewById<Button>(R.id.btn4)
        btns4.setOnClickListener(this)
        val btns5 = view.findViewById<Button>(R.id.btn5)
        btns5.setOnClickListener(this)
        val btns6 = view.findViewById<Button>(R.id.btn6)
        btns6.setOnClickListener(this)
        val btns7 = view.findViewById<Button>(R.id.btn7)
        btns7.setOnClickListener(this)
        val btns8 = view.findViewById<Button>(R.id.btn8)
        btns8.setOnClickListener(this)
        val btns9 = view.findViewById<Button>(R.id.btn9)
        btns9.setOnClickListener(this)
        val btnsBackspace = view.findViewById<Button>(R.id.btnBackspace)
        btnsBackspace.setOnClickListener(this)
        btnsBackspace.setOnLongClickListener(this)
        val btnsOk = view.findViewById<Button>(R.id.btnDone)
        btnsOk.setOnClickListener(this)
        return view
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnBackspace -> {
                eraseOneCharacter()
            }
            R.id.btnDone -> {
                savePin(p0)
            }
            else -> {
                val buttonPressed: Button = p0 as Button
                val inputNumber: String = buttonPressed.text
                    .split("btn", "ok")[0]
                concatNumbers(inputNumber)
            }
        }
    }

    override fun onLongClick(p0: View?): Boolean {
        when (p0?.id) {
            R.id.btnBackspace -> {
                eraseAllCharaceters()
            }
        }
        return true
    }

    private fun initStateMachine(){
        if(SharedApp.prefs.pin == ""){
            stateMachine = 1
            tvInstructions.setText("Ingresa el nuevo pin")
        } else {
            stateMachine = 0
            tvInstructions.setText("Ingresa el pin guardado")
        }
    }

    fun concatNumbers(numeric: String) {
        if (currentPin.length < 8) {
            currentPin += numeric
            etPin.setText(currentPin)
        }
    }

    fun eraseOneCharacter() {
        if (currentPin != null && currentPin.isNotEmpty()) {
            currentPin = currentPin.substring(0, currentPin.length - 1)
            etPin.setText(currentPin)
        }
    }

    fun eraseAllCharaceters() {
        if (currentPin != null && currentPin.isNotEmpty()) {
            currentPin = ""
            etPin.setText(currentPin)
        }
    }

    fun savePin(view: View) {
        if (currentPin.length > 3) {
            when (stateMachine) {
                0 -> {
                    if (compareSavedPin()) {
                        stateMachine++
                        tvInstructions.setText("Ingresa el nuevo pin")
                    } else {
                        showSnacky(view, 0, "El pin no coincide con el almacenado")
                    }
                }
                1 -> {
                    confirmPin = currentPin
                    stateMachine++
                    tvInstructions.setText("Confirma el pin anterior")
                }
                2 -> {
                    if(confirmPins()) {
                        SharedApp.prefs.pin = currentPin
                        if (SharedApp.prefs.pin == currentPin) {
                            showSnacky(view, 1, "Se ha almacenado correctamente el nuevo pin")
                            etPin.setText("")
                            stateMachine = 0
                        } else {
                            showSnacky(view, 0, "Hubo un error al guardar el nuevo pin")
                        }
                    } else {
                        showSnacky(view, 0, "El pin de confirmación no coincide")
                    }
                }
            }
        }else {
            showSnacky(view, 0, "El pin debe ser contener al menos cuatro dígitos")
        }
        etPin.setText("")
        currentPin=""
    }

    private fun compareSavedPin(): Boolean {
        if (SharedApp.prefs.pin == currentPin) return true
        return false
    }

    private fun confirmPins():Boolean{
        if(confirmPin == currentPin) return true
        return false
    }

    private fun showSnacky(view: View, type: Int, message: String) {
        val snacky = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setAction("Action", null)
        val snackyView = snacky.view
        when (type) {
            0 -> { //Danger
                snackyView.setBackgroundColor(Color.RED)
            }
            1 -> { //Successful
                snackyView.setBackgroundColor(Color.GREEN)
            }
        }
        snacky.show()
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
