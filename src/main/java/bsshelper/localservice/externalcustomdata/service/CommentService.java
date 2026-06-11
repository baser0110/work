package bsshelper.localservice.externalcustomdata.service;

import bsshelper.localservice.externalcustomdata.entity.Comment;
import bsshelper.localservice.externalcustomdata.repository.CommentRepository;
import bsshelper.localservice.externalcustomdata.service.listener.CacheRefreshEvent;
import bsshelper.localservice.externalcustomdata.service.listener.CacheUpdater;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService implements CacheUpdater {
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher eventPublisher;

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

    @Override
    public boolean supports(Class<?> clazz) { return clazz == Comment.class; }

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
    public Comment createComment(Comment entity) {
        entity.setId(null);
        Comment saved = commentRepository.save(entity);
        eventPublisher.publishEvent(new CacheRefreshEvent<>(saved));
        return commentRepository.save(saved);
    }

    @Transactional
    public Comment deleteComment(String id) {
        Comment entity = commentRepository.findById(id).orElse(null);
        if (entity != null) {
            commentRepository.delete(entity);
            eventPublisher.publishEvent(new CacheRefreshEvent<>(entity));
            return entity;
        }
        return null;
    }
}
