package in.akshay.events;

public class Inters_DB {
    public String eventid;
    public String name;
    public String email;
    public String mobile;
    public String query;
    public String teamname;
    public String pquerykey;

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

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }
}
