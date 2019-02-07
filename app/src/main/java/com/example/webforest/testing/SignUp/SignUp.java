package com.example.webforest.testing.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.webforest.testing.Home;
import com.example.webforest.testing.MainActivity;
import com.example.webforest.testing.Model.User;
import com.example.webforest.testing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText inputName,inputEmail,inputPassword;
    private Button btnSignUp,btnSignIn;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        //get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        initialize();

    }

    private void initialize() {

        btnSignUp = (Button)findViewById(R.id.sign_up_button);
        btnSignIn = (Button)findViewById(R.id.sign_in_button);

        inputName = (EditText)findViewById(R.id.newUsername);
        inputEmail = (EditText)findViewById(R.id.newemail);
        inputPassword = (EditText)findViewById(R.id.newpassword);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                final String name = inputName.getText().toString().trim();

                if (TextUtils.isEmpty(mail)){
                    Toast.makeText(getApplicationContext(), "Enter you email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){

                    Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length()<6){
                    Toast.makeText(getApplicationContext(), "Password too short,enter minimum 6 character", Toast.LENGTH_SHORT).show();
                    return;
                }


                //create user
                mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUp.this, "createUserWithEmail:onComplete"+task.isSuccessful(),
                                Toast.LENGTH_SHORT).show();

                       if (task.isSuccessful()){
                           //Authentication and database
                           User user = new User(name,mail,password);
                               FirebaseDatabase.getInstance().getReference("Users")
                               .child(name)
                               .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()){
                                           startActivity(new Intent(SignUp.this,Home.class));
                                           finish();
                                       }else {
                                           Toast.makeText(SignUp.this, "Getting error", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });

                       }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
