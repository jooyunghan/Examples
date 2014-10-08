package com.lge.jooyunghan.undoredo;

import android.util.Pair;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class UndoRedo {
    private ArrayList<Command> undos = new ArrayList<Command>();
    private ArrayList<Command> redos = new ArrayList<Command>();

    private final BehaviorSubject<Boolean> undoableSubject = BehaviorSubject.create(false);
    public final Observable<Boolean> undoable = undoableSubject.asObservable();
    private final BehaviorSubject<Boolean> redoableSubject = BehaviorSubject.create(false);
    public final Observable<Boolean> redoable = redoableSubject.asObservable();

    void doCommand(Command command) {
        redos.clear();
        undos.add(command);
        command.execute();
        update();
    }

    private void update() {
        redoableSubject.onNext(!redos.isEmpty());
        undoableSubject.onNext(!undos.isEmpty());
    }

    void undo() {
        Command command = undos.remove(undos.size() - 1);
        command.undo();
        redos.add(command);
        update();
    }

    void redo() {
        Command command = redos.remove(redos.size() - 1);
        command.execute();
        undos.add(command);
        update();
    }
}