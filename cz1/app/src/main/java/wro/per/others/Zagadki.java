
package wro.per.others;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Zagadki {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("difficulty")
    @Expose
    private String difficulty;
    @SerializedName("infolink")
    @Expose
    private String infolink;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("objectCount")
    @Expose
    private Integer objectCount;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getInfoLink() {
        return infolink;
    }

    public void setInfoLink(String infolink) {
        this.infolink = infolink;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
    public Integer getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(Integer objectCount) {
        this.objectCount = objectCount;
    }



}