package shapes;

import org.apache.commons.lang3.tuple.Pair;

public class HillShape extends AbstractShape {
    public HillShape(Pair<Integer, Integer> shapeCenter) {
        this.positions = new int[][][] {

                { { 1, 1, 1 },
                  { 0, 1, 0 } },

                { { 0, 1, 0 },
                  { 0, 1, 1 },
                  { 0, 1, 0 } },

                { { 1, 1, 1 },
                  { 0, 1, 0 } },

                { { 0, 1, 0 },
                  { 1, 1, 0 },
                  { 0, 1, 0 } }
        };
        this.currPosition = positions[0];
        this.currPositionPointer = 0;
        setCoordinates(shapeCenter);
    }

    @Override
    public String getColor() {
        return "\u001b[44;1m";
    }
}
