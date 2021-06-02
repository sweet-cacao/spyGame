package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayerNameConverter {
    public static String[] namesRandom = { "Доктор", "Полицейский", "Художник", "Водитель", "Инженер", "Президент", "Депутат", "Мэр", "Актер", "Блогер", "Певец"};
    static private int i = 0;

    public static String giveRandomName() {
        String name = namesRandom[i];
        i++;
        return name;
    }
}
