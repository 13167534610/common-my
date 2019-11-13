package com.common.funciton.delayqueue;

import com.common.funciton.DateUtil;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2019/11/8 18:49
 */
public class DelayMsg implements Delayed {
    private final static long DELAY = 15*60*1000L;//默认延迟15分钟

    private final String orderId;//订单号

    private final long startTime ;//开始时间

    private final long expire ;//到期时间

    private final Date createTime; //创建时间

    private final String delayMsg;//订单其他信息JSON方式保存，备用字段

    public DelayMsg(String orderId, long startTime, long n,  String delayMsg) {
        this.orderId = orderId;
        this.startTime = startTime;
        this.expire = startTime + (n * 1000);
        this.createTime = new Date();
        this.delayMsg = delayMsg;
    }

    public static long getDELAY() {
        return DELAY;
    }

    public String getOrderId() {
        return orderId;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getExpire() {
        return expire;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getDelayMsg() {
        return delayMsg;
    }

    /**
     * Returns the remaining delay associated with this object, in the
     * given time unit.
     *
     * @param unit the time unit
     * @return the remaining delay; zero or negative values indicate
     * that the delay has already elapsed
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis() , TimeUnit.MILLISECONDS);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) -o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "DelayMsg{" +
                "orderId='" + orderId + '\'' +
                ", startTime=" + DateUtil.dateToStr(new Date(startTime), DateUtil.FORMATE_4) +
                ", expire=" + DateUtil.dateToStr(new Date(expire), DateUtil.FORMATE_4) +
                ", createTime=" + DateUtil.dateToStr(createTime, DateUtil.FORMATE_4) +
                ", delayMsg='" + delayMsg + '\'' +
                '}';
    }
}
