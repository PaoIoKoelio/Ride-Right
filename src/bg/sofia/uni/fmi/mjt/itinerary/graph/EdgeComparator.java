package bg.sofia.uni.fmi.mjt.itinerary.graph;

import java.util.Comparator;

public class EdgeComparator implements Comparator<Edge> {
    @Override
    public int compare(Edge o1, Edge o2) {
        return -o1.to().getCity().name().compareTo(o2.to().getCity().name());
    }
}