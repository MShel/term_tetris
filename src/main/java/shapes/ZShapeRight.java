package shapes;

import org.apache.commons.lang3.tuple.Pair;

public class ZShapeRight extends AbstractShape {


    public ZShapeRight(Pair<Integer, Integer> shapeCenter) {
        this.positions = new int[][][] { {
            { 1, 1, 0 },
            { 0, 1, 1 } }, {
            /////
            {  0, 1 },
            {  1, 1 },
            {  1, 0 } } };
        currPosition = positions[0];
        setCoordinates(shapeCenter);
    }

    @Override
    public String getColor() {
        return "\u001b[41;1m";
    }
}
