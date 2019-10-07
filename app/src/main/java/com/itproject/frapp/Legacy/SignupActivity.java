package com.itproject.frapp.Legacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.itproject.frapp.MainGallery.HomeActivity;
import com.itproject.frapp.R;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView nameInput;
    private TextView emailInput;
    private TextView passwordInput;
    private TextView confirmPasswordInput;

    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        signupButton = findViewById(R.id.finalSignupButton);

        Log.i("hi", "onCreate: hi");

        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                newUser();
            }
        });
    }

    public void newUser() {
        String email = emailInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && password.equals(confirmPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in successful
                                Log.i("success", "onComplete: hi");
                                FirebaseUser user = mAuth.getCurrentUser();
                                openHome();
                                finish();
                            }
                            else {
                                Log.i("fail", "onComplete: bye");
                                Log.e("fail", "onComplete: :(", task.getException());
                            }
                        }
                    });
        }
        else {
            Log.i("sad reakts", email + name + password + confirmPassword);
        }
    }

    public void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
