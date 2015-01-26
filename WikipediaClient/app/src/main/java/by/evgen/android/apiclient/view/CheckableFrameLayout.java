package by.evgen.android.apiclient.view;

/**
 * Created by User on 13.11.2014.
 */

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;

//TODO hardcoded color
public class CheckableFrameLayout extends FrameLayout implements Checkable {
    private boolean mChecked;

    public CheckableFrameLayout(Context context) {
        super(context);
    }

    public CheckableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
        setBackgroundDrawable(checked ? new ColorDrawable(0xffaa00a0) : null);
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void toggle() {
        setChecked(!mChecked);
    }

}
