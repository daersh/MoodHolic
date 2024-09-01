package akatsuki.moodholic.service;

import akatsuki.moodholic.domain.Diary;
import akatsuki.moodholic.dto.ResponseDiaryPost;
import akatsuki.moodholic.repository.DiaryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class DiaryServiceImpl2 implements DiaryService{
    private final DiaryDAO diaryDAO;

    @Autowired
    public DiaryServiceImpl2(DiaryDAO diaryDAO) {
        this.diaryDAO = diaryDAO;
    }

    @Override
    @Transactional
    public ResponseDiaryPost postDiary(Diary requestDiary) {
        String fullDate = requestDiary.getDate();
        // 날짜에서 시간 제거하고 LocalDate로 변환
        LocalDate entryDate = extractDateWithoutTime(fullDate);
        if (entryDate != null) {
            String redisKey = "diary:" + requestDiary.getMember().getMemberId() + ":" + entryDate.toString();
        }

        Diary findDiary = diaryDAO.findByMemberMemberIdAndDate(requestDiary.getMember().getMemberId(),requestDiary.getDate());
        if(findDiary!=null){
            requestDiary.setDiaryId(findDiary.getDiaryId());
            if(findDiary.getStatus()==1){
                System.out.println("이미 존재하여 생성하지 않습니다.");
                return new ResponseDiaryPost("중복");
            }
        }
        requestDiary=diaryDAO.save(requestDiary);
        if(requestDiary.getStatus()==0){
            System.out.println("임시 저장 완료");
            return new ResponseDiaryPost("임시저장");
        }
        String content = requestDiary.getContent();
        String prompt = getPrompt(requestDiary, content);
        ResponseDiaryPost returnValue = new ResponseDiaryPost(prompt, requestDiary.getDiaryId());
        return returnValue;
    }
    @Override
    public Diary findDiary(int diaryId){
        return diaryDAO.findById(diaryId).orElseThrow(() -> new IllegalArgumentException("해당 게시물 없음!!!"));
    }

    @Override
    public List<Diary> getMemberDiaries(long memberId) {
        return diaryDAO.findAllByMemberMemberId(memberId);
    }

    @Override
    public String deleteDiary(int diaryId){
        diaryDAO.deleteById(diaryId);
        return "삭제 완료";
    }

    private static String getPrompt(Diary requestDiary, String content) {
        String summary = requestDiary.getSummary();
        String prompt = getString(content, summary);
        return prompt;
    }

    private static String getString(String content, String summary) {
        String prompt ="너는 심리학자야. \n" +
                "다음 다이어리를 읽고 분석하고 출력은 아래 응답 양식으로만 대답해.\n" +
                "\n" +
                "다이어리는 다음과 같아\n" +
                "오늘 기분 한줄 요약: "+ summary + "\n" +
                "내용: "+ content +
                "\n\n" +
                "응답 양식은 예시는 다음과 같아. (- : 두 특수기호는 문자열 파싱으로 구분짓기 위한 것이므로 참고할 것.장르, 카테고리는 하나만 보여준다.)\n" +
                "분석 점수(1~10): 5\n" +
                "조언 및 인용구 한 줄: 당신도 할 수 있습니다.\n" +
                "추천 영화(영화이름,장르): 인사 이야기 - 로맨스\n" +
                "추천 음악(음악이름,가수,장르): 봄날 - 방탄소년단 - 힙합 \n" +
                "추천 음식(음식이름,메뉴카테고리(한식,양식,중식,일식,아시안), 맵기(0~3)): 된장찌개 - 한식 - 1";
        return prompt;
    }

    @Override
    public List<Diary> findAllByMemberOrderByDateAsc(long memberId){
        return diaryDAO.findAllByMemberMemberIdOrderByDateAsc(memberId);
    }

    @Override
    public Long getMemberDiaryCnt(long memberId){
        return diaryDAO.countByMemberMemberId(memberId);
    }

    @Override
    public Diary getDiaryByDiaryId(int diaryId) {
        return diaryDAO.findById(diaryId).orElseThrow();
    }

    // 날짜에서 시간 제거하고 LocalDate로 변환하는 메서드
    private LocalDate extractDateWithoutTime(String fullDate) {
        try {
            return LocalDate.parse(fullDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
        } catch (DateTimeParseException e) {
            return null; // 날짜 파싱 실패 시 null 반환
        }
    }
}
