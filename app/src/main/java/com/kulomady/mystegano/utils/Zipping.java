package com.kulomady.mystegano.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author kulomady on 6/8/18.
 */


public class Zipping {

    final static String TAG = Zipping.class.getName();

    /*
    @parameter : Encrypted message {String}
    @return : Compressed byte array
     */

    public static byte[] compress(String string) throws Exception {

        ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());

        GZIPOutputStream gos = new GZIPOutputStream(os);

        gos.write(string.getBytes());
        gos.close();

        byte[] compressed = os.toByteArray();
        os.close();

        return compressed;
    }


    /*
    @parameter : byte array
    @return : Uncompressed encrypted_message {String}
     */
    public static String decompress(byte[] compressed) throws Exception {

        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);

        GZIPInputStream gis = new GZIPInputStream(bis);

        BufferedReader br = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));

        StringBuilder sb = new StringBuilder();

        String line;

        while((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        gis.close();
        bis.close();

        return sb.toString();
    }

}
