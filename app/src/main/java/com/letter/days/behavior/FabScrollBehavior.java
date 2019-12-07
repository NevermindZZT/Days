package com.letter.days.behavior;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.ViewCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class FabScrollBehavior extends CoordinatorLayout.Behavior {

    private boolean visible = true;

    public FabScrollBehavior (Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 0 && visible) {
            visible = false;
            onHide(child);
        }
        else if (dyConsumed < 0) {
            visible = true;
            onShow(child);
        }
    }

    private void onHide(View view) {
        if (view instanceof Toolbar){
            ViewCompat.animate(view).translationY(-(view.getHeight() * 2)).setInterpolator(new AccelerateInterpolator(3));
        }else if (view instanceof FloatingActionButton){
            ViewCompat.animate(view).translationY(view.getHeight() * 2).setInterpolator(new AccelerateInterpolator(3));
        }
    }

    private void onShow(View view) {
        ViewCompat.animate(view).translationY(0).setInterpolator(new DecelerateInterpolator(3));

    }
}
