package com.jooyunghan.draganddrop.app;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import rx.subjects.PublishSubject;

import java.util.ArrayList;

import static android.view.MotionEvent.*;

/**
 * Created by jooyung.han on 2014-10-08.
 */
public class MyView extends View {
    Paint paint = new Paint();

    {
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
    }

    private PublishSubject<PointF> downs = PublishSubject.create();
    private PublishSubject<PointF> moves = PublishSubject.create();
    private PublishSubject<PointF> ups = PublishSubject.create();
    private ArrayList<Path> paths = new ArrayList<>();
    private int undoStep = 0;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attr) {
        super(context, attr);

        setOnTouchListener((view, event) -> {
            int action = event.getAction();
            PointF point = new PointF(event.getX(), event.getY());
            if (action == ACTION_DOWN) downs.onNext(point);
            else if (action == ACTION_MOVE) moves.onNext(point);
            else if (action == ACTION_UP) ups.onNext(point);
            return true;
        });

        downs.subscribe(down -> {
            Path path = newPath(down);
            moves.takeUntil(ups).subscribe(move -> {
                path.lineTo(move.x, move.y);
                invalidate();
            });
        });
    }

    private Path newPath(PointF p) {
        Path path = new Path();
        path.moveTo(p.x, p.y);
        while (undoStep > 0) {
            paths.remove(paths.size() - 1);
            undoStep--;
        }
        paths.add(path);
        return path;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i=0; i<paths.size() - undoStep;i++)
            canvas.drawPath(paths.get(i), paint);
    }

    public void undo() {
        if (paths.size() > undoStep) {
            undoStep++;
            invalidate();
        }
    }

    public void redo() {
        if (undoStep > 0) {
            undoStep--;
            invalidate();
        }
    }
}
