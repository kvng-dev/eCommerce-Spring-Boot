package e_commerce.demo.service;

import e_commerce.demo.dto.CommentDTO;
import e_commerce.demo.exception.ResourceNotFoundException;
import e_commerce.demo.mapper.CommentMapper;
import e_commerce.demo.model.Comment;
import e_commerce.demo.model.Product;
import e_commerce.demo.model.User;
import e_commerce.demo.repository.CommentRepository;
import e_commerce.demo.repository.ProductRepository;
import e_commerce.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CommentMapper commentMapper;

    public CommentDTO addComment(Long productId, Long userId, CommentDTO commentDTO) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setProduct(product);
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    public List<CommentDTO> getCommentsByProduct(Long productId) {

        List<Comment> comments = commentRepository.findByProductId(productId);
        return comments.stream().map(commentMapper::toDto)
                .collect(toList());
    }

        public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
