package top.defaults.colorpicker;

public interface ColorObserver {
    /**
     * Color has changed.
     *
     * @param color the new color
     */
    void onColorSelected(int color);
}
