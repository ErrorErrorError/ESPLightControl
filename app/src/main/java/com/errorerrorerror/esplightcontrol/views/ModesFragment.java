package com.errorerrorerror.esplightcontrol.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.databinding.ModesFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ModesFragment extends Fragment {
    private ModesFragmentBinding modesFragmentBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        modesFragmentBinding = ModesFragmentBinding.inflate(inflater, container, false);

        return modesFragmentBinding.getRoot();
    }
}
