package wangwang.entity;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class SlidingMenuView extends FrameLayout {

    private View menuView, mainView;
    private int menuWidth;
    private Status status = Status.Close;
    private Status preStatus = Status.Close; // 前一次保持的状态
    private ViewDragHelper mdDragHelper;

    private OnStatusListener listener;

    public interface OnStatusListener {

        void statusChanged(Status status);

    }

    public void setOnStatusListener(OnStatusListener listener) {
        this.listener = listener;
    }

    public enum Status {
        Open, Close
    }



    WindowManager wm = (WindowManager) getContext()
            .getSystemService(Context.WINDOW_SERVICE);

    int width = wm.getDefaultDisplay().getWidth();


    public SlidingMenuView(Context context) {
        this(context, null);
    }

    public SlidingMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mdDragHelper = ViewDragHelper.create(this, callback);
    }

    Callback callback = new Callback() {

        @Override
        public boolean tryCaptureView(View view, int arg1) {
            return true; //表示子view都可以拖拽
        }

        public int getViewHorizontalDragRange(View child) {
            return menuWidth;
        }

        ;

        // 根据建议值修正将要移动的位置（重要） 此时没有发生真正的移动
        // child： 当前拖拽的View
        // left：  新的位置的建议
        // dx：变化量
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mainView) {
                if (left >= 0)
                    return 0;
                else
                    return  0;
            } else if (child == menuView) {
                if (left >= width)
                    return width;
                else
                    return width-menuWidth;
            }
            return 0;
        }

        // 当view位置改变的时候，处理重要的事情（更新状态，伴随动画，重绘界面）
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            if (changedView == mainView)
                menuView.offsetLeftAndRight(dx);//
            else
                mainView.offsetLeftAndRight(dx);//
            invalidate();
        }

        // 手指松开调用该回调
        // releasedChild 要松开的子View
        // xvel x轴移动方向  x>0 表示手向右划动  x<0 表示手向左划动  x=0 不移动
        // yvel y轴移动方向 同上x
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);



            if (releasedChild == mainView) {
                if (status == Status.Open) {
                    close();
                    return;
                }
                close();

            } else {
                if (xvel == 0  && mainView.getLeft() > menuWidth / 2.0f) {
                    open();
                } else if (xvel < 0) {
                    open();
                } else {
                    close();
                }
            }

        }

    };

    /**
     * 打开菜单
     */
    public void open() {
        if (mdDragHelper.smoothSlideViewTo(mainView, -menuWidth, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        preStatus = status;
        status = Status.Open;
        if (listener != null && preStatus == Status.Close) {
            listener.statusChanged(status);
        }
    }

    /**
     * 关闭菜单
     */
    public void close() {
        if (mdDragHelper.smoothSlideViewTo(mainView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        preStatus = status;
        status = Status.Close;
        if (listener != null && preStatus == Status.Open) {
            listener.statusChanged(status);
        }
    }

    /**
     * 切换菜单状态
     */
    public void toggle() {
        if (status == Status.Close) {
            open();
            Log.d("打开菜单","已经打开右侧菜单栏");
        } else {
            close();
            Log.d("关闭菜单","已经打开右侧菜单栏");
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // 开始执行动画
        if (mdDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount()!=2){
            throw new IllegalArgumentException("子view的数量必须为2个");
        }
        menuView = getChildAt(0);
        mainView = getChildAt(1);
        menuWidth = menuView.getLayoutParams().width;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        menuView.layout(right, 0, right + menuWidth, menuView.getMeasuredHeight());
        mainView.layout(0, 0, right, bottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mdDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mdDragHelper.processTouchEvent(event);
        return true;
    }

}