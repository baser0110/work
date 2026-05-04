package bsshelper.localservice.externalcustomdata.repository;

import bsshelper.localservice.externalcustomdata.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, String>, JpaSpecificationExecutor<Comment> {
    Optional<Comment> findByComment(String comment);
}
