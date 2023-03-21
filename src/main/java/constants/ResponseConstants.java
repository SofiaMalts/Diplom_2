package constants;

public class ResponseConstants {
    public static final int OK_CODE = 200;
    public static final int CREATED_CODE = 201;
    public static final String CREATED_STATUS = "HTTP/1.1 " + CREATED_CODE + " Created";
    public static final int BAD_REQUEST_CODE = 400;
    public static final String BAD_REQUEST_STATUS = "HTTP/1.1 " + BAD_REQUEST_CODE + " Bad Request";
    public static final int NOT_FOUND_CODE = 404;
    public static final String NOT_FOUND_STATUS = "HTTP/1.1 " + NOT_FOUND_CODE + " Not Found";
    public static final int CONFLICT_CODE = 409;
    public static final String CONFLICT_STATUS = "HTTP/1.1 " + CONFLICT_CODE + " Conflict";
    public static final int FORBIDDEN_CODE = 403;
    public static final String FORBIDDEN_STATUS = "HTTP/1.1 " + FORBIDDEN_CODE + " Forbidden";
    public static final int UNAUTHORIZED_CODE = 401;
    public static final String UNAUTHORIZED_STATUS = "HTTP/1.1 " + UNAUTHORIZED_CODE + " Unauthorized";
    public static final int SERVERERROR_CODE = 500;
    public static final String SERVERERROR_STATUS = "HTTP/1.1 " + UNAUTHORIZED_CODE + " Unauthorized";
}
