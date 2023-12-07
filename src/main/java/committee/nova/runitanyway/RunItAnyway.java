package committee.nova.runitanyway;

import java.lang.instrument.Instrumentation;

public class RunItAnyway {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new RIATransformer());
    }
}
