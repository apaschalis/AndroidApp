package com.example.ap1307.stockapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;


public class GetInfo extends Activity {
    String stockName;
    TextView currentPrice;
    TextView name;
    TextView highLowPrice;
    TextView priceChange;
    ImageView stockGraph;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);
        name = (TextView) findViewById(R.id.textView3);
        currentPrice = (TextView) findViewById(R.id.textView);
        highLowPrice = (TextView) findViewById(R.id.textView4);
        priceChange = (TextView) findViewById(R.id.textView6);
        button = (Button) findViewById(R.id.button2);
        stockGraph = (ImageView) findViewById(R.id.imageView);

        Handler handler = new Handler(Looper.getMainLooper()) ;
        Runnable myRunnable = new Runnable(){
            public void run() {
                try {
                     stockName = getIntent().getStringExtra("stockName");

                    MainActivity mainActivity = new MainActivity();
                    String quotesURL = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20%28%22"+stockName+"%22%29&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
                    String str = mainActivity.getConnection(quotesURL);
                    String graphURL = "http://chart.finance.yahoo.com/z?s="+stockName+"&t=4y&q=l&l=on&z=l&p=m50,e200,v&c=GBP,USD";


                    URL newurl = new URL(graphURL);
                    Bitmap bitmap= BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                    stockGraph.setImageBitmap(bitmap);
                        JSONObject file = new JSONObject(str);
                        JSONObject query = file.getJSONObject("query");
                        JSONObject results = query.getJSONObject("results");
                        JSONObject quotes = results.getJSONObject("quote");
                        name.setText(quotes.getString("Name"));
                        currentPrice.append("   ");
                        currentPrice.append(quotes.getString("LastTradePriceOnly"));
                        highLowPrice.append("   ");
                        highLowPrice.append(quotes.getString("DaysRange"));
                        priceChange.append("   ");
                        priceChange.append(quotes.getString("Change"));

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent("com.example.ap1307.stockapp.HistoricalQuotes");
                            intent.putExtra("stockName", stockName);
                            startActivity(intent);
                        }
                    });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }




            }
        };
        handler.post(myRunnable);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_info, menu);
        return true;
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
}
