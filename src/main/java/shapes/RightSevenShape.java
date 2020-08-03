package shapes;


import org.apache.commons.lang3.tuple.Pair;

public class RightSevenShape extends AbstractShape {

    public RightSevenShape(Pair<Integer, Integer> shapeCenter) {
        this.positions = new int[][][] {
                { { 0, 1, 1 },
                  { 0, 1, 0 },
                  { 0, 1, 0 } },

                { { 1, 1, 1 },
                  { 0, 0, 1 } },

                { { 0, 1, 0 },
                  { 0, 1, 0 },
                  { 1, 1, 0 } },

                { { 1, 0, 0 },
                  { 1, 1, 1 } }
        };
        this.currPosition = positions[0];
        this.currPositionPointer = 0;
        setCoordinates(shapeCenter);
    }

    @Override
    public String getColor() {
        return "\u001b[45;1m";
    }
}
