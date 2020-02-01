package online.goodr.ngoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText ngo_name , ngo_email , ngo_password , ngo_number ,  ngo_website;
    private Button signup;
    private ProgressBar signup_progress ;



    private FirebaseAuth mAuth;

    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        ngo_name = (EditText) findViewById(R.id.ngo_name);
        ngo_number = (EditText) findViewById(R.id.ngo_number);
        ngo_password = (EditText) findViewById(R.id.ngo_password);
        ngo_email = (EditText) findViewById(R.id.ngo_email);
        ngo_website = (EditText) findViewById(R.id.ngo_website);
        signup_progress = (ProgressBar) findViewById(R.id.sign_up_progress);



        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        signup = (Button) findViewById(R.id.sign_up);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = ngo_email.getText().toString().trim();
                String password = ngo_password.getText().toString().trim();
                final String name = ngo_name.getText().toString().trim();
                final String snumber = ngo_number.toString().trim();
                final String web = ngo_website.toString().trim();



                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    ngo_email.setError("Please Enter a Valid Email");
                    ngo_email.requestFocus();
                    return;
                }

                if (email.isEmpty()){
                    ngo_email.setError("Email is Required");
                    ngo_email.requestFocus();
                    return;
                }

                if (name.isEmpty()){
                    ngo_name.setError("Name is Required");
                    ngo_name.requestFocus();
                    return;
                }

                if (snumber.isEmpty()){
                    ngo_number.setError("Number is Required");
                    ngo_number.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    ngo_password.setError("Password is Required");
                    ngo_password.requestFocus();
                    return;
                }

                if (password.length()<6){
                    ngo_password.setError("Maximum Length of Password Should be 6");
                    ngo_password.requestFocus();
                    return;
                }

                signup_progress.setVisibility(View.VISIBLE);


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        signup_progress.setVisibility(View.GONE);
                        if (task.isSuccessful()){



                            String id = databaseUsers.push().getKey();


                            User user = new User(name , email , snumber , web);


                            databaseUsers.child(id).setValue(user);


                        }
                        else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "Email is Already Registered ", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });


            }
        });


    }
}
