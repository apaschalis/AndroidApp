package com.example.ap1307.stockapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity  {

    ListView liveStock;
    private ArrayAdapter<String> arrayAdapter ;
    ArrayList<String> listStock;
    String stockName[] =  {"GOOG","MSFT","YHOO","BP","AAPL","TSCO","TESO","LHL.F","SNE","TSCO.L","SBRY.L","^FTSE","EBAY","AMZN","0R3A.L","IBM.L",
                          "^FTMC","^FTAI","LLOY.L","VOD.L","TEF","TWTR","FB","BARC.L","HSBC","39IB.L","BC94.L","HTCXF","BSY.L","QCOM","EE","ORAN",
                           "BT-A.L","TALK.L"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        listStock = new ArrayList<String>();
        liveStock = (ListView) findViewById(R.id.getData);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_live_stock, listStock);

        Handler handler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            public void run() {
                try {


                    for (int i = 0; i < stockName.length; i++) {
                        String quotesURL = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20%28%22" + stockName[i]
                                + "%22%29&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
                        String str = getConnection(quotesURL);


                        JSONObject file = new JSONObject(str);
                        JSONObject query = file.getJSONObject("query");
                        JSONObject results = query.getJSONObject("results");
                        JSONObject quotes = results.getJSONObject("quote");

                        listStock.add(quotes.getString("Name") + ":  " + quotes.getString("LastTradePriceOnly") + "  " + quotes.getString("Change"));

                        liveStock.setAdapter(arrayAdapter);
                        liveStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                final String arrayPos = stockName[pos];
                                Intent intent = new Intent("com.example.ap1307.stockapp.GetInfo");
                                intent.putExtra("stockName", arrayPos);
                                startActivity(intent);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        handler.post(myRunnable);

        Button button = (Button) findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent helpPage = new Intent(getApplicationContext(),Help.class);
                startActivity(helpPage);
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick(View view) {

    }

    protected String getConnection(String str) {
        HttpURLConnection connect = null;
        try {
            connect = (HttpURLConnection) new URL(str).openConnection();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            if (connect.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return getResponse(connect);
            }
        } catch (MalformedURLException m) {

        } catch (IOException e) {

        }
        return "";
    }

    public String getResponse(HttpURLConnection url) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(url.getInputStream());
            char[] buffer = new char[1024];
            int reader;
            while ((reader = inputStreamReader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, reader);
            }
            inputStreamReader.close();

        } catch (IOException e) {

        }
        return stringBuilder.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        static EditText stockName;
        //TextView currentPrice;
        Button submitButton;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            stockName = (EditText) rootView.findViewById(R.id.editText);
            submitButton = (Button) rootView.findViewById(R.id.button);
//            submitButton.setEnabled(true);
//
//            if (stockName.getText() == null)
//            {
//              submitButton.setEnabled(false);
//
//            }

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("com.example.ap1307.stockapp.GetInfo");
                    intent.putExtra("stockName", stockName.getText().toString());
                    startActivity(intent);


                    //   file.close();
                    //   CSVReader csvReader = new CSVReader(new InputStreamReader(getAssets().open("quotes.csv")));


                }
            });


            return rootView;
        }

        public static String getStockName()
        {
         return stockName.getText().toString();
        }
    }
}
