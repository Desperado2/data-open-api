package com.github.desperado2.data.open.api.common.manage.config;


import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author tu nan
 * @date 2023/2/15
 **/
public class HttpServletResponseCopier extends HttpServletResponseWrapper {

    private int statusCode;
    private ServletOutputStreamCopier streamCopier;
    private PrintWriterCopier         writerCopier;
    private boolean                   useWriter;

    public HttpServletResponseCopier(HttpServletResponse response) throws IOException {
        super(response);
        useWriter = true;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writerCopier != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (streamCopier == null) {
            useWriter = false;
            streamCopier = new ServletOutputStreamCopier();
        }

        return streamCopier;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (streamCopier != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (writerCopier == null) {
            useWriter = true;
            writerCopier = new PrintWriterCopier();
        }

        return writerCopier.getWriter();
    }

    public byte[] getBytes() {
        if (streamCopier==null && writerCopier==null) {
            return new byte[0];
        }
        return useWriter ? writerCopier.getBytes() : streamCopier.getCopy();
    }

    public byte[] getStreamCopy() {
        if (useWriter) {
            throw new IllegalStateException("already use writer, please call getWriterCopy()");
        }

        return streamCopier==null ? new byte[0] : streamCopier.getCopy();
    }

    public char[] getWriterCopy() {
        if (!useWriter) {
            throw new IllegalStateException("already use outputStream, please call getStreamCopy()");
        }

        return writerCopier==null ? new char[0] : writerCopier.getCopy();
    }

    /**
     * The default behavior of this method is to call sendError(int sc, String msg)
     * on the wrapped response object.
     */
    public void sendError(int sc, String msg) throws IOException {
        ((HttpServletResponse)getResponse()).sendError(sc, msg);
        this.statusCode = sc;
    }

    /**
     * The default behavior of this method is to call sendError(int sc)
     * on the wrapped response object.
     */
    public void sendError(int sc) throws IOException {
        ((HttpServletResponse)getResponse()).sendError(sc);
        this.statusCode = sc;
    }

    public void setStatus(int sc) {
        super.setStatus(sc);
        this.statusCode = sc;
    }

    /**
     * The default behavior of this method is to call setStatus(int sc, String sm)
     * on the wrapped response object.
     */
    public void setStatus(int sc, String sm) {
        super.setStatus(sc, sm);
        this.statusCode = sc;
    }

    public int getStatus() {
        return statusCode;
    }

    public boolean isUseWriter() {
        return useWriter;
    }

    public class ServletOutputStreamCopier extends ServletOutputStream {
        private static final int INIT_BUFFER_SIZE = 1024;

        private ByteArrayOutputStream copy;

        public ServletOutputStreamCopier() {
            this.copy = new ByteArrayOutputStream(INIT_BUFFER_SIZE);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

        @Override
        public void write(int b) throws IOException {
            copy.write(b);
        }

        public byte[] getCopy() {
            return copy.toByteArray();
        }

        @Override
        public void flush() throws IOException {
            copy.flush();
        }
    }

        public class PrintWriterCopier {
            private CharArrayWriter charArrayWriter;
            private PrintWriter writer;

            public PrintWriterCopier() {
                this.charArrayWriter = new CharArrayWriter();
                this.writer = new PrintWriter(charArrayWriter);
            }

            public PrintWriter getWriter() {
                return writer;
            }

            public char[] getCopy() {
                return charArrayWriter.toCharArray();
            }

            public byte[] getBytes() {
                return charArrayWriter.toString().getBytes();
            }
        }
}
