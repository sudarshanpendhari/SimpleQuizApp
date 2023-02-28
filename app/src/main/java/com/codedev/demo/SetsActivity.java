package com.codedev.demo;

import static com.codedev.demo.SplashActivity.catList;
import static com.codedev.demo.SplashActivity.selected_cat_index;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class SetsActivity extends AppCompatActivity {

    private GridView sets_grid;
    private FirebaseFirestore firestore;
    public static List<String> setsIDs=new ArrayList<>();
    private Dialog loadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        Toolbar toolbar=findViewById(R.id.set_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(catList.get(selected_cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firestore=FirebaseFirestore.getInstance();
        loadsets();
        sets_grid=findViewById(R.id.sets_gridview);

        loadingDialog =new Dialog(SetsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();


    }

    public void loadsets(){

        setsIDs.clear();
        firestore.collection("QUIZ").document(catList.get(selected_cat_index).getId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        long noOfSets=(long) documentSnapshot.get("SETS");

                        for (int i=1;i<=noOfSets;i++)
                        {
                            setsIDs.add(documentSnapshot.getString("SET"+String.valueOf(i)+"_ID"));
                        }




                        SetsAdapter adapter = new SetsAdapter(setsIDs.size());
                        sets_grid.setAdapter(adapter);

                        loadingDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();

                    }
                });




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            SetsActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}