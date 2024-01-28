import java.time.LocalDateTime;

public class Event extends Task {

    public LocalDateTime fromDate;
    public LocalDateTime toDate;

    public Event(String name, LocalDateTime fromDate, LocalDateTime toDate) {
        super(name);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Event(String name, LocalDateTime fromDate, LocalDateTime toDate, boolean isDone) {
        super(name, isDone);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + super.outputDateAsString(fromDate) + " to: "
                + super.outputDateAsString(toDate) + ")";
    }

    @Override
    public String getType() {
        return "E";
    }

    @Override
    public String[] getTimes() {
        return new String[] { super.changeDateToString(fromDate), super.changeDateToString(toDate) };
    }
}
