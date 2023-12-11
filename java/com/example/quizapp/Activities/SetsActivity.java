package com.example.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;

import com.example.quizapp.Adapters.SetAdapter;
import com.example.quizapp.Models.SetModel;
import com.example.quizapp.R;
import com.example.quizapp.databinding.ActivitySetsBinding;

import java.util.ArrayList;

public class SetsActivity extends AppCompatActivity {

    ActivitySetsBinding binding;

    ArrayList<SetModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.setsRecy.setLayoutManager(linearLayoutManager);

        list.add(new SetModel("Aptitude_Round"));
        list.add(new SetModel("Coding_Round"));
        list.add(new SetModel("HR_Round"));

        SetAdapter adapter = new SetAdapter(this, list);
        binding.setsRecy.setAdapter(adapter);

    }
}