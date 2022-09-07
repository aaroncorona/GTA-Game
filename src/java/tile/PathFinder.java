package tile;

import main.CollisionChecker;
import main.Panel;

import java.util.LinkedList;

// TODO add path memory/cache to save some BFS runs?

// The PathFinder finds the shortest path between 2 points on the map
public class PathFinder {

    // Private constructor - Noninstantiable class
    private PathFinder() {}

    // Inner class to represent a map point as a place on a graph
    public static class Node { // TODO make private after testing is complete
        // Node vars
        public int xMapPos, yMapPos;
        private Node pathParentNode; // for the BFS traversal
        private boolean collision;
        private int travelCost = 1; // TODO not yet implemented

        // Constructor
        public Node(int xMapPos, int yMapPos, Node pathParentNode) {
            this.xMapPos = xMapPos;
            this.yMapPos = yMapPos;
            this.pathParentNode = pathParentNode;

            collision = CollisionChecker.checkTileCollision(xMapPos, yMapPos);
        }

        // Override toString() for easier debugging
        @Override
        public String toString() {
            String descr = "Node Pos: " + xMapPos + ", " + yMapPos;
            return descr;
        }

        // Overriding equals() to be able to compare two Node objects
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
            if(this.xMapPos == ((Node) otherObj).xMapPos
               && this.yMapPos == ((Node) otherObj).yMapPos) {
                return true;
            } else {
                return false;
            }
        }
    }

    // Helper method to perform BFS and find all Map Nodes the NPC can possibly land on between the start and target
    private static LinkedList<Node> performBFS(Node startNode, Node targetNode, int bfsSpeed) {
        // Lists to track BFS status
        LinkedList<Node> queue = new LinkedList<>(); // FIFO for BFS
        LinkedList<Node> visited = new LinkedList<>();
        // Check every possible movement (right, left, up, down)
        int[][] movementPermutations = {
                {bfsSpeed,0},
                {0-bfsSpeed,0},
                {0,bfsSpeed},
                {0,0-bfsSpeed},
        };
        // Add all children of the starting node
        visited.add(startNode);
        for(int i=0; i < movementPermutations.length; i++) {
            int xChildPos = startNode.xMapPos + movementPermutations[i][0];
            int yChildPos = startNode.yMapPos + movementPermutations[i][1];
            if(xChildPos >= 0 && yChildPos >= 0) { // OOB check
                Node child = new Node(xChildPos, yChildPos, startNode);
                if(!child.collision) {
                    queue.add(child);
                }
            }
        }
        // Visit all descendents
        boolean targetReached = false;
        while(!targetReached
                && !queue.isEmpty()) {
            // Jump to the next child in the queue
            Node currentNode = queue.poll();
            visited.add(currentNode);
            // First, check if the target is reached
            if(targetNode.xMapPos - currentNode.xMapPos < Panel.UNIT_SIZE
               && targetNode.yMapPos - currentNode.yMapPos < Panel.UNIT_SIZE) {
                targetReached = true;
//                System.out.println("BFS Target reached");
                break;
            }
            // Loop through every possible child and add visitable children to the queue
            for(int i=0; i < movementPermutations.length; i++) {
                int xChildPos = currentNode.xMapPos + movementPermutations[i][0];
                int yChildPos = currentNode.yMapPos + movementPermutations[i][1];
                if(xChildPos >= 0 && yChildPos >= 0) { // OOB check
                    Node child = new Node(xChildPos, yChildPos, currentNode);
                    if(!child.collision
                            && !queue.contains(child)
                            && !visited.contains(child)) {
                        queue.add(child);
                    }
                }
            }
        }
        return visited;
    }

    // Find the shortest path out of the visited nodes
    public static LinkedList<Node> getShortestPath(int xStartPoint, int yStartPoint, int speed,
                                                   int xTargetPoint, int yTargetPoint) {
        // Node for the starting point
        Node startNode = new Node(xStartPoint, yStartPoint, null);
        Node targetNode = new Node(xTargetPoint, yTargetPoint, null);
        // Increase speed for better efficiency for the BFS
        int speedAdjuster = 10;
        int bfsSpeed = speed * speedAdjuster;
        // Fill the visited path and parent mappings for a source to target BFS
        LinkedList<Node> visited = performBFS(startNode, targetNode, bfsSpeed);
        // Find the shortest path by only jumping 1 node per level from target to source through the parents
        LinkedList<Node> shortestPath = new LinkedList<>();
        shortestPath.add(visited.get(visited.size()-1)); // start at the target
        while(!shortestPath.contains(startNode)) {
            Node currentNode = shortestPath.get(0);
            Node nextNode = currentNode.pathParentNode;
            shortestPath.addFirst(nextNode); // use addFirst() to reverse order so the source appears first
        }
        // If the resultilng path is only 1 node, add a dummy node so a real path can be formed with at least 2 nodes
        if(shortestPath.size() == 1) {
            Node dummyNode = new Node(shortestPath.get(0).xMapPos, shortestPath.get(0).yMapPos + bfsSpeed, shortestPath.get(0));
            shortestPath.add(dummyNode);
        }
//        System.out.println(shortestPath);
        // Last, create "between nodes" to form an exact path that can be followed.
        // This fills in missing jumps that were skipped for better BFS efficiency
        int index = 0;
        boolean allBetweenNodesFilled = false;
        while(!allBetweenNodesFilled) {
            // Find the next 2 nodes that need more nodes added between them
            Node currentNode = shortestPath.get(index);
            Node nextNode = shortestPath.get(index + 1);
//            System.out.println("fill the gap between " + currentNode + " and " + nextNode);
            // Fill all the 9 missing nodes for the current 2 nodes
            for(int j=0; j < speedAdjuster; j++) {
                int xBetweenNode = currentNode.xMapPos;
                int yBetweenNode = nextNode.yMapPos;
                // Determine the next x or y movement needed for the next between node
                if(currentNode.xMapPos < nextNode.xMapPos) {
                    xBetweenNode = currentNode.xMapPos + speed;
                } else if(currentNode.xMapPos > nextNode.xMapPos) {
                    xBetweenNode = currentNode.xMapPos - speed;
                } else if(currentNode.yMapPos > nextNode.yMapPos) {
                    yBetweenNode = currentNode.yMapPos - speed;
                } else if(currentNode.yMapPos < nextNode.yMapPos) {
                    yBetweenNode = currentNode.yMapPos + speed;
                }
                // Create new node to decide set where we jump to next
                Node nextBetweenNode = new Node(xBetweenNode, yBetweenNode, currentNode);
                // Jump to next node
                currentNode = nextBetweenNode;
                // Add the node in between
                if(!shortestPath.contains(currentNode)) { // TODO find out root issue for why this safeguard is needed
                    shortestPath.add(index + 1 + j, currentNode);
                }
                // Check if the traversal is complete to the final node
                if(currentNode.equals(shortestPath.get(shortestPath.size()-1))) {
                    allBetweenNodesFilled = true;
                    break;
                }
            }
            index = index + speedAdjuster;
        }
        return shortestPath;
    }

    // Translate the shortest path into direction instructions that an entity can follow
    public static LinkedList<Character> getShortestPathDir(int xStartPoint, int yStartPoint, int speed,
                                                           int xTargetPoint, int yTargetPoint) {
        // First, get the shortest path Nodes
        LinkedList<Node> shortestPath = getShortestPath(xStartPoint, yStartPoint, speed, xTargetPoint, yTargetPoint);
        LinkedList<Character> shortestPathDir = new LinkedList<>();
        // Next, fill a direction for each Node that shows how to get to the next best Node
        for(int i=0; i < shortestPath.size()-1; i++) {
            // Determine the next direction the entity must travel to reach the next node
            Node currentNode = shortestPath.get(i);
            Node nextNode = shortestPath.get(i+1);
            char direction = 'R';
            if(currentNode.xMapPos < nextNode.xMapPos) {
                direction = 'R';
            } else if(currentNode.xMapPos > nextNode.xMapPos) {
                direction = 'L';
            } else if(currentNode.yMapPos > nextNode.yMapPos) {
                direction = 'U';
            } else if(currentNode.yMapPos < nextNode.yMapPos) {
                direction = 'D';
            }
            shortestPathDir.add(direction);
        }
//        System.out.println(shortestPath);
//        System.out.println(shortestPathDir);
        return shortestPathDir;
    }
}
