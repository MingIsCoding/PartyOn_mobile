package edu.sjsu.cmpe.partyon.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.User;

public class RegisterActivity extends AppCompatActivity {

    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Initializing

        mUsername = (EditText)findViewById(R.id.registerUsername);
        mPassword = (EditText)findViewById(R.id.registerPassword);
        mEmail = (EditText)findViewById(R.id.registerEmail);
        mRegisterButton = (Button) findViewById(R.id.registerButton);
        // Listening to the register button click
        //Intent intent = getIntent();
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the username and password from the text field
                //convert them into strings

                String username= mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                //toast

                User user = new User();     //use the User class in the entities
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(RegisterActivity.this, "Successfully registered User", Toast.LENGTH_LONG).show();
                            Intent intent =new  Intent(RegisterActivity.this, LoginActivity.class);
                           startActivity(intent);
                        }else{

                        }
                    }
                });


            }
        });


//        Intent intent = getIntent();
//        EditText editText = (EditText) findViewById(R.id.register_message);
//        EditText editText1 = (EditText) findViewById(R.id.register_message1);
//        EditText editText2 = (EditText) findViewById(R.id.register_message2);
//        EditText editText3 = (EditText) findViewById(R.id.register_message3);


    }
}
