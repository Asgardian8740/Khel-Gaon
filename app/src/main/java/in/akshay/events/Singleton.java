package in.akshay.events;

import static okhttp3.internal.Internal.instance;

public class Singleton {
    private static Singleton instance;
    private String email;
    private String name;




    private Singleton() {}

    public static Singleton Instance    ()
    {
        //if no instance is initialized yet then create new instance
        //else return stored instance
        if (instance == null)
        {
            instance = new Singleton();
        }
        return instance;
    }

    public void setEmail(String email){
        this.email=email;
    }
    public String getEmail(){
        return email;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }






}


