public class CommitData {
    private String author;
    private String timestamp;
    private String message;
    private int linesAdded;
    private int linesDeleted;

    public CommitData(String author, String timestamp, String message, int linesAdded, int linesDeleted) {
        this.author = author;
        this.timestamp = timestamp;
        this.message = message;
        this.linesAdded = linesAdded;
        this.linesDeleted = linesDeleted;
    }

    public String getAuthor() {
        return author;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getLinesAdded() {
        return linesAdded;
    }

    public int getLinesDeleted() {
        return linesDeleted;
    }

    @Override
    public String toString() {
        return String.format(
                "Author: %s\nTimestamp: %s\nMessage: %s\nLines Added: %d\nLines Deleted: %d\n",
                author,
                timestamp,
                message.trim(), // Trim any extra spaces or newlines
                linesAdded,
                linesDeleted
        );
    }

}

