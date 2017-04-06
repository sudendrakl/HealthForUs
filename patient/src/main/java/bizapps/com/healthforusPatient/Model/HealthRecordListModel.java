package bizapps.com.healthforusPatient.Model;

import java.util.List;

/**
 * Created by venkatat on 7/22/2016.
 */
public class HealthRecordListModel {

    public List<HealthRecordList> getData() {
        return data;
    }

    public List<HealthRecordList> data;

    public class HealthRecordList{
        public String id;
        public String guid;
        public String description;
        public String userid;

        public String getFile_record() {
            return file_record;
        }

        public String getUserid() {
            return userid;
        }

        public String getDescription() {
            return description;
        }

        public String getGuid() {
            return guid;
        }

        public String getId() {
            return id;
        }

        public String file_record;
    }
}
