package seamcarving;

import edu.princeton.cs.algs4.Picture;

public class DualGradientEnergyFunction implements EnergyFunction {
    @Override
    public double apply(Picture picture, int x, int y) {
        double deltaX;
        double deltaY;

        int deltaXRed;
        int deltaXGreen;
        int deltaXBlue;

        int deltaYRed;
        int deltaYGreen;
        int deltaYBlue;

        if (x < 0 || x > picture.width() - 1 || y < 0 || y > picture.height() - 1) {
            throw new IndexOutOfBoundsException();
        }

        if (x == 0) {
            deltaXRed = -3 * picture.get(x, y).getRed() + 4 * picture.get(x + 1, y).getRed()
                - picture.get(x + 2, y).getRed();
            deltaXGreen = -3 * picture.get(x, y).getGreen() + 4 * picture.get(x + 1, y).getGreen()
                - picture.get(x + 2, y).getGreen();
            deltaXBlue = -3 * picture.get(x, y).getBlue() + 4 * picture.get(x + 1, y).getBlue()
                - picture.get(x + 2, y).getBlue();
        } else if (x == picture.width() - 1) {
            deltaXRed = -3 * picture.get(x, y).getRed() + 4 * picture.get(x - 1, y).getRed()
                - picture.get(x - 2, y).getRed();
            deltaXGreen = -3 * picture.get(x, y).getGreen() + 4 * picture.get(x - 1, y).getGreen()
                - picture.get(x - 2, y).getGreen();
            deltaXBlue = -3 * picture.get(x, y).getBlue() + 4 * picture.get(x - 1, y).getBlue()
                - picture.get(x - 2, y).getBlue();
        } else {
            deltaXRed = picture.get(x - 1, y).getRed() - picture.get(x + 1, y).getRed();
            deltaXGreen = picture.get(x - 1, y).getGreen() - picture.get(x + 1, y).getGreen();
            deltaXBlue = picture.get(x - 1, y).getBlue() - picture.get(x + 1, y).getBlue();
        }
        deltaX = Math.pow(deltaXRed, 2) + Math.pow(deltaXGreen, 2) + Math.pow(deltaXBlue, 2);

        if (y == 0) {
            deltaYRed = -3 * picture.get(x, y).getRed() + 4 * picture.get(x, y + 1).getRed()
                - picture.get(x, y + 2).getRed();
            deltaYGreen = -3 * picture.get(x, y).getGreen() + 4 * picture.get(x, y + 1).getGreen()
                - picture.get(x, y + 2).getGreen();
            deltaYBlue = -3 * picture.get(x, y).getBlue() + 4 * picture.get(x, y + 1).getBlue()
                - picture.get(x, y + 2).getBlue();
        } else if (y == picture.height() - 1) {
            deltaYRed = -3 * picture.get(x, y).getRed() + 4 *picture.get(x, y - 1).getRed()
                - picture.get(x, y - 2).getRed();
            deltaYGreen = -3 * picture.get(x, y).getGreen() + 4 *picture.get(x, y - 1).getGreen()
                - picture.get(x, y - 2).getGreen();
            deltaYBlue = -3 * picture.get(x, y).getBlue() + 4 *picture.get(x, y - 1).getBlue()
                - picture.get(x, y - 2).getBlue();
        } else {
            deltaYRed = picture.get(x, y - 1).getRed() - picture.get(x, y + 1).getRed();
            deltaYGreen = picture.get(x, y - 1).getGreen() - picture.get(x, y + 1).getGreen();
            deltaYBlue = picture.get(x, y - 1).getBlue() - picture.get(x, y + 1).getBlue();
        }
        deltaY = Math.pow(deltaYRed, 2) + Math.pow(deltaYGreen, 2) + Math.pow(deltaYBlue, 2);

        return Math.sqrt(deltaX + deltaY);
    }
}
