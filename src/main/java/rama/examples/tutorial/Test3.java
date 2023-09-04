package rama.examples.tutorial;

import com.rpl.rama.*;
import com.rpl.rama.module.*;
import com.rpl.rama.ops.Ops;
import com.rpl.rama.ops.OutputCollector;
import com.rpl.rama.ops.RamaOperation0;
import com.rpl.rama.test.*;

import java.io.IOException;
import java.util.*;

class Brancher2 implements RamaOperation0 {
    @Override
    public void invoke(OutputCollector collector) {
        collector.emitStream("pizzaOrders", 1);
        collector.emitStream("saladOrders", 10);
        collector.emitStream("saladOrders", 11);
    }
}
class MyOperation implements RamaOperation0 {
    @Override
    public void invoke(OutputCollector collector) {
        int x = 1;
        collector.emitStream("streamA", x);
        int y = x + 2;
        collector.emit(x, y, "s");
        collector.emitStream("streamB");
    }
}
public class Test3 implements RamaModule {

    @Override
    public void define(Setup setup, Topologies topologies) {


    }

    public void main() {
        System.out.println("----------------");
//        Block.each(new Brancher2())
//                .outStream("pizzaOrders", "pizzaAnchor1", "*pizzaOrderSize")
//                .outStream("saladOrders", "saladAnchor1", "*saladOrderSize")
//                .each(Ops.DEC, "*saladOrderSize").out("*orderSize")
//                .anchor("saladAnchor2")
//                .hook("pizzaAnchor1")
//                .each(Ops.INC, "*pizzaOrderSize").out("*orderSize")
//                .anchor("pizzaAnchor2")
//                .unify("pizzaAnchor2", "saladAnchor2")
//                .each(Ops.PRINTLN, "*orderSize")
//                .execute();
//
//        Block.each(Ops.EQUAL, 1, 2).out("*condition")
//                .ifTrue("*condition",
//                        Block.each(Ops.PRINTLN, "math is dead!"))
//                .each(Ops.PRINTLN, "Conditional complete", "*condition")
//                .execute();
//        Block.ifTrue(new Expr(Ops.EQUAL, 1, 2),
//                        Block.each(Ops.PRINTLN, "math is dead!").out("*else"),
//                        Block.each(Ops.PRINTLN, "math is alive!").out("*then"))
//                .each(Ops.PRINTLN, "conditional complete", "*else")
//                .execute();
//
//        Block.each(new MyOperation()).outStream("streamA", "anchorA", "*v1")
//                .outStream("streamB", "anchorB", "*v2")
//                .out("*a", "*b", "*c");

        Block.loopWithVars(LoopVars.var("*i", 0),
                        Block.ifTrue(new Expr(Ops.NOT_EQUAL, "*i", 5),
                                Block.emitLoop("*i")
                                        .each(Ops.PRINTLN, "*i is not 5 yet")
                                        .continueLoop(new Expr(Ops.INC, "*i")))).out("*loopValue")
                .each(Ops.PRINTLN, "Emitted:", "*loopValue")
                .execute();


    }
}