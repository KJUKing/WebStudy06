package kr.or.ddit.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.board.exception.BoardException;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.vo.FreeBoardVO;
import kr.or.ddit.validate.InsertGroup;

@Controller
@RequestMapping("/board/new")
public class BoardInsertController {
	public static final String MODELNAME = "newBoard";

	@Autowired
	private BoardService service;

	@ModelAttribute(MODELNAME)
	public FreeBoardVO board() {
		return new FreeBoardVO();
	}

	@GetMapping
	public String form() {
		return "board/boardForm";
	}

	@PostMapping
	public String insert(
			@Validated(InsertGroup.class) @ModelAttribute(MODELNAME) FreeBoardVO newBoard
			, BindingResult errors
			, RedirectAttributes redirectAttributes
	) {
		try {
			String lvn = null;
			if (!errors.hasErrors()) {
				service.createBoard(newBoard);
				lvn = "redirect:/board/" + newBoard.getBoNo();
			} else {
				redirectAttributes.addFlashAttribute(MODELNAME, newBoard);
				redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + MODELNAME, errors);
				lvn = "redirect:/board/new";
			}
			return lvn;
		}catch (Throwable e) {
			throw new BoardException(e);
		}
	}
}
