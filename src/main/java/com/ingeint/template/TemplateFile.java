package com.ingeint.template;

import com.ingeint.settings.Settings;
import org.apache.commons.text.StringSubstitutor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TemplateFile {
    private File sourceFile;

    private StringSubstitutor stringSubstitutor = new StringSubstitutor(Settings.toMap());

    public TemplateFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public Path getSourcePath() {
        return Paths.get(sourceFile.getPath());
    }

    public Path getTargetPath() {
        return Paths.get(Settings.getTargetPath(), getPluginNamePath(), sourceFile.getName());
    }

    private String getPluginNamePath() {
        return Settings.getPluginName()
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "-")
                .replaceAll("-{2,}", "-");
    }

    public void writeTarget() throws IOException {
        getTargetFolder().mkdirs();
        Files.writeString(getTargetPath(), loadAndFillRawFile(), StandardCharsets.UTF_8);
    }

    private String loadAndFillRawFile() throws IOException {
        String currentFile = Files.readString(getSourcePath(), StandardCharsets.UTF_8);
        return stringSubstitutor.replace(currentFile);
    }

    private File getTargetFolder() {
        return getTargetPath().toFile().getParentFile();
    }
}