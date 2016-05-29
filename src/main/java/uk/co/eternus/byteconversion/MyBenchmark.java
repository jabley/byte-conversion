package uk.co.eternus.byteconversion;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.openjdk.jmh.annotations.Benchmark;

public class MyBenchmark {

    @Benchmark
    public void testByte2Char2Byte() throws IOException {
        // This is a demo/sample template for building your JMH benchmarks. Edit as needed.
        // Put your benchmark code here.
        StringBuilder buf = new StringBuilder();

        try (InputStream is = MyBenchmark.class.getResourceAsStream("/test.json")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line);
            }
            reader.close();

            final InputStream stream = new ByteArrayInputStream(buf.toString().getBytes());
            reader = new BufferedReader(new InputStreamReader(stream));

            copy(reader, new NullWriter());
        } catch (IOException ignore) {}

    }

    private void copy(Reader in , Writer out) throws IOException {
        char[] buffer = new char[1024];
        int len = in .read(buffer);
        while (len != -1) {
            out.write(buffer, 0, len);
            len = in .read(buffer);
        } in .close();
        out.close();
    }

    @Benchmark
    public void withoutNeedlessConversions() {
        try (InputStream is = MyBenchmark.class.getResourceAsStream("/test.json")) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            copy(reader, new NullWriter());

        } catch (IOException ignore) {}
    }

    private static class NullWriter extends Writer {

        @Override
        public void close() throws IOException {}

        @Override
        public void flush() throws IOException {}

        @Override
        public void write(char[] cbuf, int off, int len) {}

    }
}