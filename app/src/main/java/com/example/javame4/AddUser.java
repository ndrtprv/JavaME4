package com.example.javame4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;

import java.io.*;

public class AddUser extends AppCompatActivity {

    EditText input_name, input_surname, input_phone, input_email, input_address;
    Button save;
    ImageView imageView;
    private Uri selectedImage = null;
    private UserDBHelper db;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        input_name = findViewById(R.id.enterName);
        input_surname = findViewById(R.id.enterSurname);
        input_phone = findViewById(R.id.enterPhone);
        input_email = findViewById(R.id.enterEmail);
        input_address = findViewById(R.id.enterAddress);
        imageView = findViewById(R.id.addImageViewPhoto);
        save = findViewById(R.id.button6);

        imageView.setOnClickListener(v -> openGallery());
        save.setOnClickListener(v -> {
            byte[] b;
            try (InputStream inputStream = getContentResolver().openInputStream(selectedImage)) {
                Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);

                int width = originalBitmap.getWidth();
                int height = originalBitmap.getHeight();
                int maxWidth = 800;
                int maxHeight = 600;
                if (width > maxWidth || height > maxHeight) {
                    float aspectRatio = (float) width / height;
                    if (width > height) {
                        width = maxWidth;
                        height = Math.round(width / aspectRatio);
                    } else {
                        height = maxHeight;
                        width = Math.round(height * aspectRatio);
                    }
                }

                Bitmap compressedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
                ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
                compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayInputStream);
                b = byteArrayInputStream.toByteArray();
            } catch (IOException e) {
                Toast.makeText(this, "Something went wrong while getting image", Toast.LENGTH_SHORT).show();
                return;
            }
            db = new UserDBHelper(AddUser.this);
            db.insertData(input_name.getText().toString(),
                    input_surname.getText().toString(),
                    input_phone.getText().toString(),
                    input_email.getText().toString(),
                    input_address.getText().toString(),b);
        });
    }

    void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            selectedImage = data.getData();
            imageView.setImageURI(selectedImage);
        }
    }
}