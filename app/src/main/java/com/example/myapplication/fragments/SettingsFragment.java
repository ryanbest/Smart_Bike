package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsFragment extends Fragment {

    TextView tvLogout;
    EditText etNumber, etSpeed;
    Button btnSaveNumber,btnSaveLimit;
    private static final String TAG = "SettingsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        bindview(view);


        etSpeed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    Utils.saveSpeedLimit(getActivity(), Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Utils.saveContact(getActivity(), s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveUserid(getActivity(),"");
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        btnSaveNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveContact(getActivity(),etNumber.getText().toString());
                FirebaseDatabase database =  FirebaseDatabase.getInstance();
                DatabaseReference databaseReference  = database.getReference().child("test_send").child("users").child(Utils.getUserId(getActivity()));
                Log.e(TAG, "onClick: "+ databaseReference.toString());
                databaseReference.child("emergancy").setValue(etNumber.getText().toString());
                Toast.makeText(getActivity(), "Number Saved!", Toast.LENGTH_SHORT).show();

            }
        });

        btnSaveLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveSpeedLimit(getActivity(),Integer.parseInt(etSpeed.getText().toString()));
                Toast.makeText(getActivity(), "Limit Saved!", Toast.LENGTH_SHORT).show();
            }
        });


        if (Utils.getSpeedLimit(getActivity()) > 0) {
            etSpeed.setText(String.valueOf(Utils.getSpeedLimit(getActivity())));
        }

        if (!Utils.getContact(getActivity()).isEmpty()) {
            etNumber.setText(Utils.getContact(getActivity()));
        }


        return view;
    }

    private void bindview(View view) {
        tvLogout = view.findViewById(R.id.tv_logout);
        etNumber = view.findViewById(R.id.et_number);
        etSpeed = view.findViewById(R.id.et_speed);
        btnSaveLimit = view.findViewById(R.id.btn_save_limit);
        btnSaveNumber = view.findViewById(R.id.btn_save_contact);
    }

}
