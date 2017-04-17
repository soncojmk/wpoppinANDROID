package com.wpoppin.whatspoppin;


/*
This is the Post class that the objects pulled from our API are saved to
 */

public class Post {


    public long pk;
    public String url;
    public String title;
    public String author;
    public String description;
    public int price;
    public String category;
    public String date;
    public String time;
    public String ticket_link = null;
    public String image;

    public String street_address;
    public String city;
    public String zipcode;
    public String state;

    public User account;


    public User getAccount(){
        return account;
    }

    public void setAccount(User account){
        this.account = account;
    }

    public String getAddress() {
        return street_address;
    }

    public void setAddress(String address) {
        this.street_address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAuthor() {return author;}

    public String getTitle() {
        return title;
    }

    public int getPrice() {return price;}

    public String getImage() {
        try {
            String largeImage = image.replace(".jpg", ".large.jpg");

            return largeImage;
        } catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public String getDescription(){
        return description;
    }

    public long getPk() {return pk;}

    public String getCategory() {return category;}

    public String getDate() {return date;}

    public String getTime() {return time;}

    public String getTicket_link() {return ticket_link;}

    public void setTitle(String name){
        this.title = name;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.image = thumbnailUrl;
    }

    public void setDescription(String descr){
        this.description = descr;
    }

    public void setAuthor(String auth){this.author = auth; }

    public void setPrice(int pric) {this.price = pric;}

    public void setCategory(String categ) {this.category = categ;}

    public void setDate(String dat) {this.date = dat;}

    public void setTime(String tim) {this.time = tim;}

    public void setTicket_link(String ticket) {this.ticket_link = ticket;}

    public void setPk(long id) {this.pk = id;}

    public String getUrl(){return url;}

    public String getBlogShareUrl(){

        String shareUrl = url.replace("api/blog", "stories/post");
        shareUrl = shareUrl.replace(".json", "");
        return shareUrl;
    }

    public String getEventShareUrl(){

        String ShareUrl = url.replace("api/events", "post/post");
        ShareUrl = ShareUrl.replace(".json", "");
        return ShareUrl;
    }

    public void setUrl(String url){this.url = url;}



}
