package io.nya.rpc.common.exception;

public class SerializationException extends RuntimeException{
    private static final long serialVersionUID = -6783134254669118520L;

    public SerializationException(final Throwable e) {
        super(e);
    }

    public SerializationException(final String message) {
        super(message);
    }

    public SerializationException(final Throwable throwable, final String message) {
        super(message, throwable);
    }
}
