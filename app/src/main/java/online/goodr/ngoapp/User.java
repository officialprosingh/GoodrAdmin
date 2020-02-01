package online.goodr.ngoapp;


public class User {
    public String name, email , snumber , web;

    public User(){

    }

    public User(String name, String email , String snumber , String web ) {
        this.name = name;
        this.email = email;
        this.snumber = snumber;
        this.web = web;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }


}
