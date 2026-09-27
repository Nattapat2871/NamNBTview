/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 */
package org.hohigamer.nbtviewer.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public final class SnbtHighlighter {
    private SnbtHighlighter() {
    }

    public static List<Component> format(String snbt, boolean singleLine, int maxLineLen, int indentSpaces, int maxTotalChars, Colors colors) {
        if (snbt == null) {
            snbt = "";
        }
        if (((String)snbt).length() > maxTotalChars) {
            snbt = ((String)snbt).substring(0, maxTotalChars) + "... (truncated)";
        }
        List<Token> tokens = SnbtHighlighter.tokenize((String)snbt);
        Style labelStyle = SnbtHighlighter.style(colors.label());
        ArrayList<Component> out = new ArrayList<Component>();
        if (singleLine) {
            MutableComponent line = Component.empty().append((Component)Component.literal((String)"NBT: ").setStyle(labelStyle)).append((Component)SnbtHighlighter.renderLine(tokens, colors));
            out.add((Component)line);
            return out;
        }
        out.add((Component)Component.literal((String)"NBT:").setStyle(labelStyle));
        List<List<Token>> pretty = SnbtHighlighter.prettyPrint(tokens, indentSpaces);
        for (List<Token> lineTokens : pretty) {
            for (List<Token> chunk : SnbtHighlighter.wrap(lineTokens, maxLineLen)) {
                out.add((Component)SnbtHighlighter.renderLine(chunk, colors));
            }
        }
        return out;
    }

    private static MutableComponent renderLine(List<Token> tokens, Colors colors) {
        MutableComponent line = Component.empty();
        for (Token t : tokens) {
            line.append((Component)Component.literal((String)t.text).setStyle(SnbtHighlighter.styleFor(t.type, colors)));
        }
        return line;
    }

    private static Style styleFor(TokenType t, Colors c) {
        return switch (t.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> SnbtHighlighter.style(c.key());
            case 1 -> SnbtHighlighter.style(c.string());
            case 2 -> SnbtHighlighter.style(c.number());
            case 3 -> SnbtHighlighter.style(c.bool());
            case 5 -> SnbtHighlighter.style(c.punct());
            case 4 -> SnbtHighlighter.style(c.ident());
            case 6 -> SnbtHighlighter.style(c.punct());
        };
    }

    private static Style style(int rgb) {
        return Style.EMPTY.withColor(TextColor.fromRgb((int)(rgb & 0xFFFFFF)));
    }

    private static List<List<Token>> prettyPrint(List<Token> tokens, int indentSpaces) {
        ArrayList<Token> tks = new ArrayList<Token>();
        for (Token t : tokens) {
            if (t.type == TokenType.WS) continue;
            tks.add(t);
        }
        ArrayList<List<Token>> lines = new ArrayList<List<Token>>();
        List[] cur = new List[]{new ArrayList()};
        int depth = 0;
        Runnable pushLine = () -> {
            if (!cur[0].isEmpty()) {
                lines.add(cur[0]);
            }
            cur[0] = new ArrayList();
        };
        Consumer<Integer> addIndent = d -> {
            if (indentSpaces <= 0) {
                return;
            }
            cur[0].add(new Token(" ".repeat(d * indentSpaces), TokenType.PUNCT));
        };
        addIndent.accept(0);
        for (int i = 0; i < tks.size(); ++i) {
            Token next;
            Token t = (Token)tks.get(i);
            String txt = t.text;
            Token token = next = i + 1 < tks.size() ? (Token)tks.get(i + 1) : null;
            if (t.type == TokenType.PUNCT) {
                if (txt.equals("{") || txt.equals("[")) {
                    if (next != null && next.type == TokenType.PUNCT && (txt.equals("{") && next.text.equals("}") || txt.equals("[") && next.text.equals("]"))) {
                        cur[0].add(t);
                        cur[0].add(next);
                        ++i;
                        continue;
                    }
                    cur[0].add(t);
                    pushLine.run();
                    addIndent.accept(++depth);
                    continue;
                }
                if (txt.equals("}") || txt.equals("]")) {
                    pushLine.run();
                    depth = Math.max(0, depth - 1);
                    addIndent.accept(depth);
                    cur[0].add(t);
                    continue;
                }
                if (txt.equals(",")) {
                    cur[0].add(t);
                    pushLine.run();
                    addIndent.accept(depth);
                    continue;
                }
                if (txt.equals(":")) {
                    cur[0].add(t);
                    cur[0].add(new Token(" ", TokenType.PUNCT));
                    continue;
                }
            }
            cur[0].add(t);
        }
        if (!cur[0].isEmpty()) {
            lines.add(cur[0]);
        }
        return lines;
    }

    private static List<List<Token>> wrap(List<Token> tokens, int maxChars) {
        if (maxChars <= 0) {
            return List.of(tokens);
        }
        ArrayList<List<Token>> out = new ArrayList<List<Token>>();
        ArrayList<Token> cur = new ArrayList<Token>();
        int len = 0;
        for (Token t : tokens) {
            int add = t.text.length();
            if (len > 0 && len + add > maxChars) {
                out.add(cur);
                cur = new ArrayList();
                len = 0;
            }
            cur.add(t);
            len += add;
        }
        if (!cur.isEmpty()) {
            out.add(cur);
        }
        return out;
    }

    private static List<Token> tokenize(String s) {
        ArrayList<Token> out = new ArrayList<Token>();
        int i = 0;
        while (i < s.length()) {
            boolean isKey;
            int k;
            char cj;
            int j;
            char c = s.charAt(i);
            if (Character.isWhitespace(c)) {
                for (j = i + 1; j < s.length() && Character.isWhitespace(s.charAt(j)); ++j) {
                }
                out.add(new Token(s.substring(i, j), TokenType.WS));
                i = j;
                continue;
            }
            if (c == '{' || c == '}' || c == '[' || c == ']' || c == ':' || c == ',') {
                out.add(new Token(String.valueOf(c), TokenType.PUNCT));
                ++i;
                continue;
            }
            if (c == '\"') {
                int k2;
                boolean escaped = false;
                for (j = i + 1; j < s.length(); ++j) {
                    char cj2 = s.charAt(j);
                    if (escaped) {
                        escaped = false;
                        continue;
                    }
                    if (cj2 == '\\') {
                        escaped = true;
                        continue;
                    }
                    if (cj2 != '\"') continue;
                    ++j;
                    break;
                }
                String txt = s.substring(i, Math.min(j, s.length()));
                for (k2 = i = Math.min(j, s.length()); k2 < s.length() && Character.isWhitespace(s.charAt(k2)); ++k2) {
                }
                if (k2 < s.length() && s.charAt(k2) == ':') {
                    out.add(new Token(txt, TokenType.KEY));
                    continue;
                }
                out.add(new Token(txt, TokenType.STRING));
                continue;
            }
            for (j = i; j < s.length() && !Character.isWhitespace(cj = s.charAt(j)) && cj != '{' && cj != '}' && cj != '[' && cj != ']' && cj != ':' && cj != ','; ++j) {
            }
            String txt = s.substring(i, j);
            for (k = i = j; k < s.length() && Character.isWhitespace(s.charAt(k)); ++k) {
            }
            boolean bl = isKey = k < s.length() && s.charAt(k) == ':';
            if (isKey) {
                out.add(new Token(txt, TokenType.KEY));
                continue;
            }
            String lower = txt.toLowerCase();
            if (lower.equals("true") || lower.equals("false")) {
                out.add(new Token(txt, TokenType.BOOLEAN));
                continue;
            }
            if (SnbtHighlighter.looksLikeNumber(txt)) {
                out.add(new Token(txt, TokenType.NUMBER));
                continue;
            }
            out.add(new Token(txt, TokenType.IDENT));
        }
        return out;
    }

    private static boolean looksLikeNumber(String t) {
        return t.matches("[-+]?\\d+(\\.\\d+)?([eE][-+]?\\d+)?[bBsSlLfFdD]?");
    }

    public record Colors(int label, int key, int string, int number, int bool, int punct, int ident) {
    }

    private record Token(String text, TokenType type) {
    }

    private static enum TokenType {
        KEY,
        STRING,
        NUMBER,
        BOOLEAN,
        IDENT,
        PUNCT,
        WS;

    }
}
