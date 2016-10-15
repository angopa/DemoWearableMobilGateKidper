package demos.android.com.craneo.demowearablemobilgatekidper;

/**
 * Created by crane on 10/14/2016.
 */

public class Kid {
    private String name;
    private String lastName;
    private String image;

    public Kid(String name, String lastName, String image) {
        this.name = name;
        this.lastName = lastName;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImage() {
        return image;
    }
}
