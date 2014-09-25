package com.lge.jooyunghan.undoredo;

/**
 * Created by jooyung.han on 2014-09-25.
 */
interface Command {
    void execute();

    void undo();
}
