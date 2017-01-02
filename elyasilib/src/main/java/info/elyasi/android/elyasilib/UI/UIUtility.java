package info.elyasi.android.elyasilib.UI;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.Locale;


/**
 * Created by Canada on 1/17/2016.
 */
public class UIUtility {

    private static final String TAG = "UIUtility";

    public static void waitForSeconds(long pSeconds, final IPauseUI pIPauseUI){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pIPauseUI != null)
                    pIPauseUI.Run();
            }
        }, pSeconds * 1000);
    }


    public static void setProgressBarColor(ProgressBar pProgressBar, int pColor) {
        pProgressBar.getIndeterminateDrawable()
                .setColorFilter(pColor, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public static void changeLocale1(Context baseContext, String language) {
        String languageToLoad  = language; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        baseContext.getResources().updateConfiguration(config,
                baseContext.getResources().getDisplayMetrics());
    }

    public static void HideKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void ShowKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public  static View getNextView(View pCurrentView) {
        View nextTextView = null;
        ViewParent viewParent = pCurrentView.getParent();
        if (viewParent != null) {
            ViewGroup viewGroup = (ViewGroup) viewParent;
            nextTextView = viewGroup.getChildAt(viewGroup.indexOfChild(pCurrentView) + 1);
        }
        return nextTextView;
    }

    public static void onTouchEffect(View sender, MotionEvent motionEvent) {
        Log.d(TAG, "motionEvent: "  + motionEvent.getAction());
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            sender.setAlpha(0.2f);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                motionEvent.getAction() == MotionEvent.ACTION_CANCEL ) {
            sender.setAlpha(1f);
        }
    }

    public static void onTouchEffect(View sender, MotionEvent pEvent, int touchColor, int defaultColor) {
        if (pEvent.getAction() == MotionEvent.ACTION_DOWN) {
            sender.setBackgroundColor(touchColor);
        } else if (pEvent.getAction() == MotionEvent.ACTION_UP) {
            sender.setBackgroundColor(defaultColor);
        }
    }

   /* public static void setIncludedButtonsEffect(View mainButtonFrame, final int touchColor, final int defaultColor) {
        if (mainButtonFrame != null && mainButtonFrame instanceof LinearLayout) {
            LinearLayout buttonsLayout = (LinearLayout) mainButtonFrame;
            for (int c = 0; c < buttonsLayout.getChildCount(); c++) {
                if (buttonsLayout.getChildAt(c) instanceof LinearLayout) {
                    final LinearLayout btnFrame = (LinearLayout) buttonsLayout.getChildAt(c);
                    btnFrame.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            UIUtility.onTouchEffect(view, motionEvent, touchColor, defaultColor);
                            return true;
                        }
                    });

                    for (View btn : btnFrame.getTouchables()) {
                        btn.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                UIUtility.onTouchEffect(view, motionEvent, touchColor, defaultColor);
                                UIUtility.onTouchEffect(btnFrame, motionEvent, touchColor, defaultColor);
                                return true;
                            }
                        });

                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btnFrame.callOnClick();
                            }
                        });
                    }
                }
            }
        }
    }*/

    public static void setEnableForAll(ViewGroup parent, boolean value) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            child.setEnabled(value);
        }
    }

    public static void setButtonEffect(View button, final View.OnClickListener ActivityOrFragment) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "onTouch btnFrame: " + view.getId() + "\n" + motionEvent.toString());
                UIUtility.onTouchEffect(view, motionEvent);
                return false;
            }
        });


        if (!button.hasOnClickListeners()) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick btnFrame: " + view.getId());
                    ActivityOrFragment.onClick(view);
                }
            });
        }
    }

    public static void setButtonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "onTouch btnFrame: " + view.getId() + "\n" + motionEvent.toString());
                UIUtility.onTouchEffect(view, motionEvent);
                return false;
            }
        });
    }

    public static void setIncludedButtonsEffect(View mainButtonFrame, final View.OnClickListener ActivityOrFragment) {
        if (mainButtonFrame != null && (mainButtonFrame instanceof LinearLayout || mainButtonFrame instanceof RelativeLayout)) {
            ViewGroup buttonsLayout = (ViewGroup) mainButtonFrame;

            for (int c = 0; c < buttonsLayout.getChildCount(); c++) {
                //if (buttonsLayout.getChildAt(c) instanceof LinearLayout) {
                    View btnFrame = buttonsLayout.getChildAt(c);
                    setButtonEffect(btnFrame, ActivityOrFragment);
                /*
                    btnFrame.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            Log.d(TAG, "onTouch btnFrame: " + view.getId() + "\n" + motionEvent.toString());
                            UIUtility.onTouchEffect(view, motionEvent);
                            return false;
                        }
                    });


                    if (!btnFrame.hasOnClickListeners()) {
                        btnFrame.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d(TAG, "onClick btnFrame: " + view.getId());
                                ActivityOrFragment.onClick(view);
                            }
                        });
                    }*/


            }
        }
    }
}
