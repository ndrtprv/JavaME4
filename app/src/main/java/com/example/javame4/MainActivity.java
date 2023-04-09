package com.example.javame4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.*;
import androidx.recyclerview.widget.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    UserDBHelper userDBHelper;
    Button add_button, clean_table;
    ArrayList<UserInfo> users;
    CustomAdapter customAdapter;
    RecyclerView recyclerView;
    Spinner sorting;
    String sort_option;
    FrameLayout frameLayout;
    ImageView empty_image;
    TextView empty_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDBHelper = new UserDBHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        frameLayout = findViewById(R.id.frame);
        empty_image = findViewById(R.id.no_data);
        empty_text = findViewById(R.id.no_data_txt);

        sorting = findViewById(R.id.spinner);
        add_button = findViewById(R.id.button3);
        add_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddUser.class);
            startActivity(intent);
        });

        users = userDBHelper.getAllUsers("ID");
        customAdapter = new CustomAdapter(MainActivity.this, this, users);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        sorting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort_option = switch (position) {
                    case 0 -> "Name";
                    case 1 -> "Surname";
                    case 2 -> "Email";
                    default -> "ID";
                };
                users = userDBHelper.getAllUsers(sort_option);
                customAdapter = new CustomAdapter(MainActivity.this,
                        MainActivity.this, users);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        recyclerView.addItemDecoration(new DividerItemDecoration(
                    recyclerView.getContext(),
                    ((LinearLayoutManager) Objects.requireNonNull(
                            recyclerView.getLayoutManager())).getOrientation()
                )
        );

        clean_table = findViewById(R.id.button4);
        clean_table.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to clean table?")
                    .setCancelable(false)
                    .setPositiveButton("Accept", (dialog, id) -> {
                        userDBHelper.cleanTable();
                        users = userDBHelper.getAllUsers(sort_option);
                        customAdapter = new CustomAdapter(
                                MainActivity.this,this, users);
                        recyclerView.setAdapter(customAdapter);
                        recyclerView.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this));

                        empty_image.setVisibility(View.VISIBLE);
                        empty_text.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this,
                                "Cleaning table successful.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Decline", (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        });

        if (users.size() == 0) {
            empty_image.setVisibility(View.VISIBLE);
            empty_text.setVisibility(View.VISIBLE);
        } else {
            empty_image.setVisibility(View.GONE);
            empty_text.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }
}