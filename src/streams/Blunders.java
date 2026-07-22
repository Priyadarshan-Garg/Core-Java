package streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Blunders {
    public static void main(String[] args) {

        List<Integer> list = Arrays.asList(1,3,4,5,9,19);
        Set<Integer> integerSet = list.stream().map(x -> {
//            System.out.println(x); // why did it not run ?
            return 2 * x;
        }).collect(Collectors.toSet()) ;


    }

}
