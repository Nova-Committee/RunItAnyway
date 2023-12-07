package committee.nova.runitanyway;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class RIATransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(
            ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        final boolean fabric = className.equals("net/fabricmc/loader/api/metadata/ModDependency$Kind");
        final boolean forge = className.equals("net/minecraftforge/fml/loading/moddiscovery/ModInfo$ModVersion");
        if (!fabric && !forge) return classfileBuffer;
        ClassReader cr = new ClassReader(classfileBuffer);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(
                    int access, String methodName, String descriptor,
                    String signature, String[] exceptions) {
                if (methodName.equals(fabric ? "isSoft" : "isMandatory")) {
                    return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, methodName, descriptor, signature, exceptions)) {
                        @Override
                        public void visitCode() {
                            super.visitCode();
                            mv.visitInsn(fabric ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
                            mv.visitInsn(Opcodes.IRETURN);
                        }
                    };
                }
                return super.visitMethod(access, methodName, descriptor, signature, exceptions);
            }
        };
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }
}