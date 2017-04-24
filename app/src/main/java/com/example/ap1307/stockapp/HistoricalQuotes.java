package com.example.ap1307.stockapp;


import android.os.Handler;
import android.os.Looper;
import org.w3c.dom.Element;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.util.ArrayList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class HistoricalQuotes  extends ActionBarActivity   {

    String stockName;
    ListView historicListView;
    private ArrayAdapter<String> arrayAdapter;
    ArrayList<String> historicArrayList;

    Document document = null;
        DocumentBuilderFactory documentFactory;
        DocumentBuilder builder;
        String symbol;
        String date;
        String open;
        String high;
        String low;
        String close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_quotes);

        historicArrayList  = new ArrayList<>();
        historicListView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.historicitems, historicArrayList);

        Handler handler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            public void run() {
                try {
                    stockName = getIntent().getStringExtra("stockName");

                    String historicDataURL = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22"+stockName+"%22%20and%20startDate%20%3D%20%222013-11-01%22%20and%20endDate%20%3D%20%222015-04-01%22&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

                   //DefaultHttpClient httpClient = new DefaultHttpClient();
                    DocumentBuilderFactory factory = DocumentBuilderFactory.
                            newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(historicDataURL);
                    NodeList nodeList = doc.getElementsByTagName("quote");
                    String name = "Looking at historic data for: "+stockName;
                    historicArrayList.add(name);


                        for (int i = 0; i < nodeList.getLength(); i++) {
                            Element element = (Element) nodeList.item(i);
                             String str= element.getAttribute("Symbol");
                            String strDate = element.getElementsByTagName("Date").item(0).getTextContent();
                            String strOpen = element.getElementsByTagName("Open").item(0).getTextContent();
                            String strHigh = element.getElementsByTagName("High").item(0).getTextContent();
                            String strLow = element.getElementsByTagName("Low").item(0).getTextContent();
                            String strClose = element.getElementsByTagName("Close").item(0).getTextContent();

                            historicArrayList.add("Date: "+strDate + "\nOpen: " + strOpen + "\nHigh: " + strHigh + "\nLow: " + strLow + "\nClose: " + strClose+"\n");


                            historicListView.setAdapter(arrayAdapter);
                        }
                    }catch (Exception e) {
                    e.printStackTrace();

                }
                }

        };

            handler.post(myRunnable);



        }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historical_quotes, menu);
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
