/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leading.baselibrary.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.leading.baselibrary.R;

/**
 * A on-screen hint is a view containing a little message for the user and will
 * be shown on the screen continuously.  This class helps you create and show
 * those.
 *
 * <p>
 * When the view is shown to the user, appears as a floating view over the
 * application.
 * <p>
 * The easiest way to use this class is to call one of the static methods that
 * constructs everything you need and returns a new OnScreenHint object.
 */
public class OnScreenHint {
    static final String TAG = "OnScreenHint";
    static final boolean LOCAL_LOGV = false;

    final Context mContext;
    int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    int mX, mY;
    float mHorizontalMargin;
    float mVerticalMargin;
    View mView;
    View mNextView;
    private Long time;
    private final WindowManager.LayoutParams mParams =
            new WindowManager.LayoutParams();
    private final WindowManager mWM;
    private final Handler mHandler = new Handler();

    /**
     * Construct an empty OnScreenHint object.  You must call {@link #setView}
     * before you can call {@link #show}.
     *
     * @param context  The context to use.  Usually your
     *                 {@link android.app.Application} or
     *                 {@link android.app.Activity} object.
     */
    public OnScreenHint(Context context) {
        mContext = context;
        mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mY = context.getResources().getDimensionPixelSize(
                R.dimen.hint_y_offset);

        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.Animation_OnScreenHint;
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        mParams.setTitle("OnScreenHint");
    }
    /**
     * Show the view on the screen.
     */
    public void show() {
        if (mNextView == null) {
            throw new RuntimeException("setView must have been called");
        }
        new Thread(new Runnable() {

			@Override
			public void run() {
				mHandler.post(mShow);
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mHandler.post(mHide);
			}
		}).start();
        
        
    }

    /**
     * Close the view if it's showing.
     */
    public void cancel() {
        mHandler.post(mHide);
    }

    /**
     * Set the view to show.
     * @see #getView
     */
    public void setView(View view) {
        mNextView = view;
    }

    /**
     * Return the view.
     * @see #setView
     */
    public View getView() {
        return mNextView;
    }

    /**
     * Make a standard hint that just contains a text view.
     *
     * @param context  The context to use.  Usually your
     *                 {@link android.app.Application} or
     *                 {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     *
     */
    public static OnScreenHint makeText(Context context, CharSequence text,Long time) {
        OnScreenHint result = new OnScreenHint(context);
        result.time=time;
        LayoutInflater inflate =
                (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.on_screen_hint, null);
        TextView tv = (TextView) v.findViewById(R.id.message);
        tv.setText(text);

        result.mNextView = v;

        return result;
    }

    /**
     * Make a standard hint that just contains a text view with the text from a
     * resource.
     *
     * @param context  The context to use.  Usually your
     *                 {@link android.app.Application} or
     *                 {@link android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be
     *                 formatted text.
     *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static OnScreenHint makeText(Context context, int resId,Long time)
                                throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId),time);
    }

    /**
     * Update the text in a OnScreenHint that was previously created using one
     * of the makeText() methods.
     * @param resId The new text for the OnScreenHint.
     */
    public void setText(int resId) {
        setText(mContext.getText(resId));
    }

    /**
     * Update the text in a OnScreenHint that was previously created using one
     * of the makeText() methods.
     * @param s The new text for the OnScreenHint.
     */
    public void setText(CharSequence s) {
        if (mNextView == null) {
            throw new RuntimeException("This OnScreenHint was not "
                    + "created with OnScreenHint.makeText()");
        }
        TextView tv = (TextView) mNextView.findViewById(R.id.message);
        if (tv == null) {
            throw new RuntimeException("This OnScreenHint was not "
                    + "created with OnScreenHint.makeText()");
        }
        tv.setText(s);
    }
   
    private synchronized void handleShow() {
        if (mView != mNextView) {
            // remove the old view if necessary
            handleHide();
            mView = mNextView;
            final int gravity = mGravity;
            mParams.gravity = gravity;
            if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK)
                    == Gravity.FILL_HORIZONTAL) {
                mParams.horizontalWeight = 1.0f;
            }
            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK)
                    == Gravity.FILL_VERTICAL) {
                mParams.verticalWeight = 1.0f;
            }
            mParams.x = mX;
            mParams.y = mY;
            mParams.verticalMargin = mVerticalMargin;
            mParams.horizontalMargin = mHorizontalMargin;
            if (mView.getParent() != null) {
                mWM.removeView(mView);
            }
            mWM.addView(mView, mParams);
        }
    }

    private synchronized void handleHide() {
        if (mView != null) {
            // note: checking parent() just to make sure the view has
            // been added...  i have seen cases where we get here when
            // the view isn't yet added, so let's try not to crash.
            if (mView.getParent() != null) {
                mWM.removeView(mView);
            }
            mView = null;
        }
    }

    private final Runnable mShow = new Runnable() {
        public void run() {
            handleShow();
        }
    };
    
    private final Runnable mHide = new Runnable() {
        public void run() {
            handleHide();
        }
    };
}

