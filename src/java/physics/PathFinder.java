package physics;

import main.Panel;
import tile.TileManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

// The PathFinder finds the shortest path between 2 points on the map
public class PathFinder {

    // Create a cache of path results to enable pre-processing where possible
    private static HashMap<LinkedList<Node>, Node> shortestPathResults = new HashMap<>();

    // Private constructor - Noninstantiable class
    private PathFinder() {}

    // Inner class to represent a map point as a place on a graph
    protected static class Node {
        // Node vars
        protected final int X_MAP_POS, Y_MAP_POS; // make immutable for hashmaps
        private final boolean COLLISION;
        private final String NAME;

        // Path tracking vars
        private Node pathParentNode;
        private final int TRAVEL_COST;

        // Constructor
        public Node(int xMapPos, int yMapPos, Node pathParentNode) {
            this.pathParentNode = pathParentNode;

            // Round down x and y coordinates to align the Node perfectly with a Tile map pos, which greatly limits
            // the num of Nodes for efficiency (via more caching) but makes the path slightly less accurate
            xMapPos = xMapPos / Panel.UNIT_SIZE;
            xMapPos = xMapPos * Panel.UNIT_SIZE;
            this.X_MAP_POS = xMapPos;
            yMapPos = yMapPos / Panel.UNIT_SIZE;
            yMapPos = yMapPos * Panel.UNIT_SIZE;
            this.Y_MAP_POS = yMapPos;

            // Fill collision and travel cost from the closest Tile
            NAME = TileManager.getClosestTile(X_MAP_POS, Y_MAP_POS).NAME;
            COLLISION = CollisionChecker.checkTileCollision(this);
            TRAVEL_COST = TileManager.getClosestTile(X_MAP_POS, Y_MAP_POS).TRAVEL_COST;
        }

        // Override toString() for easier debugging
        @Override
        public String toString() {
            String descr = NAME + " at " + X_MAP_POS + ", " + Y_MAP_POS;
            return descr;
        }

        // Overriding equals() to be able to compare two Node objects (like using contains())
        @Override
        public boolean equals(Object otherObj) {
            // If the object is compared with itself then return true
            if (otherObj == this) {
                return true;
            }
            // Check if different class object
            if (!(otherObj instanceof Node)) {
                return false;
            }
            // Check Node position equality
            if(this.X_MAP_POS == ((Node) otherObj).X_MAP_POS
               && this.Y_MAP_POS == ((Node) otherObj).Y_MAP_POS) {
                return true;
            } else {
                return false;
            }
        }

        // Overriding hashcode() to be able to use a Node as a Key in a HashMap
        @Override
        public int hashCode() {
            // Use the Node coordinates as a Key
            int hash = 17;
            hash = hash * 31 + X_MAP_POS;
            hash = hash * 31 + Y_MAP_POS;
            return hash;
        }
    }

    // Helper method to perform a source to target BFS to find the shortest path (best parents) for every visitable node
    private static LinkedList<Node> performBFS(Node startNode, Node destinationNode) {
        // Set how many pixels each BFS traversal will jump on the map. Higher speed = faster BFS, less precise path
        int bfsSpeed = 50;
        // Lists to track the BFS status and shortest path found
        LinkedList<Node> queue = new LinkedList<>(); // FIFO for BFS
        LinkedList<Node> visited = new LinkedList<>();
        HashMap<Node, Integer> nodeShortestPathWeight = new HashMap<>(); // best result for each Node
        // Check every possible movement (right, left, up, down)
        int[][] movementPermutations = {
                {bfsSpeed,0},
                {0-bfsSpeed,0},
                {0,bfsSpeed},
                {0,0-bfsSpeed},
        };
        // Start the BFS through the graph from the start node
        visited.add(startNode);
        nodeShortestPathWeight.put(startNode, startNode.TRAVEL_COST);
        // Add all children of the starting node to continue the traversal
        for(int i=0; i < movementPermutations.length; i++) {
            int xChildPos = startNode.X_MAP_POS + movementPermutations[i][0];
            int yChildPos = startNode.Y_MAP_POS + movementPermutations[i][1];
            Node child = new Node(xChildPos, yChildPos, startNode);
            if(!child.COLLISION) {
                queue.add(child);
                child.pathParentNode = startNode;
            }
        }
        // Visit all descendents from the start until in range of the target. Simultaneously track the best path
        while(!queue.isEmpty()) {
            // First, jump to the next child in the queue. Update the path
            Node currentNode = queue.poll();
            // Update the path and path cost for the node (parent weight + current weight)
            visited.add(currentNode);
            int currentNodePathCost = nodeShortestPathWeight.get(currentNode.pathParentNode) + currentNode.TRAVEL_COST;
            nodeShortestPathWeight.put(currentNode, currentNodePathCost);
            // Loop through every possible child of the current Node
            for(int i=0; i < movementPermutations.length; i++) {
                // Get next child
                int xChildPos = currentNode.X_MAP_POS + movementPermutations[i][0];
                int yChildPos = currentNode.Y_MAP_POS + movementPermutations[i][1];
                Node childNode = new Node(xChildPos, yChildPos, currentNode);
                // Get the existing travel cost to the child if it's already been visited
                int currentChildPathCost = 1000; // triggers a visit if no value is found
                if(nodeShortestPathWeight.containsKey(childNode)) {
                    currentChildPathCost = nodeShortestPathWeight.get(childNode);
                }
                // Get the approximate distance from the child to target. This allows heuristic guiding of the BFS
                int xChildTargetDist = Math.abs(destinationNode.X_MAP_POS - childNode.X_MAP_POS);
                int yChildTargetDist = Math.abs(destinationNode.Y_MAP_POS - childNode.Y_MAP_POS);
                int xSourceTargetDist = Math.abs(destinationNode.X_MAP_POS - startNode.X_MAP_POS);
                int ySourceTargetDist = Math.abs(destinationNode.Y_MAP_POS - startNode.Y_MAP_POS);
                int oppositeDistAllowed = bfsSpeed * 4; // allow limited backward traversals in finding a better path
                if(xChildTargetDist - oppositeDistAllowed >= xSourceTargetDist
                   || yChildTargetDist - oppositeDistAllowed >= ySourceTargetDist  ) {
                    continue; // (heuristics) don't visit child if it's estimated to greatly increase travel distance
                }
                // Decide if we should visit the child. Traverse to the Child if all are true:
                // (1) The child is not beyond a max threshold in the opposite direction of the target (handled above)
                // (2) The child won't cause a collision, unless it's the target
                // (3) It has never been visited OR it's been visited, but we discovered a shorter path to that Node
                if((!childNode.COLLISION || childNode.equals(destinationNode))
                        && !queue.contains(childNode)
                        && currentNodePathCost < currentChildPathCost) {
                    queue.add(childNode);
                    childNode.pathParentNode = currentNode; // upsert parent (set or replace)
                }
            }
        }
        return visited;
    }

    // Helper method to extract the shortest path by leveraging the best parents established in the BFS
    private static LinkedList<Node> getShortestPathNodes(Node startNode, Node destinationNode) {
        LinkedList<Node> shortestPath = new LinkedList<>();
        // First, check if the path is already saved to avoid a duplicate BFS
        boolean foundPathInCache = false;
        if(shortestPathResults.containsValue(startNode)) {
            for (LinkedList<Node> key : shortestPathResults.keySet()) {
                if(key.get(0).equals(startNode) // Start & Destination Match
                        && key.get(key.size()-1).equals(destinationNode)) {
                    shortestPath = key;
                    foundPathInCache = true;
                    break;
                }
            }
        }
        // Otherwise, create a new path
        if(!foundPathInCache) {
            // Perform BFS to get the best parents (shortest path) for every single node
            LinkedList<Node> visited = performBFS(startNode, destinationNode);
            // Find the shortest path by only jumping 1 node per level from target to source through the parents
            if(visited.contains(destinationNode)) {
                shortestPath.add(visited.get(visited.indexOf(destinationNode))); // start at the target
            } else {
                shortestPath.add(visited.get(visited.size()-1)); // handle edge cases where the BFS finds no target
            }
            while(!shortestPath.contains(startNode)) {
                Node currentNode = shortestPath.get(0);
                Node nextNode = currentNode.pathParentNode;
                shortestPath.addFirst(nextNode); // use addFirst() to reverse order so the source appears first
            }
            // Update memory with new path (combine 2 Nodes as the Key)
            shortestPathResults.put(shortestPath, startNode);
        }
        // OOB check -- final list should have at least 2 elements to form a path
        if(shortestPath.size() == 1) {
            Node dummyNode = new Node(shortestPath.get(0).X_MAP_POS, shortestPath.get(0).Y_MAP_POS, shortestPath.get(0));
            shortestPath.add(dummyNode);
        } else {
            shortestPath.remove(0); // remove source that is the opposite dir from rounding down

        }
        return shortestPath;
    }

    // Gets the next direction to travel to follow the shortest path to a given target
    public static char getNextBestDir(int xStartPoint, int yStartPoint,
                                      int xTargetPoint, int yTargetPoint) {
        // Nodes for the start and end point
        Node startNode = new Node(xStartPoint, yStartPoint, null);
        Node destinationNode = new Node(xTargetPoint, yTargetPoint, null);
        // Get the next node to travel to
        LinkedList<Node> shortestPathNodes = getShortestPathNodes(startNode, destinationNode);
        Node nextNode = shortestPathNodes.get(0);
        // Determine the direction needed to get to the next node on the shortest path
        char bestDirection = 'L';
        // Diagonal Right Up
        if(xStartPoint < nextNode.X_MAP_POS
                && yStartPoint > nextNode.Y_MAP_POS) {
            bestDirection = getRandomDir(); // pick vertical or horizontal movement randomly
            while (bestDirection != 'R'
                    && bestDirection != 'U') {
                bestDirection = getRandomDir();
            }
        }
        // Diagonal Right Down
        else if(xStartPoint < nextNode.X_MAP_POS
                && yStartPoint < nextNode.Y_MAP_POS) {
            bestDirection = getRandomDir();
            while (bestDirection != 'R'
                    && bestDirection != 'D') {
                bestDirection = getRandomDir();
            }
        }
        // Diagonal Left Up
        else if(xStartPoint > nextNode.X_MAP_POS
                && yStartPoint > nextNode.Y_MAP_POS) {
            bestDirection = getRandomDir();
            while (bestDirection != 'L'
                    && bestDirection != 'U') {
                bestDirection = getRandomDir();
            }
        }
        // Diagonal Left Down
        else if(xStartPoint > nextNode.X_MAP_POS
                && yStartPoint < nextNode.Y_MAP_POS) {
            bestDirection = getRandomDir();
            while (bestDirection != 'L'
                    && bestDirection != 'D') {
                bestDirection = getRandomDir();
            }
        }
        // Only horizontal movement is needed
        else if(xStartPoint < nextNode.X_MAP_POS) {
            bestDirection = 'R';
        }
        else if(xStartPoint > nextNode.X_MAP_POS) {
            bestDirection = 'L';
        }
        // Only vertical movement is needed
        else if(yStartPoint > nextNode.Y_MAP_POS) {
            bestDirection = 'U';
        }
        else if(yStartPoint < nextNode.Y_MAP_POS) {
            bestDirection = 'D';
        }
        return bestDirection;
    }

    // Method to get a random direction for NPC movement
    public static char getRandomDir() {
        char direction = 'R';
        int randomNumForDir = new Random().nextInt(4);
        switch(randomNumForDir) {
            case 0:
                direction = 'R';
                break;
            case 1:
                direction = 'L';
                break;
            case 2:
                direction = 'U';
                break;
            case 3:
                direction = 'D';
                break;
            }
        return direction;
    }
}

