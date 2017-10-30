package io.github.tiagofrbarbosa.fleekard.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * Created by tfbarbosa on 29/10/17.
 */
public class MyUtilsTest {

    @Test
    public void longToDate() throws Exception {

        long time = System.currentTimeMillis();
        String format = "dd/MM/yy HH:mm";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(time);

        String mDate = simpleDateFormat.format(date);

        System.out.println(mDate);

        assertNotNull(mDate);
    }

}