package natjom.neopunk.security.perm;

public enum Role {
    MJ, SUPPORT, BUILDER;

    public static Role fromString(String s) {
        return switch (s.toLowerCase()) {
            case "mj" -> MJ;
            case "s", "support" -> SUPPORT;
            case "b", "builder" -> BUILDER;
            default -> null;
        };
    }
}
