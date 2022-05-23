import java.util.*;

public class IDAstar {
    public static Board solve(byte[][] start, byte[][] end){

         if(!Board.judge(start,end))
             return null;



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

            System.out.print(limit + " ");
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


//        while(ans != null){
//            System.out.println(ans);
//            ans = ans.getParent();
//        }

        return ans;


    }

    public static void main(String[] args) {
        byte[][] start = RandomBoardGenerator.generate(4);
        //int[][] end = RandomBoardGenerator.generate(start,Integer.MAX_VALUE);
        byte[][] end = RandomBoardGenerator.generate(4);
        Board.setTarget(end);
        solve(start,end);

        while(true){
            while(!Board.judge(start,end)){

                start = RandomBoardGenerator.generate(4);
                //int[][] end = RandomBoardGenerator.generate(start,Integer.MAX_VALUE);
                end = RandomBoardGenerator.generate(4);
            }

            Board.setTarget(end);

            long s = System.currentTimeMillis();
            solve(start,end);
            System.out.println("\ntime:  " + (System.currentTimeMillis() -s)/1000);
            System.out.println("");
            start = RandomBoardGenerator.generate(4);
            //int[][] end = RandomBoardGenerator.generate(start,Integer.MAX_VALUE);
            end = RandomBoardGenerator.generate(4);
        }
    }
}
