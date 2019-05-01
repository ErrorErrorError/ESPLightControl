package com.errorerrorerror.iosstyleslider;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

@InverseBindingMethods({
        @InverseBindingMethod(
                type = IOSStyleSlider.class,
                attribute = "issProgress",
                method = "getSliderProgress"),

        @InverseBindingMethod(
                type = IOSStyleSlider.class,
                attribute = "issText",
                method = "getText"),

        @InverseBindingMethod(
                type = IOSStyleSlider.class,
                attribute = "issSliderEnabled",
                method = "isSliderEnabled"
        )
})

public class IOSStyleSliderBindingAdapters {

    @BindingAdapter("issSliderEnabled")
    public static void setSliderEnabled(IOSStyleSlider view, boolean enabled) {
        if (view.isSliderEnabled() != enabled) {
            view.enableSlider(enabled);
        }
    }

    @BindingAdapter("issText")
    public static void setText(IOSStyleSlider view, String text) {
        if (!text.equals(view.getText())) {
            view.setText(text);
        }
    }

    @BindingAdapter("issAnimatedIconProgress")
    public static void setAnimatedIconProgress(IOSStyleSlider view, float progressIcon){
        if(view.getAnimatedIconProgress() != progressIcon){
            view.setAnimatedIconProgress(progressIcon);
        }
    }

    @BindingAdapter("issProgressInitialValue")
    public static void setInitialProgress(IOSStyleSlider view, int initialProgress){
        if(!view.hasSetInitialVal()){
            view.setProgressInitialValue(initialProgress);
            view.mSliderProgress = initialProgress;
        }
    }

    @BindingConversion
    public static String convertIntToString(int num) {
        return String.valueOf(num);
    }


    @BindingAdapter("issProgress")
    public static void setProgressBar(IOSStyleSlider view, int progress) {
        if (progress != view.getSliderProgress()) {
            view.setSliderProgress(progress);
        }
    }

    @BindingAdapter(value = {"issText", "issTextAttrChanged"})
    public static void setTextListener(IOSStyleSlider view,
                                       final TextWatcher watcher,
                                       final InverseBindingListener listener) {
        if (watcher == null && listener == null) {
            view.addTextChangedListener(null);
        } else {
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


    @BindingAdapter(value = {"issOnStartTrackingTouch", "issOnStopTrackingTouch",
            "issOnProgressChanged", "issProgressAttrChanged"}, requireAll = false)
    public static void setOnProgressChangeListeners(final IOSStyleSlider view,
                                                    final OnStartTrackingTouch start,
                                                    final OnStopTrackingTouch stop,
                                                    final OnProgressChanged progressChanged,
                                                    final InverseBindingListener attrChange) {
        if (start == null && stop == null && progressChanged == null && attrChange == null) {
            view.addOnProgressChanged(null);
        } else {
            view.addOnProgressChanged(new IOSStyleSlider.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(IOSStyleSlider slider, int progress) {
                        if (progressChanged != null) {
                            progressChanged.onProgressChanged(slider, progress);
                        }
                        if (attrChange != null) {
                            attrChange.onChange();
                        }
                }

                @Override
                public void onStartTrackingTouch(IOSStyleSlider slider) {
                    if (start != null) {
                        start.onStartTrackingTouch(slider);
                    }
                }

                @Override
                public void onStopTrackingTouch(IOSStyleSlider slider) {
                    if (stop != null) {
                        stop.onStopTrackingTouch(slider);
                    }
                }
            });
        }
    }

    public interface OnStartTrackingTouch {
        void onStartTrackingTouch(IOSStyleSlider slider);
    }

    public interface OnStopTrackingTouch {
        void onStopTrackingTouch(IOSStyleSlider seekBar);
    }

    public interface OnProgressChanged {
        void onProgressChanged(IOSStyleSlider slider, int progress);
    }
}

