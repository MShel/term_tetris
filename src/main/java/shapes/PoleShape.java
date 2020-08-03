package shapes;


import org.apache.commons.lang3.tuple.Pair;

public class PoleShape extends AbstractShape {

    public PoleShape(Pair<Integer, Integer> shapeCenter) {
        this.positions = new int[][][] {
                { { 1 },
                  { 1 },
                  { 1 },
                  { 1 }
                },
                {
                  { 1,1,1,1 }
                }
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
