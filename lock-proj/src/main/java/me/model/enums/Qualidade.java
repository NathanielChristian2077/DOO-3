package me.model.enums;

public enum Qualidade {
    HD,
    FULLHD,
    QHD,
    K4;

    @Override
    public String toString() {
        switch (this) {
            case HD: return "HD";
            case FULLHD: return "FullHD";
            case QHD: return "QHD";
            case K4: return "4K";
            default: return "";
        }
    }
}
