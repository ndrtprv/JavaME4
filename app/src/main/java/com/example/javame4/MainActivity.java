package com.example.javame4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    UserDBHelper userDBHelper;
    Button add_button;
    ArrayList<UserInfo> users;
    CustomAdapter customAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDBHelper = new UserDBHelper(this);
        recyclerView = findViewById(R.id.recyclerView);

        Spinner sorting = findViewById(R.id.spinner);
        add_button = findViewById(R.id.button3);
        add_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddUser.class);
            startActivity(intent);
        });

        String sort_option = String.valueOf(sorting.getSelectedItem());
        users = userDBHelper.getAllUsers(sort_option);
        customAdapter = new CustomAdapter(MainActivity.this, users);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }
}