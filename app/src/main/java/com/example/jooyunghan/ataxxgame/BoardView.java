package com.example.jooyunghan.ataxxgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Command {
    public static final int STOP = 1;
    public static final int TOUCH = 2;
    int type;
    float x;
    float y;

    private Command(int type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }
    public static Command touch(float x, float y) {
        return new Command(TOUCH, x, y);
    }
    public static Command stop() {
        return new Command(STOP, 0, 0);
    }
}
/**
 * Created by jooyung.han on 2014. 9. 17..
 */
public class BoardView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private volatile boolean running;
    private Thread thread;
    private AtaxxModel model;
    private float touched_x;
    private float touched_y;
    private boolean touched;
    private int canvasWidth;
    private int canvasHeight;
    private Bitmap canvasBitmap;
    private Canvas buffer;
    LinkedBlockingQueue<Command> queue = new LinkedBlockingQueue<Command>();

    public BoardView(Context context) {
        super(context);
    }

    public BoardView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
    }

    public void resume() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            queue.put(Command.stop());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void drawOn(Canvas canvas) {
        if (buffer == null) {
            canvasWidth = getWidth();
            canvasHeight = getHeight();
            canvasBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
            buffer = new Canvas();
            buffer.setBitmap(canvasBitmap);
        }

        model.step(touched, touched_x, touched_y);
        model.draw(canvas);

        canvas.drawBitmap(canvasBitmap, 0, 0, null);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (running) {
            Command command = null;
            try {
                command = queue.poll(1000/60, TimeUnit.MICROSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (command == null) {
                if (surfaceHolder.getSurface().isValid()) {
                    Canvas canvas = surfaceHolder.lockCanvas();
                    drawOn(canvas);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            } else {
                switch (command.type) {
                case Command.STOP:
                    running = false;
                    break;
                case Command.TOUCH:

                default:
                    break;
                }
            }
        }
    }

    public void setModel(AtaxxModel model) {
        this.model = model;
    }

    public AtaxxModel getModel() {
        return model;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touched_x = event.getX();
        touched_y = event.getY();

        int action = event.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            touched = true;
            break;
        case MotionEvent.ACTION_MOVE:
            touched = true;
            break;
        case MotionEvent.ACTION_UP:
            touched = false;
            break;
        case MotionEvent.ACTION_CANCEL:
            touched = false;
            break;
        case MotionEvent.ACTION_OUTSIDE:
            touched = false;
            break;
        default:
        }
        return true; //processed
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("jhan", "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("jhan", "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("jhan", "surfaceDestroyed");
    }
}