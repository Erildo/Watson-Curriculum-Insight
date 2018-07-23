package com.example.dell.ora;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.CategoryValueDataEntry;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.OrdinalColor;
import com.anychart.anychart.TagCloud;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChartFragment extends Fragment{
private ProgressBar pbar;
    public ChartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View x = inflater.inflate(R.layout.fragment_chart, container, false);
        AnyChartView anyChartView = x.findViewById(R.id.any_chart_view);
        pbar = x.findViewById(R.id.progressBar);
        anyChartView.setProgressBar(pbar);
        pbar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final TagCloud tagCloud = AnyChart.tagCloud();

        tagCloud.setTitle("Programming statisctics");

        OrdinalColor ordinalColor = new OrdinalColor();
        ordinalColor.setColors(new String[] {
                "#26959f", "#f18126", "#3b8ad8", "#60727b", "#e24b26"
        });
        tagCloud.setColorScale(ordinalColor);
        tagCloud.setAngles(new Double[] {-90d, 0d, 90d});

        tagCloud.getColorRange().setEnabled(true);
        tagCloud.getColorRange().setColorLineSize(15d);
       final List<Integer> list = new ArrayList<>();
        list.add(1383220000);
        list.add(1383220000);
        list.add(1316000000);
        list.add(324982000);
        list.add(263510000);
        db.collection("jobs").whereGreaterThan("skills","")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                JobsLists jbL = document.toObject(JobsLists.class);
                                 List<DataEntry> data = new ArrayList<>();
                                data.add(new CategoryValueDataEntry(jbL.getSkills(), jbL.getLocation(), list.get(new Random().nextInt(list.size()))));
                                    tagCloud.setData(data);
                            }
                        } else {
                            System.out.println("Error getting documents: "+task.getException());
                        }
                    }
                });
        anyChartView.setChart(tagCloud);
        pbar.setVisibility(View.GONE);

        return x;
    }
}