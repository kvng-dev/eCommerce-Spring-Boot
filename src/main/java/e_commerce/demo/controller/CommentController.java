package e_commerce.demo.controller;

import e_commerce.demo.dto.CommentDTO;
import e_commerce.demo.model.User;
import e_commerce.demo.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/product/{productId}")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long productId,
                                                 @AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody @Valid CommentDTO commentDTO) {
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(commentService.addComment(productId, userId, commentDTO));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(commentService.getCommentsByProduct(productId));
    }
}
