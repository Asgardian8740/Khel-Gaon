package in.akshay.events;

public class posts {
    public String imagurl;
    public String status;
    public String userid;
    public String username;
    public String profilepic;
    public String postId;

    public posts(String imagurl, String status, String userid, String username, String profilepic, String postId) {
        this.imagurl = imagurl;
        this.status = status;
        this.userid = userid;
        this.username = username;
        this.profilepic = profilepic;
        this.postId = postId;
    }

    public String getImagurl() {
        return imagurl;
    }

    public void setImagurl(String imagurl) {
        this.imagurl = imagurl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
