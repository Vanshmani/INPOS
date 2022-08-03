package project.bluetoothterminal;

import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

class Utils {
    Utils() {
    }

    static class InputFilterHex implements InputFilter {
        private final boolean mUpperCase = true;

        InputFilterHex() {
        }

        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            String str;
            int i5 = i2 - i;
            if (i5 <= 0) {
                return null;
            }
            char[] cArr = null;
            int i6 = 0;
            for (int i7 = i; i7 < i2; i7++) {
                char charAt = charSequence.charAt(i7);
                char upperCase = Character.toUpperCase(charAt);
                if (upperCase == 'A' || upperCase == 'B' || upperCase == 'C' || upperCase == 'D' || upperCase == 'E' || upperCase == 'F' || upperCase == '-' || Character.isDigit(charAt)) {
                    if ((this.mUpperCase && charAt != upperCase) || (!this.mUpperCase && charAt == upperCase)) {
                        if (cArr == null) {
                            cArr = new char[i5];
                            TextUtils.getChars(charSequence, i, i7, cArr, 0);
                        }
                        int i8 = i6 + 1;
                        if (!this.mUpperCase) {
                            upperCase = Character.toLowerCase(charAt);
                        }
                        cArr[i6] = upperCase;
                        i6 = i8;
                    } else if (cArr != null) {
                        cArr[i6] = upperCase;
                        i6++;
                    } else {
                        i6++;
                    }
                } else if (cArr == null) {
                    cArr = new char[i5];
                    TextUtils.getChars(charSequence, i, i7, cArr, 0);
                }
            }
            if (cArr == null) {
                return null;
            }
            if (i6 >= i5) {
                str = String.valueOf(cArr);
            } else {
                str = String.valueOf(cArr, 0, i6);
            }
            if (!(charSequence instanceof Spanned)) {
                return str;
            }
            SpannableString spannableString = new SpannableString(str);
            TextUtils.copySpansFrom((Spanned) charSequence, i, i6, (Class) null, spannableString, 0);
            return spannableString;
        }
    }
}
