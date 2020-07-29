package shapes;

public class BottomShape implements Shape {
    @Override
    public String getColor() {
        return "\u001b[95;1m";
    }

    @Override
    public String getSymbol() {
        return "=";
    }

    @Override
    public boolean isWall() {
        return false;
    }

    @Override
    public boolean isBottom() {
        return true;
    }

    @Override
    public boolean isCeiling() {
        return false;
    }
}
