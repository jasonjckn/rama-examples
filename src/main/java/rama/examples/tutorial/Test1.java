// package rama.examples.tutorial;

import com.rpl.rama.*;
import com.rpl.rama.impl.NativeAnyArityRamaFunction;
import com.rpl.rama.impl.Util;
import com.rpl.rama.module.*;
import com.rpl.rama.ops.Ops;
import com.rpl.rama.ops.OutputCollector;
import com.rpl.rama.ops.RamaFunction1;
import com.rpl.rama.ops.RamaOperation1;
import com.rpl.rama.test.*;

import java.util.Arrays;



public class Test1 implements RamaModule {
    @Override
    public void define(Setup setup, Topologies topologies) {
    }

    public void main() {


        Block.each(Ops.IDENTITY, "a").out("*x")
                .each(Ops.IDENTITY, "b").out("*y")
                .each(Ops.TO_STRING, "*x", "*y", 1, "!", 2, 3).out("*z")
                .each(Ops.PRINTLN, "*z")
                .execute();
    }

}

final class Comment {
    public void main() {

        int sum = 0;
        for (int i = 0; i < 100; i++) sum += i;
        System.out.println("\n\nsum = " + sum);
        sum += 100000;

       (new Test1()).main();
    }

}

//        Block.each(Ops.EXPLODE, Arrays.asList(1, 6, 3, 2))
//                .out("*v")
//                .each(Ops.TIMES, "*v", 10).out("*v2")
//                .each(Ops.PRINTLN, "Result: ", "*v", "x 10 = ", "*v2")
//                .out("*v3")
//                .execute();
//
//        Block.each(Ops.EXPLODE, Arrays.asList(1, 6, 3, 2))
//                .out("*v")
//                .each(Ops.IDENTITY, "*v")
//                .out("*v2")
//                .each(Ops.PRINTLN, "Result: ", "*v2")
//                .out("*v3")
//                .execute();
