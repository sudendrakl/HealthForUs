package bizapps.com.healthforusPatient.Model;

import java.util.List;

/**
 * Created by venkatat on 7/24/2016.
 */
public class SpecialisationModel {
    public String status;

    public List<GetSpecialisation> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    List<GetSpecialisation> data;

    public class GetSpecialisation{
        public String id;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String name;
    }
}
