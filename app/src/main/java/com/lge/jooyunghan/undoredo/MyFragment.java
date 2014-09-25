package com.lge.jooyunghan.undoredo;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MyFragment extends Fragment {
    private static final int CIRCLE = 1;
    private static final int SQUARE = 2;
    private static final int ERASER = 3;
    private final static int SIZE = 100;
    private FrameLayout canvas;
    private RadioGroup radioGroup;
    private int tool = CIRCLE;
    private int hotX;
    private int hotY;

    private final UndoRedo undoRedo = new UndoRedo();
    private final Shapes shapes = new Shapes();

    public MyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_fragment, container, false);
        canvas = (FrameLayout) rootView.findViewById(R.id.canvas);
        Button undoButton = (Button) rootView.findViewById(R.id.button_undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoRedo.undo();
            }
        });
        undoRedo.undoable.subscribe(undoButton::setEnabled);

        Button redoButton = (Button) rootView.findViewById(R.id.button_redo);
        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoRedo.redo();
            }
        });
        undoRedo.redoable.subscribe(redoButton::setEnabled);

        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroup.check(R.id.circle);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.circle) {
                    tool = CIRCLE;
                } else if (checkedId == R.id.square) {
                    tool = SQUARE;
                } else {
                    tool = ERASER;
                }
            }
        });

        canvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hotX = (int) event.getX();
                hotY = (int) event.getY();
                return false;
            }
        });

        canvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command command = makeCommand(tool, hotX, hotY);
                if (command != null) {
                    undoRedo.doCommand(command);
                }
            }
        });

        shapes.addObserver((o, d) -> refresh());
        refresh();
        return rootView;
    }

    private void refresh() {
        canvas.removeAllViews();
        for (int i = 0; i < shapes.size(); i++) {
            canvas.addView(createView(shapes.get(i)));
        }
    }

    private View createView(Shape shape) {
        if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            View v = new View(getActivity());
            v.setLayoutParams(new FrameLayout.LayoutParams(circle.radius * 2, circle.radius * 2));
            v.setBackground(getResources().getDrawable(R.drawable.circle));
            v.setX(circle.x - circle.radius);
            v.setY(circle.y - circle.radius);
            return v;
        } else {
            Square square = (Square) shape;
            View v = new View(getActivity());
            v.setLayoutParams(new FrameLayout.LayoutParams(square.w, square.h));
            v.setBackground(getResources().getDrawable(R.drawable.square));
            v.setX(square.x);
            v.setY(square.y);
            return v;
        }
    }

    private Command makeCommand(int tool, int x, int y) {
        if (tool == CIRCLE || tool == SQUARE) {
            return new AddCommand(shapes, createShape(tool, x, y));
        } else {
            int index = shapes.hitTest(x, y);
            if (index == -1) return null;
            return new RemoveCommand(shapes, index);
        }
    }

    private Shape createShape(int tool, int x, int y) {
        if (tool == CIRCLE) {
            return new Circle(x, y, SIZE / 2);
        } else {
            return new Square(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        }
    }
}
