package akatsuki.moodholic.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RequestPostDiary {
    private int memberId;
    private String content;
    private int status;
}
