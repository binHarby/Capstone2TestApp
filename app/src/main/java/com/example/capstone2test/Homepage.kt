package com.example.capstone2test

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.service.controls.Control
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.capstone2test.adapter.JournalThumbnailsAdapter
import com.example.capstone2test.const.Layout
import com.example.capstone2test.const.URLs
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.controller.VolleySingleton
import com.example.capstone2test.databinding.FragmentHomepageBinding
import com.example.capstone2test.model.User
import com.example.capstone2test.roomDatabase.ReadingViewModel
import com.example.capstone2test.roomDatabase.data.Reading
import com.example.capstone2test.viewmodels.calStateViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.Math.abs
import java.text.DateFormat
import java.util.concurrent.TimeUnit
import kotlin.concurrent.fixedRateTimer

class Homepage : Fragment() {
    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!
    private lateinit var action: NavDirections
    private lateinit var user :User
    private lateinit var androidViewModel: calStateViewModel
    private var totalSteps: Int=0
    private  var totalCals: Int=0
    private var consumedCals:Int =0
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.roatate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }
    private var clicked: Boolean = false
    private var slidhandle = Handler()
    private lateinit var readingViewModel: ReadingViewModel
    //google fit stuff?
    private val dateFormat = DateFormat.getDateInstance()
    private lateinit var fitnessOptions: FitnessOptions

    @SuppressLint("StringFormatMatches")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
        //setting up user data
        user= SessionManager.getInstance(requireActivity().applicationContext).getUser()
        androidViewModel=ViewModelProvider(this).get(calStateViewModel::class.java)
        androidViewModel.getUserState(user.token)
        androidViewModel.calState().observe(viewLifecycleOwner, Observer {
            consumedCals=it
            binding.homepageDailyCalValue.text = "(${user.calGoal} Calories Limit \n+ ${totalCals} Calories burned) \n- ${consumedCals} Consumed Calories \n= ${user.calGoal+totalCals-consumedCals}"


        })


        //set activity lvl , control level
        // This callback will only be called when MyFragment is at least Started.
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
        initializeActivityLevel()

        binding.homepageControlValue.text = user.controlLvl.replaceFirstChar { it.uppercase() }
        binding.homepageHeightTv.text =
            requireContext().resources.getString(R.string.homepage_height_tv, "${(user.height * 100).toInt()}")
        binding.homepageWeightTv.text = requireContext().resources.getString(R.string.homepage_weight_tv, "${user.weight}")
        binding.homepageAgeTv.text = requireContext().resources.getString(R.string.homepage_age_tv, user.age.toString())
        binding.homepageUserName.text = user.firstName
        // Slide show code
        with(binding.journalsThumbnailsRecyclerView) {
            adapter = JournalThumbnailsAdapter(
                requireContext(), Layout.HORIZONTAL, this
            )
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            val compositePageTranfer = CompositePageTransformer()
            compositePageTranfer.addTransformer(MarginPageTransformer(30))
            compositePageTranfer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleX = 0.85F + r * 0.25F

            }
            setPageTransformer(compositePageTranfer)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    slidhandle.removeCallbacks(sliderRunnable)
                    slidhandle.postDelayed(sliderRunnable, 3000)
                }
            })
        }


        //End Of Slide Show Stuff
        // Specify fixed size to improve performance
        //binding.journalsThumbnailsRecyclerView.setHasFixedSize(true)

        // Enable up button for backward navigation
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.settingIco.setOnClickListener {
            action = HomepageDirections.actionGlobalSettingsFrag()
            Navigation.findNavController(binding.root).navigate(action)

        }
        binding.addMedication.setOnClickListener {
            action = HomepageDirections.actionGlobalAddmed()
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.addDisease.setOnClickListener {
            action = HomepageDirections.actionGlobalDiseases()
            Navigation.findNavController(binding.root).navigate(action)

        }
        //floating buttons
        binding.bottomNavViewFAB.setOnClickListener {
            onNavFabClick()
        }
        binding.addFoodNow.setOnClickListener {
            action = HomepageDirections.homepageToAddFood()
            Navigation.findNavController(binding.root).navigate(action)


        }
        binding.addMedicationNow.setOnClickListener {
            action = HomepageDirections.homepageToAddMed()
            Navigation.findNavController(binding.root).navigate(action)
        }

        binding.addReadingsNow.setOnClickListener {
            action= HomepageDirections.homepageToReadings()
            Navigation.findNavController(binding.root).navigate(action)
        }

        binding.bottomNavView.setOnItemReselectedListener { item ->
            when (item.itemId) {

                    R.id.settings -> {
                action = HomepageDirections.actionGlobalSettingsFrag()
                Navigation.findNavController(binding.root).navigate(action)
            }
                R.id.more     -> {
                    action = HomepageDirections.actionGlobalMore()
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
            true
        }
        // Initialize: GoogleFit.
        initializeGoogleFit()
        // Syncing:
        binding.syncGoogleFit.setOnClickListener { initializeGoogleFit() }
        // Returning:

        readingViewModel = ViewModelProvider(this)[ReadingViewModel::class.java]
        readingViewModel.readAllData.observe(viewLifecycleOwner, Observer { reading ->
            getControlLevel(reading)



        })


        return binding.root
    }

    private fun getControlLevel(reading: List<Reading>?)
    {

        var result= arrayListOf<Reading>()
        var finalHashmap= HashMap<String,String>()

        for (read in reading!!)
        {

            //get list of readings for the last 7 days
            var time: Long = read.date.toLong()
            time += TimeUnit.MILLISECONDS.convert(168, TimeUnit.HOURS)
            val timeNow = System.currentTimeMillis()
            if (time > timeNow) {
                result.add(read)
            }
        }
        var sysListDiabetes= arrayListOf<Int>()
        var sysListBP= arrayListOf<Int>()
        var diaListBP=arrayListOf<Int>()
        if (result.size>0)
        {
            for (read in result)
            {
                if (read.diseaseName=="blood pressure"){
                    sysListBP.add(read.sysName.toInt())
                    diaListBP.add(read.aioName)
                }else {
                    sysListDiabetes.add(read.sysName.toInt())
                }

            }


            var medianSysDiabetes=0
            var medianSysBP=0
            var medianDiaBP=0
            if (sysListBP.size>0){
                if (sysListBP.size%2==0){
                    medianSysBP=(sysListBP[(sysListBP.size/2)-1]+sysListBP[sysListBP.size/2])/2
                    medianDiaBP=(diaListBP[(diaListBP.size/2)-1]+diaListBP[diaListBP.size/2])/2
                }else {
                    medianSysBP=sysListBP[(sysListBP.size/2)]
                    medianDiaBP=diaListBP[(diaListBP.size/2)]
                }
                val controlLvl:String =bloodPreasureDecide(Pair(medianSysBP,medianDiaBP))
                finalHashmap.put("blood_preasure",controlLvl)
            }
            if(sysListDiabetes.size>0){
                if (sysListDiabetes.size%2==0){
                    medianSysDiabetes=(sysListDiabetes[(sysListDiabetes.size/2)-1]+sysListDiabetes[sysListDiabetes.size/2])/2
                }else {
                    medianSysDiabetes=sysListDiabetes[(sysListDiabetes.size/2)]
                }
                val controlLvl:String = when(medianSysDiabetes){
                    in 0..129 -> "normal"
                    in 130..220 -> "high"
                    else -> "uncontrolled"
                }
                finalHashmap.put("diabetes",controlLvl)

            }
            user.hashMap=finalHashmap
            SessionManager.getInstance(requireActivity().applicationContext).userLogin(user)
            binding.homepageControlValue.apply {
                var levelList= mutableListOf<String>()
                for((key,value) in finalHashmap){
                    levelList.add(value)

                }
                val repeationOfElements=levelList.groupingBy { it }.eachCount() // This will return {"normal": 3, "uncontrolled":5,....}

//
                var profileLvl = repeationOfElements.maxBy { it.value }?.key //should be a string
                if (profileLvl=="high"){
                    profileLvl="controlled"
                }

                text = profileLvl

            }


        }
    }
    private fun bloodPreasureDecide(medians: Pair<Int,Int>):String {
        var (sys,dia)=medians
        if(sys<120 && dia<80){
            return "normal"
        }else if ((sys in 120..139) && (dia in 80..90)){
            return "high"
        }else{
            return "uncontrolled"
        }

    }

    private fun initializeGoogleFit() {
        // Initializing:
        val fitnessOptions: GoogleSignInOptionsExtension = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
            .build()
        val googleSignInAccount = GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions)
        // Checking:
        if (!GoogleSignIn.hasPermissions(googleSignInAccount, fitnessOptions)) {
            // Asking for google permission:
            GoogleSignIn.requestPermissions(this, 81, googleSignInAccount, fitnessOptions)
        } else {
            // Hiding:
            binding.syncGoogleFit.visibility = View.GONE
            // Running:
            Thread {
                // Repeating(3S):
                fixedRateTimer("timer", false, 0L, 3000) {
                    // Getting Step Counter:
                    Fitness.getHistoryClient(requireActivity(), googleSignInAccount)
                        .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA).addOnSuccessListener {
                            // Initializing:
                            if (it.dataPoints.size > 0)
                                totalSteps = it.dataPoints.first().getValue(Field.FIELD_STEPS).asInt()
                            // Logging:
                            binding.homepageGoogleFitSteps.text = "Total Steps Today: $totalSteps"
                            Log.d("S...", "Size: ${it.dataPoints.size}")
                            Log.d("S...", "Total: $totalSteps")
                        }
                    // Getting Calories:
                    Fitness.getHistoryClient(requireActivity(), googleSignInAccount)
                        .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED).addOnSuccessListener {
                            // Initializing:
                            if (it.dataPoints.size > 0)
                                totalCals = it.dataPoints.first().getValue(Field.FIELD_CALORIES).asFloat().toInt()
                            binding.homepageGoogleFitCals.text = "Total Calories Burned Today: $totalCals"
                            //confusing, I know but calSate here is the calories burned, I saved it globaly so I can send it on recommmendation
                            user.calState=totalCals
                            SessionManager.getInstance(requireActivity().applicationContext).userLogin(user)
                            //// Logging:
                            Log.d("S...", "Size: ${it.dataPoints.size}")
                            Log.d("S...", "Cal: $totalCals")
                        }
                }
            }.run()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Super:
        super.onActivityResult(requestCode, resultCode, data)
        // Checking:
        if (resultCode == Activity.RESULT_OK && requestCode == 81) initializeGoogleFit()
    }

    // Method(InitializeActivityLevel):
    private fun initializeActivityLevel() {
        // Applying:
        /*
          0    -> "Low"
            1    -> "Slight"
            2    -> "Moderate"
            3    -> "High"
            else -> "Extreme"
binding.homepageActValue.text = when (user.activiyLvl) {
            0    -> "Low"
            1    -> "Slight"
            2    -> "Moderate"
            3    -> "High"
            else -> "Extreme"
        }
         */
        binding.homepageActValue.apply {
            // Setting:
            user.activiyLvl = when {
                // Checking:
                totalSteps < 1000  -> 0
                totalSteps in 1001..1999 -> 1
                totalSteps in 2000..3000 -> 2
                totalSteps in 3001..4000 -> 3
                else  -> 4
            }

            SessionManager.getInstance(requireActivity().applicationContext).userLogin(user)
            text = when(user.activiyLvl){
                0    -> "Low"
                1    -> "Slight"
                2    -> "Moderate"
                3    -> "High"
                else -> "Extreme"

            }

        }
    }

    private val sliderRunnable = Runnable {
        binding.journalsThumbnailsRecyclerView.currentItem = binding.journalsThumbnailsRecyclerView.currentItem + 1
    }

    override fun onPause() {
        super.onPause()
        slidhandle.postDelayed(sliderRunnable, 3000)
    }

    override fun onResume() {
        super.onResume()
        slidhandle.postDelayed(sliderRunnable, 3000)
    }

    private fun onNavFabClick() {
        setVisiblity(clicked)
        setAnimation(clicked)
        setClicable(clicked)
        clicked = !clicked
    }

    private fun setVisiblity(clicked:Boolean){
        if (!clicked){
            binding.addFoodNow.visibility=View.VISIBLE
            binding.addMedicationNow.visibility=View.VISIBLE
            binding.addReadingsNow.visibility=View.VISIBLE
        }else {

            binding.addFoodNow.visibility = View.INVISIBLE
            binding.addMedicationNow.visibility = View.INVISIBLE
            binding.addReadingsNow.visibility = View.INVISIBLE

        }
    }


            private fun setAnimation(clicked: Boolean) {
                if (!clicked) {
                    binding.addFoodNow.startAnimation(fromBottom)
                    binding.addMedicationNow.startAnimation(fromBottom)
                    binding.addReadingsNow.startAnimation(fromBottom)
                    binding.bottomNavViewFAB.startAnimation(rotateOpen)
                } else {
                    binding.addFoodNow.startAnimation(toBottom)
                    binding.addMedicationNow.startAnimation(toBottom)
                    binding.addReadingsNow.startAnimation(toBottom)
                    binding.bottomNavViewFAB.startAnimation(rotateClose)

                }
            }

            private  fun setClicable(clicked: Boolean) {
                if (!clicked) {
                    binding.addFoodNow.isClickable = true
                    binding.addMedicationNow.isClickable = true
                    binding.addReadingsNow.isClickable = true
                } else {

                    binding.addFoodNow.isClickable = false
                    binding.addMedicationNow.isClickable = false
                    binding.addReadingsNow.isClickable = false
                }
            }
    fun getUserState() {

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, URLs.URL_STATE_GET, null //, jsonBody
            , Response.Listener { response ->
                try {
                    consumedCals=response.getJSONObject("general").getInt("total_cals")
                    Log.d("Message",consumedCals.toString())
                    //response is the answer
                    //Toast.makeText(MainActivity.this, newjson.toString(), Toast.LENGTH_SHORT).show();
                    //Log.d("response",newjson.toString());
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                it.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "Error on second request",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            //headers
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json; charset=UTF-8"
                headers["Authorization"] = "Bearer ${user.token}"
                return headers
            }
        }
        VolleySingleton.getInstance(requireContext()).AddToRequestQueue(jsonObjectRequest)
    }


                }