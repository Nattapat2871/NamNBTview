/*
 * Decompiled with CFR 0.152.
 */
package org.hohigamer.nbtviewer;

public final class NbtviewerConfig {
    public boolean shiftOnly = true;
    public boolean mobNbtOverlay = false;
    public boolean showHintLine = true;
    public boolean singleLine = false;
    public int maxLineLen = 120;
    public int indentSpaces = 2;
    public int maxTotalChars = 4096;
    public boolean autoScale = true;
    public double nbtScale = 1.0;
    public boolean copyPopup = true;
    public boolean copySfx = true;
    public boolean enabled = true;
    public String colorLabel = "#AAAAAA";
    public String colorKey = "#55FFFF";
    public String colorString = "#55FF55";
    public String colorNumber = "#FFAA00";
    public String colorBoolean = "#FF55FF";
    public String colorPunctuation = "#FFFFFF";
    public String colorIdentifier = "#FFFFFF";
    public String colorHint = "#DADADA";

    public static int parseRgb(String input, int fallback) {
        if (input == null) {
            return fallback;
        }
        String s = input.trim();
        if (s.startsWith("#")) {
            s = s.substring(1);
        }
        if (s.startsWith("0x") || s.startsWith("0X")) {
            s = s.substring(2);
        }
        if (s.length() == 8) {
            s = s.substring(2);
        }
        if (s.length() != 6) {
            return fallback;
        }
        try {
            return Integer.parseInt(s, 16) & 0xFFFFFF;
        }
        catch (NumberFormatException e) {
            return fallback;
        }
    }
}
