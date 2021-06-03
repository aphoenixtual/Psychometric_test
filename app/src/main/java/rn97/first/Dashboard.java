package rn97.first;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {
    private int counter=0;
    private int pointer=1;
    private String questions[] = {"How often you sleep well?","Have you been feeling sad?",
            "In the past two weeks, how often have you felt down, depressed or hopeless?","How much of the times do you feel energetic?",
            "Feeling bad about yourself or that you are a failure or have let yourself or your family down?",
            "How often do you feel difficult to relax?","You've ever had a hallucination or a delusion?","how frequently have you felt alone or lonely?",
            "how often did you seriously consider attempting suicide?","Over the last 12 months, did you ever plan how you might attempt suicide?",
            "Do you have nightmares? If so, how often? How does this interrupt your sleep?",
            "Do you hear voices Or sense smells that feel surreal?","Do you ever feel disconnected from yourself?",
            "How often you prefer to stay at home rather than going out and doing new things?"
    };
    int NOQ = questions.length; // NOQ = Number of Questions
    private RadioGroup radioGroup;
    private int SCORE = 0;
    private TextView question,query;


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference qRef = mRootRef.child("query1");

    String GloEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        GloEmail = getIntent().getStringExtra("key1");
        //Toast.makeText(this,"is this the EMAIL = "+newString,Toast.LENGTH_SHORT).show();
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        question = (TextView)findViewById(R.id.question);
        query = (TextView)findViewById(R.id.query);
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        String studentEmail = bundle.getString("passinfo");

        qRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                query.setText(text);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void toapi(View view){
        Intent intent = new Intent(this,AnalysisDisplay.class);
        intent.putExtra("SCORE",Integer.toString(SCORE));
        intent.putExtra("GLOEMAIL",GloEmail);
        //Toast.makeText(this,"SCORE = "+SCORE,Toast.LENGTH_LONG).show();
        startActivity(intent);
        /*
        if(counter==6)
            startActivity(new Intent(this,ApiAiTask.class));
        else{
            Toast.makeText(this,"Please Fill in all the replies",Toast.LENGTH_SHORT).show();
        }
        */
    }
    public void NextButton(View view){
        int id = radioGroup.getCheckedRadioButtonId();
        if(id==-1){
            // None selected.
            Toast.makeText(this,"Select an Option to Continue Further",Toast.LENGTH_SHORT).show();
        }else{
            // Check which RadioButton is checked & update SCORE.
            counter++;
            RadioButton rb = (RadioButton)findViewById(id);
            char ch = (rb.getText()).charAt(0);
            //Toast.makeText(this,"text = "+ch,Toast.LENGTH_SHORT).show();
            if(ch=='R') SCORE += 1;
            else if(ch=='S') SCORE += 2;
            else if(ch=='O') SCORE += 3;
            else if(ch=='M') SCORE += 5;

            if(counter==NOQ){
                // Move to the API
                Intent intent = new Intent(this,AnalysisDisplay.class);
                intent.putExtra("SCORE",Integer.toString(SCORE));
                intent.putExtra("GLOEMAIL",GloEmail);
                //Toast.makeText(this,"SCORE = "+SCORE,Toast.LENGTH_LONG).show();
                startActivity(intent);
            }else{
                //Show new question.
                question.setText(questions[pointer]);
                pointer++;
                if(pointer==NOQ){
                    Button button = (Button)findViewById(R.id.nextButton);
                    button.setText("SUBMIT");
                }
                //rb.setChecked(false);
            }
        }
    }
}
