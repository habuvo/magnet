package units;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

@XmlRootElement(name = "entries")
public class OutEntries implements Serializable {

    private ArrayList<OutEntry> entries;

    public ArrayList<OutEntry> getEntries() {
        return entries;
    }

    @XmlElement(name = "entry")
    public void setEntries(ArrayList<OutEntry> entries) {
        this.entries = entries;
    }

}
