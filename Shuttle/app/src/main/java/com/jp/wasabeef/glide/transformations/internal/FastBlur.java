package com.jp.wasabeef.glide.transformations.internal;

import android.graphics.Bitmap;

/**
 * Copyright (C) 2015 Wasabeef
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class FastBlur {

    private FastBlur() {

    }

    public static Bitmap blur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap = prepareBitmap(sentBitmap, canReuseInBitmap);
    
        if (radius < 1) {
            return null;
        }
    
        int[] pix = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pix, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
    
        int[] blurredPix = applyBlur(pix, bitmap.getWidth(), bitmap.getHeight(), radius);
    
        bitmap.setPixels(blurredPix, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
    
        return bitmap;
    }
    
    private static Bitmap prepareBitmap(Bitmap sentBitmap, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }
        return bitmap;
    }

    private static int[] applyBlur(int[] pix, int width, int height, int radius) {
        int[] blurredPix = new int[pix.length];
    
        int[] gaussMatrix = createGaussianMatrix(radius);
    
        int gaussSum = 0;
        for (int i = 0; i < gaussMatrix.length; i++) {
            gaussSum += gaussMatrix[i];
        }
    
        int gaussIndex = 0;
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int red = 0;
                int green = 0;
                int blue = 0;
    
                gaussIndex = 0;
    
                for (int row = -radius; row <= radius; row++) {
                    int currentY = Math.min(height - 1, Math.max(0, y + row)) * width;
    
                    for (int col = -radius; col <= radius; col++) {
                        int currentPixel = Math.min(width - 1, Math.max(0, x + col));
    
                        int currentGauss = gaussMatrix[gaussIndex];
                        int currentColor = pix[currentY + currentPixel];
    
                        red += Color.red(currentColor) * currentGauss;
                        green += Color.green(currentColor) * currentGauss;
                        blue += Color.blue(currentColor) * currentGauss;
    
                        gaussIndex++;
                    }
                }
    
                red /= gaussSum;
                green /= gaussSum;
                blue /= gaussSum;
    
                blurredPix[y * width + x] = Color.rgb(red, green, blue);
            }
        }
    
        return blurredPix;
    }
    
    private static int[] createGaussianMatrix(int radius) {
        int size = radius * 2 + 1;
        int[] gaussMatrix = new int[size];
    
        double sigma = radius / 3.0;
        double sigma22 = 2 * sigma * sigma;
        double sqrtPiSigma22 = Math.sqrt(Math.PI * sigma22);
        double radius2 = (double) radius * radius;
    
        int index = 0;
        int total = 0;
    
        for (int row = -radius; row <= radius; row++) {
            int distance = row * row;
            if (distance > radius2) {
                gaussMatrix[index] = 0;
            } else {
                gaussMatrix[index] = (int) (Math.exp(-(distance) / sigma22) / sqrtPiSigma22 * 255);
            }
            total += gaussMatrix[index];
            index++;
        }
    
        return gaussMatrix;
    }
    
    
}