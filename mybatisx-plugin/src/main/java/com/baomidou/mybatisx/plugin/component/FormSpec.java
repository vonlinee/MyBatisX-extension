//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.baomidou.mybatisx.plugin.component;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public abstract class FormSpec implements Serializable {
    static final DefaultAlignment LEFT_ALIGN = new DefaultAlignment("left");
    static final DefaultAlignment RIGHT_ALIGN = new DefaultAlignment("right");
    static final DefaultAlignment TOP_ALIGN = new DefaultAlignment("top");
    static final DefaultAlignment BOTTOM_ALIGN = new DefaultAlignment("bottom");
    static final DefaultAlignment CENTER_ALIGN = new DefaultAlignment("center");
    static final DefaultAlignment FILL_ALIGN = new DefaultAlignment("fill");
    private static final DefaultAlignment[] VALUES;
    public static final double NO_GROW = 0.0;
    public static final double DEFAULT_GROW = 1.0;
    private DefaultAlignment defaultAlignment;
    private Size size;
    private double resizeWeight;

    protected FormSpec(DefaultAlignment defaultAlignment, Size size, double resizeWeight) {
        this.defaultAlignment = defaultAlignment;
        this.size = size;
        this.resizeWeight = resizeWeight;
        if (resizeWeight < 0.0) {
            throw new IllegalArgumentException("The resize weight must be non-negative.");
        }
    }

    protected FormSpec(DefaultAlignment defaultAlignment, String encodedDescription) {
        this(defaultAlignment, Sizes.DEFAULT, 0.0);
        this.parseAndInitValues(encodedDescription.toLowerCase(Locale.ENGLISH));
    }

    public final DefaultAlignment getDefaultAlignment() {
        return this.defaultAlignment;
    }

    public final Size getSize() {
        return this.size;
    }

    public final double getResizeWeight() {
        return this.resizeWeight;
    }

    final boolean canGrow() {
        return this.getResizeWeight() != 0.0;
    }

    private void parseAndInitValues(String encodedDescription) {
        StringTokenizer tokenizer = new StringTokenizer(encodedDescription, ":");
        if (!tokenizer.hasMoreTokens()) {
            throw new IllegalArgumentException("The form spec must not be empty.");
        } else {
            String token = tokenizer.nextToken();
            DefaultAlignment alignment = FormSpec.DefaultAlignment.valueOf(token, this.isHorizontal());
            if (alignment != null) {
                this.defaultAlignment = alignment;
                if (!tokenizer.hasMoreTokens()) {
                    throw new IllegalArgumentException("The form spec must provide a size.");
                }

                token = tokenizer.nextToken();
            }

            this.parseAndInitSize(token);
            if (tokenizer.hasMoreTokens()) {
                this.resizeWeight = this.decodeResize(tokenizer.nextToken());
            }

        }
    }

    private void parseAndInitSize(String token) {
        if (token.startsWith("max(") && token.endsWith(")")) {
            this.size = this.parseAndInitBoundedSize(token, false);
        } else if (token.startsWith("min(") && token.endsWith(")")) {
            this.size = this.parseAndInitBoundedSize(token, true);
        } else {
            this.size = this.decodeAtomicSize(token);
        }
    }

    private Size parseAndInitBoundedSize(String token, boolean setMax) {
        int semicolonIndex = token.indexOf(59);
        String sizeToken1 = token.substring(4, semicolonIndex);
        String sizeToken2 = token.substring(semicolonIndex + 1, token.length() - 1);
        Size size1 = this.decodeAtomicSize(sizeToken1);
        Size size2 = this.decodeAtomicSize(sizeToken2);
        if (size1 instanceof ConstantSize) {
            if (size2 instanceof Sizes.ComponentSize) {
                return new BoundedSize(size2, setMax ? null : size1, setMax ? size1 : null);
            } else {
                throw new IllegalArgumentException("Bounded sizes must not be both constants.");
            }
        } else if (size2 instanceof ConstantSize) {
            return new BoundedSize(size1, setMax ? null : size2, setMax ? size2 : null);
        } else {
            throw new IllegalArgumentException("Bounded sizes must not be both logical.");
        }
    }

    private Size decodeAtomicSize(String token) {
        Sizes.ComponentSize componentSize = Sizes.ComponentSize.valueOf(token);
        return (Size) (componentSize != null ? componentSize : ConstantSize.valueOf(token, this.isHorizontal()));
    }

    private double decodeResize(String token) {
        if (!token.equals("g") && !token.equals("grow")) {
            if (!token.equals("n") && !token.equals("nogrow") && !token.equals("none")) {
                if ((token.startsWith("grow(") || token.startsWith("g(")) && token.endsWith(")")) {
                    int leftParen = token.indexOf(40);
                    int rightParen = token.indexOf(41);
                    String substring = token.substring(leftParen + 1, rightParen);
                    return Double.parseDouble(substring);
                } else {
                    throw new IllegalArgumentException("The resize argument '" + token + "' is invalid. " + " Must be one of: grow, g, none, n, grow(<double>), g(<double>)");
                }
            } else {
                return 0.0;
            }
        } else {
            return 1.0;
        }
    }

    public final String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.defaultAlignment);
        buffer.append(":");
        buffer.append(this.size.toString());
        buffer.append(':');
        if (this.resizeWeight == 0.0) {
            buffer.append("noGrow");
        } else if (this.resizeWeight == 1.0) {
            buffer.append("grow");
        } else {
            buffer.append("grow(");
            buffer.append(this.resizeWeight);
            buffer.append(')');
        }

        return buffer.toString();
    }

    public final String toShortString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.defaultAlignment.abbreviation());
        buffer.append(":");
        buffer.append(this.size.toString());
        buffer.append(':');
        if (this.resizeWeight == 0.0) {
            buffer.append("n");
        } else if (this.resizeWeight == 1.0) {
            buffer.append("g");
        } else {
            buffer.append("g(");
            buffer.append(this.resizeWeight);
            buffer.append(')');
        }

        return buffer.toString();
    }

    abstract boolean isHorizontal();

    final int maximumSize(Container container, List components, FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure, FormLayout.Measure defaultMeasure) {
        return this.size.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure);
    }

    static {
        VALUES = new DefaultAlignment[]{LEFT_ALIGN, RIGHT_ALIGN, TOP_ALIGN, BOTTOM_ALIGN, CENTER_ALIGN, FILL_ALIGN};
    }

    public static final class DefaultAlignment implements Serializable {
        private final transient String name;
        private static int nextOrdinal = 0;
        private final int ordinal;

        private DefaultAlignment(String name) {
            this.ordinal = nextOrdinal++;
            this.name = name;
        }

        private static DefaultAlignment valueOf(String str, boolean isHorizontal) {
            if (!str.equals("f") && !str.equals("fill")) {
                if (!str.equals("c") && !str.equals("center")) {
                    if (isHorizontal) {
                        if (!str.equals("r") && !str.equals("right")) {
                            return !str.equals("l") && !str.equals("left") ? null : FormSpec.LEFT_ALIGN;
                        } else {
                            return FormSpec.RIGHT_ALIGN;
                        }
                    } else if (!str.equals("t") && !str.equals("top")) {
                        return !str.equals("b") && !str.equals("bottom") ? null : FormSpec.BOTTOM_ALIGN;
                    } else {
                        return FormSpec.TOP_ALIGN;
                    }
                } else {
                    return FormSpec.CENTER_ALIGN;
                }
            } else {
                return FormSpec.FILL_ALIGN;
            }
        }

        public String toString() {
            return this.name;
        }

        public char abbreviation() {
            return this.name.charAt(0);
        }

        private Object readResolve() {
            return FormSpec.VALUES[this.ordinal];
        }
    }
}
