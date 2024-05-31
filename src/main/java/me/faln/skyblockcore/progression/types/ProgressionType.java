package me.faln.skyblockcore.progression.types;

public enum ProgressionType {

    WOODCUTTING,
    FARMING,
    SLAYING,
    MINING,
    FISHING;

    public static String normalize(final ProgressionType type) {
        return switch (type) {
            case FARMING, SLAYING, MINING, FISHING, WOODCUTTING -> type.name().charAt(0) + type.name().substring(1, type.name().length() - 1);
        };
    }

}
