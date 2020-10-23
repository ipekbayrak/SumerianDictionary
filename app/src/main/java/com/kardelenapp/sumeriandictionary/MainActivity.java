package com.kardelenapp.sumeriandictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private EditText editText;
    private Button button;
    private AdView mAdView;
    private DBHelper mydb ;
    private Map<String, Object> hash;
    private ListView listview;
    private TextView textView1;
    boolean isEngDen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        mAdView = (AdView) findViewById(R.id.adView);
        editText = (EditText) findViewById(R.id.editText);
        listview = (ListView) findViewById(R.id.listView);
        textView1 = (TextView) findViewById(R.id.textView1);

        mydb = new DBHelper(this);


        loadAdds();
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() > 0)
                {
                    bringResults();
                }
                else
                {
                    listview.setAdapter(null);
                    textView1.setText("");
                }
            }

        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEngDen){
                    isEngDen=true;
                    button.setText("Sumerian\n-> English");

                    if(editText.length() > 0)
                    {
                        bringResults();
                    }
                    else
                    {
                        listview.setAdapter(null);
                        textView1.setText("");
                    }

                }
                else {
                    isEngDen=false;
                    button.setText("English ->\nSumerian");

                    if(editText.length() > 0)
                    {
                        bringResults();
                    }
                    else
                    {
                        listview.setAdapter(null);
                        textView1.setText("");
                    }
                }

            }
        });


        listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hash = (Map<String,Object>) listview.getItemAtPosition(position);

                String s1 = (String)hash.get("id");
                int i = Integer.parseInt(s1);


                if(isEngDen)
                {
                    textView1.setText(mydb.getMeaningEng(i));
                }
                else
                {
                    textView1.setText(mydb.getMeaningToki(i));
                }

            }
        });


    }

    public void bringResults()
    {
        if (!isEngDen)
        {
            SimpleAdapter adapter2 = new SimpleAdapter(MainActivity.this, mydb.getSimilarToki(editText.getText().toString()),
                    android.R.layout.simple_list_item_1,
                    new String[] { "isim"  },
                    new int[] { android.R.id.text1 });


            listview.setAdapter(adapter2);
        }
        else
        {
            SimpleAdapter adapter2 = new SimpleAdapter(MainActivity.this, mydb.getSimilar(editText.getText().toString()),
                    android.R.layout.simple_list_item_1,
                    new String[] { "isim" },
                    new int[] { android.R.id.text1 });

            //setContentView(listView);

            listview.setAdapter(adapter2);
        }


        /*
        SimpleAdapter adapter2 = new SimpleAdapter(TokiPonaDictionary.this, mydb.getSimilar(editText.getText().toString()),
                android.R.layout.simple_list_item_1,
                new String[] { "isim" },
                new int[] { android.R.id.text1 });

        //setContentView(listView);

        listview.setAdapter(adapter2);
        */
    }



    void loadAdds(){
        mAdView = (AdView) findViewById(R.id.adView);

        mAdView.setVisibility(View.GONE);



        mAdView.setAdListener(new AdListener() {
            private void showToast(String message) {
                View view = (AdView) findViewById(R.id.adView);
                if (view != null) {
                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //showToast(String.format("Ad failed to load with error code %d.", errorCode));
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                // showToast("Ad opened.");
            }

            @Override
            public void onAdClosed() {
                // showToast("Ad closed.");
            }

            @Override
            public void onAdLeftApplication() {
                // showToast("Ad left application.");
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();




        mAdView.loadAd(adRequest);
    }
}
