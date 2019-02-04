package com.reallyinvincible.veto.bottomfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.reallyinvincible.veto.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomSheetKeyFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_key_fragment, container, false);
        return view;
    }
}
