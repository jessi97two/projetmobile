package projet.jet.classe;

/**
 * Created by Jess on 01/05/2017.
 */
public class PropositionsSondages {

    String name;
    String id;
    int value; /* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */

    public PropositionsSondages(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    public PropositionsSondages(String name, String id, int value) {
        this.name = name;
        this.id = id;
        this.value = value;
    }
}
