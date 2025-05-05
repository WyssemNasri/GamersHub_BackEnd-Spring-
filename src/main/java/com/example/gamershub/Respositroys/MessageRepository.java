    package com.example.gamershub.Respositroys;

    import com.example.gamershub.entity.Message;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.util.List;
    import java.util.Optional;

    public interface MessageRepository extends JpaRepository<Message, Long> {

        List<Message> findBySenderIdAndReceiverIdOrderByCreatedAtAsc(Long senderId, Long receiverId);
        Message findTopBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByCreatedAtDesc(
                Long senderId1, Long receiverId1, Long senderId2, Long receiverId2
        );
        List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreatedAtDesc(Long senderId, Long receiverId, Long senderId2, Long receiverId2);

        Optional<Message> findTopBySenderIdAndReceiverIdOrderByCreatedAtDesc(Long senderId, Long receiverId);
        @Query("""
        SELECT m FROM Message m 
        WHERE (m.sender.id = :userId AND m.receiver.id = :friendId)
        OR (m.sender.id = :friendId AND m.receiver.id = :userId)
        ORDER BY m.createdAt DESC LIMIT 1
    """)
    Optional<Message> findLastMessageBetweenUsers(@Param("userId") Long userId, @Param("friendId") Long friendId);

    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreatedAtAsc(
    Long senderId1, Long receiverId1, Long senderId2, Long receiverId2
);



    }
