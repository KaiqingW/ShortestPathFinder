
import map.*;
import java.util.Comparator;
import java.util.HashSet;

public class Controller{
//    int _width, _height;
//    Map _map;
//    View _view;
//    Node _start, _dest;
//
//    public Controller(int r, int c){
//        _map = new Map(r,c);
//        _height = r;
//        _width = c;
//        int sX = (int)(Math.random() * _width);
//        int sY = (int)(Math.random() * _height);
//        int tX = (int)(Math.random() * _width);
//        int tY = (int)(Math.random() * _height);
//        _start = _map.getMap()[sY][sX];
//        _dest = _map.getMap()[tY][tX];
//    }
//
//    public int hexdist(int y1, int x1, int y2, int x2) {
//        int dx = x1-x2, dy = y1-y2;
//        int dd = Math.abs(dx - dy);
//        dy = Math.abs(dy);
//        int max = Math.abs(dx);
//        if (dy>max) max = dy;
//        if (dd>max) max = dd;
//        return max;
//    }
//
//    public void search(int sX, int sY, int tX, int tY){
//        int rows = _map.getRows();
//        int cols = _map.getCols();
//        HashSet<Node> interior = new HashSet<>();
//        FlexQ<Node> openList = new FlexQ<Node>(rows * cols, new Comparator<Node>() {
//            @Override
//            public int compare(Node o1, Node o2) {
//                return o2.compareTo(o1);
//            }
//        });
//
//        Node current = _map.getMap()[sY][sX];
//        openList.insert(current);
//
//        while (!interior.contains(_map.getMap()[tY][tX])) {
//            current = openList.pop();
//            interior.add(current);
//
//            for (Node n : _map.findNeighbours(current)) {
//                if (n != null && !interior.contains(n)) {
//                    Node temp = new Node(n._y, n._x);
//                    temp._accumulateCost = current._accumulateCost + temp._cost;
//                    temp._estCost = n._accumulateCost + hexdist(n._y, n._x, _dest._y, _dest._x);
//                    temp._prev = current;
//                    if (n.getIndex() != -1) {
//                        n.copy(temp);
//                        openList.insert(n);
//                    } else if (n.compareTo(temp) > 0) {
//                        n.copy(temp);
//                    }
//                }
//            }
//        }
//    }
}
