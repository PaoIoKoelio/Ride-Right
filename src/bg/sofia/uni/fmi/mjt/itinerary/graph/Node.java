package bg.sofia.uni.fmi.mjt.itinerary.graph;

import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.RideRight;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    private City city;

    private List<Edge> neighbours;

    private BigDecimal h;

    private BigDecimal f = BigDecimal.valueOf(Double.MAX_VALUE);

    private BigDecimal g = BigDecimal.valueOf(Double.MAX_VALUE);

    private Edge parent = null;

    Node(City city) {
        this.city = city;
        this.neighbours = new ArrayList<>();
        this.h = BigDecimal.valueOf(0);
    }

    public City getCity() {
        return city;
    }

    public BigDecimal getG() {
        return g;
    }

    public BigDecimal getF() {
        return f;
    }

    public void setF(BigDecimal f) {
        if (f != null) {
            this.f = f;
        }
    }

    public void setG(BigDecimal g) {
        if (g != null) {
            this.g = g;
        }
    }

    public Edge getParent() {
        return parent;
    }

    public void setParent(Edge e) {
        if (e != null) {
            this.parent = e;
        }
    }

    public List<Edge> getNeighbours() {
        return neighbours;
    }

    public BigDecimal getHeuristic() {
        return h;
    }

    public void calculateHeuristic(City end) {
        this.h = BigDecimal.valueOf(city.calculateDistance(end)).multiply(BigDecimal.valueOf(RideRight.PRICEPERKM));
    }

    public void addNeighbour(Edge edge) {
        if (edge != null) {
            neighbours.add(edge);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean equals(Object node) {
        if (node instanceof Node) {
            return ((Node) node).city.name().equals(this.city.name());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return city.name().hashCode();
    }

    @Override
    public int compareTo(Node o) {
        return this.f.compareTo(o.f);
    }
}