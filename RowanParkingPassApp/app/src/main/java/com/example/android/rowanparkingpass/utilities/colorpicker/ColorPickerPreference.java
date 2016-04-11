package com.example.android.rowanparkingpass.utilities.colorpicker;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.rowanparkingpass.R;

/**
 * ColorPreference based on ColorPickerDialog of Stock Calendar
 */
public class ColorPickerPreference extends Preference {

    private int[] mColorChoices = {};
    private int mValue = 0;
    private int mItemLayoutId = R.layout.calendar_grid_item_color;
    private int mNumColumns = 5;
    private View mPreviewView;

    public ColorPickerPreference(Context context) {
        super(context);
        initAttrs(null, 0);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs, 0);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs, defStyle);
    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.ColorPickerPreference, defStyle, defStyle);

        try {
            mItemLayoutId = a.getResourceId(R.styleable.ColorPickerPreference_cal_itemLayout, mItemLayoutId);
            mNumColumns = a.getInteger(R.styleable.ColorPickerPreference_cal_numColumns, mNumColumns);
            int choicesResId = a.getResourceId(R.styleable.ColorPickerPreference_cal_choices,
                    R.array.default_color_choice_values);
            if (choicesResId > 0) {
                String[] choices = a.getResources().getStringArray(choicesResId);
                mColorChoices = new int[choices.length];
                for (int i = 0; i < choices.length; i++) {
                    mColorChoices[i] = Color.parseColor(choices[i]);
                }
            }

        } finally {
            a.recycle();
        }
        setWidgetLayoutResource(mItemLayoutId);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mPreviewView = view.findViewById(R.id.calendar_color_view);
        setColorViewValue(mPreviewView, mValue);
    }

    public void setValue(int value) {
        if (callChangeListener(value)) {
            mValue = value;
            persistInt(value);
            notifyChanged();
        }
    }

    @Override
    protected void onClick() {
        super.onClick();

        ColorPickerDialog colorcalendar = (ColorPickerDialog) ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                mColorChoices, getValue(), mNumColumns, Utils.isTablet(getContext()) ? ColorPickerDialog.SIZE_LARGE : ColorPickerDialog.SIZE_SMALL);

        //colorcalendar.setPreference(this);

        Activity activity = (Activity) getContext();
        activity.getFragmentManager().beginTransaction()
                .add(colorcalendar, getFragmentTag())
                .commit();

        colorcalendar.setOnColorSelectedListener(listener);
    }

    /**
     * Implement listener to get selected color value
     */
    ColorPickerSwatch.OnColorSelectedListener listener = new ColorPickerSwatch.OnColorSelectedListener() {

        @Override
        public void onColorSelected(int color) {
            setValue(color);
        }
    };

    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();

        Activity activity = (Activity) getContext();
        ColorPickerDialog colorcalendar = (ColorPickerDialog) activity
                .getFragmentManager().findFragmentByTag(getFragmentTag());
        if (colorcalendar != null) {
            // re-bind listener to fragment
            colorcalendar.setOnColorSelectedListener(listener);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(0) : (Integer) defaultValue);
    }

    public String getFragmentTag() {
        return "color_" + getKey();
    }

    public int getValue() {
        return mValue;
    }

    private static void setColorViewValue(View view, int color) {
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            Resources res = imageView.getContext().getResources();

            Drawable currentDrawable = imageView.getDrawable();
            GradientDrawable colorChoiceDrawable;
            if (currentDrawable != null && currentDrawable instanceof GradientDrawable) {
                // Reuse drawable
                colorChoiceDrawable = (GradientDrawable) currentDrawable;
            } else {
                colorChoiceDrawable = new GradientDrawable();
                colorChoiceDrawable.setShape(GradientDrawable.OVAL);
            }

            // Set stroke to dark version of color
            int darkenedColor = Color.rgb(
                    Color.red(color) * 192 / 256,
                    Color.green(color) * 192 / 256,
                    Color.blue(color) * 192 / 256);

            colorChoiceDrawable.setColor(color);
            colorChoiceDrawable.setStroke((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 1, res.getDisplayMetrics()), darkenedColor);
            imageView.setImageDrawable(colorChoiceDrawable);

        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        }
    }


}
