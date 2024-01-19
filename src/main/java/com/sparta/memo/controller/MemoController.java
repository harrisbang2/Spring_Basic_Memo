package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // Memo Max ID Check
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);

        // DB 저장
        memoList.put(memo.getId(), memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemo() {
        // Map To List
        List<MemoResponseDto> responseList = memoList.values().stream()
                .map(MemoResponseDto::new).toList();
        return responseList;
    }

    // 업데이트
    @PutMapping("/memos/{id}")
    // json 받아올때는 @RequestBody
    public long Updatamemp(@PathVariable long id, @RequestBody MemoRequestDto requestDto) {
        // 해당 메모가 존제 하는지.
        if (memoList.containsKey(id)) {
            Memo memo = memoList.get(id);
            // 메모 수정
            memo.update(requestDto);
            return memo.getId();
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재 하지 않아요");
        }
    }

    // delete 지우는거
    @DeleteMapping("/memos/{id}")
    public long deleteMemo(@PathVariable long id) {
        // 해당 메모가 db 에 존샣라는지
        if (memoList.containsKey(id)) {
            // 메모 삭제
            memoList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재 하지 않아요");
        }
    }

}