package com.blogspot.rajbtc.onlineclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.rajbtc.onlineclass.dataclass.MyUserData;
import com.blogspot.rajbtc.onlineclass.utility.UsefulClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();;
    private UsefulClass progress;
    private EditText passForUser;
    private String passForusr;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress=new UsefulClass(this);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
       if(firebaseUser!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }

    public void createAccountClick(View view){
        setContentView(R.layout.activity_signup);
        passForUser=findViewById(R.id.signup_passForUserEt);
        ((Spinner)findViewById(R.id.signup_studentTypeSpn)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    passForUser.setVisibility(View.INVISIBLE);
                }
                else{
                    passForUser.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void signUpClick(View view){



        final String name, school, email, pass, userType;
        name=((EditText)findViewById(R.id.signup_nameEt)).getText().toString();
        school=((EditText)findViewById(R.id.signup_schoolEt)).getText().toString();
        email=((EditText)findViewById(R.id.signup_emailEt)).getText().toString();
        pass=((EditText)findViewById(R.id.signup_passEt)).getText().toString();
        userType=((Spinner)findViewById(R.id.signup_studentTypeSpn)).getSelectedItem().toString();

        passForusr=passForUser.getText().toString();
        if(userType.equals("Admin") && passForusr.equals("")){
            Toast.makeText(getApplicationContext(),"Please input first!",Toast.LENGTH_LONG).show();
            return;
        }
        if(name.equals("") || school.equals("") || email.equals("") || pass.equals("") || !(userType.equals("User") || userType.equals("Admin"))){
            Toast.makeText(getApplicationContext(),"Please input first",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.length()<8){
            Toast.makeText(getApplicationContext(),"Minimum password length is 8",Toast.LENGTH_SHORT).show();
            return;
        }

        progress.start();

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"User registered: "+user.getEmail(),Toast.LENGTH_SHORT).show();

                            passForusr=passForUser.getText().toString();




                           databaseReference.child("Users").child(user.getUid()).setValue(new MyUserData(name,email,school,userType,passForusr));




                          alreadyAccountClick(null);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progress.stop();
                    }
                });


    }

    public void alreadyAccountClick(View view){
        setContentView(R.layout.activity_login);
    }

    public void loginClick(View view){
        final String email, pass;
        email=((EditText)findViewById(R.id.loginEmailEt)).getText().toString();
        pass=((EditText)findViewById(R.id.loginPassEt)).getText().toString();

        if(email.equals("") || pass.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Login failed!",Toast.LENGTH_SHORT).show();
            return;
        }


        progress.start();

        mAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_SHORT).show();

                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        MyUserData myUserData=snapshot.getValue(MyUserData.class);
                        getSharedPreferences("userData",MODE_PRIVATE).edit().putString("userType",myUserData.getUserType()).putString("passForUser",myUserData.getPassForUser()).apply();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                progress.stop();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed!",Toast.LENGTH_SHORT).show();
                progress.stop();
            }
        });
    }


}