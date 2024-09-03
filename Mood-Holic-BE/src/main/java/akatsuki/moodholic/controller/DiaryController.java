package akatsuki.moodholic.controller;

import akatsuki.moodholic.domain.*;
import akatsuki.moodholic.dto.ResponseDiary;
import akatsuki.moodholic.dto.ResponseDiaryPost;
import akatsuki.moodholic.request.RequestPostDiary;
import akatsuki.moodholic.service.facade.DiaryFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diary")
@Tag(name = "다이어리 컨트롤러", description = "맴버의 다이어리 리스트, 상세 리스트")
public class DiaryController {
    DiaryFacadeService facadeService;

    @Autowired
    public DiaryController(DiaryFacadeService facadeService) {
        this.facadeService = facadeService;
    }

    @GetMapping("/{diaryId}")
    @Operation(summary = "다이어리 상세 조회", description = "한 다이어리의 정보와 GPT의 응답을 반환합니다.")
    public ResponseEntity<ResponseDiary> getDiary(@PathVariable int diaryId){
        ResponseDiary response = facadeService.getDiary(diaryId);

        return ResponseEntity.ok().body(response);
    }
    @PostMapping("")
    @Operation(summary = "다이어리 저장", description = "요청온 다이어리의 상태에 따라 임시 저장 또는 저장을 수행합니다. " +
            "저장 시 ChatGPT에 프롬프트 전달하여 GPT의 응답을 받아 DB에 저장합니다.")
    public ResponseEntity<ResponseDiaryPost> postDiary(@RequestBody RequestPostDiary diary){
        ResponseDiaryPost response = facadeService.postDiary(diary);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("{diaryId}")
    @Operation(summary = "다이어리 삭제", description = "선택한 다이어리와 다이어리와 연관된 모든 객체들을 삭제합니다.")
    public ResponseEntity<String> deleteDiary(@PathVariable int diaryId){
        String response = facadeService.deleteDiary(diaryId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{memberid}/diaries")
    @Operation(summary = "맴버의 다이어리 리스트 조회", description = "멤버가 작성한 모든 다이어리를 반환합니다.")
    public ResponseEntity<List<Diary> > getMemberDiaries(@PathVariable long memberid){
        List<Diary> response = facadeService.getMemberDiaries(memberid);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{memberId}/diaries-cnt")
    @Operation(summary = "맴버의 다이어리 리스트 갯수 조회", description = "멤버가 작성한 모든 다이어리 갯수를 반환합니다.")
    public ResponseEntity<Long > getMemberDiaryCnt(@PathVariable long memberId){
        Long response = facadeService.getMemberDiaryCnt(memberId);
        return ResponseEntity.ok().body(response);
    }


    @PutMapping("/{diaryId}/like")
    @Operation(summary = "멤버 좋아요 체크", description = "GPT가 추천한 음식, 음악, 영화를 좋아요 누를 경우 이를 DB에 반환합니다.")
    public ResponseEntity<String> putMemberLike(@PathVariable int diaryId
            , @RequestParam(name = "food") boolean food
            , @RequestParam(name = "music") boolean music
            , @RequestParam(name = "movie") boolean movie) {
        String returnValue = facadeService.putMemberLike(diaryId,food,music,movie);
        return ResponseEntity.ok().body(returnValue);
    }
    @PutMapping("/{diaryId}/movie-like")
    public ResponseEntity putMemberMovieLike(@PathVariable int diaryId){
        facadeService.putMemberMovieLike(diaryId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{diaryId}/food-like")
    public ResponseEntity putMemberFoodLike(@PathVariable int diaryId){
        facadeService.putMemberFoodLike(diaryId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{diaryId}/music-like")
    public ResponseEntity putMemberMusicLike(@PathVariable int diaryId){
        facadeService.putMemberMusicLike(diaryId);
        return ResponseEntity.ok().build();
    }
}
