package com.example.introfirestore.CRUD;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseCRUD {
    String TAG = "firebaseLogs";
    InternalStorageCRUD internalStorageCRUD = new InternalStorageCRUD();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("FoodList")
            .document("List");

    public void updateFire(String string) {
        Map<String, Object> data = new HashMap<>();
        data.put(string.trim(),string);
        docRef.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println(string+"added");
                Log.d(TAG, "update Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "update Failed");
            }
        });
    }
    public void deleteFire(String string){
        Map<String,Object> updates = new HashMap<>();
        updates.put(string.toLowerCase().trim(), FieldValue.delete());

        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println(string+"delete");
                Log.d(TAG, "delete Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failed");
                Log.d(TAG, "delete Failed");
            }
        });
    }

    public int readFire(Context context){
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //Set<String> map = documentSnapshot.getData().keySet();
                for (String s : documentSnapshot.getData().keySet()
                ) {
                    internalStorageCRUD.updateFile(context, "tempfoodlist.txt", s.toLowerCase());
                }

            }
        });

        return 1;
    }


}


