package in.akshay.events;

public class getInterDB {
        public String eventid;
        public String name;
        public String email;
        public String mobile;
        public String query;
        public String pquerykey;
        public String nameteam;

    public String getNameteam() {
        return nameteam;
    }

    public void setNameteam(String nameteam) {
        this.nameteam = nameteam;
    }

    public getInterDB(String eventid, String name, String email, String mobile, String query, String pquerykey, String nameteam) {
        this.eventid = eventid;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.query = query;
        this.pquerykey = pquerykey;
        this.nameteam = nameteam;
    }

    public String getPquerykey() {
            return pquerykey;
        }

        public void setPquerykey(String pquerykey) {
            this.pquerykey = pquerykey;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getEventid() {
            return eventid;
        }

        public void setEventid(String eventid) {
            this.eventid = eventid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }


