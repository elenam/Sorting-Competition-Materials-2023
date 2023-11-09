public class Pair implements Comparable<Pair> {

    int first;
    String second;

    public Pair(int first, String second) {
        this.first = first;
        this.second = second;
    }

    //get our sum of prime factors
    public int getFirst() { 
        return first;
    }

    //get our associated string
    public String getSecond() {
        return second;
    }

    @Override
    public int compareTo(Pair o) {
        if (this.first > o.first)
            return 1;
        else if (this.first == o.first)
            if (Integer.parseInt(this.second) > Integer.parseInt(o.second))
                return -1;
            else if (Integer.parseInt(this.second) <= Integer.parseInt(o.second))
                return 0;
        return -1;

    }

    public String toString() {
        return "(" + first +
                ", " + second +
                ')';
    }
}


