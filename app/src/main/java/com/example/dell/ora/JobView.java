package com.example.dell.ora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class JobView extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_view);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String title_ex = getIntent().getStringExtra("title");
            String skills_ex = getIntent().getStringExtra("skills");
            String description_ex = getIntent().getStringExtra("description");
            String company_ex = getIntent().getStringExtra("company");
            TextView title = findViewById(R.id.title);
            TextView comp = findViewById(R.id.company_name);
            TextView desc = findViewById(R.id.description);
            TextView sk = findViewById(R.id.skills);
            title.setText(title_ex);
            comp.setText(company_ex);
            desc.setText(description_ex);
            sk.setText(skills_ex);
        }

    }


