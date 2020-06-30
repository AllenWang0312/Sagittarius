package edu.tjrac.swant.common.util;

import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.ViewCompat;

public class BarrierfreeUtils {


    /**
     * 设置无障碍焦点
     * 若关闭，用户控件将失去焦点，不会播报内容
     * @param view  指定控件
     * @param focused true打开，false关闭
     */
    public static void setAccessibilityFocusable(View view, boolean focused) {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            if (focused) {
                ViewCompat.setImportantForAccessibility(view, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
            } else {
                ViewCompat.setImportantForAccessibility(view, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
            }
        }
    }

    /**
     * 动态设置指定控件的播报内容
     * @param view 指定控件l
     * @param contentDesc 播报语音内容
     */
    public static void setAccessibilityDesc(final View view, final String contentDesc) {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            view.setAccessibilityDelegate(new View.AccessibilityDelegate() {
                @Override
                public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
                    super.onInitializeAccessibilityNodeInfo(host, info);
                    if (contentDesc != null) {
                        info.setContentDescription(contentDesc);
                    }
                    info.setClassName(view.getClass().getName());
                }
            });
        }
    }

    /**
     * 定位指定控件播放语音内容
     * @param view  指定控件
     */
    public static void obtainBlindModeFocusable(final View view) {
        view.postDelayed(() -> {
            if (android.os.Build.VERSION.SDK_INT >= 15) {
                view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
            }
        }, 100);
    }

    /**
     * 始终播报语音内容，不用定位焦点
     * @param view
     * @param desc 描述内容，将它放到event的getText()中，然后请求View的父类来发出事件
     * @desc ccessibilityEvent.TYPE_ANNOUNCEMENT是代表元素需要TalkBack服务来读出描述内容。
     */
    public static void setSuperclassFocusableDesc(final View view, final String desc) {
        view.postDelayed(() -> {
            if(android.os.Build.VERSION.SDK_INT >= 16){
                AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_ANNOUNCEMENT);
                event.setPackageName(view.getContext().getPackageName());
                event.setClassName(view.getClass().getName());
                event.setSource(view);
                event.getText().add(desc);
                view.getParent().requestSendAccessibilityEvent(view, event);
            }
        }, 100);
    }

    /**
     * 经试用无效， 还请大神赐教
     */
    public static void setDialogUnbroadcast(final View view) {
        if (android.os.Build.VERSION.SDK_INT >= 15) {
            view.setAccessibilityDelegate(new View.AccessibilityDelegate() {

                @Override
                public void sendAccessibilityEvent(View host, int eventType) {
                    // 弹出Popup或dialog后，不自动读各项内容
                    if (eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        super.sendAccessibilityEvent(host, eventType);
                    }
                }
            });
        }
    }
}
