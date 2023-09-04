package rama.examples.tutorial;

import com.rpl.rama.Block;
import com.rpl.rama.Expr;
import com.rpl.rama.LoopVars;
import com.rpl.rama.RamaModule;
import com.rpl.rama.ops.Ops;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Test4 implements RamaModule {
    @Override
    public void define(Setup setup, Topologies topologies) {

    }

    public static void main (String[] args) {
//        Block.loopWithVars(LoopVars.var("*i", 0),
//                        Block.ifTrue(new Expr(Ops.NOT_EQUAL, "*i", 5),
//                                Block.emitLoop("*i")
//                                        .each(Ops.PRINTLN, "*i is not 5 yet")
//                                        .continueLoop(new Expr(Ops.INC, "*i")))).out("*loopValue")
//                .each(Ops.PRINTLN, "Emitted:", "*loopValue")
//                .execute();

        Map data = new HashMap() {{
            put("a0", new HashMap() {{
                put("a1", Arrays.asList(9, 3, 6));
                put("b1", Arrays.asList(0, 8));
            }});
            put("b0", new HashMap() {{
                put("c1", Arrays.asList("x", "y"));
            }});
        }};
    }
}