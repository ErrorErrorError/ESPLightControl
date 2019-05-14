package com.errorerrorerror.esplightcontrol.base;

import android.app.Dialog;
import android.os.Bundle;

import com.errorerrorerror.esplightcontrol.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class BaseRoundedBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

}
