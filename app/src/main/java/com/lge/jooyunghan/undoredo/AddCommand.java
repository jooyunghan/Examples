package com.lge.jooyunghan.undoredo;

/**
* Created by jooyung.han on 2014-09-26.
*/
class AddCommand implements Command {
    private Shapes shapes;
    private final Shape shape;

    public AddCommand(Shapes shapes, Shape shape) {
        this.shapes = shapes;
        this.shape = shape;
    }

    @Override
    public void execute() {
        shapes.add(shape);
    }

    @Override
    public void undo() {
        shapes.remove(shape);
    }
}
