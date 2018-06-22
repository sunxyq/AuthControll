package com.sf.arch.udata.privilege.common.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Encryptor {
    private static final Logger logger = LogManager.getLogger(Encryptor.class);

    final static String key = "udatakey";

    public static String RC4(String aInput, String aKey) {
        int[] iS = new int[256];
        byte[] iK = new byte[256];

        for (int i = 0; i < 256; i++)
            iS[i] = i;

        int j = 1;

        for (short i = 0; i < 256; i++) {
            iK[i] = (byte) aKey.charAt((i % aKey.length()));
        }

        j = 0;

        for (int i = 0; i < 255; i++) {
            j = (j + iS[i] + iK[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
        }


        int i = 0;
        j = 0;
        char[] iInputChar = aInput.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for (short x = 0; x < iInputChar.length; x++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
            int t = (iS[i] + (iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char) iY;
            iOutputChar[x] = (char) (iInputChar[x] ^ iCY);
        }

        return new String(iOutputChar);

    }


    public static String encode(String text) {
        logger.info("information before encode: " + text);
        String information = RC4(text, key);
        char c;
        String string;
        String out = "";
        int temp;
        for (int i = 0; i < information.length(); i++) {
            c = information.charAt(i);
            temp = (int) c;
            string = Integer.toString(temp);
            for (int j = string.length(); j < 3; j++) {
                string = "0" + string;
            }
            out += string;
        }
        logger.info("information after encode: " + out);
        return out;
    }

    public static String decode(String text) {
        logger.info("information before decode: " + text);
        char c;
        String string;
        String information = "";
        for (int i = 0; i < text.length(); i+=3) {
            string = text.substring(i, i+3);
            c = (char)Integer.parseInt(string);
            information += c;
        }
        information = RC4(information, key);
        logger.info("information after decode: " + information);
        return information;
    }
}
