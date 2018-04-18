package co.edu.pdam.eci.persistenceapiintegration.data.entity;
import com.j256.ormlite.field.DatabaseField;

/**
 * @author Santiago Carrillo
 */

//TODO add database annotations and proper model structure
public class Team
    extends BaseEntity
{
    @DatabaseField
    String name;
    @DatabaseField
    String shortName;
    @DatabaseField
    String imageUrl;


    public Team(){

    }

    public Team(String name, String shortName, String imageUrl) {
        this.name = name;
        this.shortName = shortName;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
