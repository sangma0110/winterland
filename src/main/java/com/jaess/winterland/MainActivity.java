package com.jaess.winterland;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    // Write a message to the database
    private FirebaseDatabase database;
    private DatabaseReference ref;

    private String name;
    private TextView userName;
    private TextView tokenNum;
    private Button draw, regi, list;
    private String token, num;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);
        tokenNum = findViewById(R.id.tokNum);
        database = FirebaseDatabase.getInstance();

        ref = database.getReference().child("UserAccount").child("User1");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.getKey().equals("name")) name = ds.getValue(String.class);
                    if(ds.getKey().equals("tokens")) token = ds.getValue(String.class);
                    if(ds.getKey().equals("tokens")) num = ds.getValue(String.class);
                }
                userName.setText(name);
                tokenNum.setText(token);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        draw = findViewById(R.id.drawBtn);
        regi = findViewById(R.id.regiBtn);
        list = findViewById(R.id.listBtn);

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(num);
                if(n == 0) Toast.makeText(getApplicationContext(), "You don't have a Token. Register your item first.", Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(getApplicationContext(), WithdrawGift.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Uploadimage.class);
                startActivity(intent);
                finish();
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), KwanyeobMain.class);
                startActivity(intent);
                finish();
            }
        });
    }
}