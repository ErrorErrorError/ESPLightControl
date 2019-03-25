package com.errorerrorerror.esplightcontrol.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.databinding.PresetsFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PresetsFragment extends Fragment {
    private PresetsFragmentBinding presetsFragmentBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presetsFragmentBinding = PresetsFragmentBinding.inflate(inflater, container, false);
        return presetsFragmentBinding.getRoot();
    }
}
