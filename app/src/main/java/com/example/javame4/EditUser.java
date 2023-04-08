package com.example.javame4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditUser extends AppCompatActivity {

    EditText update_name, update_surname, update_phone, update_email, update_address;
    Button edit, delete;
    ImageView imageView;
    private UserDBHelper db;
    private Uri selectedImage = null;
    String name, surname, phone, email, address;
    int id;
    byte[] photo;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        update_name = findViewById(R.id.editName);
        update_surname = findViewById(R.id.editSurname);
        update_phone = findViewById(R.id.editPhone);
        update_email = findViewById(R.id.editEmail);
        update_address = findViewById(R.id.editAddress);
        imageView = findViewById(R.id.editImageViewPhoto);

        getAndSetIntentData();

        imageView.setOnClickListener(v -> openGallery());

        edit = findViewById(R.id.edit_button);
        edit.setOnClickListener(v -> {
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
                photo = byteArrayInputStream.toByteArray();
            } catch (IOException e) {
                Toast.makeText(this, "Something went wrong while getting image", Toast.LENGTH_SHORT).show();
                return;
            }
            db = new UserDBHelper(EditUser.this);
            db.updateUser(id, update_name.getText().toString(),
                    update_surname.getText().toString(),
                    update_phone.getText().toString(),
                    update_email.getText().toString(),
                    update_address.getText().toString(), photo);
        });
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id")
                && getIntent().hasExtra("name")
                && getIntent().hasExtra("surname")
                && getIntent().hasExtra("phone")
                && getIntent().hasExtra("email")
                && getIntent().hasExtra("address")
                && getIntent().hasExtra("photo")) {
            id = getIntent().getIntExtra("id",1);
            name = getIntent().getStringExtra("name");
            surname = getIntent().getStringExtra("surname");
            phone = getIntent().getStringExtra("phone");
            email = getIntent().getStringExtra("email");
            address = getIntent().getStringExtra("address");
            photo = getIntent().getByteArrayExtra("photo");

            Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);

            update_name.setText(name);
            update_surname.setText(surname);
            update_phone.setText(phone);
            update_email.setText(email);
            update_address.setText(address);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 110, 180, false));
        } else {
            Toast.makeText(this,"Data is absent.",Toast.LENGTH_SHORT).show();
        }
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