package com.jaess.winterland;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity
{

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef ;
    private EditText mEtEmail,mEtPwd ;
    private Button mBtnRegister ;

    /*
    private EditText mEtGender,mEtHeight, mEtWeight ;

     */
    private EditText mEtName,mEtLocation ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mBtnRegister = findViewById(R.id.btn_register);

        /*

        mEtGender = findViewById(R.id.et_gender);
        mEtHeight = findViewById(R.id.et_height);
        mEtWeight = findViewById(R.id.et_weight);

         */

        mEtName = findViewById(R.id.et_name);
        mEtLocation = findViewById(R.id.et_location) ;






        mBtnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                /*
                String strGender = mEtGender.getText().toString();
                String strHeight = mEtHeight.getText().toString();
                String strWeight = mEtWeight.getText().toString();
                 */
                String strName = mEtName.getText().toString();
                String strLocation = mEtLocation.getText().toString();


                mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);
                            /*
                            account.setGender(strGender);
                            account.setHeight(strHeight);
                            account.setWeight(strWeight);

                            */
                            account.setName(strName);
                            account.setLocation(strLocation);


                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(RegisterActivity.this,"successfully signed up",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this,"Failed to sign up",Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });
    }
}
