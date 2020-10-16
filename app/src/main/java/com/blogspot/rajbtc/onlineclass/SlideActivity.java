package com.blogspot.rajbtc.onlineclass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.rajbtc.onlineclass.adapter.ClassUserAdapter;
import com.blogspot.rajbtc.onlineclass.adapter.SlideAdapter;
import com.blogspot.rajbtc.onlineclass.dataclass.ClassData;
import com.blogspot.rajbtc.onlineclass.dataclass.ClassUpData;
import com.blogspot.rajbtc.onlineclass.dataclass.SlideDataforAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SlideActivity extends AppCompatActivity {

    private String adminID,adminPass;
    private FloatingActionButton fab;
    private RecyclerView slideRecy;
    private DatabaseReference fireRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        fireRef=FirebaseDatabase.getInstance().getReference("Data");

        initView();
        checkAdminOrUser();
        loadFireData("");
    }


    void initView(){
        fab=findViewById(R.id.slide_Fab);
        slideRecy=findViewById(R.id.slide_Recy);
        slideRecy.setHasFixedSize(true);
        slideRecy.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upToDb();
            }
        });

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
            adminID= FirebaseAuth.getInstance().getCurrentUser().getEmail();
            adminPass=getSharedPreferences("userData",MODE_PRIVATE).getString("passForUser","null");
            if(adminPass.equals("null")){
                Toast.makeText(getApplicationContext(),"Please login again!",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        }



    }



    void loadFireData(final String day){
        final ArrayList<SlideDataforAdapter> arrayList=new ArrayList<>();
        slideRecy.setAdapter(null);
        fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("Slide").child("EEE").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                SlideDataforAdapter val=snapshot.getValue(SlideDataforAdapter.class);
                val.setReference(snapshot.getRef());
                arrayList.add(val);
                slideRecy.setAdapter(new SlideAdapter(SlideActivity.this,arrayList));
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



    void upToDb(){

        final LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText slideNameEt=new EditText(this);
        slideNameEt.setHint("Slide name");
        final EditText giverEt=new EditText(this);
        giverEt.setHint("Provided by");
        final EditText linkEt=new EditText(this);
        linkEt.setHint("URL");
        linkEt.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        linearLayout.addView(slideNameEt);
        linearLayout.addView(giverEt);
        linearLayout.addView(linkEt);


                AlertDialog.Builder builder=new AlertDialog.Builder(SlideActivity.this);
                builder.setView(linearLayout).setTitle("Input")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                String slideName=slideNameEt.getText().toString().replace(" ","");
                                String givenBy=giverEt.getText().toString().replace(" ","");
                                String link=linkEt.getText().toString().replace(" ","");

                                if(slideName.equals("") || givenBy.equals("") || link.equals("")){
                                    Toast.makeText(getApplicationContext(),"Please fulfil all input",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                SlideDataforAdapter data=new SlideDataforAdapter(null,slideName,givenBy,link);

                                fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("Slide").child("EEE").push().setValue(data);




                            }
                        }).setNegativeButton("Cancel",null);

                AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }


    }

