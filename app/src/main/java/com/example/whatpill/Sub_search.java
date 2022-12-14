package com.example.whatpill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.whatpill.fragment.Search;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Sub_search extends AppCompatActivity {

    String keyword, str;

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Intent intent = getIntent();
                keyword = intent.getStringExtra("keyword");
                str = getNaverSearch(keyword);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView searchResult2 = (TextView) findViewById(R.id.searchResult2);
                        searchResult2.setText(str);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    {
        thread.start();
    }

    public String getNaverSearch(String keyword) {

        String clientID = "cAwPpH8r3mtbTtf8BRka";
        String clientSecret = "0pLEnL5MaK";
        StringBuffer sb = new StringBuffer();

        try {
            String text = URLEncoder.encode(keyword, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/encyc.xml?query=" + text + "&display=10" + "&start=1";

            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Naver-Client-Id", clientID);
            conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            String tag;

            //inputStream???????????? xml??? ??????
            xpp.setInput(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName(); //?????? ?????? ????????????

                        if (tag.equals("item")) ; //????????? ?????? ??????
                        else if (tag.equals("title")) {

                            sb.append("?????? ??????: ");

                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");

                        } else if (tag.equals("description")) {

                            sb.append("??????: ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        }
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            return e.toString();
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_search);
    }




}