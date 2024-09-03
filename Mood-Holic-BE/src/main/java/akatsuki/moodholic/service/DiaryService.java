package akatsuki.moodholic.service;

import akatsuki.moodholic.domain.Diary;
import akatsuki.moodholic.dto.ResponseDiary;
import akatsuki.moodholic.dto.ResponseDiaryPost;
import akatsuki.moodholic.request.RequestPostDiary;

import java.util.List;

public interface DiaryService {

    ResponseDiaryPost postDiary(RequestPostDiary diary);

    String deleteDiary(int diaryId);

    List<Diary> getMemberDiaries(long memberid);

    Diary findDiary(int diaryId);

    List<Diary> findAllByMemberOrderByDateAsc(long memberId);

    Long getMemberDiaryCnt(long memberId);

    Diary getDiaryByDiaryId(int diaryId);
}
