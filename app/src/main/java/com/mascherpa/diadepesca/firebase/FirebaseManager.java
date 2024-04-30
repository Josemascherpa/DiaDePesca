package com.mascherpa.diadepesca.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    public FirebaseManager(){
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }
    public void changeNameDatabase(String name){
        DatabaseReference userRef = database.getReference("users").child(auth.getUid()).child("name");
        userRef.setValue(name);
    }

}
