package committee.nova.runitanyway;

import java.lang.instrument.Instrumentation;
import java.util.*;

public class RunItAnyway {
    private static final Map<String, List<RIAPlatform>> platforms = new HashMap<>();

    public static void premain(String agentArgs, Instrumentation inst) {
        Arrays.stream(RIAPlatform.values()).forEach(p -> {
            if (!platforms.containsKey(p.getClassName())) platforms.put(p.getClassName(), new ArrayList<>());
            platforms.get(p.getClassName()).add(p);
        });
        inst.addTransformer(new RIATransformer());
    }

    public static List<RIAPlatform> getPlatform(String className) {
        return platforms.get(className);
    }
}
