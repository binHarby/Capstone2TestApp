package com.example.capstone2test.model;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class User {
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("id")
    private int id;
    @SerializedName("age")
    private int age;
    @SerializedName("tdee")
    private int tdee;
    @SerializedName("bmi")
    private int bmi;
    /* possibly null value */
    @SerializedName("cal_goal")
    private int calGoal;
    /* possibly null value */
    @SerializedName("cal_diff")
    private int calDiff;
    @SerializedName("weight")
    private int weight;
    @SerializedName("activity_lvl")
    private int activiyLvl;
    @SerializedName("control_lvl")
    private String controlLvl;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("access_token")
    private String token;
    @SerializedName("birthday")
    private Date birthday;
    @SerializedName("bloodtype")
    private String bloodtype;
    @SerializedName("gender")
    private String gender;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("height")
    private double height;
    @SerializedName("macros")
    private Macros macrosGoal;
    @SerializedName("minerals")
    private Minerals mineralsGoal;
    @SerializedName("vitamins")
    private Vitamins vitaminsGoal;
    @SerializedName("traces")
    private Traces tracesGoal;
    private HashMap<String,String> hashMap;
    private int calState;


    public User() {
    }

    public User(String token) {
        this.token = token;
    }

    @SuppressLint("SimpleDateFormat")
    public User(String firstName, String lastName, int id, int age, int tdee, int bmi, int calGoal, int calDiff, int weight, int activiyLvl, String controlLvl, String email, String password, String token, String birthday, String bloodtype, String gender, String created_at, String updated_at, double height, Macros macrosGoal, Minerals mineralsGoal, Vitamins vitaminsGoal, Traces tracesGoal,HashMap<String,String> hashMap,int calState) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.age = age;
        this.tdee = tdee;
        this.bmi = bmi;
        this.calGoal = calGoal;
        this.calDiff = calDiff;
        this.weight = weight;
        this.activiyLvl = activiyLvl;
        this.controlLvl = controlLvl;
        this.email = email;
        this.password = password;
        this.token = token;
        try {
            Date date1= null;
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(parseTodaysDate(birthday));
            this.birthday =date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.bloodtype = bloodtype;
        this.gender = gender;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.height = height;
        this.macrosGoal = macrosGoal;
        this.mineralsGoal = mineralsGoal;
        this.vitaminsGoal = vitaminsGoal;
        this.tracesGoal = tracesGoal;
        this.hashMap = hashMap;
        this.calState=calState;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public int getCalState() {
        return calState;
    }

    public void setCalState(int calState) {
        this.calState = calState;
    }

    public void setHashMap(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTdee() {
        return tdee;
    }

    public void setTdee(int tdee) {
        this.tdee = tdee;
    }

    public int getBmi() {
        return bmi;
    }

    public void setBmi(int bmi) {
        this.bmi = bmi;
    }

    public int getCalGoal() {
        return calGoal;
    }

    public void setCalGoal(int calGoal) {
        this.calGoal = calGoal;
    }

    public int getCalDiff() {
        return calDiff;
    }

    public void setCalDiff(int calDiff) {
        this.calDiff = calDiff;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getActiviyLvl() {
        return activiyLvl;
    }

    public void setActiviyLvl(int activiyLvl) {
        this.activiyLvl = activiyLvl;
    }

    public String getControlLvl() {
        return controlLvl;
    }

    public void setControlLvl(String controlLvl) {
        this.controlLvl = controlLvl;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBloodtype() {
        return bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        this.bloodtype = bloodtype;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Macros getMacrosGoal() {
        return macrosGoal;
    }

    public void setMacrosGoal(Macros macrosGoal) {
        this.macrosGoal = macrosGoal;
    }

    public Minerals getMineralsGoal() {
        return mineralsGoal;
    }

    public void setMineralsGoal(Minerals mineralsGoal) {
        this.mineralsGoal = mineralsGoal;
    }

    public Vitamins getVitaminsGoal() {
        return vitaminsGoal;
    }

    public void setVitaminsGoal(Vitamins vitaminsGoal) {
        this.vitaminsGoal = vitaminsGoal;
    }

    public Traces getTracesGoal() {
        return tracesGoal;
    }

    public void setTracesGoal(Traces tracesGoal) {
        this.tracesGoal = tracesGoal;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public static String parseTodaysDate(String time) {



        String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";

        String outputPattern = "yyyy-MM-dd";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);

            Log.i("mini", "Converted Date Today:" + str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}
