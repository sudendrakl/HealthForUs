package bizapps.com.healthforusPatient.Model;

import java.util.List;

/**
 * Created by venkatat on 7/31/2016.
 */
public class BlogModel {

    public String status;

    public List<BlogData> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public List<BlogData> data;

    public class BlogData{
        public String id;
        public String guid;
        public String description;
        public String userid;
        public String file_record;
        public String title;
        public String dr_name;

        public String getDr_pic() {
            return dr_pic;
        }

        public String getDr_name() {
            return dr_name;
        }

        public String getTitle() {
            return title;
        }

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

        public String dr_pic;
    }
}
