package com.codedev.demo;

import static com.codedev.demo.SetsActivity.setsIDs;
import static com.codedev.demo.SplashActivity.catList;
import static com.codedev.demo.SplashActivity.selected_cat_index;
import static com.codedev.demo.infoActivity.setNo;

import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView question,qCount,timer;
    private Button option1,option2,option3,option4;
    private List<Questions> questionsList;
    private int quesNum;
    private CountDownTimer countDown;
    private int score;
    private int incorrect;
    private int unans;
    private FirebaseFirestore firestore;

    private Dialog loadingDialog;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        question=findViewById(R.id.question);
        qCount=findViewById(R.id.quest_no);
        timer=findViewById(R.id.countdown);
        option1=findViewById(R.id.option1);
        option2=findViewById(R.id.option2);
        option3=findViewById(R.id.option3);
        option4=findViewById(R.id.option4);

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        loadingDialog=new Dialog(QuestionActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        questionsList=new ArrayList<>();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                super.onAdClicked();


            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened();
            }
        });


        firestore=FirebaseFirestore.getInstance();
        getQuestionslist();

        score=0;

    }
    private void getQuestionslist(){
        questionsList.clear();

        firestore.collection("QUIZ").document(catList.get(selected_cat_index).getId())
                .collection(setsIDs.get(setNo)).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList=new ArrayMap<>();
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots)
                        {
                            docList.put(doc.getId(),doc);
                        }
                        QueryDocumentSnapshot quesListDoc=docList.get("QUESTIONS_LIST");
                        String count=quesListDoc.getString("COUNT");
                        for (int i=0;i<Integer.valueOf(count);i++)
                        {
                            String quesID=quesListDoc.getString("Q"+String.valueOf(i+1)+"_ID");

                            QueryDocumentSnapshot quesDoc=docList.get(quesID);
                            questionsList.add(new Questions(

                                    quesDoc.getString("QUESTION"),
                                    quesDoc.getString("A"),
                                    quesDoc.getString("B"),
                                    quesDoc.getString("C"),
                                    quesDoc.getString("D"),
                                    Integer.valueOf(quesDoc.getString("ANSWER"))
                            ));
                        }
                        setQuestion();
                        loadingDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuestionActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });



    }
    private void setQuestion(){
        timer.setText(String.valueOf(10));
        question.setText(questionsList.get(0).getQuestion());
        option1.setText(questionsList.get(0).getOptionA());
        option2.setText(questionsList.get(0).getOptionB());
        option3.setText(questionsList.get(0).getOptionC());
        option4.setText(questionsList.get(0).getOptionD());
        qCount.setText(String.valueOf(1)+"/"+String.valueOf(questionsList.size()));
        starttimer();
        quesNum=0;
    }
    private void starttimer(){
        countDown =new CountDownTimer(12000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished<10000)
                    timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                changeQuestion();

            }
        };
        countDown.start();
    }

    @Override
    public void onClick(View view) {
        int selectedOption =0;
        switch (view.getId()){
            case R.id.option1:
                selectedOption=1;
                break;
            case R.id.option2:
                selectedOption=2;
                break;
            case R.id.option3:
                selectedOption=3;
                break;
            case R.id.option4:
                selectedOption=4;
                break;
            default:
                break;
        }
        countDown.cancel();
        checkAnswer(selectedOption,view);
    }
    private void checkAnswer(int selectedOption,View view){
        if(selectedOption==questionsList.get(quesNum).getCorrectAns())
        {
            //right answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }
        else
        {
            //wrong answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            switch (questionsList.get(quesNum).getCorrectAns()){
                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
            incorrect++;
        }
        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        },500);

    }
    private void changeQuestion(){
        if (quesNum<questionsList.size()-1)
        {
            quesNum++;
            playAnim(question,0,0);
            playAnim(option1,0,1);
            playAnim(option2,0,2);
            playAnim(option3,0,3);
            playAnim(option4,0,4);

            qCount.setText(String.valueOf(quesNum + 1)  +  "/"  +  String.valueOf(questionsList.size()));
            timer.setText(String.valueOf(10));
            starttimer();


        }else{
            //go to score activity
            Intent intent=new Intent(QuestionActivity.this,ScoreActivity.class);
            intent.putExtra("SCORE",String.valueOf(score)+"/"+String.valueOf(questionsList.size()));
            intent.putExtra("score",String.valueOf(score));
            intent.putExtra("incorrect",String.valueOf(incorrect));
            unans=questionsList.size()-(score+incorrect);
            intent.putExtra("unanswered",String.valueOf(unans));

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // QuestionActivity.this.finish();
        }
    }
    private void playAnim(View view,final int value, int viewNum){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (value==0)
                        {
                            switch (viewNum){
                                case 0:
                                    ((TextView)view).setText(questionsList.get(quesNum).getQuestion());
                                    break;
                                case 1:
                                    ((Button)view).setText(questionsList.get(quesNum).getOptionA());
                                    break;
                                case 2:
                                    ((Button)view).setText(questionsList.get(quesNum).getOptionB());
                                    break;
                                case 3:
                                    ((Button)view).setText(questionsList.get(quesNum).getOptionC());
                                    break;
                                case 4:
                                    ((Button)view).setText(questionsList.get(quesNum).getOptionD());
                                    break;
                            }

                            if (viewNum!=0)
                                ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF3700B3")));
                            playAnim(view,1,viewNum);
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        QuestionActivity.super.onBackPressed();
                        countDown.cancel();
                    }
                }).create().show();

    }
}