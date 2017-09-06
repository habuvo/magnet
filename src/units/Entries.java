package units;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

@XmlRootElement(name = "entries")
public class Entries implements Serializable {

    private ArrayList<Entry> entries;

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    @XmlElement(name = "entry")
    public void setEntries(ArrayList<Entry> entries) {
        this.entries = entries;
    }

}
