package com.zxw.lock;

import lombok.*;

/**
 * @author zxw
 * @date 2020/7/2 21:23
 */
@Data
@ToString
@EqualsAndHashCode
public class ZNodeName implements Comparable<ZNodeName> {
    private final String name;
    private String prefix;
    private int sequence = -1;

    public ZNodeName(String name) {
        this.name = name;
        this.prefix = name;
        int idx = name.lastIndexOf('-');
        if (idx >= 0) {
            this.prefix = name.substring(0, idx);
            try {
                this.sequence = Integer.parseInt(name.substring(idx + 1));
                // If an exception occurred we misdetected a sequence suffix,
                // so return -1.
            } catch (NumberFormatException e) {
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
    }

    public int compareTo(ZNodeName o) {
        int answer = this.prefix.compareTo(o.prefix);
        if (answer == 0) {
            int s1 = this.sequence;
            int s2 = o.sequence;
            if (s1 == -1 && s2 == -1) {
                return this.name.compareTo(o.name);
            }
            answer = s1 == -1 ? 1 : s2 == -1 ? -1 : s1 - s2;
        }
        return answer;
    }
}
