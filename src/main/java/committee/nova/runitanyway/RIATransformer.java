package committee.nova.runitanyway;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.List;

public class RIATransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(
            ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        final List<RIAPlatform> platforms = RunItAnyway.getPlatform(className);
        if (platforms == null || platforms.isEmpty()) return classfileBuffer;
        ClassReader cr = new ClassReader(classfileBuffer);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(
                    int access, String methodName, String descriptor,
                    String signature, String[] exceptions) {
                for (final RIAPlatform platform : platforms) {
                    if (methodName.equals(platform.getMethodName())) {
                        return new MethodVisitor(
                                Opcodes.ASM9,
                                super.visitMethod(access, methodName, descriptor, signature, exceptions)) {
                            @Override
                            public void visitCode() {
                                super.visitCode();
                                platform.getModification().accept(mv);
                            }
                        };
                    }
                }
                return super.visitMethod(access, methodName, descriptor, signature, exceptions);
            }
        };
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }
}