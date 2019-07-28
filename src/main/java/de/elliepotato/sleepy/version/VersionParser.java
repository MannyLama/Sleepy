package de.elliepotato.sleepy.version;

import de.elliepotato.sleepy.util.NumberTools;
import org.apache.commons.lang.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ellie :: 28/07/2019
 */
public class VersionParser {

    private static final Pattern VERSION_TAG = Pattern.compile("^(\\d){1,2}[.](\\d){1,2}[-]([a-zA-Z]+)([-](\\d)+)?$");

    private final String fullVersion;

    private final int version;
    private final int subVersion;
    private final String classifier;
    private final int build;

    public VersionParser(String versionString)
            throws IllegalArgumentException {
        this.fullVersion = versionString;

        final Matcher matcher = VERSION_TAG.matcher(versionString.trim());

        if (!matcher.find() && !matcher.matches()) {
            throw new IllegalStateException("version string invalid");
        }

        Optional<Integer> version = NumberTools.tryParse(matcher.group(1));
        this.version = version.orElseThrow(() -> new IllegalArgumentException("bad version string (" + matcher.group(1) + ")"));

        Optional<Integer> subVersion = NumberTools.tryParse(matcher.group(2));
        this.subVersion = subVersion.orElseThrow(() -> new IllegalArgumentException("bad sub version string (" + matcher.group(2) + ")"));

        String buildClassifier = matcher.group(3);
        if (StringUtils.isEmpty(buildClassifier))
            throw new IllegalArgumentException("bad version classifier (" + matcher.group(3) + ")");
        this.classifier = buildClassifier;

        // dev
        Optional<Integer> build = NumberTools.tryParse(matcher.group(5));
        this.build = build.orElse(0);
    }

    public String getFullVersion() {
        return fullVersion;
    }

    public int getVersion() {
        return version;
    }

    public int getSubVersion() {
        return subVersion;
    }

    public String getClassifier() {
        return classifier;
    }

    public int getBuild() {
        return build;
    }

}
