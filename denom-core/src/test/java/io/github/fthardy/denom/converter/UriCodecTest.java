package io.github.fthardy.denom.converter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UriCodecTest {

    @Test
    void null_and_empty_is_passed_back() {
        assertThat(UriCodec.encodeUriConform(null)).isNull();
        assertThat(UriCodec.decodeUriConform(null)).isNull();
        assertThat(UriCodec.encodeUriConform("")).isEmpty();
        assertThat(UriCodec.decodeUriConform("")).isEmpty();
    }

    @Test
    void encode_decode__No_safe_symbol_exclusion__Nothing_to_encode() {
        String allUnreservedAndSafeSymbos = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!$&'()*+,;=:@[]-_.~";
        assertThat(UriCodec.decodeUriConform(UriCodec.encodeUriConform(allUnreservedAndSafeSymbos))).isEqualTo(allUnreservedAndSafeSymbos);
    }

    @Test
    void encode_decode__Safe_symbol_exclusion() {
        String allUnreservedAndSafeSymbos = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!$&'()*+,;=:@[]-_.~";
        String encoded = UriCodec.encodeUriConform(allUnreservedAndSafeSymbos, "()");
        assertThat(encoded).isEqualTo("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!$&'%28%29*+,;=:@[]-_.~");
        assertThat(UriCodec.decodeUriConform(encoded)).isEqualTo(allUnreservedAndSafeSymbos);
    }

    @Test
    void no_safe_symbol_exclusion__Encode_3_in_a_row() {
        String toEncode = "abc§?XYZ123";
        assertThat(UriCodec.decodeUriConform(UriCodec.encodeUriConform(toEncode))).isEqualTo(toEncode);
    }

    @Test
    void decode_and_encode() {
        String encoded = "abc%0Fxyz";
        assertThat(UriCodec.encodeUriConform(UriCodec.decodeUriConform(encoded))).isEqualTo(encoded);
    }

    @Test
    void decode__Invalid_hex_encoding() {
        assertThrows(IllegalArgumentException.class, () -> UriCodec.decodeUriConform("abc%xy"));
    }

    @Test
    void decode__Invalid_hex_encoding_at_end() {
        assertThrows(IllegalArgumentException.class, () -> UriCodec.decodeUriConform("abc%0"));
    }
}