import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PointProperties {
    public String gridId;
    public int gridX;
    public int gridY;
}
