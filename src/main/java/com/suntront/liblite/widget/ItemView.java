/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, Jeek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.suntront.liblite.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntront.liblite.R;


/**
 * Copyright (C), 2015-2019, Suntront
 * Author: Jeek
 * Date: 2019/4/12 0012 09:43
 * Description:通用itemview
 */
public class ItemView extends FrameLayout {

    RelativeLayout rl_itemview_root;
    private TextView tv_title;
    private TextView tv_summary;
    private TextView tv_center;
    private ImageView iv_icon;
    private ImageView iv_right;
    private View divider_line_top;
    private View divider_line_bottom;
    private SwitchButton switchButton;

    private boolean isSwitch;
    private Drawable icon;
    private String title;
    private String summary;
    private String center;
    private Type type = Type.ITEMVIEW_SINGLE;


    private OnSwitchListener onSwitchListener;

    public ItemView(Context context) {
        super(context);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.itemview);
        icon = a.getDrawable(R.styleable.itemview_itemview_icon);
        title = a.getString(R.styleable.itemview_itemview_title);
        isSwitch = a.getBoolean(R.styleable.itemview_itemview_isswitch, false);
        summary = a.getString(R.styleable.itemview_itemview_summary);
        center = a.getString(R.styleable.itemview_itemview_center);
        type = Type.getType(a.getInt(R.styleable.itemview_itemview_type, 0));
        a.recycle();
        init();
    }

    private void init() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.itemview_icon_text, this);
        this.switchButton = view.findViewById(R.id.switcbutton);
        this.iv_icon = view.findViewById(R.id.iv_icon);
        this.tv_title = view.findViewById(R.id.tv_title);
        this.tv_summary = view.findViewById(R.id.tv_summary);
        this.tv_center = view.findViewById(R.id.tv_center);
        this.iv_right = view.findViewById(R.id.iv_right);
        this.divider_line_top = view.findViewById(R.id.divider_line_top);
        this.divider_line_bottom = view.findViewById(R.id.divider_line_bottom);
        this.rl_itemview_root = view.findViewById(R.id.cl_itemview_root);
        //setvalue


        if (TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.GONE);
        } else {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        }

        if (TextUtils.isEmpty(center)) {
            tv_center.setVisibility(View.GONE);
        } else {
            tv_center.setVisibility(View.VISIBLE);
            tv_center.setText(center);
        }

        if (TextUtils.isEmpty(summary)) {
            tv_summary.setVisibility(View.GONE);
        } else {
            tv_summary.setVisibility(View.VISIBLE);
            tv_summary.setText(summary);
        }

        if (icon == null) {
            iv_icon.setVisibility(View.GONE);
        } else {
            iv_icon.setVisibility(View.VISIBLE);
            iv_icon.setImageDrawable(icon);
        }

        if (isSwitch) {
            iv_right.setVisibility(View.GONE);
            switchButton.setVisibility(View.VISIBLE);
        }


        switch (type) {

            case ITEMVIEW_SINGLE:
            case ITEMVIEW_TOP:
                divider_line_top.setVisibility(View.VISIBLE);
                divider_line_bottom.setVisibility(View.VISIBLE);
                break;

            case ITEMVIEW_MIDDLE:
            case ITEMVIEW_BOTTOM:
                divider_line_top.setVisibility(View.GONE);
                divider_line_bottom.setVisibility(View.VISIBLE);
                break;

            default:
                divider_line_top.setVisibility(View.GONE);
                divider_line_bottom.setVisibility(View.VISIBLE);
                break;
        }

    }

    public TextView getTv_title() {
        return tv_title;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
    }

    public TextView getTv_summary() {
        return tv_summary;
    }

    public void setTv_summary(TextView tv_summary) {
        this.tv_summary = tv_summary;
    }

    public ImageView getIv_icon() {
        return iv_icon;
    }

    public void setIv_icon(ImageView iv_icon) {
        this.iv_icon = iv_icon;
    }

    public ImageView getIv_right() {
        return iv_right;
    }

    public void setIv_right(ImageView iv_right) {
        this.iv_right = iv_right;
    }

    public SwitchButton getSwitchButton() {
        return switchButton;
    }

    public void setSwitchButton(SwitchButton switchButton) {
        this.switchButton = switchButton;
    }

    public boolean isSwitch() {
        return isSwitch;
    }

    public void setSwitch(boolean aSwitch) {
        isSwitch = aSwitch;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    private enum Type {
        ITEMVIEW_SINGLE(0),
        ITEMVIEW_TOP(1),
        ITEMVIEW_MIDDLE(2),
        ITEMVIEW_BOTTOM(3);

        private int value;

        Type(int value) {
            this.value = value;
        }

        public static Type getType(int value) {
            switch (value) {
                case 0:
                    return Type.ITEMVIEW_SINGLE;
                case 1:
                    return Type.ITEMVIEW_TOP;
                case 2:
                    return Type.ITEMVIEW_MIDDLE;
                case 3:
                    return Type.ITEMVIEW_BOTTOM;
            }
            return Type.ITEMVIEW_SINGLE;
        }

    }

    public interface OnSwitchListener {
        void onSwith(boolean isSelect);
    }


}
