package com.baomidou.mybatisx.util;

import java.util.stream.Collector;

public class MybatisXCollectors {

    public static Collector<CharSequence, MultiStringJoiner, String> joining(String delimiter) {
        return joining(delimiter, 3);
    }

    public static Collector<CharSequence, MultiStringJoiner, String> joining(String delimiter,
                                                                             int step) {
        return joining(delimiter, "", "", step);
    }


    public static Collector<CharSequence, MultiStringJoiner, String> joining(String delimiter,
                                                                             String prefix,
                                                                             String suffix,
                                                                             int step) {
        return Collector.of(() -> new MultiStringJoiner(delimiter, prefix, suffix, "\n", step),
                MultiStringJoiner::add,
                MultiStringJoiner::merge,
                MultiStringJoiner::toString);
    }
}
