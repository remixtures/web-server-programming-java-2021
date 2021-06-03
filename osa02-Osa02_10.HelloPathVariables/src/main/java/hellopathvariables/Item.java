package hellopathvariables;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item {

    private String identifier;
    private String name;
    private String type;

    public Item(String name, String type) {
        this.identifier = UUID.randomUUID().toString();
        this.name = name;
        this.type = type;
    }
}
