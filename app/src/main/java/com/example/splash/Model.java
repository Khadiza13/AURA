package com.example.splash;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Model {
    public String imageName;
    public String imageName1;
    public String imageName2;
    public String imageURL;

    // Required no-argument constructor for Firebase
    public Model() {
    }

    public Model(String imageName, String imageName1, String imageName2, String imageURL) {
        this.imageName = imageName;
        this.imageName1 = imageName1;
        this.imageName2 = imageName2;
        this.imageURL = imageURL;
    }

    public String getHeader()  {
        return imageName;
    }

    public void setHeader(String imageName) {
        this.imageName = imageName;
    }

    public String getDesc()  {
        return imageName1;
    }

    public void setDesc(String imageName1) {
        this.imageName1 = imageName1;
    }

    public String getPhoneNumber()  {
        return imageName2;
    }

    public void setPhoneNumber(String imageName2) {
        this.imageName2 = imageName2;
    }

    public String getImgname()  {
        return imageURL;
    }

    public void setImgname(String imageURL) {
        this.imageURL = imageURL;
    }
}
