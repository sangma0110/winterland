package com.jaess.winterland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.InputStream;

public class RandomGift extends AppCompatActivity {

    private Gift randomGift;
    private EditText mEtGiftFrom, mEtGiftEmail, mEtGiftWhat;
    private ImageView mIvGift;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_gift);

        Intent intent = getIntent();
        randomGift = (Gift) intent.getSerializableExtra("Gifts");

        mEtGiftFrom = findViewById(R.id.gift_get_from);
        mEtGiftEmail = findViewById(R.id.gift_get_email);
        mEtGiftWhat = findViewById(R.id.gift_get_what);
        mIvGift = findViewById(R.id.gift_get);
        button3 = findViewById(R.id.button3);

        mEtGiftFrom.setText(randomGift.Name);
        mEtGiftEmail.setText(randomGift.Email);
        mEtGiftWhat.setText(randomGift.Description);

        new DownloadImageTask(mIvGift).execute(randomGift.ImageURL);


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), sendEmail.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}