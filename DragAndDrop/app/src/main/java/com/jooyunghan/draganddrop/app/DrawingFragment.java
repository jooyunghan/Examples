package com.jooyunghan.draganddrop.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class DrawingFragment extends Fragment {

    public DrawingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        MyView canvas = (MyView) rootView.findViewById(R.id.canvas);
        rootView.findViewById(R.id.undo_button).setOnClickListener((v) -> canvas.undo());
        rootView.findViewById(R.id.redo_button).setOnClickListener((v) -> canvas.redo());
        return rootView;
    }
}
