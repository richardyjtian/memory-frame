package io.github.richardyjtian.photoframe;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class JumpText implements TextWatcher {

    private EditText mThisView = null;
    private View mNextView = null;

    public JumpText(EditText mThisView, View mNextView){
        super();
        this.mThisView = mThisView;
        if(mThisView != null){
            this.mNextView = mNextView;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = s.toString();
        if(str.contains("\r") || str.contains("\n")){
            mThisView.setText(str.replace("\r","").replace("\n",""));
            if(mNextView != null){
                mNextView.requestFocus();
                if(mNextView instanceof EditText){
                    EditText et = (EditText) mNextView;
                    et.setSelection(et.getText().length());
                }
            }
        }

    }
}
