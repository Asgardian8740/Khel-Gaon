package in.akshay.events;


public class EventDB {


    public String eventname;
    public String organname;
    public String desc;
    public String email;
    public String phone;
    public String city;
    public String category;
    public String startdate;
    public String enddate;
    public String eventid;
    public String venue;

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getOrganname() {
        return organname;
    }

    public void setOrganname(String organname) {
        this.organname = organname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public EventDB(String eventname, String organname, String desc, String email, String phone, String city, String category, String startdate, String enddate, String venue, String eventid) {
        this.eventname = eventname;
        this.organname = organname;
        this.desc = desc;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.category = category;
        this.startdate = startdate;
        this.enddate = enddate;
        this.eventid = eventid;
        this.venue = venue;
    }


}
