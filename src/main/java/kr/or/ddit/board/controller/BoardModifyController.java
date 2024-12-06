package kr.or.ddit.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.board.exception.BoardException;
import kr.or.ddit.board.exception.WriterAuthenticationException;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.vo.FreeBoardVO;
import kr.or.ddit.validate.UpdateGroup;

@Controller
@RequestMapping("/board")
public class BoardModifyController {
	public static final String MODELNAME = "targetBoard";
	@Autowired
	private BoardService service;

	@GetMapping("{boNo}/auth")
	public String authenticateForm() {
		return "board/authForm";
	}

	@PostMapping("{boNo}/auth")
	public String authenticateWriter(FreeBoardVO inputData, RedirectAttributes redirectAttributes) {
		try {
			FreeBoardVO authenticated = service.writerAuthenticate(inputData.getBoNo(), inputData.getBoPass());
			redirectAttributes.addFlashAttribute(MODELNAME, authenticated);
			return "redirect:/board/{boNo}/edit";
		} catch (WriterAuthenticationException e) {
			redirectAttributes.addFlashAttribute("message", "작성자 인증 오류");
			return "redirect:/board/{boNo}/auth";
		}
	}

	@GetMapping("{boNo}/edit")
	public String form(Model model) {
		if (model.containsAttribute(MODELNAME)) {
			return "board/boardEdit";
		} else {
			return "redirect:/board/{boNo}/auth";
		}
	}

	@PostMapping("{boNo}/edit")
	public String update(@Validated(UpdateGroup.class) @ModelAttribute(MODELNAME) FreeBoardVO board,
			BindingResult errors, RedirectAttributes redirectAttributes) {
		
		String lvn = null;
		if (!errors.hasErrors()) {
			try {
				service.modifyBoard(board);
				lvn = "redirect:/board/{boNo}";
			} catch (BoardException e) {
				redirectAttributes.addFlashAttribute(MODELNAME, board);
				redirectAttributes.addFlashAttribute("message", e.getMessage());
				lvn = "redirect:/board/{boNo}/auth";
			}
		} else {
			redirectAttributes.addFlashAttribute(MODELNAME, board);
			redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX, errors);
			return "redirect:/board/{boNo}/edit";
		}
		board.setAtchFile(null);
		return lvn;
	}
	
	@DeleteMapping("{boNo}")
	public String delete(@PathVariable int boNo, @RequestParam String password , RedirectAttributes redirectAttributes) {
		try {
			service.removeBoard(boNo, password);
			return "redirect:/board";
		}catch (BoardException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/board/{boNo}/auth";
		}
	}
}
