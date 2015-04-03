package de.bit3.jsass.type;

public class SassColor {
    private double red = 0;
    private double green = 0;
    private double blue = 0;
    private double alpha = 1;

    public SassColor() {
    }

    public SassColor(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public SassColor(double red, double green, double blue, double alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public double getRed() {
        return red;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public double getGreen() {
        return green;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public double getBlue() {
        return blue;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public String toString() {
        return String.format("rgba(%f,%f,%f,%f)", red, green, blue, alpha);
    }
}
