package com.example.problemsolver.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.problemsolver.R;


public class NewProblemFragment extends Fragment {

    private EditText type, description, area, address;
    private Button problemSender;

    public NewProblemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_problem, container, false);

        type = view.findViewById(R.id.problem_input_name);
        description = view.findViewById(R.id.problem_input_description);
        area = view.findViewById(R.id.problem_input_area);
        address = view.findViewById(R.id.problem_input_address);

        problemSender = view.findViewById(R.id.btn_problem);

        return view;
    }
}
