package com.mascherpa.diadepesca.firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mascherpa.diadepesca.R;
import com.mascherpa.diadepesca.databinding.MainBinding;
import com.mascherpa.diadepesca.load.Loading;

public class FirebaseManager {
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    Context context;
    String client_id;

    public String nameUser;
    MainBinding managerUI;
    public FirebaseManager(Context getContexts, String getClient_id, MainBinding binding){
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        context = getContexts;
        client_id = getClient_id;
        managerUI = binding;
        GetNameUser();
        getImageUser();

    }

    private void getImageUser() {
        DatabaseReference imageRef = database.getReference("users").child(auth.getUid()).child("profile");
        imageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Obtener el valor de la clave "name"
                String linkimage = dataSnapshot.getValue(String.class);
                Glide.with(context)
                        .load(linkimage)
                        .placeholder(R.drawable.refreshwidget)
                        .error(R.drawable.app_widget_background)
                        .transform(new CircleCrop())
                        .into(managerUI.imageUser);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura de datos, si es necesario
                Log.e("Error", "Error al leer el valor de la clave 'name': " + databaseError.getMessage());
            }
        });

    }

    public void GetNameUser(){
        DatabaseReference userRef = database.getReference("users").child(auth.getUid()).child("name");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Obtener el valor de la clave "name"
                nameUser = dataSnapshot.getValue(String.class);
                managerUI.tvNombreUser.setText("Hello "+nameUser+"!!");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura de datos, si es necesario
                Log.e("Error", "Error al leer el valor de la clave 'name': " + databaseError.getMessage());
            }
        });
    }
    public void changeNameDatabase(String name){
        DatabaseReference userRef = database.getReference("users").child(auth.getUid()).child("name");
        userRef.setValue(name);
        showMessage("nombre cambiado con exito!");
    }

    public void deleteAccount(String userID) {
        if (user != null) {
            // Intenta eliminar la cuenta
            user.delete()
                    .addOnCompleteListener(taskDelete -> {
                        if (taskDelete.isSuccessful()) {
                            DatabaseReference userRef = database.getReference("users").child(userID);
                            userRef.removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {//la cuenta se elimino de la bd
                                    //redirige a una nueva Activity
                                    Intent intent = new Intent(context, Loading.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                } else {//la cuenta no se pudo elimino de la bd
                                    showMessage("Error al eliminar el usuario de la base de datos: " + task.getException().getMessage());
                                }
                            });
                            FirebaseAuth.getInstance().signOut();
                            //Limpiola autenticacion de persistencia de datos de auth para que se pueda volver a elegir cuenta
                            ClearCacheGoogle();
                        } else {//error al eliminar la cuenta
                            showMessage("Error al eliminar la cuenta: " + taskDelete.getException().getMessage());
                        }
                    });
        } else {
            showMessage("El usuario no está autenticado");
        }
    }

    public void ClearCacheGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(client_id)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        mGoogleSignInClient.signOut();
    }

    private void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
