//package akatsuki.moodholic.service.facade;
//
//import akatsuki.moodholic.domain.Diary;
//import akatsuki.moodholic.domain.Member;
//import akatsuki.moodholic.dto.ResponseDiary;
//import akatsuki.moodholic.dto.ResponseDiaryPost;
//import akatsuki.moodholic.repository.DiaryDAO;
//import akatsuki.moodholic.repository.MemberDAO;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class DiaryFacadeServiceTest {
//    @Autowired
//    DiaryFacadeService diaryFacadeService;
//    @Autowired
//    MemberDAO memberDAO;
//
//    int diaryId=10;
//    private long memberId=9;
//    /*given*/
//    /*when*/
//    /*then*/
//
//    Diary Testdiary = new Diary("2025-01-01",
//            "너 내 동료가 돼라",0,null,"원피스같은 기분", new Member(1));
//
//    @BeforeEach
//    void before(){
//        Diary Testdiary = new Diary("2025-01-01",
//                "너 내 동료가 돼라",0,null,"원피스같은 기분", new Member(1));
//    }
//
//    @Test
//    @DisplayName("다이어리 정보 불러오기")
//    void getDiary(){
//        /*given*/
//        diaryId=10;
//        /*when*/
//        ResponseDiary responseDiary = diaryFacadeService.getDiary(diaryId);
//        /*then*/
//        assertEquals(diaryId,responseDiary.getDiary().getDiaryId());
//    }
//    @Test
//    @DisplayName("삭제되거나 존재하지 않는 다이어리 정보 불러오기")
//    void getNullDiary(){
//        /*given*/
//        diaryId=1;
//        /*then*/
//        assertThrows(IllegalArgumentException.class, ()->{
//            /*when*/
//            diaryFacadeService.getDiary(diaryId);
//        });
//    }
//
//
//    //    다이어리 임시저장
//    @Test
//    @DisplayName("다이어리 임시저장")
//    void postDiary1(){
//        /*given*/
//        Member member = memberDAO.findById(memberId).orElseThrow();
//        Testdiary.setMember(member);
//        /*when*/
//        ResponseDiaryPost text = diaryFacadeService.postDiary(Testdiary);
//        /*then*/
//        assertEquals("임시저장",text.getResponse());
//    }
//    //    다이어리 저장
//    @Test
//    @DisplayName("다이어리 저장")
//    void postDiary2(){
//        /*given*/
//        Member member = memberDAO.findById(memberId).orElseThrow();
//        Testdiary.setMember(member);
//        Testdiary.setStatus(1);
//        /*when*/
//        ResponseDiaryPost text = diaryFacadeService.postDiary(Testdiary);
//        /*then*/
//        assertNotEquals("중복",text.getResponse());
//        assertNotEquals("임시저장",text.getResponse());
//    }
//    //    다이어리 중복저장
//    @Test
//    @DisplayName("다이어리 중복저장")
//    void postDiary3(){
//        /*given*/
//        ResponseDiary diary = diaryFacadeService.getDiary(diaryId);
//        /*when*/
//        ResponseDiaryPost text = diaryFacadeService.postDiary(diary.getDiary());
//        /*then*/
//        assertEquals("중복",text.getResponse());
//    }
//
//    @Test
//    @DisplayName("다이어리 제거")
//    void deleteDiary(){
//        /*given*/
//        diaryId=1;
//        /*when*/
//        String response = diaryFacadeService.deleteDiary(diaryId);
//        /*then*/
//        assertEquals("삭제 완료",response);
//    }
//
//    @Test
//    @DisplayName("추천 항목 좋아요 추가")
//    void putMemberLike(){
//        diaryId=10;
//        boolean food=true;
//        boolean music=true;
//        boolean movie=true;
//        String response = diaryFacadeService.putMemberLike(diaryId,food,music,movie);
//
//        assertEquals("좋아요 입력 완료",response);
////        assertEquals(food);
//    }
//}