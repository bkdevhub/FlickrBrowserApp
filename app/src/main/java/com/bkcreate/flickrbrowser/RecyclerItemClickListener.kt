package com.bkcreate.flickrbrowser

import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

private const val TAG = "RecyclerItemClickLis"

class RecyclerItemClickListener(
    context: Context,
    recyclerView: RecyclerView,
    private val listener: OnRecyclerClickListener) : RecyclerView.SimpleOnItemTouchListener() {

    interface OnRecyclerClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    //add gestureDetector
    private val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean { //Standard ist "MotionEvent?", es ist aber nicht möglich ein TapEvent mit null auszulösen
            Log.d(TAG, ".onSingleTapUp: starts")
            val childView = recyclerView.findChildViewUnder(e.x,e.y)
            Log.d(TAG, ".onSingleTapUp: calling listener.onItemClick")
            if (childView != null){
                listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView))
            }
            return true // super.onSingleTapUp(e)
        }

        override fun onLongPress(e: MotionEvent) { //gleiches wie  bei onSingleTapUp
            Log.d(TAG, ".onLongPress: starts")
            val childView = recyclerView.findChildViewUnder(e.x,e.y)
            Log.d(TAG, ".onLongPress: calling listener.onItemLongClick")
            if (childView != null){
                listener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))
            }
        }
    })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, "onInterceptTouchEvent called with $e")

        val result = gestureDetector.onTouchEvent(e)
        //return super.onInterceptTouchEvent(rv, e)
        return result
    }



}