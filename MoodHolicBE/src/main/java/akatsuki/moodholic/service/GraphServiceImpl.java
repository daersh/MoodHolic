package akatsuki.moodholic.service;

import akatsuki.moodholic.domain.Diary;
import akatsuki.moodholic.domain.DiaryEmotion;
import akatsuki.moodholic.domain.Emotion;
import akatsuki.moodholic.repository.DiaryDAO;
import akatsuki.moodholic.repository.DiaryEmotionDAO;
import akatsuki.moodholic.repository.MemberDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GraphServiceImpl implements GraphService{

    DiaryDAO diaryDAO;
    DiaryEmotionDAO diaryEmotionDAO;
    double sum =0;
    double cnt =0;
    String past;

    @Autowired
    public GraphServiceImpl(DiaryEmotionDAO diaryEmotionDAO,DiaryDAO diaryDAO) {
        this.diaryEmotionDAO = diaryEmotionDAO;
        this.diaryDAO = diaryDAO;
    }

     @Override
     public HashMap<String,Double> GetEmotionMonth(long memberId){
        HashMap<String,Double> returnValue = new HashMap<>();
        past= new String();
        sum=0;
        cnt=0;
        List<Diary> diaryList = diaryDAO.findAllByMemberMemberIdOrderByDateAsc(memberId);
        List<DiaryEmotion> emotionList = new ArrayList<>();


        diaryList.forEach(diary->{
            String[] date = diary.getDate().split("-");
            System.out.println("date = " + date);
            String cmpDate = date[0]+"-"+date[1];
            DiaryEmotion score = diaryEmotionDAO.findByDiaryIdDiaryId(diary.getDiaryId());
            if(score!=null) {
                if (past.equals(cmpDate)) {
                    sum += score.getEmotionId();
                    cnt++;
                } else {
                    returnValue.put(past, (sum / cnt));
                    this.past = cmpDate;
                    sum = score.getEmotionId();
                    cnt = 1;
                }
            }
        });

        return returnValue;
    }
}
