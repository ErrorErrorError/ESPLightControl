package com.errorerrorerror.iosstyleslider;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

@InverseBindingMethods({
        @InverseBindingMethod(
                type = IOSStyleSlider.class,
                attribute = "issSetProgressBar",
                method = "getSliderProgress"),

        @InverseBindingMethod(
                type = IOSStyleSlider.class,
                attribute = "issText",
                method = "getText")
})

public class IOSStyleSliderBindingAdapters {

    @BindingAdapter("issText")
    public static void setText(IOSStyleSlider view, String text) {
        if (!text.equals(view.getText())) {
            view.setText(text);
        }
    }


    @BindingAdapter("issProgress")
    public static void setProgressBar(IOSStyleSlider view, int progress) {
        if (progress != view.getSliderProgress()) {
            view.setSliderProgress(progress);
        }
    }

    @BindingAdapter(value = "issTextAttrChanged")
    public static void setTextListener(IOSStyleSlider view, final InverseBindingListener listener){
        if(listener != null){
            view.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    listener.onChange();
                }
            });
        }
    }

    @BindingAdapter(value = "issProgressAttrChanged")
    public static void setProgressListener(IOSStyleSlider view, final InverseBindingListener listener){
        if(listener != null){
            view.addOnProgressChanged(new IOSStyleSlider.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(IOSStyleSlider slider, int progress) {
                    listener.onChange();
                }

                @Override
                public void onStartTrackingTouch(IOSStyleSlider slider) {

                }

                @Override
                public void onStopTrackingTouch(IOSStyleSlider slider) {

                }
            });
        }
    }
}

