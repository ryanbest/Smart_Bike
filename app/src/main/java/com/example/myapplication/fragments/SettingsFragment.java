package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utils;

public class SettingsFragment extends Fragment {

    TextView tvLogout;
    EditText etNumber, etSpeed;

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
                Utils.saveSpeedLimit(getActivity(), Integer.parseInt(s.toString()));
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


        if (Utils.getSpeedLimit(getActivity()) > 0) {
            etSpeed.setText(Utils.getSpeedLimit(getActivity()));
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
    }

}
