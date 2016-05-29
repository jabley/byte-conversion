/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

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