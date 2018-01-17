package generator;

/**
 * @author Quinn
 * @date 2018/1/17
 * @package generator
 */
public class Property {
    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
