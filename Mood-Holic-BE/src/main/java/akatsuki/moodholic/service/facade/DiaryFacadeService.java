package akatsuki.moodholic.service.facade;

import akatsuki.moodholic.domain.*;
import akatsuki.moodholic.dto.ResponseDiary;
import akatsuki.moodholic.dto.ResponseDiaryPost;
import akatsuki.moodholic.etc.DataParse;
import akatsuki.moodholic.request.RequestPostDiary;
import akatsuki.moodholic.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <h1>Diary-Pasade-Service</h1>
 * 다이어리 컨트롤러 로직을 처리하는 서비스
 *
 * <ul>
 *     <li>getDiary: 하나의 다이어리의 상세조회로 GPT가 응답하였던 추천 항목, 조언도 함께 조회</li>
 *     <li>getMemberDiary: 맴버의 모든 다이어리 조회</li>
 *     <li>deleteDiary: 하나의 다이어리와 해당 다이어리와 연관된 모든 항목 삭제 </li>
 *     <li>postDiary: 다이어리 저장하기 위한 로직으로 날짜에 따라 중복 처리, 저장, 임시저장 로직, GPT 요청, 응답 전달</li>
 *     <li>saveGPTResponse: 응답한 GPT의 추천 항목이 이미 존재하는 지 확인 후 없으면 해당 메뉴 추가</li>
 *     <li>save: 다이어리와 연결</li>
 *
 *</ul>
 * */

@Service
public class DiaryFacadeService {
    private final DiaryService diaryService;
//    private final ChatGPTService chatGPTService;
    private final DiaryFoodService diaryFoodService;
    private final DiaryEmotionService diaryEmotionService;
    private final DiaryMovieService diaryMovieService;
    private final DiaryMusicService diaryMusicService;
    private final CommentService commentService;
    private final FoodService foodService;
    private final MovieService movieService;
    private final MusicService musicService;
    @Autowired
    public DiaryFacadeService(DiaryService diaryService
//                              ,ChatGPTService chatGPTService
            , DiaryFoodService diaryFoodService, DiaryEmotionService diaryEmotionService, DiaryMovieService diaryMovieService, DiaryMusicService diaryMusicService, CommentService commentService, FoodService foodService, MovieService movieService, MusicService musicService) {
        this.diaryService = diaryService;
//        this.chatGPTService = chatGPTService;
        this.diaryFoodService = diaryFoodService;
        this.diaryEmotionService = diaryEmotionService;
        this.diaryMovieService = diaryMovieService;
        this.diaryMusicService = diaryMusicService;
        this.commentService = commentService;
        this.foodService = foodService;
        this.movieService = movieService;
        this.musicService = musicService;
    }

    public ResponseDiary getDiary(int diaryId) {
        Diary diary = diaryService.findDiary(diaryId);
        DiaryEmotion emotion = diaryEmotionService.findEmotionByDiaryId(diaryId);
        DiaryFood food = diaryFoodService.findFoodByDiaryId(diaryId);
        DiaryMovie movie = diaryMovieService.findMovieByDiaryId(diaryId);
        DiaryMusic music = diaryMusicService.findMusicByDiaryId(diaryId);
        Comment comment = commentService.findCommentByDiaryId(diaryId);
        String emotionValue= new String();
        emotionValue = parseEmotion(emotion, emotionValue);

        return new ResponseDiary(diary,emotionValue,food.getFoodId(),movie.getMovieId(),music.getMusicId(),comment);
    }

    private static String parseEmotion(DiaryEmotion emotion, String emotionValue) {
        if(emotion !=null) {
            if (emotion.getEmotionId() < 4)  emotionValue = "나쁨";
            else if(emotion.getEmotionId()<7) emotionValue ="보통";
            else emotionValue ="좋음";
        }
        return emotionValue;
    }

    public List<Diary> getMemberDiaries(long memberId){
        return diaryService.getMemberDiaries(memberId);
    }

    public Long getMemberDiaryCnt(long memberId){
        return diaryService.getMemberDiaryCnt(memberId);
    }

    @Transactional
    public String deleteDiary(int diaryId) {
        diaryFoodService.delete(diaryId);
        diaryEmotionService.delete(diaryId);
        diaryMusicService.delete(diaryId);
        diaryMovieService.delete(diaryId);
        commentService.delete(diaryId);
        diaryService.deleteDiary(diaryId);
        return "삭제 완료";
    }

    @Transactional
    public ResponseDiaryPost postDiary(RequestPostDiary requestdiary) {
        ResponseDiaryPost prompt=diaryService.postDiary(requestdiary);

        if(!prompt.getResponse().equals("임시저장") && !prompt.getResponse().equals("중복")) {
//            DataParse response = chatGPTService.Response(prompt.getResponse());
            DataParse response = null;
//            saveGPTResponse(requestdiary.getMemberId(), response, requestdiary);
            return null;
        }
        return prompt;
    }

    private int saveGPTResponse(long memberId, DataParse response, Diary diary) {
        System.out.println("response = " + response);
        Food food = foodService.findFoodByFoodName(response.getFood().getFoodName());
        Movie movie = movieService.findByMovieName(response.getMovie().getMovieName());
        Music music = musicService.findByMusicName(response.getMusic().getMusicName());
        if(food ==null)
            food=foodService.saveFood(response.getFood());
        if(movie==null)
            movie=movieService.saveMovie(response.getMovie());
        if(music==null)
            music=musicService.saveMusic(response.getMusic());
        save(response, diary, food, movie, music);
        return 0;
    }

    private void save(DataParse response, Diary diary, Food food, Movie movie, Music music) {
        diaryFoodService.saveDiaryFood(new DiaryFood(diary, food,false));
        diaryMovieService.saveDiaryMovie(new DiaryMovie(diary, movie,false));
        diaryMusicService.saveDiaryMusic(new DiaryMusic(diary, music,false));
        diaryEmotionService.saveDiaryEmotion(new DiaryEmotion(diary, response.getEmotionScore()));
        commentService.saveComment(new Comment(diary, response.getComment().getCommentContent()));
    }


    public String putMemberLike(int diaryId, boolean food, boolean music, boolean movie) {
        diaryMovieService.likeMovie(diaryId,movie);
        diaryFoodService.likeFood(diaryId,food);
        diaryMusicService.likeMusic(diaryId,music);

        return "좋아요 입력 완료";
    }

    public void putMemberMovieLike(int diaryId) {
        diaryMovieService.likeMovie(diaryId, true);
    }
    public void putMemberFoodLike(int diaryId){
        diaryFoodService.likeFood(diaryId,true);
    }
    public void putMemberMusicLike(int diaryId){
        diaryMusicService.likeMusic(diaryId,true);
    }

}
