package com.all.language.translate.voice.translator.ui.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class CustomEdittext extends androidx.appcompat.widget.AppCompatEditText {
    private KeyImageChange keyImageChange;

    public CustomEdittext(Context context) {
        super(context);
    }

    public CustomEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setKeyImageChange(KeyImageChange listener) {
        this.keyImageChange = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyImageChange != null) {
            keyImageChange.onKeyImage(keyCode, event);
        }
        return false;
    }

    public interface KeyImageChange {
        void onKeyImage(int keyCode, KeyEvent keyEvent);
    }
}
