package bsshelper.localservice.externalcustomdata.service;

import bsshelper.localservice.externalcustomdata.entity.Comment;
import bsshelper.localservice.externalcustomdata.repository.CommentRepository;
import bsshelper.localservice.externalcustomdata.service.aop.CachePopulator;
import bsshelper.localservice.externalcustomdata.service.aop.RefreshCache;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService implements CachePopulator {
    private final CommentRepository commentRepository;

    @Override
    @PostConstruct
    public void populate() {
        Map<String, String> commentsMap = new ConcurrentHashMap<>();

        List<Comment> list = getAllComment();
        for (Comment com: list) {
            commentsMap.put(com.getComment(), com.getComment());
        }

        CustomDataService.CommentsMap.keySet().retainAll(commentsMap.keySet());
        CustomDataService.CommentsMap.putAll(commentsMap);
    }

    public List<Comment> getAllComment() {
        return commentRepository.findAll();
    }

    public Optional<Comment> findByComment(String comment) {
        return commentRepository.findByComment(comment);
    }

    public Optional<Comment> getCommentById(String id) {
        return commentRepository.findById(id);
    }

    @Transactional
    @RefreshCache
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    @RefreshCache
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }
}
