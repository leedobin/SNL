package com.four_leader.snl.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by LeeDoBin on 2019-03-09.
 */

public class ConnectAPI extends AsyncTask<String[], Void, String> {
    @Override
    protected String doInBackground(String[]... params) {
        String res="";
        try {
            String url = params[0][0];
            String id = params[0][1];

            url = url + "?" + id;
            Log.i("tagtt" , "url : " + url);
            Log.i("ttt,", "sda");
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            conn.setReadTimeout(1000);
            conn.setConnectTimeout(1000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type","application/json");

            byte[] outputInBytes = id.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write( outputInBytes );
            os.close();

            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = br.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            br.close();

            res = response.toString();

            Log.i("tagtt","result : " + res);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}
