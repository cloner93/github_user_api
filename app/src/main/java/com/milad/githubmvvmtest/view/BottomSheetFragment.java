package com.milad.githubmvvmtest.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.milad.githubmvvmtest.R;
import com.milad.githubmvvmtest.model.Repository.StoreUserName;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private Button button;
    private TextInputEditText editText;
    private TextInputLayout inputLayout;
    private StoreUserName storeUserName;
    private String tempName = "";
    private onBottomsheetNameIdListener callback;

    public static BottomSheetFragment newInstance() {
        BottomSheetFragment fragment = new BottomSheetFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setOnBottomsheetNameIdListener(onBottomsheetNameIdListener callback) {
        this.callback = callback;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_bottomsheet, null);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        button = contentView.findViewById(R.id.setNameBtn);
        editText = contentView.findViewById(R.id.edittext_repo_name);
        inputLayout = contentView.findViewById(R.id.layout_repo_name);
        button.setOnClickListener(this);
        storeUserName = new StoreUserName(getActivity().getApplication());
        tempName = storeUserName.getUserName();
        if (!tempName.equals("")) {
            editText.setText(tempName);
            setCancelable(true);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        String name = editText.getText().toString().trim();
        if (name != tempName)
            if (!name.equals("")) {
                inputLayout.setErrorEnabled(false);
                setUserName(name);
                callback.setOnNameId(name);
                dismiss();

            } else {
                inputLayout.setErrorEnabled(true);
                inputLayout.setError("Enter your repository name");
            }
    }

    private void setUserName(String name) {
        storeUserName.setUserName(name);
    }

    interface onBottomsheetNameIdListener {
        void setOnNameId(String nameID);
    }
}
