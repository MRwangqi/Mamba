package com.codelang.mamba.core

import com.codelang.mamba.config.Config
import com.codelang.mamba.utils.CheckUtils
import com.codelang.mamba.utils.Logger
import org.objectweb.asm.*
import org.objectweb.asm.commons.AdviceAdapter

class MambaClassVisitor extends ClassVisitor {

    String className = ""

    public MambaClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor)

    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions)

        mv = new AdviceAdapter(Opcodes.ASM6, mv, access, name, desc) {

            private boolean isTrack = false
            private int varInsnIndex = 0

            @Override
            AnnotationVisitor visitAnnotation(String annotation, boolean visible) {
                //if (Type.getDescriptor(Track.class) == annotation)
                if (annotation == Config.ANNOTATION_TRACK_DESC) {
                    isTrack = true
                }
                return super.visitAnnotation(annotation, visible)
            }

            @Override
            protected void onMethodEnter() {
                super.onMethodEnter()

                // 获取方法的所有参数类型
                final Type[] argTypes = Type.getArgumentTypes(desc)

                // 第一个参数：当前的 class 类
                mv.visitLdcInsn(Type.getType("L" + className + ";"))
                // 第二个参数：当前的方法名称
                mv.visitLdcInsn(name)

                // 最多支持 5 个参数
                int typeSize = argTypes.size()
                if (typeSize > 5) {
                    typeSize = 5
                }

                // trackEnable 可用、方法添加了 Track 注解、方法参数至少要有一个
                if (CheckUtils.trackEnable && isTrack && typeSize > 0) {
                    // 创建 new Object[typeSize]
                    mv.visitInsn(ICONST_0 + typeSize)
                    mv.visitTypeInsn(ANEWARRAY, "java/lang/Object")
                    // 遍历参数，将参数设置进 object 数组中
                    varInsnIndex = 0
                    for (int i = 0; i < typeSize; i++) {
                        Type type = argTypes[i]
                        mv.visitInsn(DUP)
                        mv.visitInsn(ICONST_0 + i)
                        visitVarOrMethod(type.internalName)
                        mv.visitInsn(AASTORE)
                    }
                    mv.visitMethodInsn(INVOKESTATIC, Config.GENERATE_TO_CLASS_NAME, Config.REGISTER_METHOD_ENTER_NAME, "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)V", false);
                } else if (CheckUtils.methodEnable) {
                    mv.visitMethodInsn(INVOKESTATIC, Config.GENERATE_TO_CLASS_NAME, Config.REGISTER_METHOD_ENTER_NAME, "(Ljava/lang/Class;Ljava/lang/String;)V", false)
                }

            }

            @Override
            protected void onMethodExit(int opcode) {
                super.onMethodExit(opcode)
                if (CheckUtils.methodEnable) {
                    mv.visitLdcInsn(Type.getType("L" + className + ";"))
                    mv.visitLdcInsn(name)
                    mv.visitMethodInsn(INVOKESTATIC, Config.GENERATE_TO_CLASS_NAME, Config.REGISTER_METHOD_EXIT_NAME, "(Ljava/lang/Class;Ljava/lang/String;)V", false);
                }
            }

            /**
             * int、float、boolean、byte、short、对象 为一个 varInsnIndex
             * double、long 的基础类型为两个 varInsnIndex
             * @param paramsType
             */
            private void visitVarOrMethod(String paramsType) {
                switch (paramsType) {
                    case "Z":
                        varInsnIndex++
                        mv.visitVarInsn(ILOAD, varInsnIndex)
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false)
                        break
                    case "java/lang/Boolean":
                        varInsnIndex++
                        mv.visitVarInsn(ILOAD, varInsnIndex)
                        break
                    case "B":
                        varInsnIndex++
                        mv.visitVarInsn(ILOAD, varInsnIndex)
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false)
                        break
                    case "java/lang/Byte":
                        varInsnIndex++
                        mv.visitVarInsn(ILOAD, varInsnIndex)
                        break
                    case "C":
                        varInsnIndex++
                        mv.visitVarInsn(ILOAD, varInsnIndex)
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false)
                        break
                    case "java/lang/Character":
                        varInsnIndex++
                        mv.visitVarInsn(ILOAD, varInsnIndex)
                        break
                    case "S":
                        varInsnIndex++
                        mv.visitVarInsn(ILOAD, varInsnIndex)
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false)
                        break
                    case "java/lang/Short":
                        varInsnIndex++
                        mv.visitVarInsn(ILOAD, varInsnIndex)
                        break
                    case "I":
                        varInsnIndex++
                        mv.visitVarInsn(ILOAD, varInsnIndex)
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false)
                        break
                    case "java/lang/Integer":
                        varInsnIndex++
                        mv.visitVarInsn(ILOAD, varInsnIndex)
                        break
                    case "J":
                        varInsnIndex++
                        mv.visitVarInsn(LLOAD, varInsnIndex)
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false)
                        varInsnIndex++
                        break
                    case "java/lang/Long":
                        varInsnIndex++
                        mv.visitVarInsn(LLOAD, varInsnIndex)
                        break
                    case "F":
                        varInsnIndex++
                        mv.visitVarInsn(FLOAD, varInsnIndex)
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false)
                        break
                    case "java/lang/Float":
                        varInsnIndex++
                        mv.visitVarInsn(FLOAD, varInsnIndex)
                        break
                    case "D":
                        varInsnIndex++
                        mv.visitVarInsn(DLOAD, varInsnIndex)
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false)
                        varInsnIndex++
                        break
                    case "java/lang/Double":
                        varInsnIndex++
                        mv.visitVarInsn(DLOAD, varInsnIndex)
                        break
                    default:
                        varInsnIndex++
                        mv.visitVarInsn(ALOAD, varInsnIndex)
                }
            }

        }
        return mv
    }


}