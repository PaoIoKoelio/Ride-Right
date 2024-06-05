package bg.sofia.uni.fmi.mjt.itinerary.graph;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public record Edge(BigDecimal weight, Node from,
                   Node to, VehicleType vehicleType, BigDecimal price)
        implements Comparable<Edge> {

    @Override
    public int compareTo(Edge edge) {
        return this.weight.compareTo(edge.weight);
    }
}
 