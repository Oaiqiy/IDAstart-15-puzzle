import java.util.*;

public class IDAstar {
    public static Board solve(byte[][] start, byte[][] end){

        System.out.println("IDA* start at " + new Date());

        if(!Board.judge(start,end))
             return null;

         Deque<Board> deque = new LinkedList<>();
         HashMap<Long,Byte> closed = new HashMap<>();


        Board.setTarget(end);


        Board ans;
        Board board = new Board(start,(byte) 0,null);
        int limit = board.getDistance();
        if(limit == 0)
            return board;


        IDAstarLoop:
        while(true){

            System.out.println("iterate: " + limit + " at " + new Date());
            deque.add(board);

            while(!deque.isEmpty()){
                Board b = deque.pollLast();

                List<Board> next = b.move();
                for(Board x : next){

                    if(x.getDistance() == 0){
                        ans = x;
                        break IDAstarLoop;
                    }

                    byte cost = (byte) x.getCost();

                    if(cost > limit)
                        continue ;


                    Long hash = x.hash();
                    if(closed.containsKey(hash)){
                        if(cost < closed.get(hash)){
                            closed.replace(hash, cost);
                            deque.addLast(x);
                        }
                    }else {
                        closed.put(hash,cost);
                        deque.addLast(x);
                    }

                }

            }

            limit += 2;
            closed.clear();

        }

        System.out.println("IDA* finish at " + new Date());

        return ans;

    }

    public static void main(String[] args) {
        byte[][] start = RandomBoardGenerator.generate(4);
        //byte[][] end = RandomBoardGenerator.generate(start,Integer.MAX_VALUE);
        byte[][] end = RandomBoardGenerator.generate(4);

//        start = new byte[][]{{10,15,3,8},{1,2,12,5},{9,11,7,13},{6,14,0,4}};
//        end = new byte[][]{{15,5,8,1},{2,0,3,14},{4,12,6,7},{9,10,11,13}};

        start = new byte[][]{{9,4,10,2},{13,5,11,14},{15,6,0,7},{1,12,3,8}};
        end = new byte[][]{{14,6,15,7},{2,11,5,9},{10,8,3,4},{12,0,13,1}};
        Board.setTarget(end);
//        solve(start,end);

        while(true){
            while(!Board.judge(start,end)){

                start = RandomBoardGenerator.generate(4);
                //int[][] end = RandomBoardGenerator.generate(start,Integer.MAX_VALUE);
                end = RandomBoardGenerator.generate(4);
            }

            Board.setTarget(end);

            long s = System.currentTimeMillis();
            Board a = solve(start,end);
            System.out.println("\ntime:  " + (System.currentTimeMillis() -s)/1000);
            int count = 0;
            while(a != null){
                //System.out.println(a);
                count++;
                a = a.getParent();
            }
            System.out.println(count);

            System.out.println("");
            start = RandomBoardGenerator.generate(4);
            //int[][] end = RandomBoardGenerator.generate(start,Integer.MAX_VALUE);
            end = RandomBoardGenerator.generate(4);
        }
    }
}
