package bizapps.com.healthforusPatient.data;

import lombok.Data;
import lombok.ToString;

/**
 * Created by sudendra.kamble on 22/09/16.
 */

@Data
@ToString
public class GcmUpdateResponseDto {
  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  boolean status;
  String response;
  String error;
  int code = -1;
}
