package com.blogspot.rajbtc.onlineclass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.blogspot.rajbtc.onlineclass.adapter.ClassUserAdapter;
import com.blogspot.rajbtc.onlineclass.utility.FirebaseMani;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RoutineActivity extends AppCompatActivity {
    private RecyclerView classRecy;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        classRecy=findViewById(R.id.routine_subRecy);
        classRecy.setHasFixedSize(true);
        classRecy.setLayoutManager(new LinearLayoutManager(this));
        initClickFunc();


    }




    void initClickFunc(){

        findViewById(R.id.routine_sunBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map=new HashMap<>();
                map.put("ClassName","333");
                map.put("Teacher","raj");
                map.put("StartTime","fdf");
                map.put("LastUpdate","rajer");
                map.put("Duration","rawe3j");
                map.put("Link","www.google.com");
                map.put("Day","Sunday");
                new FirebaseMani(getApplicationContext()).uploadToFire("Test",map);
            }
        });



        findViewById(R.id.routine_monBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseMani(getApplicationContext()).loadFromFire("Test","Sunday",classRecy);
            }
        });



        findViewById(R.id.routine_tueBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        findViewById(R.id.routine_wedBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        findViewById(R.id.routine_thusSunBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        findViewById(R.id.routine_friBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        findViewById(R.id.routine_satBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

}