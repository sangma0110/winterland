package com.jaess.winterland;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.*;

public class sendEmail extends AppCompatActivity {
    private EditText nameTxt, itemNameTxt, addressTxt;
    private Button conBtn;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String name, item, address, email, n;
    private int min, max, random;
    private ArrayList<DataSnapshot> gifts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_email);
        database = FirebaseDatabase.getInstance();

        nameTxt = findViewById(R.id.nameText);
        itemNameTxt = findViewById(R.id.itemNameText);
        addressTxt = findViewById(R.id.addressText);
        conBtn = findViewById(R.id.confirmBtn);

        ref = database.getReference().child("UserAccount").child("User1");
        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                                          for(DataSnapshot ds: snapshot.getChildren()){
                                              if(ds.getKey().equals("name")) name = ds.getValue(String.class);
                                              if(ds.getKey().equals("location")) address = ds.getValue(String.class);
                                              if(ds.getKey().equals("email")) email = ds.getValue(String.class);
                                              if(ds.getKey().equals("tokens")) n = ds.getValue(String.class);
                                          }
                                          nameTxt.setText(name);
                                          addressTxt.setText(address);
                                      }

                                      @Override
                                      public void onCancelled(@NonNull DatabaseError error) {
                                          // calling on cancelled method when we receive
                                          // any error or we are not able to get the data.
                                          Toast.makeText(sendEmail.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                                      }
                                  }
        );

        ref = database.getReference().child("Gifts").child("item1");
        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                                          for(DataSnapshot ds: snapshot.getChildren()){
                                              if(ds.getKey().equals("Name")) item = ds.getValue(String.class);
                                          }
                                          itemNameTxt.setText(item);
                                      }

                                      @Override
                                      public void onCancelled(@NonNull DatabaseError error) {
                                          // calling on cancelled method when we receive
                                          // any error or we are not able to get the data.
                                          Toast.makeText(sendEmail.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                                      }
                                  }
        );

        conBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                name = nameTxt.getText().toString();
                address = addressTxt.getText().toString();
                item = itemNameTxt.getText().toString();

                final String id = "winterlandtest@gmail.com";
                final String pw = "testing123!";

                String message = "Confirmation Letter from Secret Santa for " +
                        name + "\n" + "Your Item:" + item + "\n" +
                        "Your Address:" + address + "\n\n Happy Christmas! \n Your Secret Santa.";

                Properties prop = new Properties();
                prop.put("mail.smtp.auth","true");
                prop.put("mail.smtp.starttls.enable","true");
                prop.put("mail.smtp.host","smtp.gmail.com");
                prop.put("mail.smtp.port","587");

                Session session=Session.getInstance(prop,
                        new javax.mail.Authenticator(){
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(id,pw);
                            }
                        });
                try{
                    Message msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(id));
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                    msg.setSubject("Merry Christmas! Secret Santa is Coming!");
                    msg.setText(message);
                    Transport.send(msg);
                    Toast.makeText(getApplicationContext(), "email sent successfully!", Toast.LENGTH_LONG).show();
                    decreaseToken();

                    //remove random item
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

                            random = new Random().nextInt((max - min) + 1) + min;
                            Log.e("random number", String.valueOf(random));

                            DataSnapshot randomGift = gifts.get(random);
                            Gift gift = randomGift.getValue(Gift.class);

                            randomGift.getRef().removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void decreaseToken(){
        int save = Integer.parseInt(n);
        save--;
        n = Integer.toString(save);
        ref = database.getReference().child("UserAccount").child("User1");
        ref.child("tokens").setValue(n);
    }
}