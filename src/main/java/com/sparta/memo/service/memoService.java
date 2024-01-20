package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.memoRepositary.memoRepository;
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
@Component
public class memoService {
    private final memoRepository repo; // repository
    public memoService(memoRepository jdbcTemplate) {
        repo = jdbcTemplate; // repository
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) { // create memo service
        Memo memo = new Memo(requestDto);
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체
        memo = repo.createkeyHolder(memo,keyHolder);
        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        return repo.repoGetAllMemo();
    }

    public Long putMemo(long id, MemoRequestDto requestDto) {
        return repo.putMemo(requestDto,id);
    }
    public Long deleteMemo(long id) {
        // 해당 메모가 DB에 존재하는지 확인
        return repo.deleteMemo(id);
    }
}
