package mixter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

class Message {
    private final DecisionProjection projection;

    public Message(List<Event> eventHistory) {
        projection = new DecisionProjection();
        for (Event event : eventHistory) {
            projection.apply(event);
        }
    }

    public static void publish(PublishMessage publishMessage, EventPublisher eventPublisher) {
        MessageId messageId = new MessageId();
        eventPublisher.publish(new MessagePublished(messageId, publishMessage.getMessage()));
    }

    public void republish(UserId userId, EventPublisher eventPublisher) {
        if (projection.republishers.contains(userId)) {
            return;
        }
        MessageRepublished event = new MessageRepublished(projection.getId(), userId);
        eventPublisher.publish(event);
        projection.apply(event);
    }

    private class DecisionProjection {
        private MessageId id;

        public Set<UserId> republishers = new HashSet<>();


        public DecisionProjection() {
        }

        public MessageId getId() {
            return id;
        }

        public void apply(Event event) {
            if(event instanceof MessagePublished) {
                this.apply((MessagePublished) event);
            } else if (event instanceof MessageRepublished) {
                this.apply((MessageRepublished) event);
            }
        }

        private void apply(MessagePublished event) {
            id = event.getMessageId();
        }

        private void apply(MessageRepublished event) {
            republishers.add(event.getUserId());
        }

    }

    public static class MessageId {
        private final String id;

        public MessageId() {
            this.id = UUID.randomUUID().toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MessageId messageId = (MessageId) o;

            return id.equals(messageId.id);

        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public String toString() {
            return id;
        }

        public String getId() {
            return id;
        }
    }
}