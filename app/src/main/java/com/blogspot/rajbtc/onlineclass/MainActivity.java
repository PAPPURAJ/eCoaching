package com.blogspot.rajbtc.onlineclass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    private String adminID="",adminPass="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setupToolbar();
        sp=getSharedPreferences("adminInfo",MODE_PRIVATE);
        adminID=sp.getString("classID","");
        adminPass=sp.getString("classPass","");
    }

    public void routineClick(View view) {
        if(adminID.equals("") || adminPass.equals("")){
            Toast.makeText(getApplicationContext(),"Please connect a class",Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this,RoutineActivity.class));
    }


    public void slideClick(View view) {
        if(adminID.equals("") || adminPass.equals("")){
            Toast.makeText(getApplicationContext(),"Please connect a class",Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, SlideActivity.class));
    }



    void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent in = new Intent(MainActivity.this, LoginActivity.class);

                if(item.getItemId()==R.id.menu_connectClass){
                    LinearLayout main=new LinearLayout(getApplicationContext());
                    main.setOrientation(LinearLayout.VERTICAL);
                    final EditText ID=new EditText(getApplicationContext());
                    ID.setHint("Admin email");
                    final EditText pass=new EditText(getApplicationContext());
                    pass.setHint("Class pass");
                    main.addView(ID);
                    main.addView(pass);

                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Connect class").setView(main).
                            setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String classID=ID.getText().toString();
                                    String classPass=pass.getText().toString();

                                    if(classID.equals("") || classPass.equals("")){
                                        Toast.makeText(getApplicationContext(),"Please input first",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    adminID=classID;
                                    adminPass=classPass;
                                    sp.edit().putString("classID",classID).putString("classPass",classPass).apply();

                                }
                            }).setNegativeButton("Cancel",null).show();
                }
                else if(item.getItemId()==R.id.menu_logout){
                    FirebaseAuth.getInstance().signOut();
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                }

                return  true;
            }
        });

    }



}