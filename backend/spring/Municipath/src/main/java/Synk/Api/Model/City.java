package Synk.Api.Model;

public class City {

    String Id;
    String Name;
    String curator;
    int cap;
    Position Pos;

    public City(String id, String name, Position pos, String curator, int cap) {
        this.Id = id;
        this.Name = name;
        this.Pos = pos;
        this.curator = curator;
        this.cap = cap;
    }


    public String getId() {
        return Id;
    }

    public int getCap() {
        return cap;
    }
    public String getCurator() {
        return curator;
    }

    public String getName() {
        return Name;
    }

    public Position getPos() {
        return Pos;
    }
    
    public void setId(String id) {
        Id = id;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }
    
    public void setCurator(String curator) {
        this.curator = curator;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPos(Position pos) {
        Pos = pos;
    }

    


}
