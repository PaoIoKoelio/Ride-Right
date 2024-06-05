package bg.sofia.uni.fmi.mjt.itinerary.graph;

import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.Journey;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Graph {
    private Set<Node> nodes;

    public Graph(Set<Node> nodes) {
        this.nodes = nodes;
    }

    public static Graph of(List<Journey> edges) {
        Set<Node> nodes = new HashSet<>();
        for (Journey journey : edges) {
            nodes.add(new Node(journey.from()));
            nodes.add(new Node(journey.to()));
            BigDecimal priceCoefficient = journey.vehicleType().getGreenTax().add(BigDecimal.valueOf(1));
            BigDecimal totalPrice = priceCoefficient.multiply(journey.price());
            for (Node node2 : nodes) {
                if (node2.getCity().equals(journey.from())) {
                    for (Node node3 : nodes) {
                        if (node3.getCity().equals(journey.to())) {
                            node2.addNeighbour(
                                    new Edge(totalPrice, node2, node3,
                                            journey.vehicleType(),
                                            journey.price()));
                        }
                    }
                }
            }
        }
        return new Graph(nodes);
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public boolean containsCity(City city) {
        for (Node node : nodes) {
            if (node.getCity().equals(city)) {
                return true;
            }
        }
        return false;
    }

    public Journey getShortestDirectPath(City start, City destination)
            throws NoPathToDestinationException {
        BigDecimal shortest = BigDecimal.valueOf(Double.MAX_VALUE);
        Edge shortestEdge = null;
        for (Node node : nodes) {
            if (node.getCity().equals(start)) {
                for (Edge neighbourNode : node.getNeighbours()) {
                    if (neighbourNode.to().getCity().equals(destination)
                            && neighbourNode.weight().compareTo(shortest) < 0) {
                        shortest = neighbourNode.weight();
                        shortestEdge = neighbourNode;
                    }
                }
            }
        }
        if (shortestEdge == null) {
            throw new NoPathToDestinationException();
        }
        return new Journey(shortestEdge.vehicleType(), start,
                destination, shortestEdge.price());
    }

    public void proceedWithEdge(PriorityQueue<Node> openList, PriorityQueue<Node> closedList, Node n) {
        for (Edge edge : n.getNeighbours()) {
            Node m = edge.to();
            BigDecimal totalWeight = n.getG().add(edge.weight());
            if (!openList.contains(m) && !closedList.contains(m)) {
                m.setParent(edge);
                m.setG(totalWeight);
                m.setF(m.getG().add(m.getHeuristic()));
                openList.add(m);
            } else {
                if (totalWeight.compareTo(m.getG()) < 0) {
                    m.setParent(edge);
                    m.setG(totalWeight);
                    m.setF(m.getG().add(m.getHeuristic()));
                    if (closedList.contains(m)) {
                        closedList.remove(m);
                        openList.add(m);
                    }
                }
            }
        }
    }

    public List<Journey> recoverPath(Node n) {
        List<Journey> trip = new ArrayList<>();
        while (n.getParent() != null) {
            trip.add(new Journey(n.getParent().vehicleType(), n.getParent().from().getCity(),
                    n.getParent().to().getCity(), n.getParent().price()));
            n = n.getParent().from();
        }
        return trip.reversed();
    }

    public List<Journey> aStar(City start, City destination) throws NoPathToDestinationException {
        PriorityQueue<Node> closedList = new PriorityQueue<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();
        List<Journey> trip = new ArrayList<>();
        Node startNode = null;
        Node endNode = null;
        for (Node node : nodes) {
            node.calculateHeuristic(destination);
            if (node.getCity().equals(start)) {
                startNode = node;
            }
            if (node.getCity().equals(destination)) {
                endNode = node;
            }
        }
        startNode.setF(startNode.getG().add(startNode.getHeuristic()));
        openList.add(startNode);
        while (!openList.isEmpty()) {
            Node n = openList.peek();
            if (n.equals(endNode)) {
                return recoverPath(n);
            }
            n.getNeighbours().sort(new EdgeComparator());
            proceedWithEdge(openList, closedList, n);
            openList.remove(n);
            closedList.add(n);
        }
        throw new NoPathToDestinationException();
    }
}

