import java.util.*;

public class IDAstar {
    public static void solve(int[][] start, int[][] end){

        assert Board.judge(start,end);


        PriorityQueue<Board> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Board::getCost));
        Set<Long> closed = new HashSet<>();

        final int length = 4;

        Board.setTarget(end);

        System.out.println(new Board(start,0,null));
        System.out.println(new Board(end,0,null));

//        if(!judge(start,end)){
//            System.out.println("heli");
//            return;
//        }


//        while(!judge(start,end)){
//            System.out.println("cannot");
//            start = RandomBoardGenerator.generate(length);
//            end = RandomBoardGenerator.generate(length);
//        }

        Board ans = null;
        int limit = new Board(start,0,null).getDistance();

        Loop:
        while(true){

            System.out.println(limit);
            Board board = new Board(start,0,null);
            priorityQueue.add(board);


            while(!priorityQueue.isEmpty()){
                Board b = priorityQueue.poll();
                //System.out.println(b);
                if(b.getDistance() == 0){
                    ans = b;
                    break Loop;
                }

                closed.add(b.hash());
                List<Board> next = b.move();
                for(Board x : next){
                    if(closed.contains(x.hash()) || x.getCost() > limit)
                        continue;
                    priorityQueue.add(x);
                }

            }
            limit++;
            closed.clear();
        }

        while(ans != null){
            System.out.println(ans);
            ans = ans.getParent();
        }


    }

    public static void main(String[] args) {
        int[][] start = RandomBoardGenerator.generate(4);
        //int[][] end = RandomBoardGenerator.generate(start,Integer.MAX_VALUE);
        int[][] end = RandomBoardGenerator.generate(4);
        Board.setTarget(end);
        solve(start,end);
    }
}
