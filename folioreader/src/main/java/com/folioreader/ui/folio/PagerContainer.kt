package com.folioreader.ui.folio

import android.content.Context
import android.graphics.Point
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout


class PagerContainer : FrameLayout, ViewPager.OnPageChangeListener {
    var viewPager: ViewPager? = null
        private set
    var mNeedsRedraw = false

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        init()
    }

    private fun init() {
//Disable clipping of children so non-selected pages are visible
        clipChildren = false

//Child clipping doesn't work with hardware acceleration in Android 3.x/4.x
//You need to set this value here if using hardware acceleration in an
// application targeted at these releases.
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        try {
            viewPager = getChildAt(0) as ViewPager
            viewPager!!.setOnPageChangeListener(this)
        } catch (e: Exception) {
            throw IllegalStateException("The root child of PagerContainer must be a ViewPager")
        }
    }

    private val mCenter: Point = Point()
    private val mInitialTouch: Point = Point()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mCenter.x = w / 2
        mCenter.y = h / 2
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
//We capture any touches not already handled by the ViewPager
// to implement scrolling from a touch outside the pager bounds.
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mInitialTouch.x = ev.x.toInt()
                mInitialTouch.y = ev.y.toInt()
                ev.offsetLocation((mCenter.x - mInitialTouch.x).toFloat(), (mCenter.y - mInitialTouch.y).toFloat())
            }
            else -> ev.offsetLocation((mCenter.x - mInitialTouch.x).toFloat(), (mCenter.y - mInitialTouch.y).toFloat())
        }
        return viewPager!!.dispatchTouchEvent(ev)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//Force the container to redraw on scrolling.
//Without this the outer pages render initially and then stay static
        val scrollX = viewPager!!.scrollX
        val childCount = viewPager!!.childCount
        for (i in 0 until childCount) {
            val child: View = viewPager!!.getChildAt(i)
            val lp = child.getLayoutParams() as ViewPager.LayoutParams
            if (lp.isDecor) continue
            val transformPos: Float = (child.getLeft() - scrollX) as Float / child.getWidth()
            transformPage(child, transformPos)
        }
        if (mNeedsRedraw) invalidate()
    }

    override fun onPageSelected(position: Int) {
//         mPager. getChildAt(3).setAlpha(05f);
    }

    override fun onPageScrollStateChanged(state: Int) {
        mNeedsRedraw = state != ViewPager.SCROLL_STATE_IDLE
    }

    fun transformPage(view: View, position: Float) {
        val pageWidth: Int = view.getWidth()
        val pageHeight: Int = view.getHeight()
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
//            view.setAlpha(MIN_ALPHA);
            view.setScaleX(MIN_SCALE)
            view.setScaleY(MIN_SCALE)
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
            val vertMargin = pageHeight * (1 - scaleFactor) / 2
            val horzMargin = pageWidth * (1 - scaleFactor) / 2
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2)
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2)
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor)
            view.setScaleY(scaleFactor)

            // Fade the page relative to its size.
//            view.setAlpha(MIN_ALPHA +  (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            view.clearAnimation()
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
//            view.setAlpha(MIN_ALPHA);
            view.setScaleX(MIN_SCALE)
            view.setScaleY(MIN_SCALE)
        }
    }

    companion object {
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
    }
}