import java.util.*;

public class Astar {
    public static Board solve(byte[][] start, byte[][] end){

        PriorityQueue<Board> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Board::getCost));
        Set<Board> closed = new HashSet<>();

        Board.setTarget(end);


        Board board = new Board(start,(byte) 0,null);
        priorityQueue.add(board);

        Board ans = null;
        while(!priorityQueue.isEmpty()){
            Board b = priorityQueue.poll();

            if(b.getDistance() == 0){
                ans = b;
                break;
            }

            closed.add(b);
            List<Board> next = b.move();
            for(Board x : next){
                if(closed.contains(x))
                    continue;
                priorityQueue.add(x);
            }

        }

        return ans;
    }
}
