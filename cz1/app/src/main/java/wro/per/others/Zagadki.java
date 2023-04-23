
package wro.per.others;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Zagadki {

    @SerializedName("riddle_id")
    @Expose
    private Integer riddleId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("congrats")
    @Expose
    private String congrats;
    @SerializedName("points")
    @Expose
    private Integer points;

    public Integer getRiddleId() {
        return riddleId;
    }

    public void setRiddleId(Integer riddleId) {
        this.riddleId = riddleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCongrats() {
        return congrats;
    }

    public void setCongrats(String congrats) {
        this.congrats = congrats;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

}