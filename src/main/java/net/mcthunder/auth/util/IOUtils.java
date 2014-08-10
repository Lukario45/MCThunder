package net.mcthunder.auth.util;

import java.io.*;

public class IOUtils {
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    public static void closeQuietly(Closeable close) {
        try {
            if (close != null)
                close.close();
        } catch (IOException e) {
        }
    }

    public static String toString(InputStream input, String encoding) throws IOException {
        StringWriter writer = new StringWriter();
        InputStreamReader in = encoding != null ? new InputStreamReader(input, encoding) : new InputStreamReader(input);
        char[] buffer = new char[4096];
        int n = 0;
        while (-1 != (n = in.read(buffer))) {
            writer.write(buffer, 0, n);
        }

        in.close();
        return writer.toString();
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = in.read(buffer))) {
            out.write(buffer, 0, n);
        }

        in.close();
        out.close();
        return out.toByteArray();
    }
}