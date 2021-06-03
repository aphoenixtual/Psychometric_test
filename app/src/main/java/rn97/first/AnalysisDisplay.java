package rn97.first;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnalysisDisplay extends AppCompatActivity {

    static int sum=0,count=0,displayScore=0;
    static int sumb=0,countb=0,displayScoreb=0;
    static float avg,avgb;
    //get these values from previous activity
    static String baseScore,baseEmail,baseBranch="min";
    DatabaseReference databaseStudents;
    TextView t1,t2,t3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_display);
        baseScore = getIntent().getStringExtra("SCORE");
        baseEmail = getIntent().getStringExtra("GLOEMAIL");

    }
    @Override
    protected void onStart() {
        super.onStart();

        t1 = (TextView)findViewById(R.id.text_user_score);
        t2 = (TextView)findViewById(R.id.text_branch_avg_score);
        t3 = (TextView)findViewById(R.id.text_avg_score);

        databaseStudents = FirebaseDatabase.getInstance().getReference("students");
        databaseStudents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //studentList.clear();
                sum=0;count=0;
                for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                    Student student = studentSnapshot.getValue(Student.class);
                    //studentList.add(student);
                    int cscore = Integer.parseInt(student.getStudentScore());

                    //if mailid from base class matches then update this student:
                    if(student.getStudentMailId().equals(baseEmail)){
                        updateScore(student.getStudentId(),student.getStudentName(),baseEmail,baseScore,student.getStudentBranch());
                        cscore = Integer.parseInt(baseScore);
                    }

                    if(cscore==0)
                        continue;
                    sum  = sum + cscore;
                    count++;
                    if(student.getStudentBranch().equals(baseBranch)){
                        sumb = sumb + cscore;
                        countb++;
                    }

                }
                //StudentList adapter = new StudentList(MainActivity.this, studentList);
                //listViewStudents.setAdapter(adapter);
                //calculating average

                avg = ((float)(sum))/count;
                avgb = ((float)(sumb))/countb;
                displayScore = calcScore(avg);
                displayScoreb = calcScore(avgb);

                t1.setText(getString(R.string.your_score_s,String.valueOf(calcScore(Integer.parseInt(baseScore)))));
                t2.setText(getString(R.string.branch_aggregate_score_s,String.valueOf(displayScoreb)));
                t3.setText(getString(R.string.college_aggregate_score_s,String.valueOf(calcScore(displayScore))));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateScore(String id,String name,String mail,String score,String branch){

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("students").child(id);
        Student student = new Student(id,name,mail,score,branch);
        dr.setValue(student);
        Toast.makeText(this,"Score Recorded Successfully",Toast.LENGTH_LONG).show();

    }
    public void toApi(View v){
        Intent intent = new Intent(this,ApiAiTask.class);
        intent.putExtra("SCORE",baseScore);
        startActivity(intent);
    }
    public int calcScore(float no){
        return Math.round(100-(no*100/70));
    }
}
