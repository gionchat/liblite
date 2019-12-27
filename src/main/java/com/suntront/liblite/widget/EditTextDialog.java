package com.suntront.liblite.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.suntront.liblite.R;
import com.suntront.liblite.utils.ViewFindUtils;

/**
 * Author: Jeek
 * Date: 2019/5/16 14:31
 * Description: ${DESCRIPTION}
 */
public class EditTextDialog extends BaseDialog<EditTextDialog> {

    private Context context;
    private TextView tv_title;
    private TextView tv_cancel;
    private TextView tv_ok;
    private EditText et_content;
    private EditTextDialogListener editTextDialogListener;
    private String content;
    private String title;

    public EditTextDialog(Context context, String title, String content) {
        super(context);
        this.context = context;
        this.content = content;
        this.title = title;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        heightScale(0.3f);
        showAnim(new com.flyco.animation.Attention.ShakeHorizontal());
        View inflate = View.inflate(context, R.layout.dialog_custom_base, null);
        tv_cancel = ViewFindUtils.find(inflate, R.id.tv_cancel);
        tv_ok = ViewFindUtils.find(inflate, R.id.tv_ok);
        tv_title = ViewFindUtils.find(inflate, R.id.tv_title);
        et_content = ViewFindUtils.find(inflate, R.id.et_content);
        et_content.setText(content);
        tv_title.setText(title);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextDialogListener != null) {
                    editTextDialogListener.onCancle();
                }
                dismiss();
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextDialogListener != null) {
                    editTextDialogListener.onOk(et_content.getText().toString());
                }
            }
        });

        return;
    }

    public void setEditTextDialogListener(EditTextDialogListener editTextDialogListener) {
        this.editTextDialogListener = editTextDialogListener;
    }


    public interface EditTextDialogListener {

        void onCancle();

        void onOk(String content);
    }
}
