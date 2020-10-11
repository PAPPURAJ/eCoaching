package com.blogspot.rajbtc.onlineclass.utility;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.rajbtc.onlineclass.adapter.ClassUserAdapter;
import com.blogspot.rajbtc.onlineclass.dataclass.ClassData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseMani {
    private Context context;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    public FirebaseMani(Context context){
        this.context=context;
    }

    public void uploadToFire(String collectionPath, Map<String,String> map){
        db.collection(collectionPath).add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context,"Uploading Success!",Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }


    public void loadFromFire(String collectionPath, final String searchDay, final RecyclerView recyclerView){
        db.collection(collectionPath).whereEqualTo("Day",searchDay)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<ClassData> dataArrayList=new ArrayList<>();
                for(QueryDocumentSnapshot qn:queryDocumentSnapshots){
                    if(qn.getString("Day").toLowerCase().equals(searchDay.toLowerCase())){
                        dataArrayList.add(new ClassData(qn.getId(),qn.getString("ClassName"),qn.getString("Day"),qn.getString("Teacher"),qn.getString("StartTime"),qn.getString("LastUpdate"),qn.getString("Duration"),qn.getString("Link")));
                    }
                }
                recyclerView.setAdapter(new ClassUserAdapter(context,dataArrayList));
            }
        });

    }

}
