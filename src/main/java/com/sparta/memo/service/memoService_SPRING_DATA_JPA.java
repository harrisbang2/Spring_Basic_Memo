package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.memoRepositary.MemoRepository_Spring_Data_JPA;
import com.sparta.memo.memoRepositary.memoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.context.ApplicationContext;

@Component
public class memoService_SPRING_DATA_JPA {
    private final MemoRepository_Spring_Data_JPA repo; // repository
    public memoService_SPRING_DATA_JPA(ApplicationContext applicationContext) {
        MemoRepository_Spring_Data_JPA repo = applicationContext.getBean(MemoRepository_Spring_Data_JPA.class);
        this.repo = repo; // repository
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) { // create memo service
        Memo memo = new Memo(requestDto);
        // DB 저장
        memo = repo.save(memo);
        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {

        return repo.findAll().stream().map(MemoResponseDto::new).toList();
    }//
    @Transactional
    public Long UpdateMemo(long id, MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);
        // memo 내용 수정
        // 객체에 병경하면 자동 Dirty Check 함,
        memo.update(requestDto);
        return id;
    }

    public Long deleteMemo(long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);
        repo.delete(memo);
        return id;
    }
    private  Memo findMemo(Long id){
        Memo memo =  repo.findById(id).orElseThrow(()->
                new IllegalArgumentException("선택하신 메모는 존재하지 않아요")
        );
        return memo;
    }
}
