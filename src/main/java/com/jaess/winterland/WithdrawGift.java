package com.jaess.winterland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class WithdrawGift extends AppCompatActivity {
    Button btn;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    private int min, max, random;
    private ArrayList<DataSnapshot> gifts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_gift);

        btn = findViewById(R.id.button);

        gifts = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Gifts");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("Children Count", String.valueOf(snapshot.getChildrenCount()));

                min = 0;
                max = (int) snapshot.getChildrenCount() - 1;

                for(DataSnapshot ds: snapshot.getChildren()){
                    gifts.add(ds);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                random = new Random().nextInt((max - min) + 1) + min;
                Log.e("random number", String.valueOf(random));

                DataSnapshot randomGift = gifts.get(random);
                Gift gift = randomGift.getValue(Gift.class);

                //randomGift.getRef().removeValue();

                Intent intent = new Intent(WithdrawGift.this, RandomGift.class);
                intent.putExtra("Gifts", gift);
                startActivity(intent);
                finish();
            }
        });
    }

}