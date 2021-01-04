package openflow.exception;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class MessageParseException extends Exception{
    private static final long serialVersionUID = -75893812926304726L;

    public MessageParseException() {
        super();
    }

    public MessageParseException(String message, Throwable cause) {
        super(message, cause);
        this.setStackTrace(cause.getStackTrace());
    }

    public MessageParseException(String message) {
        super(message);
    }

    public MessageParseException(Throwable cause) {
        super(cause);
        this.setStackTrace(cause.getStackTrace());
    }
}
