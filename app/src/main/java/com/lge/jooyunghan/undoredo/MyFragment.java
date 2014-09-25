package com.lge.jooyunghan.undoredo;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MyFragment extends Fragment {
    private static final int CIRCLE = 1;
    private static final int SQUARE = 2;
    private static final int ERASURE = 3;
    private final static int SIZE = 30;
    private FrameLayout canvas;
    private Button redoButton;
    private Button undoButton;
    private RadioGroup radioGroup;
    private int tool;
    private int hotX;
    private int hotY;
    private ArrayList<Command> commands = new ArrayList<Command>();
    private int position = 0;

    public MyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d("jhan", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d("jhan", "onCreateView");
        View rootView = inflater.inflate(R.layout.my_fragment, container, false);
        canvas = (FrameLayout) rootView.findViewById(R.id.canvas);
        undoButton = (Button) rootView.findViewById(R.id.button_undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });
        undoButton.setEnabled(false);
        redoButton = (Button) rootView.findViewById(R.id.button_redo);
        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redo();
            }
        });
        redoButton.setEnabled(false);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroup.check(R.id.circle);
        tool = CIRCLE;

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.circle) {
                    tool = CIRCLE;
                } else if (checkedId == R.id.square) {
                    tool = SQUARE;
                } else {
                    tool = ERASURE;
                }
            }
        });

        canvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                hotX = (int) x;
                hotY = (int) y;
                return false;
            }
        });

        canvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command command = getCommand(tool, hotX, hotY);
                if (command != null) {
                    doCommand(command);
                }
            }
        });
        return rootView;
    }

    private void doCommand(Command command) {
        while (commands.size() > position) {
            commands.remove(position);
        }
        commands.add(command);
        position++;
        command.execute();
        undoButton.setEnabled(true);
        redoButton.setEnabled(false);
    }

    private void undo() {
        position--;
        Command command = commands.get(position);
        command.undo();
        redoButton.setEnabled(true);
        if (position == 0)
            undoButton.setEnabled(false);
    }

    private void redo() {
        Command command = commands.get(position);
        command.execute();
        position++;
        undoButton.setEnabled(true);
        if (position >= commands.size())
            redoButton.setEnabled(false);
    }

    private Command getCommand(int tool, int x, int y) {
        if (tool == CIRCLE) {
            final View v = new View(getActivity().getApplicationContext());
            v.setLayoutParams(new FrameLayout.LayoutParams(SIZE, SIZE));
            v.setBackgroundColor(Color.BLUE);
            v.setX(x - SIZE / 2);
            v.setY(y - SIZE / 2);
            return new Command() {
                @Override
                public void execute() {
                    canvas.addView(v);
                }
                @Override
                public void undo() {
                    canvas.removeView(v);
                }
            };
        } else if (tool == SQUARE) {
            final View v = new View(getActivity().getApplicationContext());
            v.setLayoutParams(new FrameLayout.LayoutParams(SIZE, SIZE));
            v.setBackgroundColor(Color.RED);
            v.setX(x - SIZE / 2);
            v.setY(y - SIZE / 2);
            return new Command() {
                @Override
                public void execute() {
                    canvas.addView(v);
                }
                @Override
                public void undo() {
                    canvas.removeView(v);
                }
            };
        } else { // erasure
            int count = canvas.getChildCount();
            Rect rect = new Rect();

            for (int i = 0; i < count; i++) {
                final View childAt = canvas.getChildAt(i);
                childAt.getHitRect(rect);
                rect.inset(-30, -30);
                if (rect.contains(x, y)) {
                    return new Command() {
                        @Override
                        public void execute() {
                            canvas.removeView(childAt);
                        }
                        @Override
                        public void undo() {
                            canvas.addView(childAt);
                        }
                    };
                }
            }
            return null;
        }
    }

}
