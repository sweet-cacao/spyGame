package utils;

import lombok.Data;

@Data
public class InternalMessage {
    private String type = "";
    private String value = "";

    public InternalMessage(String q) {
        if (q != null) {
            String[] l = q.split(":");
            if (l.length == 2) {
                type = l[0];
                value = l[1];
            } else {
                type = "playerMessage";
            }
        }
    }
}
