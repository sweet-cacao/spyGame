package model.message;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO сделать валидацию по формату входящего сообщения

@Getter
public class PlayerMessage {
    private final String message;
    private final String nextPlayerToAnswer;

    public PlayerMessage(String q) {
        List<String> list = Arrays
                .stream(q.split("#"))
                .collect(Collectors.toList());
        nextPlayerToAnswer =  list.get(list.size() - 1);
        int len = nextPlayerToAnswer.length() + 1;
        int all = q.length();
        int last = all - len;
        System.out.println("IN_PRINT_MESSAGE: "+ q);
        message = q.substring(0, last);
    }
}
