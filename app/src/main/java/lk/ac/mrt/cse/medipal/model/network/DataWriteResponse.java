package lk.ac.mrt.cse.medipal.model.network;

/**
 * Created by lakshan on 11/16/17.
 */

public class DataWriteResponse {
    private boolean success;
    private String message;

    public DataWriteResponse() {
    }

    public DataWriteResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
