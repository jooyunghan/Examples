package com.example.jooyunghan.ataxxgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by jooyung.han on 2014. 9. 17..
 */
public class AtaxxModel {

    private boolean prevTouched;
    private Rect bounds = new Rect();
    private Random r = new Random();

    abstract class Animator {
        protected final int duration;
        protected int count;
        Runnable onCompleteCallback = null;

        Animator(int duration) {
            this.count = 0;
            this.duration = duration;
        }

        public void onComplete(Runnable runnable) {
            onCompleteCallback = runnable;
        }

        protected abstract void onStep();

        public void animate() {
            if (count > duration) {
                if (onCompleteCallback != null) {
                    onCompleteCallback.run();
                    onCompleteCallback = null;
                }
            } else {
                onStep();
                count++;
            }
        }
    }

    class MoveAnimator extends Animator {

        private final Ball ball;
        private final float sx;
        private final float sy;
        private final float dx;
        private final float dy;

        public MoveAnimator(Ball ball, int duration, float x, float y) {
            super(duration);
            this.ball = ball;
            this.sx = ball.x;
            this.sy = ball.y;
            this.dx = x;
            this.dy = y;
        }

        protected void onStep() {
            if (++count >= duration) {
                ball.x = dx;
                ball.y = dy;
            } else {
                float t = count / (float)duration;
                // interpolate
                //t = -t * (t - 2);
                ball.x = sx + t * (dx - sx);
                ball.y = sy + t * (dy - sy);
            }
        }
    }

    class Ball {
        int color;
        float x;
        float y;
        private Animator animator;
        boolean toBeRemoved = false;
        float size = 30;

        public Ball(int color, float x, float y) {
            this.color = color;
            this.x = x;
            this.y = y;
        }

        public void moveTo(int duration, float x, float y) {
            animator = new MoveAnimator(this, duration, x, y);
            animator.onComplete(new Runnable() {
                @Override
                public void run() {
                    animator = null;
                }
            });
        }

        public void step() {
            if (animator == null) {
                if (probability(30)) {
                    moveTo(randomDuration(60, 120), randomX(), randomY());
                } else {
                    moveTo(randomDuration(120, 180), randomX(), randomY());
                }
            }

            if (animator != null) {
                animator.animate();
            }
        }

        public boolean hit(float x, float y) {
            return Math.max(Math.abs(this.x - x), Math.abs(this.y - y)) < size * 2;
        }

        public void kill() {
            animator = new KillAnimator(this, 60);
            animator.onComplete(new Runnable() {
                @Override
                public void run() {
                    toBeRemoved = true;
                }
            });
        }

        private void draw(Canvas canvas, Paint paint, RectF r) {
            r.set(x - size, y - size, x + size, y + size);
            paint.setColor(color);
            canvas.drawOval(r, paint);
        }
    }

    private boolean probability(int percentage) {
        return r.nextInt(100) < percentage;
    }

    class KillAnimator extends Animator {

        private final Ball ball;
        private final float oldSize;
        private final int a;
        private final int r;
        private final int g;
        private final int b;

        private final float newSize;
        private final int newAlpha;

        KillAnimator(Ball ball, int duration) {
            super(duration);
            this.ball = ball;
            this.oldSize = ball.size;
            this.a = Color.alpha(ball.color);
            this.r = Color.red(ball.color);
            this.g = Color.green(ball.color);
            this.b = Color.blue(ball.color);
            this.newSize = ball.size * 1.5f;
            this.newAlpha = 0;
        }

        @Override
        protected void onStep() {
            if (++count >= duration) {
                ball.size = newSize;
                ball.color = Color.argb(newAlpha, r, g, b);
            } else {
                float t = count / (float)duration;
                // interpolate
                t = -t * (t - 2);
                ball.size = oldSize + t * (newSize - oldSize);
                ball.color = Color.argb((int)(a + t * (newAlpha - a)), r, g, b);
            }
        }
    }


    ArrayList<Ball> balls = new ArrayList<Ball>();
    long start;
    long end;
    void reset() {
        start = System.currentTimeMillis();
        end = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            balls.add(randomBall());
        }
    }



    private int randomDuration(int begin, int end) {
        return begin + r.nextInt(end - begin);
    }

    private Ball randomBall() {
        Ball ball = new Ball(randomColor(), randomX(), randomY());
        return ball;
    }

    private int randomColor() {
        int[] arr = new int[3];
        arr[0] = 0x80;
        arr[1] = 0xFF;
        arr[2] = r.nextInt(0x10) * 8 + 0x80;
        // Fisher–Yates shuffle
        for (int i1 = arr.length - 1; i1 >= 0; i1--) {
            int i2 = r.nextInt(i1 + 1);
            int tmp = arr[i2];
            arr[i2] = arr[i1];
            arr[i1] = tmp;
        }
        return Color.rgb(arr[0], arr[1], arr[2]);
    }

    private float randomX() {
        return bounds.left + r.nextFloat() * bounds.width();
    }

    private float randomY() {
        return bounds.top + r.nextFloat() * bounds.height();
    }

    public void step(boolean touched, float touched_x, float touched_y) {
        if (touched) {

        }

        if (prevTouched && !touched) {
            //            follower.moveTo(target.x, target.y);
        }
//
        if (!prevTouched && touched) {
            if (balls.isEmpty())
                reset();
            else {
                for (Ball b : balls) {
                    if (b.hit(touched_x, touched_y)) {
                        b.kill();
                    }
                }
            }
        }

        prevTouched = touched;
        for (Ball b : balls) {
            b.step();
        }

        for (Iterator<Ball> bi = balls.iterator(); bi.hasNext(); ) {
            Ball b = bi.next();
            if (b.toBeRemoved) {
                bi.remove();
            }
        }
    }

    RectF rect = new RectF();

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        canvas.getClipBounds(bounds);

        paint.setColor(Color.WHITE);
        canvas.drawRect(bounds, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(100);

        if (balls.isEmpty()) {
            canvas.drawText("Touch to play", 200, 300, paint);
            canvas.drawText(formatElapsed(), 500, 200, paint);
        } else {
            canvas.drawText("" + balls.size(), 200, 200, paint);
            end = System.currentTimeMillis();
            canvas.drawText(formatElapsed(), 500, 200, paint);
            for (Ball b : balls) {
                b.draw(canvas, paint, rect);
            }
        }
    }

    private String formatElapsed() {
        long e = end - start;
        return String.format("%02d:%02d", e/1000, (e%1000)/10);
    }

}
