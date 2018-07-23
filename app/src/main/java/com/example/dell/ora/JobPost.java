package com.example.dell.ora;

import android.app.ActionBar;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JobPost extends AppCompatActivity {
    private static final String TAG = "";
    private Button submit;
    private EditText skills,location,description,title,name;
    private FirebaseFirestore db;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post);
        db = FirebaseFirestore.getInstance();

        toolbar= findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        title= findViewById(R.id.title);
        location= findViewById(R.id.location);
        skills= findViewById(R.id.skills);
        description= findViewById(R.id.description);
        name= findViewById(R.id.name);


        submit= findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {
                createPost();
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Maino.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
    public void createPost() {
        if (TextUtils.isEmpty(title.getText().toString().trim())) {
            title.setError( "First name is required!" );
            return;
        }
        String title_val = title.getText().toString().trim();

        if (TextUtils.isEmpty(location.getText().toString().trim())) {
            location.setError( "Location  is required!" );
            return;
        }
        String location_val = location.getText().toString().trim();

        String[] items = skills.getText().toString().trim().split(",");
        int itemCount = items.length;

        if (TextUtils.isEmpty(skills.getText().toString().trim())) {
            skills.setError( "Skills are required!" );
            if(itemCount>3) {
                skills.setError("Jo me shume se 3!");
                return;
            }
            return;
        }
        String skills_val = skills.getText().toString().trim();

        if (TextUtils.isEmpty(description.getText().toString().trim())) {
            description.setError( "First name is required!" );
            return;
        }
        String description_val = description.getText().toString().trim();
        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            name.setError( "First name is required!" );
            return;
        }
        String name_val = name.getText().toString().trim();

            Map<String, Object> jobs = new HashMap<>();

            jobs.put("co_name",name_val);
            jobs.put("date",new Date());
            jobs.put("description",description_val);
            jobs.put("location",location_val);
            jobs.put("skills",skills_val);
            jobs.put("title",title_val);

            db.collection("jobs")
                    .add(jobs)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText( JobPost.this,"Please wait till is reviewed", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(JobPost.this, Maino.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w( TAG,"Gabim!", e);
                        }
                    });
        }

    }