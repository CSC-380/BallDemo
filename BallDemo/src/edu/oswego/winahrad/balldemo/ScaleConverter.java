package edu.oswego.winahrad.balldemo;

public class ScaleConverter {

    private final float metersPerPixel;

    public ScaleConverter(float metersPerPixel) {
        this.metersPerPixel = metersPerPixel;
    }

    public float getScale() {
        return metersPerPixel;
    }

    public float metersToPixels(float meters) {
        return meters / metersPerPixel;
    }

    public float pixelsToMeters(float pixels) {
        return pixels * metersPerPixel;
    }
}
