package net.minecraftforge.srg2source.api;

// Note that these extensions ends with .
public enum LanguageType {
    JAVA(".java"),
    KOTLIN(".kt"),
    SCALA(".scala"),
    GROOVY(".groovy");

    // It is required to add more checks at `isSourceFile` when adding more language types

    public final String fileExtension;

    LanguageType(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }


    public static boolean isSourceFile(String fileName) {
        if (fileName.endsWith(JAVA.fileExtension)) return true;
        if (fileName.endsWith(KOTLIN.fileExtension)) return true;
        if (fileName.endsWith(SCALA.fileExtension)) return true;
        if (fileName.endsWith(GROOVY.fileExtension)) return true;

        return false;
    }
}
