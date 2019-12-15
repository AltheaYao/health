import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.junit.Test;
public class Testdemo {
    @Test
    public void show (){
        Calendar cal = Calendar.getInstance();//创建一个日历对象,获取的时候默认就是今日日期
        cal.setFirstDayOfWeek(Calendar.MONDAY);//中国周的第一天是周1,但是美国是周日为第一天,所以我们要先把设置改过来
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);//给这个对象设置,这个周的周一日期
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));//获取对象转换成字符串输出

    }
    @Test
    public void show2(){
        String a = new String("abc");
        String c ="abc";
        System.out.println(a==c);
    }
}
