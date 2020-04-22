package com.mete.template;

import java.nio.file.Path;

public class FingerPath {
    public int colour;
    public int strokewidth;
    public Path path;

    public FingerPath(int colour, int strokewidth, Path path) {
        this.colour = colour;
        this.strokewidth = strokewidth;
        this.path = path;
    }
}
