package bg.sofia.uni.fmi.mjt.itinerary;

public record City(String name, Location location) {
    public static final int METERSINKILOMETERS = 1000;

    public City {
        if (name == null || location == null) {
            throw new IllegalArgumentException();
        }
    }

    public double calculateDistance(City city) {
        return ((double) (Math.abs(city.location().y() - this.location.y())
                + Math.abs(city.location().x() - this.location.x()))) / METERSINKILOMETERS;
    }
}

