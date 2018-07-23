package com.example.dell.ora;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.personality_insights.v2.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v2.model.Profile;
import com.ibm.watson.developer_cloud.personality_insights.v2.model.Trait;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AnalyzerFragment extends Fragment {
    private static String text;
    private static ListView lv;
    private File root;
    private static final int DIALOG_LOAD_FILE = 111;
    private static Context fragment_activity;
    private static ArrayList<HashMap<String, String>> array_list;
    private  InputStream is;
    private Button b1;
    private  ViewGroup transitionsContainer;
    public AnalyzerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View x = inflater.inflate(R.layout.fragment_analyzer, container, false);
        lv = x.findViewById(R.id.list);
        transitionsContainer = x.findViewById(R.id.viewgroup);

          b1 = x.findViewById(R.id.buttonStripText);
        Button button = x.findViewById(R.id.pick);
        setup();

        if (isConnected()) {
            fragment_activity = getActivity().getApplicationContext();
            array_list = new ArrayList<>();

            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("file/*");
                    startActivityForResult(intent,DIALOG_LOAD_FILE);

                }
            });
            b1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    stripText(v);
                    new IBM().execute();
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Internet Reuired!");
            builder.setMessage("No Internet Connection");
        }
        return x;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case DIALOG_LOAD_FILE:
                if(resultCode==RESULT_OK){
                    try {
                        is = getActivity().getApplicationContext().getContentResolver().openInputStream(data.getData());
                        TransitionManager.beginDelayedTransition(transitionsContainer);
                        b1.setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    private void setup() {
        PDFBoxResourceLoader.init(getActivity().getApplicationContext());
        root = android.os.Environment.getExternalStorageDirectory();

    }
    public void stripText(View v) {
        String parsedText = null;
        try {
            PDDocument document = PDDocument.load( is );
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setStartPage(0);
            pdfStripper.setEndPage(1);
            text = pdfStripper.getText(document);
                if (document != null) document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    private static class IBM extends AsyncTask<Void, Void, Void> {
        private String all_items;

        protected Void doInBackground(Void... voids) {
            final PersonalityInsights service = new PersonalityInsights();
            service.setUsernameAndPassword("username", "password");
            service.setEndPoint("https://gateway.watsonplatform.net/personality-insights/api");
            Profile profile = service.getProfile(text);
            Trait trait = profile.getTree();

            List<Trait> traits1=trait.getChildren();
            for( Trait myTrait1 : traits1 ) {
                List<Trait> myTraits2=myTrait1.getChildren();
                for( Trait myTrait2 : myTraits2 ) {
                    List<Trait> myTraits3=myTrait2.getChildren();
                    int i = 0;
                    for( Trait myTrait3 : myTraits3 ) {
                        if (i == 3) {
                            String myName3 = myTrait3.getName();
                            Double myPercentage3 = myTrait3.getPercentage();

                            HashMap<String, String> tmp = new HashMap<>();
                            tmp.put("name", myName3);
                            tmp.put("percentage", Double.toString(myPercentage3));
                            array_list.add(tmp);
                        }
                        i++;

                        }
                    }
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ListAdapter adapter = new SimpleAdapter(fragment_activity, array_list,
                    R.layout.watson_output, new String[]{"name", "percentage"},
                    new int[]{R.id.name, R.id.percentage});
            lv.setAdapter(adapter);
        }
    }
    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
