package com.wpoppin.whatspoppin;

public class Post {


    public long pk;
    public String title;
    public String author;
    public String description;
    public int price;
    public String category;
    public String date;
    public String time;
    public String ticket_link;
    public String image;

    public String getAuthor() {return author;}

    public String getTitle() {
        return title;
    }

    public int getPrice() {return price;}

    public String getImage(){
        return image;
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



}
