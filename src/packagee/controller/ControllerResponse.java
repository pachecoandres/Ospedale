package packagee.controller;

public class ControllerResponse {

    private int code;
    private String message;
    private String data;

    public ControllerResponse(int code, String message, String data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public boolean isOk() {
        return code >= 200 && code < 300;
    }
}
