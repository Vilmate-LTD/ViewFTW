package com.vilmate.igor.viewsftw.lib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public abstract class ViewFtw extends FrameLayout {
    private boolean isFirstTimeWindowVisible = true;
    private Bundle viewRestoredState;
    private boolean isStarted;

    private Dialog dialog;

    public ViewFtw(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        if (visibility == VISIBLE) {
            if (isFirstTimeWindowVisible) {
                onViewCreated(viewRestoredState);
                isFirstTimeWindowVisible = false;
            }
            onStart();
            isStarted = true;
        } else {
            onStop();
            isStarted = false;
        }
    }

    @Override
    public final SavedState onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState state = new SavedState(superState);
        state.viewState = onSaveViewInstanceState();

        return state;
    }

    protected Bundle onSaveViewInstanceState() {
        return new Bundle();
    }

    @Override
    final public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;

            viewRestoredState = savedState.viewState;

            state = savedState.getSuperState();
        }
        super.onRestoreInstanceState(state);
    }

    public void onViewCreated(Bundle savedState) {}

    public void onStart() {}

    public void onStop() {}

    public Activity getActivity() {
        return getActivity(getContext());
    }

    public Activity getActivity(Context context) {
        if (null == context) {
            return null;
        } else if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper)context).getBaseContext());
        }

        return null;
    }

    public LayoutInflater getLayoutInflater() {

        return LayoutInflater.from(getContext());
    }


    @NonNull
    public String getString(@StringRes int stringRes) throws Resources.NotFoundException {
        return getResources().getString(stringRes);
    }

    @NonNull
    public String getString(@StringRes int id, Object... formatArgs) throws Resources.NotFoundException {
        return getResources().getString(id, formatArgs);
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setLayoutWidthHeight(int width, int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = width;
        params.height = height;
        setLayoutParams(params);
    }

    public void showAsDialog() {
        showAsDialog(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                true);
    }

    public void showAsDialog(int width, int height, boolean cancelableOnTouchOutside) {
        if (null == this.getActivity()) {
            return;
        }
        if (ViewGroup.class.isInstance(getParent())) {
            ViewGroup parent = (ViewGroup) getParent();
            parent.removeAllViews();
        }

        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(this);
        dialog.setCanceledOnTouchOutside(cancelableOnTouchOutside);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ViewFtw.this.onDismiss(dialog);
            }
        });
        dialog.show();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        if (null != window) {
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = width;
            layoutParams.height = height;
            window.setAttributes(layoutParams);
        }

        setLayoutWidthHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    protected void onDismiss(DialogInterface dialogInterface) {}

    public Dialog getDialog() {
        return dialog;
    }

    public void dismiss() {
        if (null == dialog) {
            throw new UnsupportedOperationException("This view was not shown as a dialog. You should call BaseDialogView.showAsDialog() first");
        } else {
            dialog.dismiss();
        }
    }

    public boolean isDialog() {
        return (null != dialog);
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
    }

    /**
     * the wrapper for saving view state.
     */
    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        Bundle viewState;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.viewState = in.readBundle(getClass().getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBundle(this.viewState);
        }
    }
}