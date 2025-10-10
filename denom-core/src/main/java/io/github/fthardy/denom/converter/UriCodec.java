package io.github.fthardy.denom.converter;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * A utility class providing methods for encoding and decoding a string in a RFC-3986-conform representation aka percent-encoding/decoding (UTF-8).
 * <p>
 * NOTE:
 * <ul>
 *     <li>Characters which are "unreserved" and in the set of safe symbols are NOT going to be encoded.</li>
 *     <li>All letters (lower- and upper case) and all digits are unreserved as well as the characters {@code -._~}.</li>
 *     <li>The following characters are safe-symbols: {@code !$&'()*+,;=:@[]}</li>
 *     <li>However, there is the opportunity to exclude characters from the safe-symbol set by defining a set of non-safe symbols.</li>
 *     <li>"+" remains a plus (no space!), as this is form-specific (application/x-www-form-urlencoded), but not part of the general URI specification.</li>
 *     <li>A raw '%' is always encoded as "%25" (no "smart" preservation of already encoded sequences), thus the result is canonical and free of ambiguous
 *     percentage sequences.</li>
 * </ul>
 * </p>
 */
public final class UriCodec {

    private UriCodec() {}

    /**
     * Encodes a given string sequence into a RFC3986-conform (percent-notation, UTF-8) representation.
     *
     * @param toEncode the string to be encoded.
     *
     * @return the encoded string.
     */
    public static String encodeUriConform(String toEncode) {
        return encodeUriConform(toEncode, null);
    }

    /**
     * Encodes a given string sequence into a RFC3986-conform (percent-notation, UTF-8) representation.
     *
     * @param toEncode the string to be encoded.
     * @param nonSafeChars a string containing the non-safe characters (these are going to be encoded).
     *
     * @return the encoded string.
     */
    public static String encodeUriConform(String toEncode, String nonSafeChars) {
        if (toEncode == null || toEncode.isEmpty()) {
            return toEncode;
        }

        StringBuilder strBuilder = new StringBuilder(toEncode.length() + 16);
        for (int cp : toEncode.codePoints().toArray()) {
            List<Integer> excludedSafeSymbols = nonSafeChars == null || nonSafeChars.isEmpty() ? //
                    Collections.emptyList() : nonSafeChars.codePoints().boxed().distinct().toList();

            if ((isUnreserved(cp) || isSafeSymbol(cp)) && !excludedSafeSymbols.contains(cp)) {
                strBuilder.appendCodePoint(cp);
            } else { // UTF-8 bytewise percent-encoding
                for (byte b : new String(Character.toChars(cp)).getBytes(StandardCharsets.UTF_8)) {
                    strBuilder.append('%');
                    String hex = Integer.toHexString(b & 0xFF).toUpperCase();
                    if (hex.length() == 1) {
                        strBuilder.append('0');
                    }
                    strBuilder.append(hex);
                }
            }
        }

        return strBuilder.toString();
    }

    /**
     * Decodes a given RFC-3986 (percent-notation, UTF-8) encoded string back into its "normal" string representation.
     * <p>
     * Only valid %HH-sequences are accepted. Otherwise, an {@code IllegalArgumentException} is thrown.
     * </p>
     *
     * @param toDecode the encoded string to be decoded.
     *
     * @return the decoded representation of the input string.
     */
    public static String decodeUriConform(String toDecode) {
        if (toDecode == null || toDecode.isEmpty()) {
            return toDecode;
        }

        StringBuilder strBuilder = new StringBuilder(toDecode.length());
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        for (int i = 0; i < toDecode.length(); ) {
            char c = toDecode.charAt(i);
            if (c == '%') { // collect consecutive %HH-sequences as bytes
                buffer.reset();
                while (i < toDecode.length() && toDecode.charAt(i) == '%') {
                    if (i + 2 >= toDecode.length()) {
                        throw new IllegalArgumentException("Invalid percent encoding at index " + i);
                    }
                    buffer.write(hexToByte(toDecode.charAt(i + 1), toDecode.charAt(i + 2)));
                    i += 3;
                }
                strBuilder.append(buffer.toString(StandardCharsets.UTF_8));
            } else {
                strBuilder.append(c);
                i++;
            }
        }
        return strBuilder.toString();
    }

    private static boolean isUnreserved(int cp) {
        return (cp >= 'a' && cp <= 'z') || (cp >= 'A' && cp <= 'Z') || (cp >= '0' && cp <= '9') || cp == '-' || cp == '.' || cp == '_' || cp == '~';
    }

    /**
     * Additional characters which are not going to be modified:
     * - sub-delims: "! $ & ' ( ) * + , ; ="
     * - used separator/context characters: ":" "@"
     * - square brackets and commas for your notation: "[" "]" ","
     */
    private static boolean isSafeSymbol(int cp) {
        return switch (cp) { // is NOT going to remain as safe; encodeUriConform encoded '%' into %25
            case '!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=', ':', '@', '[', ']' -> true;
            default -> false;
        };
    }

    private static int hexToByte(char h1, char h2) {
        final int hi = Character.digit(h1, 16);
        final int lo = Character.digit(h2, 16);
        if (hi < 0 || lo < 0) {
            throw new IllegalArgumentException("Invalid hex digits: '" + h1 + h2 + "'!");
        }
        return (hi << 4) | lo;
    }
}
