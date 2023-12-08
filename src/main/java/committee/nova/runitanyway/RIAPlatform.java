package committee.nova.runitanyway;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.function.Consumer;

public enum RIAPlatform {
    FABRIC(
            "net/fabricmc/loader/api/metadata/ModDependency$Kind",
            "isSoft",
            mv -> {
                mv.visitInsn(Opcodes.ICONST_1);
                mv.visitInsn(Opcodes.IRETURN);
            }
    ),
    MODERN_FORGE(
            "net/minecraftforge/fml/loading/moddiscovery/ModInfo$ModVersion",
            "isMandatory",
            mv -> {
                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitInsn(Opcodes.IRETURN);
            }
    ),
    NEOFORGE(
            "net/neoforged/fml/loading/moddiscovery/ModInfo$ModVersion",
            "isMandatory",
            mv -> {
                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitInsn(Opcodes.IRETURN);
            }
    );

    private final String className;
    private final String methodName;
    private final Consumer<MethodVisitor> modification;

    RIAPlatform(String className, String methodName, Consumer<MethodVisitor> modification) {
        this.className = className;
        this.methodName = methodName;
        this.modification = modification;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Consumer<MethodVisitor> getModification() {
        return modification;
    }
}
