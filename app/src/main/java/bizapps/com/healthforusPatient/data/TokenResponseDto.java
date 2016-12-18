package bizapps.com.healthforusPatient.data;

import lombok.Data;
import lombok.ToString;

/**
 * Created by sudendra.kamble on 22/09/16.
 */
@Data
@ToString
public class TokenResponseDto {
  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  boolean success;
  String token;
  String message;
  int code = -1;
}
