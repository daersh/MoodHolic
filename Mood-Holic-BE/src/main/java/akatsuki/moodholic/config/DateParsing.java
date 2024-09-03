package akatsuki.moodholic.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateParsing {

    public static LocalDate strToLdt(String str){
        if (!Objects.equals(str, ""))
            return LocalDate.parse(str,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return null;
    }

    public static String LdtToStr(LocalDateTime localDateTime){
        if (localDateTime!=null)
            return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return null;
    }

}
