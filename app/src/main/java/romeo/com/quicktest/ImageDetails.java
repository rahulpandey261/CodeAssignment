package romeo.com.quicktest;

/**
 * Created by RAJA on 06-06-2016.
 */
public class ImageDetails {
    private String imageName = null;
    private String imageUrl = null;
    private int imageWidth = -1;
    private int imageHeight = -1;

    public ImageDetails(String imageName, String imageUrl, int imageHeight, int imageWidth){
        this.imageName = imageName;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.imageUrl = imageUrl;
    }
    public String getImageName(){
        return this.imageName;
    }
    public int getImageWidth(){
        return this.imageWidth;
    }
    public int getImageHeight(){
        return this.imageHeight;
    }
    public String getImageUrl(){
        return this.imageUrl;
    }
}
