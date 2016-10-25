package demos.android.com.craneo.demowearablemobilgatekidper.model;

/**
 * Created by crane on 10/14/2016.
 */

public class Kid {
    private long id;
    private String name;
    private String lastName;
    private String image;

    public Kid(){}

    public Kid(String name, String lastName, String image) {
        this.name = name;
        this.lastName = lastName;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return lastName + ", " + name;
    }
}
