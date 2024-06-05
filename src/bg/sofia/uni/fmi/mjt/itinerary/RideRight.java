package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import bg.sofia.uni.fmi.mjt.itinerary.graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;

public class RideRight implements ItineraryPlanner {
    public static final double PRICEPERKM = 20;

    private List<Journey> schedule;

    public RideRight(List<Journey> schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException();
        }
        this.schedule = schedule;
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(
            City start, City destination, boolean allowTransfer)
            throws CityNotKnownException, NoPathToDestinationException {
        if (start == null || destination == null) {
            throw new IllegalArgumentException();
        }
        SequencedCollection<Journey> trip = new ArrayList<>();
        Graph cities = Graph.of(schedule);
        if (!cities.containsCity(start) || !cities.containsCity(destination)) {
            throw new CityNotKnownException();
        }
        if (!allowTransfer) {
            trip.add(cities.getShortestDirectPath(start, destination));
        } else {
            trip.addAll(cities.aStar(start, destination));
        }
        return trip;
    }
}