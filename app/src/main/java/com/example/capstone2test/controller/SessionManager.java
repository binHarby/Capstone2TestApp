package com.example.capstone2test.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.capstone2test.MainActivity;
import com.example.capstone2test.model.Macros;
import com.example.capstone2test.model.Minerals;
import com.example.capstone2test.model.Traces;
import com.example.capstone2test.model.User;
import com.example.capstone2test.model.Vitamins;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class SessionManager {
    /* Fields from user class
   private int id;
   private int age;
   private int tdee;
   private int bmi;
   private int calGoal;
   private int calDiff;
   private int weight;
   private int activiyLvl;
   private String controlLvl;
   private String email;
   private String password;
   private String token;
   private String birthday;
   private String bloodtype;
   private String gender;
   private String created_at;
   private String updated_at;
   private double height;
   private Macros macrosGoal;
   private Minerals mineralsGoal;
   private Vitamins vitaminsGoal;
   private Traces tracesGoal;
    */
    private static final String SHARED_PREF_NAME="userToken";
    /* Int values
    **/
    private static final String KEY_ID="userID";
    private static final String KEY_AGE="userAGE";
    private static final String KEY_TDEE="userTdee";

    private static final String KEY_BMI="userBMI";
    private static final String KEY_CAL_GOAL="CALGOAL";
    private static final String KEY_CAL_DIFF="CALDIFF";
    private static final String KEY_WEIGHT="userBMI";
    private static final String KEY_ACTIVITY_LVL="ACTIVITYLVL";
    /* Double values or floats */
    private static final String KEY_HEIGHT="HEIGHT";
    /* String Values */
    private static final String KEY_CONTROL_LVL="controlLvl";
    private static final String KEY_BLOODTYPE="BLOODTYPE";
    private static final String KEY_EMAIL="email";
    private static final String KEY_GENDER="gender";
    private static final String KEY_PASSWORD="password";
    private static final String KEY_TOKEN="token";
    private static final String KEY_Login="login";
    private static final String KEY_FIRST_NAME="userFirst";
    private static final String KEY_LAST_NAME="userLast";
    /* Dates */
    private static final String KEY_BIRTHDAY="birthday";
    private static final String KEY_CREATED_AT="createdAT";
    private static final String KEY_UPDATED_AT="updatedAt";

    /* Objects (Saved As Json strings) */
    private static final String KEY_GOAL_MACROS="macros";
    private static final String KEY_GOAL_MINERALS="MINERALS";
    private static final String KEY_GOAL_VITAMINS="VITAMINS";
    private static final String KEY_GOAL_TRACES="TRACES";

    /*utils */
    private final Gson gson = new Gson();
    private String obj;
    private Macros macros;
    private Minerals minerals;
    private Vitamins vitamins;
    private Traces traces;
    private String tmp;





    private static SessionManager mInstance;
    private static Context mCtx;

    public SessionManager(Context context) {
        mCtx=context;
    }
    public static synchronized SessionManager getInstance(Context context){
        if(mInstance==null){
            mInstance= new SessionManager(context);
        }
        return mInstance;
    }
    public void userLogin(User user){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        /*ints*/
        editor.putInt(KEY_ID,user.getId());
        editor.putInt(KEY_AGE,user.getAge());
        editor.putInt(KEY_TDEE,user.getTdee());
        editor.putInt(KEY_BMI,user.getBmi());
        editor.putInt(KEY_CAL_GOAL,user.getCalGoal());
        editor.putInt(KEY_CAL_DIFF,user.getCalDiff());
        editor.putInt(KEY_WEIGHT,user.getWeight());
        editor.putInt(KEY_ACTIVITY_LVL,user.getActiviyLvl());
        /* Double values or floats */
        editor.putFloat(KEY_HEIGHT,(float) user.getHeight());
        /* Strings */
        editor.putString(KEY_EMAIL,user.getEmail());
        editor.putString(KEY_PASSWORD,user.getPassword());
        editor.putString(KEY_TOKEN,user.getToken());
        editor.putString(KEY_CONTROL_LVL,user.getControlLvl());
        editor.putString(KEY_FIRST_NAME,user.getFirstName());
        editor.putString(KEY_BIRTHDAY,user.getBirthday().toString());
        editor.putString(KEY_LAST_NAME,user.getLastName());
        editor.putString(KEY_BLOODTYPE,user.getBloodtype());
        editor.putString(KEY_GENDER,user.getGender());

        /* Objects (Saved As Json strings) */
        if(user.getMacrosGoal() !=null){
            obj = gson.toJson(user.getMacrosGoal());

            editor.putString(KEY_GOAL_MACROS,obj);

        }else {
            editor.putString(KEY_GOAL_MACROS,null);
        }
        if(user.getMineralsGoal() !=null){
            String obj = gson.toJson(user.getMineralsGoal());
            editor.putString(KEY_GOAL_MINERALS,obj);

        }else {
            editor.putString(KEY_GOAL_MINERALS,null);
        }
        if(user.getVitaminsGoal() !=null){
            String obj = gson.toJson(user.getVitaminsGoal());
            editor.putString(KEY_GOAL_VITAMINS,obj);

        }else {
            editor.putString(KEY_GOAL_VITAMINS,null);
        }
        if(user.getTracesGoal() !=null){
            String obj = gson.toJson(user.getTracesGoal());
            editor.putString(KEY_GOAL_TRACES,obj);

        }else {
            editor.putString(KEY_GOAL_TRACES,null);
        }



        editor.apply();



    }
    public boolean isUserLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_Login,false) ;

    }
    public void setUserLoggedIn(boolean b){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        /*ints*/
        editor.putBoolean(KEY_Login,b);

        editor.apply();


    }
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN,null) !=null;

    }
    public User getToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new User(sharedPreferences.getString(KEY_TOKEN,null));
    }
    public void userLogout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx,MainActivity.class));



    }


    public User getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);



        tmp = sharedPreferences.getString(KEY_GOAL_MACROS,null);
        if (tmp !=null){
            macros=gson.fromJson(tmp,Macros.class);

        }else {
            macros=null;
        }
        tmp = sharedPreferences.getString(KEY_GOAL_MINERALS,null);
        if (tmp !=null){
            minerals=gson.fromJson(tmp,Minerals.class);

        }else {
            minerals=null;
        }
        tmp = sharedPreferences.getString(KEY_GOAL_VITAMINS,null);
        if (tmp !=null){
            vitamins=gson.fromJson(tmp,Vitamins.class);

        }else {
            vitamins=null;
        }
        tmp = sharedPreferences.getString(KEY_GOAL_TRACES,null);
        if (tmp !=null){
            traces=gson.fromJson(tmp,Traces.class);

        }else {
            traces=null;
        }

        /*
        Reading(int id, int age, int tdee, int bmi, int calGoal
            , int calDiff, int weight, int activiyLvl, String controlLvl
            , String email, String password, String token, String birthday
            , String bloodtype, String gender, String created_at, String updated_at
            , double height, Macros macrosGoal, Minerals mineralsGoal, Vitamins vitaminsGoal
            , Traces tracesGoal)

         */
        return new User(
                sharedPreferences.getString(KEY_FIRST_NAME,null),sharedPreferences.getString(KEY_LAST_NAME,null),
                sharedPreferences.getInt(KEY_ID,-1),sharedPreferences.getInt(KEY_AGE,-1),
                sharedPreferences.getInt(KEY_TDEE,-1),sharedPreferences.getInt(KEY_BMI,-1),
                sharedPreferences.getInt(KEY_CAL_GOAL,-1),sharedPreferences.getInt(KEY_CAL_DIFF,-1),
                sharedPreferences.getInt(KEY_WEIGHT,-1),sharedPreferences.getInt(KEY_ACTIVITY_LVL,-1),
                sharedPreferences.getString(KEY_CONTROL_LVL,null),sharedPreferences.getString(KEY_EMAIL,null),
                sharedPreferences.getString(KEY_PASSWORD,null),sharedPreferences.getString(KEY_TOKEN,null),
                sharedPreferences.getString(KEY_BIRTHDAY,null),sharedPreferences.getString(KEY_BLOODTYPE,null),
                sharedPreferences.getString(KEY_GENDER,null),sharedPreferences.getString(KEY_CREATED_AT,null),
                sharedPreferences.getString(KEY_UPDATED_AT,null),(double) sharedPreferences.getFloat(KEY_HEIGHT,0),
                macros,minerals,vitamins,traces

        );
    }

}
