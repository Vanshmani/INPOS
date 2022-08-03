package project.bluetoothterminal;

class OneComment {
    String comment;
    boolean isAscii;
    boolean left;

    OneComment(boolean z, String str, boolean z2) {
        this.left = z;
        this.comment = str;
        this.isAscii = z2;
    }

    /* access modifiers changed from: package-private */
    public Boolean getIsAscii() {
        return Boolean.valueOf(this.isAscii);
    }

    /* access modifiers changed from: package-private */
    public String getComment() {
        return this.comment;
    }
}
