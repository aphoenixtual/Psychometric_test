package rn97.first;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewSignUp = (TextView)findViewById(R.id.textViewSignUp);

        buttonLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        // To check if the Activity is being called by SignUp Activity.
        /*
        Intent intent = this.getIntent();
        if(intent!=null){
            String R_email = intent.getStringExtra("key1");
            String R_password = intent.getStringExtra("key2");
            editTextEmail.setText(R_email);
            editTextPassword.setText(R_password);
            loginUser();
            return;
        }
        */

       if( firebaseAuth.getCurrentUser() != null){
           //He is signed in.
         /*  Intent intent = new Intent(MainActivity.this,Dashboard.class);
           intent.putExtra("key1",firebaseAuth.getCurrentUser().getEmail());
           //  intent.putExtra("key2",password);
           startActivity(intent);
           finish();*/

       }
//Open Profile Activity.

    }
    private void loginUser(){
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email Cannot Be Empty",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password Cannot Be Empty",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Logging In");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            // Start Profile Activity.
                            finish();
                            Toast.makeText(MainActivity.this,"Successful LogIn",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,Dashboard.class);
                            intent.putExtra("key1",email);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,"unSuccessful LogIn",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            //startActivity(new Intent(this,Dashboard.class));
            loginUser();
        }
        if(v == textViewSignUp){
            // Open Sign Up Activity.
            Intent intent = new Intent(this,SignUp.class);
            startActivity(intent);
        }
    }
}
