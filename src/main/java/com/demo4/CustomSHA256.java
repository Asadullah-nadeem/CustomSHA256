package com.demo4;

public class CustomSHA256 {

    private static final int[] INITIAL_HASH = {
            0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
            0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
    };

    private static final int[] K = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    private static int rightRotate(int value, int shift) {
        return (value >>> shift) | (value << (32 - shift));
    }

    private static byte[] preprocess(byte[] message) {
        int originalBitLength = message.length * 8;
        int newLength = originalBitLength + 1 + 64;
        int blocks = (newLength + 511) / 512;
        int totalLength = blocks * 512 / 8;

        byte[] padded = new byte[totalLength];
        System.arraycopy(message, 0, padded, 0, message.length);
        padded[message.length] = (byte) 0x80; // Append '1' bit

        for (int i = 0; i < 8; i++) {
            padded[padded.length - 8 + i] = (byte) (originalBitLength >>> (56 - i * 8));
        }

        return padded;
    }

    public static String hash(byte[] input) {
        byte[] padded = preprocess(input);
        int[] hash = INITIAL_HASH.clone();

        for (int i = 0; i < padded.length; i += 64) {
            int[] block = new int[64];
            for (int j = 0; j < 16; j++) {
                block[j] = ((padded[i + j*4] & 0xFF) << 24) |
                        ((padded[i + j*4 + 1] & 0xFF) << 16) |
                        ((padded[i + j*4 + 2] & 0xFF) << 8) |
                        (padded[i + j*4 + 3] & 0xFF);
            }

            for (int j = 16; j < 64; j++) {
                int s0 = rightRotate(block[j-15], 7) ^ rightRotate(block[j-15], 18) ^ (block[j-15] >>> 3);
                int s1 = rightRotate(block[j-2], 17) ^ rightRotate(block[j-2], 19) ^ (block[j-2] >>> 10);
                block[j] = block[j-16] + s0 + block[j-7] + s1;
            }

            int a = hash[0], b = hash[1], c = hash[2], d = hash[3],
                    e = hash[4], f = hash[5], g = hash[6], h = hash[7];

            for (int j = 0; j < 64; j++) {
                int S1 = rightRotate(e, 6) ^ rightRotate(e, 11) ^ rightRotate(e, 25);
                int ch = (e & f) ^ (~e & g);
                int temp1 = h + S1 + ch + K[j] + block[j];
                int S0 = rightRotate(a, 2) ^ rightRotate(a, 13) ^ rightRotate(a, 22);
                int maj = (a & b) ^ (a & c) ^ (b & c);
                int temp2 = S0 + maj;

                h = g;
                g = f;
                f = e;
                e = d + temp1;
                d = c;
                c = b;
                b = a;
                a = temp1 + temp2;
            }
            hash[0] += a;
            hash[1] += b;
            hash[2] += c;
            hash[3] += d;
            hash[4] += e;
            hash[5] += f;
            hash[6] += g;
            hash[7] += h;
        }

        StringBuilder hexString = new StringBuilder();
        for (int value : hash) {
            hexString.append(String.format("%08x", value));
        }
        return hexString.toString();
    }
}