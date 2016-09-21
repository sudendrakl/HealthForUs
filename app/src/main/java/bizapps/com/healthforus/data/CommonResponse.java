package bizapps.com.healthforus.data;

import lombok.Data;
import lombok.ToString;

/**
 * Created by sudendra.kamble on 22/09/16.
 */

@Data
@ToString
public class CommonResponse {
  boolean status;
  String response;
}
