package com.sparta.memo.memoRepositary;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
@Component
public class memoRepository {
    Memo memo; // memo
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setmemoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;// repository
    }


    public Memo createkeyHolder(Memo memo, KeyHolder keyHolder) {
        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, memo.getUsername());
                    preparedStatement.setString(2, memo.getContents());
                    return preparedStatement;
                },
                keyHolder);
        Long id = keyHolder.getKey().longValue();
        memo.setId(id);
        return memo;
    }

    public List<MemoResponseDto> repoGetAllMemo() {
        String sql = "SELECT * FROM memo";
        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                return new MemoResponseDto(id, username, contents);
            }
        });
    }

    public Long putMemo(MemoRequestDto requestDto, long id) {
        // memo 내용 수정
        // 해당 메모가 DB에 존재하는지 확인
        memo = findById(id);
        if(memo != null) {
        String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);
        return id;
        }
        else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }


    ////////////////////////////////////////////////////////////////////////
    private Memo findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM memo WHERE id = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                memo = new Memo();
                memo.setUsername(resultSet.getString("username"));
                memo.setContents(resultSet.getString("contents"));
                return memo;
            }
            else {
                return null;
            }
        }, id);
    }

    public Long deleteMemo(long id) {
        memo = findById(id);
        if(memo != null) {
            // memo 삭제
            String sql = "DELETE FROM memo WHERE id = ?";
            jdbcTemplate.update(sql, id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}
