package com.example.capstone2test

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.capstone2test.databinding.FragmentSignup2contBinding

class Signup2cont : Fragment() {
    private var _binding: FragmentSignup2contBinding?=null
    private val binding get() = _binding!!
    private lateinit var action: NavDirections
    private val args:Signup2contArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentSignup2contBinding.inflate(inflater,container,false)
        val bloodtypes = mutableListOf<Any>("O+","O-","A+","A-","B+","B-","AB+","AB-")
        val bloodAdapter = ArrayAdapter(requireContext(), R.layout.gender_list_item, bloodtypes)
        (binding.signup2Bloodtype.editText as? AutoCompleteTextView)?.setAdapter(bloodAdapter)
        val activityLvl = mutableListOf<Any>("Not Active","Slightly Active","Moderately Active","Highly Active","Extremely Active")
        val actAdapter = ArrayAdapter(requireContext(), R.layout.gender_list_item, activityLvl)
        (binding.signup2ActivityLvl.editText as? AutoCompleteTextView)?.setAdapter(actAdapter)

        //code here
        binding.signup2Btn.setOnClickListener {

            val bloodtype:String = (binding.signup2Bloodtype.editText as AutoCompleteTextView)?.text.toString()


            if (TextUtils.isEmpty(binding.signup2Height.editText?.text)){
                with(binding.signup2Height){
                    error="Please enter height in cm"
                    requestFocus()
                }
                return@setOnClickListener
            }
            binding.signup2Height.error=""
            if(TextUtils.isEmpty(binding.signup2Weight.editText?.text)){
                with(binding.signup2Weight){
                    error="Please enter weight in kg"
                    requestFocus()
                }
                return@setOnClickListener
            }
            binding.signup2Weight.error=""
            if(TextUtils.isEmpty(binding.signup2Bloodtype.editText?.text)){
                with(binding.signup2Bloodtype){
                    error="Please select your blood type"
                    requestFocus()
                }

                return@setOnClickListener
            }
            binding.signup2Bloodtype.error=""
            if (TextUtils.isEmpty((binding.signup2Bloodtype.editText as AutoCompleteTextView)?.text.toString())){
                with(binding.signup2ActivityLvl){
                    error="Please select your activity level"
                    requestFocus()
                }

                return@setOnClickListener
            }
            binding.signup2ActivityLvl.error=""
            val height:Float = binding.signup2Height.editText?.text.toString().toFloat()/100
            val weight:Int = binding.signup2Weight.editText?.text.toString().toInt()
            val activity:Int =when((binding.signup2ActivityLvl.editText as AutoCompleteTextView)?.text.toString().trim()){
                "Not Active" -> 0
                "Slightly Active" -> 1
                "Moderately Active"-> 2
                "Highly Active" -> 3
                else -> 4
            }

            action = Signup2contDirections.continueSignup3(args.firstName,args.lastName,args.gender,args.birthday,

                height,weight,bloodtype,activity


                )
            Navigation.findNavController(binding.root).navigate(action)
        }
        return binding.root
    }


}