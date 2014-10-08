package com.lge.jooyunghan.undoredo;

/**
* Created by jooyung.han on 2014-09-26.
*/
class RemoveCommand implements Command {

    private Shapes shapes;
    private final int index;
    private final Shape shape;

    public RemoveCommand(Shapes shapes, int index) {
        this.shapes = shapes;
        this.index = index;
        this.shape = shapes.get(index);
    }

    @Override
    public void execute() {
        shapes.remove(index);
    }

    @Override
    public void undo() {
        shapes.add(index, shape);
    }
}
