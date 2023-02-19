package com.example.demoapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class editActivity extends AppCompatActivity {
    ImageView imageView ,back_img,Heart,Circle,Square,Rectangle;
    Button btn_rotate,btn_flip,btn_crop,btn_edit;
    final int PIC_CROP = 1;

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_img);


        Heart = findViewById(R.id.img_heart);
        Heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap mergedImage2 = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(),imageView.getDrawingCache().getConfig());
                Bitmap mergedImage = Bitmap.createBitmap(Heart.getWidth(), Heart.getHeight(),Heart.getDrawingCache().getConfig());
                Canvas canvas = new Canvas(mergedImage);
                canvas.drawBitmap(mergedImage2, 0, 0, null);
                Paint paint = new Paint();
                paint.setAlpha(128); // Set alpha to 50%
                canvas.drawBitmap(mergedImage, 0, 0, paint);

                imageView.setImageBitmap(mergedImage);
            }
        });

        back_img = findViewById(R.id.back_edit);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(editActivity.this,MainActivity.class);
                startActivity(backIntent);
            }
        });

        btn_edit = findViewById(R.id.bt_final);
        imageView = findViewById(R.id.ed_IMG);

        String selectedImageUri = getIntent().getStringExtra("selectedImage");
        Uri selectedImage = Uri.parse(selectedImageUri);

        imageView.setImageURI(selectedImage);
        btn_rotate = findViewById(R.id.bt_rotate);
        btn_rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setRotation(imageView.getRotation() + 90);
            }
        });

        btn_flip = findViewById(R.id.bt_flip);
        btn_flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setScaleX(imageView.getScaleX() * -1);
            }
        });

        btn_crop = findViewById(R.id.bt_crop);
        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performCrop(selectedImage);
            }
        });
    }
    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");

                imageView.setImageBitmap(selectedBitmap);

                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(editActivity.this, MainActivity.class);
                        intent.putExtra("selectedBitIMG", selectedBitmap.toString());
                        startActivity(intent);

                    }
                });
            }
        }


    }

}
