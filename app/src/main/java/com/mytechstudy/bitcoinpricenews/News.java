package com.mytechstudy.bitcoinpricenews;


public class News {

    String newsImage;
    String newsHeadline;
    String newsParagraph;

    public News() {
    }

    public News(String newsImage, String newsHeadline, String newsParagraph) {
        this.newsImage = newsImage;
        this.newsHeadline = newsHeadline;
        this.newsParagraph = newsParagraph;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getNewsHeadline() {
        return newsHeadline;
    }

    public void setNewsHeadline(String newsHeadline) {
        this.newsHeadline = newsHeadline;
    }

    public String getNewsParagraph() {
        return newsParagraph;
    }

    public void setNewsParagraph(String newsParagraph) {
        this.newsParagraph = newsParagraph;
    }
}
