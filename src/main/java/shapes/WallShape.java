package shapes;

public class WallShape implements Shape {
    @Override
    public String getColor() {
        return "\u001b[95;1m";
    }

    @Override
    public String getSymbol() {
        return "|";
    }

    @Override
    public boolean isWall() {
        return true;
    }

    @Override
    public boolean isBottom() {
        return false;
    }

    @Override
    public boolean isCeiling() {
        return false;
    }
}
