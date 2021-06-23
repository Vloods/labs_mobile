package ru.tishin.asyncapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button button = null;
    private CbrfLoader cbrf_loader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button_exchange_rate);
    }

    private String getUrl(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
        return getResources().getString(R.string.cbrf_request, dateFormat.format(date));
    }

    public void OnClick(View view) {
        cbrf_loader = new CbrfLoader();
        cbrf_loader.execute(getUrl());
    }

    private String[] findCurrencies(XmlPullParser parser, ArrayList<String> charCodes) throws XmlPullParserException, IOException {
        String[] currencyArray = new String[charCodes.size()];
        int foundCurrenciesCount = 0;
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT && foundCurrenciesCount != charCodes.size()){
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("CharCode")){
                parser.next();
                if (charCodes.contains(parser.getText())){
                    String currency = new String();
                    // переменная index нужна, чтобы найденные валюты были в том же порядке,
                    // в котором они изначально требовались, а не в том, в котором они идут в потоке xml
                    int index = charCodes.indexOf(parser.getText());
                    while (!(parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals("Valute"))){
                        parser.next();
                        if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("Name")){
                            parser.next();
                            currency = parser.getText() + ": ";
                        }
                        if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("Value")){
                            parser.next();
                            currency = currency + parser.getText();
                            currencyArray[index] = currency;
                            ++foundCurrenciesCount;
                        }
                    }
                }
            }
            parser.next();
        }
        return currencyArray;
    }

    private void parseResults(String resultString){
        TextView textView = (TextView) findViewById(R.id.text_exchange_rate);
        textView.setText(resultString);
    }

    private class CbrfLoader extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params){
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.connect();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                StringBuilder stringBuilder = new StringBuilder();
                InputStream is = httpURLConnection.getInputStream();

                parser.setInput(is, "windows-1251");
                parser.next();

                ArrayList<String> currenciesToFind = new ArrayList<String>();
                currenciesToFind.add("USD");
                currenciesToFind.add("EUR");

                for (String currency : findCurrencies(parser,currenciesToFind)){
                    stringBuilder.append(currency).append("\n");
                }
                return stringBuilder.toString();

            } catch (XmlPullParserException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

            finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            button.setEnabled(true);
            parseResults(result);
        }

        @Override
        protected void onPreExecute() {
            button.setEnabled(false);
        }


    }
}