package com.greenears.tasktracker.Views.CustomViews

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

/***
 * A Customizable ViewPager capable of vertical scrolling and scroll clamping.
 */
class CustomViewPager : ViewPager {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init()
    }

    private var _dragPagingEnabled = true
    private var _isVerticalPagingEnabled = false
    private var _customPageTransformer: CustomPageTransformer? = null

    private val _minimumTranslationById = SparseArray<Float>()
    private val _maximumTranslationById = SparseArray<Float>()

    /***
     * Initialization method. Creates default Page Transformer and configures overscroll mode
     */
    private fun init() {
        _customPageTransformer = CustomPageTransformer()
        setPageTransformer(false, _customPageTransformer)
        overScrollMode = OVER_SCROLL_NEVER
    }

    /***
     * Setter for vertical paging
     * @param enableVerticalPaging a boolean indicating whether the ViewPager should page vertically
     */
    fun setVerticalPagingEnabled(enableVerticalPaging: Boolean) {
        _isVerticalPagingEnabled = enableVerticalPaging
    }

    /***
     * Setter for minimum unit translation of a view
     * @param viewId the id of the view to clamp to the minimum value
     * @param minimumUnitTranslation the minimum unit translation
     */
    fun setViewMinimumUnitTranslation(viewId: Int?, minimumUnitTranslation: Float) {
        viewId?.also { key ->
            _minimumTranslationById.put(key, minimumUnitTranslation)
        }
    }

    /***
     * Setter for maximum unit translation of a view
     * @param viewId the id of the view to clamp to the minimum value
     * @param maximumUnitTranslation the maximum unit translation
     */
    fun setViewMaximumUnitTranslation(viewId: Int?, maximumUnitTranslation: Float) {
        viewId?.also { key ->
            _maximumTranslationById.put(key, maximumUnitTranslation)
        }
    }

    /***
     * Swaps the x and y positions of a passed in event (for interpreting vertical drags
     * in terms the native horizontal scroller can handle)
     * @param motionEvent the MotionEvent to flip the position values of
     */
    private fun swapXY(motionEvent: MotionEvent) {
        val width = width.toFloat()
        val height = height.toFloat()

        val newX = motionEvent.y / height * width
        val newY = motionEvent.x / width * height

        motionEvent.setLocation(newX, newY)
    }

    /***
     * Setter for paging enabled
     * @param dragPagingEnabled a boolean indicating whether the view pager should be draggable
     */
    fun setDragPagingEnabled(dragPagingEnabled: Boolean) {
        _dragPagingEnabled = dragPagingEnabled
    }

    /***
     * Touch interceptor method
     * @param motionEvent the motion event to evaluate for interception
     * @return True if the event should be intercepted, otherwise false
     */
    override fun onInterceptTouchEvent(motionEvent: MotionEvent): Boolean {
        if (!_dragPagingEnabled) {
            return false
        }

        if (_isVerticalPagingEnabled) {
            swapXY(motionEvent)
        }
        val intercepted = super.onInterceptTouchEvent(motionEvent)
        if (_isVerticalPagingEnabled) {
            swapXY(motionEvent)
        }

        return intercepted
    }

    /***
     * Touch handling method
     * @param motionEvent the motion event to handle
     * @return True if the event has been handled, otherwise false
     */
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        if (!_dragPagingEnabled) {
            return false
        }

        if (_isVerticalPagingEnabled) {
            swapXY(motionEvent)
        }
        val consumed = super.onTouchEvent(motionEvent)
        if (_isVerticalPagingEnabled) {
            swapXY(motionEvent)
        }
        return consumed
    }

    /**
     * Returns false for the canScroll method of ViewPager to disable scrolling of child views.
     */
    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        return false
    }

    /***
     * The Page Transformer used by the Customizable ViewPager.
     */
    private inner class CustomPageTransformer : ViewPager.PageTransformer {

        override fun transformPage(view: View, currentUnitTranslation: Float) {
            view.setLayerType(View.LAYER_TYPE_NONE, null)
            var minimumUnitTranslation = -1f
            var maximumUnitTranslation = 1f
            val centerUnitTranslation = 0f

            if (currentUnitTranslation < minimumUnitTranslation ||
                    currentUnitTranslation > maximumUnitTranslation) {
                // If the view is out of bounds, we don't need to calculate its position or draw it
                view.alpha = 0f
            } else {
                view.alpha = 1f

                // Calculate the current translation within the whole unaltered range
                val finalUnitTranslation: Float
                val absoluteUnitTranslation = (currentUnitTranslation - minimumUnitTranslation) /
                        (maximumUnitTranslation - minimumUnitTranslation)

                // Adjust the values for minimum and maximum, if they have been set
                _minimumTranslationById[view.id]?.also {
                    minimumUnitTranslation = it
                }

                _maximumTranslationById[view.id]?.also {
                    maximumUnitTranslation = it
                }

                // If the view is centered and at rest, the full calculation is unnecessary
                if (absoluteUnitTranslation.toDouble() == 0.5) {
                    if (centerUnitTranslation < minimumUnitTranslation) {
                        // If its centered position would be below the set minimum, clamp it
                        finalUnitTranslation = minimumUnitTranslation
                    } else if (centerUnitTranslation > maximumUnitTranslation) {
                        // If its centered position would be above the set maximum, clamp it
                        finalUnitTranslation = maximumUnitTranslation
                    } else {
                        // If its centered position is allowable, use it as is
                        finalUnitTranslation = centerUnitTranslation
                    }
                } else {
                    // Calculate the current translation within the relevant half
                    val relativeUnitTranslation: Float
                    if (absoluteUnitTranslation >= 0.5) {
                        relativeUnitTranslation = (absoluteUnitTranslation - 0.5f) * 2
                    } else {
                        relativeUnitTranslation = absoluteUnitTranslation * 2
                    }

                    // Transform the relative translation into the actual allowable range
                    if ((centerUnitTranslation < minimumUnitTranslation &&
                            absoluteUnitTranslation > 0.5) ||
                            (centerUnitTranslation > maximumUnitTranslation &&
                                    absoluteUnitTranslation < 0.5)) {
                        // The allowable bounds are smaller than half of the unaltered range
                        // Minimum + (relative * (maximum - minimum))
                        finalUnitTranslation = minimumUnitTranslation + relativeUnitTranslation *
                                (maximumUnitTranslation - minimumUnitTranslation)
                    } else if (absoluteUnitTranslation < 0.5) {
                        // Minimum + (relative * (center - minimum))
                        finalUnitTranslation = minimumUnitTranslation + relativeUnitTranslation *
                                (centerUnitTranslation - minimumUnitTranslation)
                    } else {
                        // center + (relative * (maximum - center))
                        finalUnitTranslation = centerUnitTranslation + relativeUnitTranslation *
                                (maximumUnitTranslation - centerUnitTranslation)
                    }
                }

                if (_isVerticalPagingEnabled) {
                    view.translationX = view.width * -currentUnitTranslation
                    val yPosition = finalUnitTranslation * view.height
                    view.translationY = yPosition
                } else {
                    view.translationX = view.width * (finalUnitTranslation - currentUnitTranslation)
                    view.translationY = 0f
                }

            }
        }
    }
}