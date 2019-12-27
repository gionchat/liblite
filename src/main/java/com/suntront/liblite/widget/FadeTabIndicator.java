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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntront.liblite.R;


/**
 * Copyright (C), 2015-2019, Suntront
 * Author: Jeek
 * Date: 2019/4/27 0027 10:09
 * Description:
 */

public class FadeTabIndicator extends LinearLayout {
    private ViewPager mViewPager;
    private PageListener pageListener = new PageListener();
    private LayoutParams tabLayoutParams;
    private int imageSize = 22; // dp
    private int textSize = 13; // sp

    public FadeTabIndicator(Context context) {
        super(context);
        init();
    }

    public FadeTabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FadeTabIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setPadding(0, 15, 0, 15);
        setBackgroundResource(R.drawable.trans);
        tabLayoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
        imageSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, imageSize, getResources().getDisplayMetrics());
    }

    public void setViewPager(ViewPager pager) {
        this.mViewPager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.setOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    public void setCurrentItem(int item) {
        mViewPager.setCurrentItem(item, false);
        tabSelect(item);
    }

    public void notifyDataSetChanged() {

        removeAllViews();

        int tabCount = mViewPager.getAdapter().getCount();

        FadingTab tabs = (FadingTab) mViewPager.getAdapter();
        for (int i = 0; i < tabCount; i++) {
            addTab(i, mViewPager.getAdapter().getPageTitle(i).toString(),
                    tabs.getTabNormalIconResId(i), tabs.getTabSelectIconResId(i),
                    tabs.getTabNormalTextColorResId(i), tabs.getTabSelectTextColorResId(i));
        }
    }

    private void addTab(final int position, String text, int normalResId, int selectResId,
                        int textNormalColorResId, int textSelectColorResId) {
        TabView tabView = new TabView(getContext(), text, normalResId, selectResId,
                textNormalColorResId, textSelectColorResId);
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItem(position);
            }
        });
        addView(tabView, position, tabLayoutParams);
    }

    private void tabSelect(int index) {
        final int tabCount = getChildCount();
        for (int i = 0; i < tabCount; i++) {
            getChildAt(i).setSelected(i == index);
        }
    }

    public interface FadingTab {

        int getTabNormalIconResId(int position);

        int getTabSelectIconResId(int position);

        int getTabNormalTextColorResId(int position);

        int getTabSelectTextColorResId(int position);
    }

    private class FadingImageView extends FrameLayout {
        private ImageView mNormalImage;
        private ImageView mSelectImage;

        public FadingImageView(Context context, int normalResId, int selectResId) {
            super(context);
            mNormalImage = new ImageView(context);
            mSelectImage = new ImageView(context);
            mNormalImage.setImageResource(normalResId);
            mSelectImage.setImageResource(selectResId);
            mNormalImage.setAlpha(1.0f);
            mSelectImage.setAlpha(0.0f);
            addView(mNormalImage, 0, new LayoutParams(72, 72, Gravity.CENTER));
            addView(mSelectImage, 1, new LayoutParams(72, 72, Gravity.CENTER));
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            setAlpha(selected ? 1.0f : 0.0f);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void setAlpha(float alpha) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mNormalImage.setAlpha(1 - alpha);
                mSelectImage.setAlpha(alpha);
            } else {
                mNormalImage.setAlpha((int) (1 - alpha));
                mSelectImage.setAlpha((int) alpha);
            }
        }
    }

    private class FadingTextView extends FrameLayout {
        private TextView mNormalTextView;
        private TextView mSelectTextView;

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public FadingTextView(Context context, String text, int normalResId, int selectResId) {
            super(context);
            mNormalTextView = new TextView(context);
            mSelectTextView = new TextView(context);
            mNormalTextView.setTextColor(getResources().getColor(normalResId));
            mSelectTextView.setTextColor(getResources().getColor(selectResId));
            mNormalTextView.setAlpha(1.0f);
            mSelectTextView.setAlpha(0.0f);
            mNormalTextView.setText(text);
            mSelectTextView.setText(text);
            mNormalTextView.setTextSize(textSize);
            mSelectTextView.setTextSize(textSize);
            addView(mNormalTextView, 0, new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
            addView(mSelectTextView, 1, new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            setAlpha(selected ? 1.0f : 0.0f);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void setAlpha(float alpha) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mNormalTextView.setAlpha(1 - alpha);
                mSelectTextView.setAlpha(alpha);
            } else {
                mNormalTextView.setAlpha((int) (1 - alpha));
                mSelectTextView.setAlpha((int) alpha);
            }
        }
    }

    private class TabView extends RelativeLayout {
        private FadingImageView fadingImageView;
        private FadingTextView fadingTextView;

        public TabView(Context context, String text, int normalResId, int selectResId,
                       int textNormalColorResId, int textSelectColorResId) {
            super(context);
            LayoutParams ivLayoutParams =
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            fadingImageView = new FadingImageView(context, normalResId, selectResId);
            fadingImageView.setId(100);
            addView(fadingImageView, ivLayoutParams);

            LayoutParams tvLayoutParams =
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            tvLayoutParams.addRule(RelativeLayout.BELOW, 100);
            fadingTextView = new FadingTextView(context, text, textNormalColorResId, textSelectColorResId);
            addView(fadingTextView, tvLayoutParams);
        }

        public FadingImageView getFadingImageView() {
            return fadingImageView;
        }

        public FadingTextView getFadingTextView() {
            return fadingTextView;
        }
    }

    private class PageListener implements OnPageChangeListener {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            ((TabView) getChildAt(position)).getFadingImageView().setAlpha(1.0f - positionOffset);
            ((TabView) getChildAt(position)).getFadingTextView().setAlpha(1.0f - positionOffset);
            if (position + 1 <= getChildCount() - 1) {
                ((TabView) getChildAt(position + 1)).getFadingImageView().setAlpha(positionOffset);
                ((TabView) getChildAt(position + 1)).getFadingTextView().setAlpha(positionOffset);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageSelected(int position) {
            tabSelect(position);
            Log.i("jun_tag", "fade tag position: " + position);
        }
    }
}
