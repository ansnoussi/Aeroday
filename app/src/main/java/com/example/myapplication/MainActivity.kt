package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.RecyclerViews.CompetitionAdapter
import com.example.myapplication.RecyclerViews.CompetitionAdapter.OnCompetitionListener
import com.example.myapplication.Room.Model.AirshowParticipant
import com.example.myapplication.Room.Model.Competition
import com.example.myapplication.Room.ViewModel.AirshowParticipantViewModel
import com.example.myapplication.Room.ViewModel.CompetitionViewModel
import com.example.myapplication.Room.ViewModel.VoterViewModel
//import jdk.nashorn.internal.runtime.PropertyMap.diff
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Date
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class MainActivity  :  AppCompatActivity(), OnCompetitionListener{
    lateinit var competitionViewModel: CompetitionViewModel

    public val voterViewModel : VoterViewModel by viewModels()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date : LocalDateTime = LocalDateTime.now()
            val aerodayDate : LocalDateTime = LocalDateTime.of(2020,4,12,0,0,0)
            val differenceBetweenTwoDays = Duration.between(date,aerodayDate).toDays()
            countDown_text.text = "J-$differenceBetweenTwoDays. Stay Tuned !"
        }


        //recyclerview settings
        challenges_recycler_view.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        challenges_recycler_view.setHasFixedSize(true)

        val competitionAdapter : CompetitionAdapter = CompetitionAdapter(this)
        challenges_recycler_view.adapter = competitionAdapter

        val competitionViewModel : CompetitionViewModel by viewModels()
        val airshowParticipantViewModel : AirshowParticipantViewModel by viewModels()

        competitionViewModel.all.subscribe {
            competitions ->

                competitionAdapter.setCompetitions(competitions as ArrayList<Competition>)
            Log.d("mymessage : ","${competitionAdapter.getCompetitions()}")
        }


        voterViewModel.allVoters.subscribe { voters ->
            Log.d("allVoters : ", "$voters")
        }

    }

    @SuppressLint("CheckResult")
    override fun onCompetitionClick(position: Int) : Unit{
        var exitfunction = false
        val allCompetitions: ArrayList<Competition> = CompetitionAdapter.competitions
        var competition = allCompetitions.get(position)

        Log.d("thecompetitionis : ", "$competition")
        if ((competition.name.toUpperCase().trim() == "AIRSHOW") && (competition.active==true)) {

            Log.d("hani hna", "hani hna")
            if(LoadingActivity.voterExistence == true){
                Toast.makeText(this, "You already Voted Sorry !!", Toast.LENGTH_SHORT)
                    .show()
            }else{
                var intent = Intent(this, VotingActivity::class.java)
                startActivity(intent)
            }

        }
    }


}
