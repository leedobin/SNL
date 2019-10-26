package com.four_leader.snl.util;

import android.app.Application;

public class APIs extends Application {

    public static String baseURL = "https://snltest.run.goorm.io/SNL/SNL";

    public static String sendMail = baseURL + "/sendMail.php";
    public static String idcheck = baseURL + "/USER/idcheck.php";
    public static String signup = baseURL + "/USER/signup.php";
    public static String nicknamecheck = baseURL + "/USER/nicknamecheck.php";
}
