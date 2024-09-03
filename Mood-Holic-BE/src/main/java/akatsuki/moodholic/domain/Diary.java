package akatsuki.moodholic.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="diary")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Diary implements Comparable<Diary>{

    @Id
    @Column(name = "diary_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int diaryId;

    @Column(name = "date")
    private String date;
    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private int status;

    @Column(name = "img_path")
    private String imgPath;
    @Column(name = "summary")
    private String summary;

    @ManyToOne
    @JoinColumn(name= "member_id")
    private Member member;

    public Diary(String date, String content, int status, String imgPath, String summary, Member member) {
        this.date = date;
        this.content = content;
        this.status = status;
        this.imgPath = imgPath;
        this.summary = summary;
        this.member = member;
    }


    @Override
    public int compareTo(Diary o) {
        return this.diaryId - o.diaryId;
    }
}
