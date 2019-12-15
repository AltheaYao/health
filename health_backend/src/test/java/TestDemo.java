import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestDemo {
    @Test
    public void show(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH+1,0);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));

    }
}
