package com.example.phnf2.projeto2unidade.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerLinkMapsClickListener implements RecyclerView.OnItemTouchListener {

    OnLinkMapsClickListener mlistener;
    GestureDetector mgestureDetector;

    public interface OnLinkMapsClickListener{

        void onLinkMapsLongClick(View view, int posicao);
    }

    public RecyclerLinkMapsClickListener(Context context,final RecyclerView Rview, OnLinkMapsClickListener listener) {
        mlistener = listener;
        mgestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);

                View childView = Rview.findChildViewUnder(e.getX(), e.getY());
                if(childView != null && mlistener != null){
                    mlistener.onLinkMapsLongClick(childView,Rview.getChildAdapterPosition(childView));

                }
            }
        });


    }


    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        mgestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
