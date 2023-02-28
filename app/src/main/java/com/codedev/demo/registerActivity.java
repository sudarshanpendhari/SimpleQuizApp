package com.codedev.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText collegeName;
    private EditText passwordacc;
    private EditText mobile;
    private Button save;
    private TextView tvLoginHere;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        collegeName = findViewById(R.id.collegeName);
        mobile = findViewById(R.id.mobile);
        passwordacc = findViewById(R.id.passwordacc);
        save = findViewById(R.id.save);
        auth = FirebaseAuth.getInstance();
        tvLoginHere=findViewById(R.id.tvLoginHere);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getName = name.getText().toString();
                String getEmail = email.getText().toString();
                String getCollegeName = collegeName.getText().toString();
                String getMobile = mobile.getText().toString();
                String getPassword = passwordacc.getText().toString();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name", getName);
                hashMap.put("email", getEmail);
                hashMap.put("collegeName", getCollegeName);
                hashMap.put("mobile", getMobile);
                hashMap.put("password", getPassword);

                if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword) || TextUtils.isEmpty(getCollegeName) || TextUtils.isEmpty(getMobile)) {
                    Toast.makeText(registerActivity.this, "enter email and password both", Toast.LENGTH_SHORT).show();
                } else {
                    regis(getEmail, getPassword);
                }

                FirebaseFirestore.getInstance().collection("user")
                        .add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(registerActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }






            private void regis(String getEmail, String getPassword) {
                auth.createUserWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(registerActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(registerActivity.this, "User Registered Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(registerActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(registerActivity.this, "." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        tvLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registerActivity.this,MainActivity.class));
            }
        });
    }

}