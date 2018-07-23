package com.example.dell.ora;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JobsFragment extends Fragment{
    private RecyclerView recycle;
    Query query;
    private FirebaseFirestore fb;
    private FirestoreRecyclerAdapter<JobsLists, JobsViewHolder> adapter;
    public JobsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View jobsView = inflater.inflate(R.layout.fragment_jobs, container, false);
        recycle =  jobsView.findViewById(R.id.recycle);
        TextView txt=  jobsView.findViewById(R.id.text);
        recycle.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        fb = FirebaseFirestore.getInstance();

        query = fb.collection("jobs")
                .orderBy("skills", Query.Direction.DESCENDING)
                .startAt("ghj")
                .limit(3);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                DocumentSnapshot lastVisible = documentSnapshots.getDocuments()
                                        .get(documentSnapshots.size()-1);
                                Query next = fb.collection("jobs")
                                        .orderBy("date", Query.Direction.DESCENDING)
                                        .startAfter(lastVisible)
                                        .limit(3);
                                adapter.notifyDataSetChanged();
                            }
                        });
            }
        });


        jobView(query);
        return jobsView;
    }
    public void jobView(Query query){
        recycle.setItemAnimator(new DefaultItemAnimator());
        FirestoreRecyclerOptions<JobsLists> options = new FirestoreRecyclerOptions.Builder<JobsLists>()
                .setQuery(query, JobsLists.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<JobsLists, JobsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final JobsViewHolder holder, final int position, @NonNull final JobsLists jobsmodel) {

                holder.setTitle(jobsmodel.getTitle());
                holder.setDate(jobsmodel.getDate());
                holder.setCo_Name(jobsmodel.getCo_Name());
                holder.setSkills(jobsmodel.getSkills());
                holder.setLocation(jobsmodel.getLocation());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, JobView.class);
                        intent.putExtra("title",  jobsmodel.getTitle());
                        intent.putExtra("skills",  jobsmodel.getSkills());
                        intent.putExtra("company",  jobsmodel.getCo_Name());
                        intent.putExtra("description",  jobsmodel.getDescription());
                        context.startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public JobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_card, parent, false);
                return new JobsViewHolder(view);
            }
            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };
        recycle.setAdapter(adapter);

    }

    private class JobsViewHolder extends RecyclerView.ViewHolder   {
        private View view;

        JobsViewHolder(final View itemView) {
            super(itemView);
            view = itemView;

    }

        private TextView ti,co,da,ta,lo;
        public void setTitle(String name) {
            ti = view.findViewById(R.id.title);
            ti.setText(name);
        }
        public void setDate(Date date) {
            da =  view.findViewById(R.id.date);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = sdf.format(date);
            da.setText(dateString);
        }

        public void setCo_Name(String co_name) {
            co = view.findViewById(R.id.co_name);
            co.setText(co_name);
        }
        public void setSkills(String skills) {
            ta =  view.findViewById(R.id.tags);
            ta.setText(skills);
        }
        public void setLocation(String location) {
            lo =  view.findViewById(R.id.location);
            lo.setText(location);
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

}