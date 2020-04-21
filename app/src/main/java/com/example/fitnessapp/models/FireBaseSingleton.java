package com.example.fitnessapp.models;

import com.example.fitnessapp.keys.KeysFirebaseStore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireBaseSingleton {

    private FireBaseSingleton(){ }

    private static FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    public static DocumentReference documentReference(String collectionPath){

        String userID = fAuth.getUid();

        if (userID != null) {
            return fStore.collection(collectionPath).document(userID);
        }

        return null;
    }
}
