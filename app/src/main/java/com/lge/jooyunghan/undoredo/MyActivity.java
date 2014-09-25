package com.lge.jooyunghan.undoredo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import java.util.ArrayList;


public class MyActivity extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        canvas = (FrameLayout) findViewById(R.id.canvas);
        undoButton = (Button) findViewById(R.id.button_undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });
        undoButton.setEnabled(false);
        redoButton = (Button) findViewById(R.id.button_redo);
        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redo();
            }
        });
        redoButton.setEnabled(false);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
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
                if (command != null)
                    doCommand(command);
            }
        });
    }

    private Command getCommand(int tool, int x, int y) {
        if (tool == ERASURE) {
            int count = canvas.getChildCount();
            Rect rect = new Rect();
            for (int i = 0; i < count; i++) {
                final View childAt = canvas.getChildAt(i);
                childAt.getHitRect(rect);
                rect.inset(-30, -30);
                if (rect.contains(x, y)) {
                    return new Command(tool, x, y);
                }
            }
            return null;
        } else {
            return new Command(tool, x, y);
        }
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

    private Runnable doAction(int tool, int x, int y) {
        if (tool == CIRCLE) {
            final View v = new View(this);
            v.setLayoutParams(new FrameLayout.LayoutParams(SIZE, SIZE));
            v.setBackgroundColor(Color.BLUE);
            v.setX(x - SIZE / 2);
            v.setY(y - SIZE / 2);
            canvas.addView(v);
            return new Runnable() {
                @Override
                public void run() {
                    canvas.removeView(v);
                }
            };
        } else if (tool == SQUARE) {
            final View v = new View(this);
            v.setLayoutParams(new FrameLayout.LayoutParams(SIZE, SIZE));
            v.setBackgroundColor(Color.RED);
            v.setX(x - SIZE / 2);
            v.setY(y - SIZE / 2);
            canvas.addView(v);
            return new Runnable() {
                @Override
                public void run() {
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
                    canvas.removeViewAt(i);
                    return new Runnable() {

                        @Override
                        public void run() {
                            canvas.addView(childAt);
                        }
                    };
                }
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class Command {

        private final int tool;
        private final int x;
        private final int y;
        private Runnable undo;

        Command(int tool, int x, int y) {
            this.tool = tool;
            this.x = x;
            this.y = y;
        }

        public void execute() {
            undo = doAction(tool, x, y);
        }

        public void undo() {
            undo.run();
        }
    }
}
