package net.mcthunder.message;

public enum ChatFormat {
    BOLD,
    UNDERLINED,
    STRIKETHROUGH,
    ITALIC,
    OBFUSCATED;

    public static ChatFormat byName(String name) {
        name = name.toLowerCase();
        for (ChatFormat format : values()) {
            if (format.toString().equals(name)) {
                return format;
            }
        }

        return null;
    }

    public String toString() {
        return name().toLowerCase();
    }
}