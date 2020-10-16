package com.blogspot.rajbtc.onlineclass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.blogspot.rajbtc.onlineclass.adapter.ClassUserAdapter;
import com.blogspot.rajbtc.onlineclass.dataclass.ClassData;
import com.blogspot.rajbtc.onlineclass.dataclass.ClassUpData;
import com.blogspot.rajbtc.onlineclass.dataclass.RealtimeDataClass;
import com.blogspot.rajbtc.onlineclass.dataclass.MyUserData;
import com.blogspot.rajbtc.onlineclass.utility.FirebaseMani;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RoutineActivity extends AppCompatActivity {
    private RecyclerView classRecy;
    private FloatingActionButton fab;
    private DatabaseReference fireRef=FirebaseDatabase.getInstance().getReference("Data");

    private String adminID,adminPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        classRecy=findViewById(R.id.routine_subRecy);
        classRecy.setHasFixedSize(true);
        classRecy.setLayoutManager(new LinearLayoutManager(this));
        fab=findViewById(R.id.routine_plusFlb);
        initClickFunc();
        checkAdminOrUser();





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent in = new Intent(RoutineActivity.this, LoginActivity.class);

                if(item.getItemId()==R.id.menu_connectClass){
                    Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();
                }
                else if(item.getItemId()==R.id.menu_logout){
                    FirebaseAuth.getInstance().signOut();
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                }

                return  true;
            }
        });





        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String day=new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        Log.e("====Log",day);
        loadFireData(day);


    }


    void checkAdminOrUser(){
        String userType=getSharedPreferences("userData",MODE_PRIVATE).getString("userType","null");
        if(userType.equals("null")){
            Toast.makeText(getApplicationContext(),"Please login again!",Toast.LENGTH_LONG).show();
            finish();
        }
        else if(userType.toLowerCase().equals("user")){
            adminID=getSharedPreferences("adminInfo",MODE_PRIVATE).getString("classID","null");
            adminPass=getSharedPreferences("adminInfo",MODE_PRIVATE).getString("classPass","null");
            if(adminID.equals("null") || adminPass.equals("")){
                Toast.makeText(getApplicationContext(),"Please login again!",Toast.LENGTH_LONG).show();
                finish();
            }
            fab.hide();
        }

        else {
            adminID=FirebaseAuth.getInstance().getCurrentUser().getEmail();
            adminPass=getSharedPreferences("userData",MODE_PRIVATE).getString("passForUser","null");
            if(adminPass.equals("null")){
                Toast.makeText(getApplicationContext(),"Please login again!",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        }



    }



    void initClickFunc(){


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view= LayoutInflater.from(RoutineActivity.this).inflate(R.layout.dialog_class_input,null);
                 AlertDialog.Builder builder=new AlertDialog.Builder(RoutineActivity.this);
                builder.setView(view).setTitle("Input")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    String className=((TextView)view.findViewById(R.id.dialog_ClassInput_clsNameEt)).getText().toString();
                                    String day=((Spinner)view.findViewById(R.id.dialog_ClassInput_daySpn)).getSelectedItem().toString();
                                    String duration=((TextView)view.findViewById(R.id.dialog_ClassInput_durationEt)).getText().toString();
                                    String startTime=((TextView)view.findViewById(R.id.dialog_ClassInput_startTimeEt)).getText().toString();
                                    String link=((TextView)view.findViewById(R.id.dialog_ClassInput_linkEt)).getText().toString();
                                    String teacher=((TextView)view.findViewById(R.id.dialog_ClassInput_teacherNameEt)).getText().toString();

                                    if(className.equals("") || day.equals("") || duration.equals("") || startTime.equals("") || link.equals("") || teacher.equals("")){
                                        Toast.makeText(getApplicationContext(),"Please fulfil all input",Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    ClassUpData classUpData=new ClassUpData(className,teacher,startTime,duration,link);

                                    fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("routine").child(day).push().setValue(classUpData);




                            }
                        }).setNegativeButton("Cancel",null);

                AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }
        });




        findViewById(R.id.routine_sunBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //loadFireData("Sunday");
            }
        });



        findViewById(R.id.routine_monBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFireData("Monday");
            }
        });



        findViewById(R.id.routine_tueBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFireData("Tuesday");
            }
        });



        findViewById(R.id.routine_wedBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFireData("Wednesday");
            }
        });



        findViewById(R.id.routine_thusSunBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFireData("Thursday");
            }
        });



        findViewById(R.id.routine_friBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFireData("Friday");
            }
        });


        findViewById(R.id.routine_satBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFireData("Saturday");

            }
        });


    }



    void loadFireData(final String day){
        final ArrayList<ClassData> arrayList=new ArrayList<>();
        classRecy.setAdapter(null);
        fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("routine").child(day).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ClassUpData val=snapshot.getValue(ClassUpData.class);
                Log.e("=========",val.getClassName()+" ");
                ClassData classData=new ClassData(snapshot.getRef(),val.getClassName(),day,val.getTeacher(),val.getStartTime(),"333","-"+val.getDuration()+"hour",val.getClassLink());
                arrayList.add(classData);
                classRecy.setAdapter(new ClassUserAdapter(RoutineActivity.this,arrayList));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}