import java.util.*;

public class Board {
    private byte[][] board;
    private byte distance;
    private byte moved;

    private static byte[][] target;
    private static int[][] locArray;

    private Board parent;

    public Board(byte[][] board, byte moved, Board parent){
        this.board = new byte[board.length][board.length];
        for(int i = 0;i < board.length;i++)
            System.arraycopy(board[i], 0, this.board[i], 0, board.length);
        this.moved = moved;
        this.parent = parent;
        distance = calculateDistance();
    }


    public byte calculateDistance(){
        distance = 0;
        int[][] array = calculateLocArray(board);

        for(int i = 1;i<array.length;i++){
            distance += Math.abs(array[i][0] - locArray[i][0])  + Math.abs(array[i][1] - locArray[i][1]);
        }

        return  distance;
    }

    public int getCost(){
        return moved + distance;
    }

    public List<Board> move(){
        int x = 0, y = 0;
        int length = board.length;

        int[] loc = findZero(board);
        x = loc[0];
        y = loc[1];

        List<Board> result = new ArrayList<>(4);
        byte nextMoved = moved;
        nextMoved++;
        if(x > 0){
            swap(x-1,y,x,y);
            result.add(new Board(board,nextMoved,this));
            swap(x-1,y,x,y);
        }

        if(x < length - 1){
            swap(x+1,y,x,y);
            result.add(new Board(board,nextMoved,this));
            swap(x+1,y,x,y);
        }

        if(y > 0){
            swap(x,y-1,x,y);
            result.add(new Board(board,nextMoved,this));
            swap(x,y-1,x,y);
        }

        if(y < length - 1){
            swap(x,y+1,x,y);
            result.add(new Board(board,nextMoved,this));
            swap(x,y+1 ,x,y);
        }


        return result;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;

        for(int i = 0;i<board.length;i++)
            for(int j = 0;j<board.length;j++)
                if(board[i][j] != board1.board[i][j])
                    return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) hash();
    }

    public long hash(){
        long result = 0;
        for (byte[] ints : board)
            for (int j = 0; j < board.length; j++) {
                result += ints[j];
                result <<= 4;
            }
        return result;
    }


    public static int[][] decode(int length, long code){
        int[][] b = new int[length][length];

        long c = 15;
        int offset = 0;

        for(int i = length - 1; i >= 0;i--)
            for(int j = length -1; j >= 0;j--){
                b[i][j] = (int) (c & code) >> offset;
                offset += 4;
                c <<= 4;
            }

        return b;
    }

    public static void setTarget(byte[][] target_){
        target = target_;
        locArray = calculateLocArray(target);
    }

    public static int[][] calculateLocArray(byte[][] b){
        int[][] array = new int[b.length * b.length][2];
        for(int i = 0;i<b.length;i++)
            for(int j = 0;j<b.length;j++){
                array[b[i][j]][0] = i;
                array[b[i][j]][1] = j;
            }
        return array;
    }

    private void swap(int x1, int y1, int x2, int y2){
        byte t = board[x1][y1];
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = t;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i = 0;i< board.length;i++){
            sb.append("{");
            for(int j = 0;j< board.length;j++){
                sb.append(board[i][j]);
                if(j != board.length - 1)
                    sb.append(',');
            }

            sb.append("}");
            if(i != board.length - 1)
                sb.append(',');
        }
        sb.append("}");

        return sb.toString();
    }

    public static boolean judge(byte[][] one , byte[][] two){

        int reverseOne = reverseOrderCount(one);
        int reverseTwo = reverseOrderCount(two);

        if(one.length % 2 != 0){
            return (reverseOne - reverseTwo) % 2==0;
        }else{
            int[] locOne = findZero(one);
            int[] locTwo = findZero(two);

            if((reverseOne - reverseTwo) % 2 ==0)
                return (locOne[0] - locTwo[0]) % 2 == 0;
            else
                return (locOne[0] - locTwo[0]) % 2 != 0;
        }



    }

    public static int[] findZero(byte[][] board){

        int length = board.length;
        int[] result = new int[2];
        for(int i = 0;i<length;i++)
            for(int j = 0;j<length;j++)
                if(board[i][j] == 0){
                    result[0] = i;
                    result[1] = j;
                    break;
                }
        return result;
    }

    public static int reverseOrderCount(byte[][] board){
        int length = board.length;
        int[] record = new int[length * length];
        int index = 0;
        int ans = 0;

        for(int i = 0 ; i < length;i++)
            for(int j = 0;j < length;j++){
                if(board[i][j] == 0){
                    record[index] = 0;
                    index++;
                    continue;
                }
                record[index] = board[i][j];
                for(int k = index-1;k>=0;k--)
                    if(record[index] < record[k])
                        ans++;
                index++;

            }

        return ans;
    }

    public int getDistance(){
        return distance;
    }

    public Board getParent(){
        return parent;
    }

    public static void main(String[] args) {
        PriorityQueue<Board> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Board::getCost));
        Set<Board> closed = new HashSet<>();



        final int length = 4;
        byte[][] start = RandomBoardGenerator.generate(length);
        byte[][] end = RandomBoardGenerator.generate(start,100000);

        Board.setTarget(end);

        System.out.println(closed.size());
//        int[][] start = new int[][]{{12,3,5,4},{6,13,2,14},{15,0,1,7},{8,9,10,11}};
//        int[][] end = new int[][]{{13,12,5,14},{6,3,0,4},{15,1,7,2},{8,9,10,11}};


//        int[][] start = {{6,8,1,10},{4,9,5,12},{14,11,7,0},{2,3,13,15}};
//        int[][] end = {{4,6,8,10},{1,11,5,9},{14,15,12,13},{0,2,3,7}};
//        while(judge(start,end)){
//            start = RandomBoardGenerator.generate(length);
//            end = RandomBoardGenerator.generate(start,10000);
//
//            System.out.println("--");
//        }



        System.out.println(new Board(start,(byte) 0,null));
        System.out.println(new Board(end,(byte) 0,null));

//        if(!judge(start,end)){
//            System.out.println("heli");
//            return;
//        }


//        while(!judge(start,end)){
//            System.out.println("cannot");
//            start = RandomBoardGenerator.generate(length);
//            end = RandomBoardGenerator.generate(length);
//        }


        Board board = new Board(start,(byte) 0,null);
        priorityQueue.add(board);

        Board ans = null;
        while(!priorityQueue.isEmpty()){
            Board b = priorityQueue.poll();
            //System.out.println(b);
            if(b.distance == 0){
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

        System.out.println("----------------------------");

        while(ans != null){
            System.out.println(ans);
            ans = ans.parent;
        }


    }


}
