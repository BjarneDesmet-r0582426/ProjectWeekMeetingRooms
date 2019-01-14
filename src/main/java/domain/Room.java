package domain;

import java.util.Objects;

public class Room {

    private final String number;
    private final String name;
    private final int capacity;
    private final int plugCount;

    public Room(String number, String name, int capacity, int plugCount) {
        this.number = number;
        this.name = name;
        this.capacity = capacity;
        this.plugCount = plugCount;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPlugCount() {
        return plugCount;
    }

    public String getEmail() {
        return "HSR-" + name.replace(" ", "-") + "@ucll.be";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
