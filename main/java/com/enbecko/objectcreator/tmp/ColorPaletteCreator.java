package com.enbecko.objectcreator.tmp;

import org.lwjgl.util.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Niclas on 22.11.2016.
 */
public class ColorPaletteCreator {

    private static int power = 6;

    public static void main(String[] args) {
        BufferedImage image = new BufferedImage((int) (Math.pow(2, power * 1.5F)), (int) (Math.pow(2, power * 1.5F)), BufferedImage.TYPE_INT_ARGB);
        int part = (int) Math.pow(2, power * 0.5F);
        int fullPart = (int) Math.pow(2, power);
        int mul = 256 / fullPart;
        for (int ww = 0; ww < part; ww++) {
            for (int hh = 0; hh < part; hh++) {
                for (int w = 0; w < fullPart; w++) {
                    for (int h = 0; h < fullPart; h++) {
                        image.setRGB(fullPart * ww + w, fullPart * hh + h, new Color(w * mul, h * mul, (ww + hh * part) * mul).getRGB());
                    }
                }
            }
        }
        try {
            File outputfile = new File("saved.png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
        }
    }

}
