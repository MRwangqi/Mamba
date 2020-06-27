package com.codelang.mamba

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.codelang.mamba.core.MambaClassVisitor
import com.codelang.mamba.utils.CheckUtils
import com.codelang.mamba.utils.Logger
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES

public class MambaTransform extends Transform {
    Project project

    MambaTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return MambaPlugin.EXT_NAME
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        Logger.e("start transform")
        def startTime = System.currentTimeMillis()

        inputs.each { TransformInput input ->
            // scan class files
            input.directoryInputs.each { DirectoryInput directoryInput ->
                handleDirectoryInputs(directoryInput, outputProvider)
            }
            // 扫描 jar
            input.jarInputs.each { JarInput jarInput ->
                handleJarInputs(jarInput, outputProvider)
            }
        }

        def endTime = System.currentTimeMillis()
        Logger.e("cost timeMillis = " + (endTime - startTime))
    }


    private static void handleDirectoryInputs(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachFileRecurse { File file ->
                def name = file.name
                if (CheckUtils.checkClass(file.absolutePath)) {
                    Logger.e("directory className = " + name)
                    // 插桩
                    ClassReader cr = new ClassReader(file.bytes)
                    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
                    ClassVisitor cv = new MambaClassVisitor(cw)
                    cr.accept(cv, EXPAND_FRAMES)
                    byte[] code = cw.toByteArray()
                    FileOutputStream fos = new FileOutputStream(
                            file.parentFile.absolutePath + File.separator + name)
                    fos.write(code)
                    fos.close()
                }
            }
        }
        def dest = outputProvider.getContentLocation(directoryInput.name,
                directoryInput.contentTypes, directoryInput.scopes,
                Format.DIRECTORY)
        FileUtils.copyDirectory(directoryInput.file, dest)
    }


    private static void handleJarInputs(JarInput jarInput, TransformOutputProvider outputProvider) {
        if (jarInput.file.getAbsolutePath().endsWith(".jar")) {

            def jarName = jarInput.name
            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4)
            }
            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()
            File tmpFile = new File(jarInput.file.getParent() + File.separator + "mamba_temp.jar")
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))

            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = jarFile.getInputStream(jarEntry)

                if (CheckUtils.checkClass(entryName)) {
                    Logger.e("jar className = " + entryName)

                    jarOutputStream.putNextEntry(zipEntry)
                    // 插桩
                    ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    ClassVisitor cv = new MambaClassVisitor(classWriter)
                    classReader.accept(cv, EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }

            jarOutputStream.close()
            jarFile.close()

            def dest = outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
            FileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
        }
    }

}