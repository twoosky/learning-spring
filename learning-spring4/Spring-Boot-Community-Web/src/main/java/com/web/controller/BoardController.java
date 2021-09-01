package com.web.controller;

import com.web.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/board")     // API URL 경로를 `/board`로 정의
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping({"", "/"})    // 매핑 경로를 중괄호를 사용해 여러 개 받을 수 있음
    // @RequestParam("가져올 데이터 이름")[데이터 타입][가져온 데이터를 담을 변수]
    public String board(@RequestParam(value="idx", defaultValue = "0") Long idx, Model model){
        model.addAttribute("board", boardService.findBoardByIdx(idx));
        return "/board/form";
    }

    @GetMapping("/list")
    // @PageableDefault 어노테이션의 파라미터인 size, sort, direction등을 사용하여 페이징 처리에 관한 규약을 정의할 수 있습니다.
    public String list(@PageableDefault Pageable pageable, Model model){
        model.addAttribute("boardList", boardService.findBoardList(pageable));
        return "/board/list";      // `src/resources/templates`를 기준으로 데이터를 바인딩할 타깃의 뷰 경로를 지정
    }


}
