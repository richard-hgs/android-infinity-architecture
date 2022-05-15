package com.infinity.architecture.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.infinity.architecture.utils.datepicker.CustomDatePicker;
import com.infinity.architecture.utils.datetime.DataHoraHelper;
import com.infinity.architecture.utils.datetime.DateFormats;
import com.infinity.architecture.utils.datetimepicker.CustomDateTimePicker;
import com.infinity.architecture.utils.dimens.DimenUtils;
import com.infinity.architecture.utils.locale.LocaleHelper;
import com.infinity.architecture.utils.timepicker.CustomTimePicker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InfinityTextInputLayPicker extends InfinityTextInputLayout {
    private final String TAG = "CustomTxtInputLayPicker";

    private int pickerType;     // 1=DATE, 2=TIME, 3=DATE_TIME
    private String hint;
    private int textSize;
    private int datePickerThemeResId;
    private int timePickerThemeResId;
    private Locale datePickerLocale;
    private Locale timePickerLocale;
    private String datePickerFormatSet;
    private String timePickerFormatSet;
    private String dateTimePickerFormatSet;

    private String strCurrentDate;
    private Date                      currentDate;
    private InfinityTextInputEditText infinityTextInputEditText = null;
    private CustomDateTimePicker      customDateTimePicker      = null;
    private CustomDatePicker customDatePicker = null;
    private CustomTimePicker customTimePicker = null;

    private int editTextBg;

    private ArrayList<DatePickerListener> datePickerListenersList = new ArrayList<>();

    public InfinityTextInputLayPicker(Context context) {
        super(context);
        init(null);
    }

    public InfinityTextInputLayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public InfinityTextInputLayPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextInputLayPicker);

            pickerType = typedArray.getInt(R.styleable.CustomTextInputLayPicker_picker_type, 1);    // Picker type 1=DATE
            hint = typedArray.getString(R.styleable.CustomTextInputLayPicker_hint);
            textSize = typedArray.getDimensionPixelSize(R.styleable.CustomTextInputLayPicker_text_size, DimenUtils.spToPx(getContext(), 12));
            datePickerThemeResId = typedArray.getResourceId(R.styleable.CustomTextInputLayPicker_date_picker_theme, 0);
            timePickerThemeResId = typedArray.getResourceId(R.styleable.CustomTextInputLayPicker_time_picker_theme, 0);
            editTextBg = typedArray.getResourceId(R.styleable.CustomTextInputLayPicker_edit_text_bg, 0);

            datePickerLocale = LocaleHelper.PORTUGUES;
            timePickerLocale = LocaleHelper.PORTUGUES;
            datePickerFormatSet = DateFormats.PT_BR_4;
            timePickerFormatSet = DateFormats.PT_BR_6;
            dateTimePickerFormatSet = DateFormats.PT_BR_8;

            typedArray.recycle();
        }

        infinityTextInputEditText = new InfinityTextInputEditText(getContext());
        infinityTextInputEditText.setId(View.generateViewId());
        infinityTextInputEditText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        infinityTextInputEditText.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
        infinityTextInputEditText.setCompoundDrawablePadding(DimenUtils.dpToPixel(getContext(), 8));
        infinityTextInputEditText.setHint(hint);
        infinityTextInputEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        if (editTextBg > 0) {
            infinityTextInputEditText.setBackgroundResource(editTextBg);
        }

        if (pickerType == 1 || pickerType == 3) {
            // Date / DateTime
            infinityTextInputEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_date_24), null, null, null);
        } else if (pickerType == 2) {
            // Time
            infinityTextInputEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_time_24), null, null, null);
        }

        addView(infinityTextInputEditText);

        // DESABILITA O TECLADO DE APARECER NO CAMPO
        infinityTextInputEditText.setRawInputType(InputType.TYPE_NULL);
        infinityTextInputEditText.setFocusable(false);
        infinityTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "clicado: Data");

                    // FECHA TECLADO SE ESTIVER ABERTO
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(infinityTextInputEditText.getWindowToken(), 0);

                    if (pickerType == 1) {
                        // Date picker
                        customDatePicker = new CustomDatePicker(getContext(), datePickerThemeResId, datePickerLocale, datePickerFormatSet, new CustomDatePicker.Listener() {
                            @Override
                            public boolean onDateSet(DatePicker view, Date date, String dateStr) {
                                infinityTextInputEditText.setText(dateStr);
                                strCurrentDate = dateStr;
                                currentDate = date;
                                notifyListenersDateHasChanged();
                                return true;
                            }

                            @Override
                            public boolean onCanceled() {
                                return true;
                            }

                            @Override
                            public void onParseException(ParseException e) {
                                e.printStackTrace();
                            }
                        });
                        customDatePicker.show();
                    } else if (pickerType == 2) {
                        // Time picker
                        customTimePicker = new CustomTimePicker(getContext(), timePickerThemeResId, timePickerLocale, timePickerFormatSet, new CustomTimePicker.Listener() {
                            @Override
                            public boolean onTimeSet(TimePicker view, Date date, String dateStr) {
                                infinityTextInputEditText.setText(dateStr);
                                strCurrentDate = dateStr;
                                currentDate = date;
                                notifyListenersDateHasChanged();
                                return true;
                            }

                            @Override
                            public boolean onCanceled() {
                                return true;
                            }

                            @Override
                            public void onParseException(ParseException e) {
                                e.printStackTrace();
                            }
                        });
                        customTimePicker.show();
                    } else if (pickerType == 3) {
                        // Date Time picker
                        customDateTimePicker = new CustomDateTimePicker(getContext(), datePickerThemeResId, timePickerThemeResId, datePickerLocale, timePickerLocale, datePickerFormatSet, timePickerFormatSet, dateTimePickerFormatSet, new CustomDateTimePicker.Listener() {
                            @Override
                            public boolean onDateSet(Date date, String dateStr) {
                                infinityTextInputEditText.setText(dateStr);
                                strCurrentDate = dateStr;
                                currentDate = date;
                                notifyListenersDateHasChanged();
                                // mListener.fragProdSetStartDate(date);
                                return true;
                            }

                            @Override
                            public void onParseException(ParseException e) {
                                e.printStackTrace();
                                // errorMsgOnUiThread("DATE_PICKER: " + e.getMessage(), null);
                            }
                        });
                        customDateTimePicker.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //errorMsgOnUiThread("AVISO", "CLICK_DATA: Ocorreu um problema ao clicar no campo data!");
                }
            }
        });
        infinityTextInputEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                infinityTextInputEditText.setText(null);
                strCurrentDate = null;
                currentDate = null;
                notifyListenersDateHasChanged();
                // mListener.fragProdSetStartDate(null);
                return true;
            }
        });
    }

    private void updateDateOnInputField(@Nullable Date date, boolean notifyDateHasChanged) throws Exception {
        strCurrentDate = null;
        if (date != null) {
            if (pickerType == 1) {
                // Date picker
                strCurrentDate = DataHoraHelper.formatDate(date, datePickerFormatSet, LocaleHelper.PORTUGUES);
            } else if (pickerType == 2) {
                // Time picker
                strCurrentDate = DataHoraHelper.formatDate(date, timePickerFormatSet, LocaleHelper.PORTUGUES);
            } else if (pickerType == 3) {
                // Date time picker
                strCurrentDate = DataHoraHelper.formatDate(date, dateTimePickerFormatSet, LocaleHelper.PORTUGUES);
            }
        }
        infinityTextInputEditText.setText(strCurrentDate);
        if (notifyDateHasChanged) {
            notifyListenersDateHasChanged();
        }
    }

    private void updateStrDateOnInputField(@Nullable String strDate, String dateFormat, boolean notifyDateHasChanged) throws Exception {
        currentDate = null;
        if (strDate != null) {
            if (pickerType == 1) {
                // Date picker
                currentDate = DataHoraHelper.getDateFromString(datePickerLocale, dateFormat, strDate);
            } else if (pickerType == 2) {
                // Time picker
                currentDate = DataHoraHelper.getDateFromString(timePickerLocale, dateFormat, strDate);
            } else if (pickerType == 3) {
                // Date time picker
                currentDate = DataHoraHelper.getDateFromString(datePickerLocale, dateFormat, strDate);
            }
        }

        updateDateOnInputField(currentDate, notifyDateHasChanged);
    }

    private void notifyListenersDateHasChanged() {
        for (DatePickerListener listenerAt : datePickerListenersList) {
            listenerAt.onDateChange(currentDate, strCurrentDate);
        }
    }

    public void addDatePickerListener(DatePickerListener datePickerListener) {
        datePickerListenersList.add(datePickerListener);
    }

    public void removeDatePickerListener(DatePickerListener datePickerListener) {
        datePickerListenersList.remove(datePickerListener);
    }

    public void clearDatePickerListeners() {
        datePickerListenersList.clear();
    }

    public void setDate(@Nullable Date date) throws Exception {
        this.currentDate = date;
        updateDateOnInputField(date, false);
    }

    public @Nullable Date getDate() {
        return this.currentDate;
    }

    public void setStrDate(@Nullable String strDate) throws Exception {
        if (pickerType == 1) {
            // Date picker
            updateStrDateOnInputField(strDate, datePickerFormatSet, false);
        } else if (pickerType == 2) {
            // Time picker
            updateStrDateOnInputField(strDate, timePickerFormatSet, false);
        } else if (pickerType == 3) {
            // Date time picker
            updateStrDateOnInputField(strDate, dateTimePickerFormatSet, false);
        }
    }

    @Nullable
    public String getStrDate() {
        return strCurrentDate;
    }

    public static interface DatePickerListener {
        void onDateChange(@Nullable Date date, @Nullable String strDate);
    }
}
