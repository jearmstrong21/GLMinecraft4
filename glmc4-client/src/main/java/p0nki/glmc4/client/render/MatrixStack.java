package p0nki.glmc4.client.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Stack;
import java.util.function.Consumer;

public final class MatrixStack {

    private final Stack<Matrix4f> stack = new Stack<>();

    private final Consumer<Matrix4f> onChange;

    public MatrixStack(Consumer<Matrix4f> onChange) {
        this.onChange = onChange;
        stack.push(new Matrix4f());
    }

    private void changed() {
        onChange.accept(stack.peek());
    }

    public void push() {
        stack.push(new Matrix4f(stack.peek()));
        changed();
    }

    public void pop() {
        stack.pop();
        changed();
    }

    public Matrix4f get() {
        return stack.peek();
    }

    public void lookAt(Vector3f direction) {
        stack.push(new Matrix4f(stack.pop()).mul(new Matrix4f().rotateTowards(direction, new Vector3f(0, 1, 0))));
        changed();
    }

    public void translate(Vector3f position) {
        stack.push(new Matrix4f(stack.pop()).translate(position));
        changed();
    }

    public void translate(float x, float y, float z) {
        stack.push(new Matrix4f(stack.pop()).translate(x, y, z));
        changed();
    }

    public void scale(float amount) {
        scale(amount, amount, amount);
        changed();
    }

    public void scale(float x, float y, float z) {
        stack.push(new Matrix4f(stack.pop()).scale(x, y, z));
        changed();
    }

}
